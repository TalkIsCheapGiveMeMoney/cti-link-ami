package com.tinet.ctilink.ami.ivrmonitor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.asteriskjava.manager.ResponseEvents;
import org.asteriskjava.manager.action.StatusAction;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.StatusEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.alibaba.dubbo.config.annotation.Service;
import com.tinet.ctilink.ami.AmiListener;
import com.tinet.ctilink.ami.AmiManager;
import com.tinet.ctilink.inc.Const;
import com.tinet.ctilink.util.DateUtil;

/**
 * IVR查询服务的实现
 * 
 * @author mucw
 *
 */
@Service
public class IvrMonitorServiceImp implements IvrMonitorService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private static ConcurrentMap<String, List<IvrStat>> ivrStatMap = new ConcurrentHashMap<String, List<IvrStat>>();
	@Autowired
	private AmiManager amiManager;
	
	
	@Scheduled(cron="0 0 0 * * ?")
	@Override
	public void clearMonitorData(){
		ivrStatMap.clear();
	}

	@Override
	public List<IvrStat> getIvrStats(String enterpriseId, String ivrId, Date fromDate, Date toDate){
		List<IvrStat> ivrStatList = ivrStatMap.get(enterpriseId + ivrId);
		if (ivrStatList == null || ivrStatList.size()==0) {
			ivrStatList = new ArrayList<IvrStat>(Collections.nCopies(2880, null));
			ivrStatMap.put(enterpriseId + ivrId, ivrStatList);
		}
		int fromPeriod = getIvrStatPeriod(fromDate);
		int toPeriod = getIvrStatPeriod(toDate);
		List<IvrStat> reList = new ArrayList<IvrStat>();
		getIvrStat(enterpriseId, ivrId, toDate); //更新未初始化的ivrStat
		reList.addAll(ivrStatList.subList(fromPeriod, toPeriod));
		return reList;
	}

	@Override
	public List<IvrEntry> getIvrEntry(String enterpriseId, String ivrId){
		List<String> actionVariables = new ArrayList<String>();
		actionVariables.add(Const.CDR_CALL_TYPE);
		actionVariables.add(Const.CDR_CUSTOMER_NUMBER);
		actionVariables.add(Const.CDR_NUMBER_TRUNK);
		actionVariables.add(Const.IVR_ID);
		actionVariables.add(Const.CDR_START_TIME);
		actionVariables.add(Const.ENTERPRISE_ID);

		StatusAction statusAction = new StatusAction();
		statusAction.setVariables(actionVariables);
		List<IvrEntry>  ivrStatsList = new ArrayList<IvrEntry>();

		ResponseEvents responseEvents = null;
		AmiListener amiListener = amiManager.getManager();
		try {
			responseEvents = amiListener.sendEventGenerateAction(statusAction, 5000);
		} catch (Exception e) {
			logger.error("获取进入enterprise +ivr(" + enterpriseId + ivrId + ")的电话时发生异常", e);
			e.printStackTrace();
			return null;
		}
		for (ManagerEvent event : responseEvents.getEvents()) {
			if (event instanceof StatusEvent) {
				Map<String, String> responseVariables = ((StatusEvent) event).getVariables();
				String callType = responseVariables.get(Const.CDR_CALL_TYPE);
				if (callType.equals(String.valueOf(Const.CDR_CALL_TYPE_IB)) || callType.equals(String.valueOf(Const.CDR_CALL_TYPE_OB_WEBCALL)) || callType.equals(String.valueOf(Const.CDR_CALL_TYPE_PREDICTIVE_OB))) {
					if(enterpriseId.equals(responseVariables.get(Const.ENTERPRISE_ID)) && ivrId.equals(responseVariables.get(Const.IVR_ID)) && ((StatusEvent) event).getSeconds() != null){
						IvrEntry ivrEntry = new IvrEntry();
						ivrEntry.setTel(responseVariables.get(Const.CDR_CUSTOMER_NUMBER));
						ivrEntry.setNumberTrunk(responseVariables.get(Const.CDR_NUMBER_TRUNK));
						ivrEntry.setStartTime(DateUtil.format(new Date(Long.parseLong(responseVariables.get(Const.CDR_START_TIME))*1000), "yyyy-MM-dd HH:mm:ss"));
						ivrEntry.setduration(((StatusEvent) event).getSeconds());
						ivrStatsList.add(ivrEntry);
					}
				}
			}
		}
		return ivrStatsList;
	}

	
	/**
	 * 设置进入ivr的来电数
	 * 
	 * @param enterpriseId
	 * @param ivrId
	 * @param eventTime
	 */
	public void setIvrIncomings(String enterpriseId, String ivrId, Date eventTime,String enterpriseIdIvrIdCount) {
		getIvrStat(enterpriseId, ivrId, eventTime).setIncomings(enterpriseIdIvrIdCount);
	}
	
	
	/**
	 * 获取某个时间对应统计周期的ivr统计数据
	 * 
	 * @param enterpriseId
	 * @param ivrId
	 * @param date
	 * @return
	 */
	private IvrStat getIvrStat(String enterpriseId, String ivrId, Date date) {
		List<IvrStat> ivrStatList = ivrStatMap.get(enterpriseId + ivrId);
		if (ivrStatList == null) {
			ivrStatList = new ArrayList<IvrStat>(Collections.nCopies(2880, null));
			ivrStatMap.put(enterpriseId + ivrId, ivrStatList);
		}

		int period = getIvrStatPeriod(date);

		IvrStat ivrStat = ivrStatList.get(period);
		if (ivrStat == null) {
			String initValue = "0";
			int initPeriod = period - 1;//依据第一个非零值初始化未初始化的变量
			while(initPeriod >= 0){
				IvrStat currIvrStat = ivrStatList.get(initPeriod);
				if(currIvrStat == null){
					initPeriod--;
					continue;
				}else{
					initValue = currIvrStat.getIncomings();
					initPeriod--;
					break;
				}
			}
			initPeriod = initPeriod + 1 ;
			while(initPeriod <= period){
				IvrStat currIvrStat = ivrStatList.get(initPeriod);
				if(currIvrStat == null){
					currIvrStat = new IvrStat();
					currIvrStat.setEnterpriseId(enterpriseId);
					currIvrStat.setIvrId(ivrId);
					currIvrStat.setPeriod(initPeriod);
					currIvrStat.setIncomings(initValue);
					ivrStatList.set(initPeriod, currIvrStat);
				}
				initPeriod++;
			}
		}

		return ivrStatList.get(period);
	}
	
	/**
	 * 根据时间计算对应的统计周期，值范围为0-2879，计算公式为hour*60*2+minute*2+second/30
	 * 
	 * @param date
	 * @return
	 */
	private int getIvrStatPeriod(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		int period = hour * 120 + minute * 2 + (second % 30 == 0 ? second / 30 : second / 30 + 1);
		return period;
	}
	
	
}


