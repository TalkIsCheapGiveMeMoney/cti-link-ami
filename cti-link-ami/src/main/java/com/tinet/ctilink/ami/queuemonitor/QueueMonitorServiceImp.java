package com.tinet.ctilink.ami.queuemonitor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.ami.AmiListener;
import com.tinet.ctilink.ami.AmiManager;
import com.tinet.ctilink.ami.cache.CacheService;
import com.tinet.ctilink.inc.Const;
import com.tinet.ctilink.util.DateUtil;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.manager.ResponseEvents;
import org.asteriskjava.manager.action.QueueStatusAction;
import org.asteriskjava.manager.event.QueueEntryEvent;
import org.asteriskjava.manager.event.ResponseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.alibaba.dubbo.config.annotation.Service;



/**
 * 队列状态查询服务的实现
 * 
 * @author Jiangsl
 *
 */
@Service
public class QueueMonitorServiceImp implements QueueMonitorService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private static ConcurrentMap<String, QueueStatus> queueStatusMap = new ConcurrentHashMap<String, QueueStatus>();
	private static ConcurrentMap<String, List<QueueStat>> queueStatMap = new ConcurrentHashMap<String, List<QueueStat>>();
	private static ConcurrentMap<String, Map<String,Date>> queueWaitingMap = new ConcurrentHashMap<String,Map<String,Date>>();
	
	@Autowired
	private AmiManager amiManager;

	@Autowired
	private CacheService cacheService;

	@Scheduled(cron="0 0 0 * * ?")
	@Override
	public void clearMonitorData(){
		queueStatMap.clear();
	}

	/**
	 * 根据队列名称获取队列状态
	 *
	 * @param queueName
	 * @return
	 */
	@Override
	public QueueStatus getQueueStatus(String queueName) {
		return queueStatusMap.get(queueName);
	}

	@Override
	public List<QueueStatus> getQueueStatus(String... queueNames) {
		List<QueueStatus> queueStatusList = new ArrayList<QueueStatus>();

		for (String queueName : queueNames) {
			queueStatusList.add(queueStatusMap.get(queueName));
		}
		return queueStatusList;
	}

	@Override
	public List<QueueEntry> getQueueEntry(Integer ctiId, String queueName) {

		QueueStatusAction queueStatusAction = new QueueStatusAction();
		queueStatusAction.setQueue(queueName);

		ResponseEvents responseEvents = null;
		AmiListener amiListener = amiManager.getManager();
		try {
			responseEvents = amiListener.sendEventGenerateAction(queueStatusAction, 5000);
		} catch (Exception e) {
			logger.error("获取队列(" + queueName + ")的等待成员时发生异常", e);
			return null;
		}

		List<QueueEntry> queueEntryList = new ArrayList<QueueEntry>();

		for (ResponseEvent event : responseEvents.getEvents()) {
			if (event instanceof QueueEntryEvent) {
				QueueEntryEvent queueEntryEvent = (QueueEntryEvent) event;
				AsteriskChannel channel = amiListener.getAsteriskServer()
						.getChannelByName(queueEntryEvent.getChannel());

				QueueEntry queueEntry = new QueueEntry();
				queueEntry.setPosition(queueEntryEvent.getPosition());
				queueEntry.setJoinTime(DateUtil.addSecond(new Date(), -queueEntryEvent.getWait().intValue()));
				queueEntry.setWaitTime(queueEntryEvent.getWait());
				queueEntry.setPriority(queueEntryEvent.getPriority());

				if (channel != null) {
					queueEntry.setUniqueId(channel.getId());
					queueEntry.setCustomerNumber(channel.getVariable(Const.CDR_CUSTOMER_NUMBER));
					queueEntry.setOverflow(channel.getNoCacheVariable(AmiAction.VARIABLE_QUEUE_ENTRY_OVERFLOW));
					queueEntry.setStartTime(DateUtil.format(channel.getVariable(Const.CDR_START_TIME),
							DateUtil.FMT_DATE_YYYY_MM_DD_HH_mm_ss));
					queueEntry.setCustomerVip(channel.getVariable(Const.CDR_CUSTOMER_VIP));
					if (cacheService.getQueueCall(channel.getId()) != null) {
						queueEntry.setCallStatus("ringing");
					}
				} else {
					queueEntry.setCustomerNumber("");
					queueEntry.setOverflow("");
				}

				queueEntryList.add(queueEntry);
			}
		}

		return queueEntryList;

	}

	@Override
	public List<QueueStat> getQueueStats(Integer ctiId, String queueName, Date fromDate, Date toDate) {
		List<QueueStat> queueStatList = queueStatMap.get(queueName);
		if (queueStatList == null || queueStatList.size()==0) {
			queueStatList = new ArrayList<QueueStat>(Collections.nCopies(144, null));
			queueStatMap.put(queueName, queueStatList);
		}

		int fromPeriod = getQueueStatPeriod(fromDate);
		int toPeriod = getQueueStatPeriod(toDate);
		List<QueueStat> reList = new ArrayList<QueueStat>();
		reList.addAll(queueStatList.subList(fromPeriod, toPeriod));
		return reList;
	}

	/**
	 * 设置队列状态
	 * 
	 * @param queueStatus
	 */
	public void setQueueStatus(QueueStatus queueStatus) {
		queueStatusMap.put(queueStatus.getQueueName(), queueStatus);
	}

	/**
	 * 设置队列成员的接听数
	 * 
	 * @param queueName
	 * @param cid
	 * @param memberCalls
	 */
	public void setMemberCalls(String queueName, String cid, Integer memberCalls) {
		QueueStatus queueStatus = getQueueStatus(queueName);
		if (queueStatus != null) {
			queueStatus.getMemberCalls().put(cid, memberCalls);
		}
	}

	/**
	 * 递增队列的来电数
	 * 
	 * @param queueName
	 * @param eventTime
	 */
	public void increaseQueueIncomings(String queueName, Date eventTime) {
		getQueueStat(queueName, eventTime).increaseIncomings();
	}
	
	/**
	 * 递增队列的接通数
	 * 
	 * @param queueName
	 * @param eventTime
	 */
	public void increaseQueueCompleted(String queueName, Date eventTime) {
		getQueueStat(queueName, eventTime).increaseCompleted();
	}
	
	/**
	 * 插入队列等待的channel
	 * 
	 * @param queueName
	 * @param channel
	 */
	public void insertQueueWaiting(String queueName, String channel,Date date) {
		Map<String,Date> queueWaitings = queueWaitingMap.get(queueName);
		if(queueWaitings == null){
			queueWaitings = new HashMap<String,Date>();
			queueWaitings.put(channel, date);
			queueWaitingMap.put(queueName, queueWaitings);
		}else{
			queueWaitings.put(channel, date);
		}
	}
	
	/**
	 * 删除队列等待的channel
	 * 
	 * @param queueName
	 * @param channel
	 */
	public void deleteQueueWaiting(String queueName, String channel) {
		Map<String,Date> queueWaitings = queueWaitingMap.get(queueName);
		if(queueWaitings == null){
			queueWaitings = new HashMap<>();
			queueWaitingMap.put(queueName, queueWaitings);
		}else{
			queueWaitings.remove(channel);
		}
	}
	
	
	/**
	 * 获取队列等待channels
	 * 
	 * @param queueName
	 */
	public Map<String,Date> getQueueWaitings(String queueName) {
		Map<String,Date> queueWaitings = queueWaitingMap.get(queueName);
		if(queueWaitings == null){
			queueWaitings = new HashMap<String,Date>();
			queueWaitingMap.put(queueName, queueWaitings);
			return queueWaitings;
		}else{
			return queueWaitings;
		}
	}
	

	/**
	 * 获取某个时间对应统计周期的队列统计数据
	 * 
	 * @param queueName
	 * @param date
	 * @return
	 */
	private QueueStat getQueueStat(String queueName, Date date) {
		List<QueueStat> queueStatList = queueStatMap.get(queueName);
		if (queueStatList == null) {
			queueStatList = new ArrayList<QueueStat>(Collections.nCopies(144, null));
			queueStatMap.put(queueName, queueStatList);
		}

		int period = getQueueStatPeriod(date);

		QueueStat queueStat = queueStatList.get(period);
		if (queueStat == null) {
			queueStat = new QueueStat();
			queueStat.setQueueName(queueName);
			queueStat.setPeriod(period);
			queueStatList.set(period, queueStat);
		}

		return queueStat;
	}

	/**
	 * 根据时间计算对应的统计周期，值范围为0-143，计算公式为hour*6+minute/10
	 * 
	 * @param date
	 * @return
	 */
	private int getQueueStatPeriod(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int period = hour * 6 + (minute % 10 == 0 ? minute / 10 : minute / 10 + 1);
		return period;
	}
}
