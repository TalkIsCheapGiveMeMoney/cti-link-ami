/*
 * Copyright 2004-2006 Stefan Reuter
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package org.asteriskjava.live.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tinet.ctilink.ami.cache.CacheService;
import com.tinet.ctilink.cache.CacheKey;
import com.tinet.ctilink.cache.RedisService;
import com.tinet.ctilink.model.EnterpriseHotline;
import com.tinet.ctilink.model.EnterpriseSetting;
import com.tinet.ctilink.model.Queue;
import com.tinet.ctilink.util.*;
import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.AsteriskQueue;
import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.live.QueueMemberState;
import org.asteriskjava.manager.EventTimeoutException;
import org.asteriskjava.manager.ResponseEvents;
import org.asteriskjava.manager.action.QueueStatusAction;
import org.asteriskjava.manager.event.JoinEvent;
import org.asteriskjava.manager.event.LeaveEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.QueueEntryEvent;
import org.asteriskjava.manager.event.QueueMemberAddedEvent;
import org.asteriskjava.manager.event.QueueMemberEvent;
import org.asteriskjava.manager.event.QueueMemberPausedEvent;
import org.asteriskjava.manager.event.QueueMemberPenaltyEvent;
import org.asteriskjava.manager.event.QueueMemberRemovedEvent;
import org.asteriskjava.manager.event.QueueMemberStatusEvent;
import org.asteriskjava.manager.event.QueueParamsEvent;
import org.asteriskjava.manager.response.ManagerError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.ami.AmiEvent;
import com.tinet.ctilink.ami.event.AmiEventPublisher;
import com.tinet.ctilink.ami.log.AmiLogQueueEngine;
import com.tinet.ctilink.ami.queuemonitor.QueueMonitorServiceImp;
import com.tinet.ctilink.ami.queuemonitor.QueueStatus;
import com.tinet.ctilink.ami.util.AmiUtil;
import com.tinet.ctilink.ami.wrapup.AmiWrapup;
import com.tinet.ctilink.ami.wrapup.AmiWrapupEngine;
import com.tinet.ctilink.inc.Const;
import com.tinet.ctilink.ami.online.CtiAgent;
import com.tinet.ctilink.ami.online.CtiAgentService;

/**
 * Manages queue events on behalf of an AsteriskServer.
 *
 * @author srt
 * @version $Id: QueueManager.java 1157 2008-08-27 11:56:48Z srt $
 */
public class QueueManager {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final AsteriskServerImpl server;
	private final ChannelManager channelManager;

	private AmiLogQueueEngine amiLogQueueEngine;
	private AmiEventPublisher amiEventPublisher;
	private AmiWrapupEngine amiWrapupEngine;
	private CtiAgentService ctiAgentService;
	private CacheService cacheService;
	private QueueMonitorServiceImp queueMonitorService;
	private RedisService redisService;

	/**
	 * A map of ACD queues by there name.
	 */
	private final Map<String, AsteriskQueueImpl> queues;

	QueueManager(AsteriskServerImpl server, ChannelManager channelManager) {
		this.server = server;
		this.channelManager = channelManager;
		this.queues = new HashMap<String, AsteriskQueueImpl>();

		this.amiLogQueueEngine = ContextUtil.getBean(AmiLogQueueEngine.class);
		this.amiEventPublisher = ContextUtil.getBean(AmiEventPublisher.class);
		this.amiWrapupEngine = ContextUtil.getBean(AmiWrapupEngine.class);
		this.ctiAgentService = ContextUtil.getBean(CtiAgentService.class);
		this.cacheService = ContextUtil.getBean(CacheService.class);
		this.queueMonitorService = ContextUtil.getBean(QueueMonitorServiceImp.class);
		this.redisService = ContextUtil.getBean(RedisService.class);
	}

	void initialize() throws ManagerCommunicationException {
		logger.info("QueueManager启动成功");

		// ccic to sync queue data. just send queue status action to manager.
		// try {
		// Thread.sleep(2000);// sleep 2 seconds for wait asterisk manager
		// // module start.
		// server.sendAction(new CommandAction("queue show"), 60000);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		ResponseEvents re;

		try {
			for (;;) {

				re = server.sendEventGeneratingAction(new QueueStatusAction());
				if (re.getResponse() instanceof ManagerError) {
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					break;
				}
			}

		} catch (ManagerCommunicationException e) {
			final Throwable cause = e.getCause();

			if (cause instanceof EventTimeoutException) {
				// this happens with Asterisk 1.0.x as it doesn't send a
				// QueueStatusCompleteEvent
				re = ((EventTimeoutException) cause).getPartialResult();
			} else {
				throw e;
			}
		}

		for (ManagerEvent event : re.getEvents()) {
			if (event instanceof QueueParamsEvent) {
				handleQueueParamsEvent((QueueParamsEvent) event);
			} else if (event instanceof QueueMemberEvent) {
				handleQueueMemberEvent((QueueMemberEvent) event);
			} else if (event instanceof QueueEntryEvent) {
				handleQueueEntryEvent((QueueEntryEvent) event);
			}
		}
	}

	void disconnected() {
		synchronized (queues) {
			for (AsteriskQueueImpl queue : queues.values()) {
				queue.cancelServiceLevelTimer();
			}
			queues.clear();
		}
	}

	/**
	 * Gets (a copy of) the list of the queues.
	 *
	 * @return a copy of the list of the queues.
	 */
	Collection<AsteriskQueue> getQueues() {
		Collection<AsteriskQueue> copy;

		synchronized (queues) {
			copy = new ArrayList<AsteriskQueue>(queues.values());
		}
		return copy;
	}

	/**
	 * Adds a queue to the internal map, keyed by name.
	 *
	 * @param queue the AsteriskQueueImpl to be added
	 */
	private void addQueue(AsteriskQueueImpl queue) {
		synchronized (queues) {
			queues.put(queue.getName(), queue);
		}
	}

	/**
	 * Called during initialization to populate the list of queues.
	 *
	 * @param event the event received
	 */
	public void handleQueueParamsEvent(QueueParamsEvent event) {
		AsteriskQueueImpl queue;
		final String name;
		final Integer max;
		final String strategy;
		final Integer serviceLevel;
		final Integer weight;

		name = event.getQueue();
		max = event.getMax();
		strategy = event.getStrategy();
		serviceLevel = event.getServiceLevel();
		weight = event.getWeight();

		queue = queues.get(name);

		if (queue == null) {
			queue = new AsteriskQueueImpl(server, name, max, strategy, serviceLevel, weight);
			logger.info("Adding new queue " + queue);
			addQueue(queue);
		} else {
			// We should never reach that code as this method is only called for
			// initialization
			// So the queue should never be in the queues list
			synchronized (queue) {
				queue.setMax(max);
				queue.setServiceLevel(serviceLevel);
				queue.setWeight(weight);
			}
		}

		// 队列监控
		QueueStatus queueStatus = queueMonitorService.getQueueStatus(event.getQueue());

		if (queueStatus == null) {
			queueStatus = new QueueStatus();
			queueStatus.setQueueName(event.getQueue());
			queueStatus.setMemberCalls(new ConcurrentHashMap<String, Integer>());
			queueMonitorService.setQueueStatus(queueStatus);
		}

		queueStatus.setMax(event.getMax());
		queueStatus.setStrategy(event.getStrategy());
		queueStatus.setCalls(event.getCalls());
		queueStatus.setHoldTime(event.getHoldTime());
		queueStatus.setTalkTime(event.getTalkTime());
		queueStatus.setCompleted(event.getCompleted());
		queueStatus.setAbandoned(event.getAbandoned());
		queueStatus.setServiceLevel(event.getServiceLevel());
		queueStatus.setServiceLevelPerf(event.getServiceLevelPerf());
		queueStatus.setWeight(event.getWeight());
	}

	/**
	 * Called during initialization to populate the members of the queues.
	 *
	 * @param event the QueueMemberEvent received
	 */
	public void handleQueueMemberEvent(QueueMemberEvent event) {
		final AsteriskQueueImpl queue = queues.get(event.getQueue());
		if (queue == null) {
			logger.error("Ignored QueueEntryEvent for unknown queue " + event.getQueue());
			return;
		}

		AsteriskQueueMemberImpl member = queue.getMember(event.getLocation());
		if (member == null) {
			member = new AsteriskQueueMemberImpl(server, queue, event.getLocation(),
					QueueMemberState.valueOf(event.getStatus()), event.getPaused(), event.getPenalty(),
					event.getMembership());
			queue.addMember(member);
		} else {
			AsteriskQueueMemberImpl newMember = new AsteriskQueueMemberImpl(server, queue, event.getLocation(),
					QueueMemberState.valueOf(event.getStatus()), event.getPaused(), event.getPenalty(),
					event.getMembership());
			queue.removeMember(member);
			queue.addMember(newMember);
		}

		// 队列监控
		queueMonitorService.setMemberCalls(event.getQueue(), event.getName(), event.getCallsTaken());
	}

	/**
	 * Called during initialization to populate entries of the queues. Currently
	 * does the same as handleJoinEvent()
	 *
	 * @param event - the QueueEntryEvent received
	 */
	private void handleQueueEntryEvent(QueueEntryEvent event) {
		final AsteriskQueueImpl queue = getQueueByName(event.getQueue());
		final AsteriskChannelImpl channel = channelManager.getChannelImplByName(event.getChannel());

		if (queue == null) {
			logger.error("Ignored QueueEntryEvent for unknown queue " + event.getQueue());
			return;
		}
		if (channel == null) {
			logger.error("Ignored QueueEntryEvent for unknown channel " + event.getChannel());
			return;
		}

		if (queue.getEntry(event.getChannel()) != null) {
			logger.error("Ignored duplicate queue entry during population in queue " + event.getQueue()
					+ " for channel " + event.getChannel());
			return;
		}

		// Asterisk gives us an initial position but doesn't tell us when he
		// shifts the others
		// We won't use this data for ordering until there is a appropriate
		// event in AMI.
		// (and refreshing the whole queue is too intensive and suffers
		// incoherencies
		// due to asynchronous shift that leaves holes if requested too fast)
		int reportedPosition = event.getPosition();

		queue.createNewEntry(channel, reportedPosition, event.getDateReceived());

	}

	/**
	 * Called from AsteriskServerImpl whenever a new entry appears in a queue.
	 *
	 * @param event the JoinEvent received
	 */
	public void handleJoinEvent(JoinEvent event) {

		final AsteriskChannelImpl channel = channelManager.getChannelImplByName(event.getChannel());

		if (channel == null) {
			logger.error("Ignored JoinEvent for unknown channel " + event.getChannel());
			return;
		}

		// 广播消息
		Map<String, String> joinQueueEvent = new HashMap<String, String>();
		joinQueueEvent.put(AmiAction.VARIABLE_NAME, AmiEvent.JOIN_QUEUE);
		joinQueueEvent.put(AmiAction.VARIABLE_UNIQUEID, channel.getId());
		joinQueueEvent.put(AmiAction.VARIABLE_CALL_TYPE, channel.getVariable(Const.CDR_CALL_TYPE));
		String overflow = channel.getNoCacheVariable(AmiAction.VARIABLE_QUEUE_ENTRY_OVERFLOW);
		joinQueueEvent.put(AmiAction.VARIABLE_QUEUE_ENTRY_OVERFLOW, overflow);// 溢出次数

		int enterpriseId = Integer.parseInt(channel.getVariable(Const.ENTERPRISE_ID));
		String customerNumber = channel.getVariable(Const.CDR_CUSTOMER_NUMBER);

		joinQueueEvent.put(AmiAction.VARIABLE_CUSTOMER_NUMBER, customerNumber);
		joinQueueEvent.put(AmiAction.VARIABLE_CUSTOMER_NUMBER_TYPE,
		channel.getVariable(Const.CDR_CUSTOMER_NUMBER_TYPE));
		joinQueueEvent.put(AmiAction.VARIABLE_CUSTOMER_AREA_CODE, channel.getVariable(Const.CDR_CUSTOMER_AREA_CODE));
		joinQueueEvent.put(AmiAction.VARIABLE_ENTERPRISE_ID, String.valueOf(enterpriseId));
		String joinDate = DateUtil.format(new Date(), DateUtil.FMT_DATE_YYYY_MM_DD_HH_mm_ss);
		joinQueueEvent.put(AmiAction.VARIABLE_QUEUE_ENTRY_JOIN_TIME, joinDate); // 进入队列时间
		String startTime = channel.getVariable(Const.CDR_START_TIME);
		if (StringUtils.isNotEmpty(startTime)) {
			joinQueueEvent.put(AmiAction.VARIABLE_START_TIME,
					DateUtil.format(startTime, DateUtil.FMT_DATE_YYYY_MM_DD_HH_mm_ss)); // 进入系统时间
		} else {
			joinQueueEvent.put(AmiAction.VARIABLE_START_TIME, ""); // 进入系统时间
		}
		String cutomerVip = channel.getVariable(Const.CDR_CUSTOMER_VIP);
		joinQueueEvent.put(Const.CDR_CUSTOMER_VIP, cutomerVip); // 来电客户的level
		joinQueueEvent.put(AmiAction.VARIABLE_QUEUE_ENTRY_POSITION, event.getPosition().toString());
		joinQueueEvent.put(AmiAction.VARIABLE_QID, event.getQueue());

		amiEventPublisher.publish(joinQueueEvent);

		final AsteriskQueueImpl queue = getQueueByName(event.getQueue());
		if (queue == null) {
			logger.error("Ignored JoinEvent for unknown queue " + event.getQueue());
			return;
		}
		if (queue.getEntry(event.getChannel()) != null) {
			logger.error("Ignored duplicate queue entry in queue " + event.getQueue() + " for channel "
					+ event.getChannel());
			return;
		}
		// Asterisk gives us an initial position but doesn't tell us when he
		// shifts the others
		// We won't use this data for ordering until there is a appropriate
		// event in AMI.
		// (and refreshing the whole queue is too intensive and suffers
		// incoherencies
		// due to asynchronous shift that leaves holes if requested too fast)
		int reportedPosition = event.getPosition();
		queue.createNewEntry(channel, reportedPosition, event.getDateReceived());
		
		// 队列来电数统计
		queueMonitorService.increaseQueueIncomings(event.getQueue(), event.getDateReceived());
		queueMonitorService.insertQueueWaiting(event.getQueue(),event.getChannel(),event.getDateReceived());
	}

	/**
	 * Called from AsteriskServerImpl whenever an enty leaves a queue.
	 *
	 * @param event - the LeaveEvent received
	 */
	public void handleLeaveEvent(LeaveEvent event) {

		final AsteriskChannelImpl channel = channelManager.getChannelImplByName(event.getChannel());
		if (channel == null) {
			logger.error("Ignored LeaveEvent for unknown channel " + event.getChannel());
			return;
		}
		// 广播消息
		Map<String, String> leaveQueueEvent = new HashMap<String, String>();
		leaveQueueEvent.put(AmiAction.VARIABLE_NAME, AmiEvent.LEAVE_QUEUE);
		leaveQueueEvent.put(AmiAction.VARIABLE_UNIQUEID, channel.getId());
		leaveQueueEvent.put(AmiAction.VARIABLE_CALL_TYPE, channel.getVariable(Const.CDR_CALL_TYPE));

		int enterpriseId = Integer.parseInt(channel.getVariable(Const.ENTERPRISE_ID));
		String customerNumber = channel.getVariable(Const.CDR_CUSTOMER_NUMBER);

		leaveQueueEvent.put(AmiAction.VARIABLE_CUSTOMER_NUMBER, customerNumber);
		leaveQueueEvent.put(AmiAction.VARIABLE_CUSTOMER_NUMBER_TYPE,
				channel.getVariable(Const.CDR_CUSTOMER_NUMBER_TYPE));
		leaveQueueEvent.put(AmiAction.VARIABLE_CUSTOMER_AREA_CODE, channel.getVariable(Const.CDR_CUSTOMER_AREA_CODE));
		leaveQueueEvent.put(AmiAction.VARIABLE_ENTERPRISE_ID, String.valueOf(enterpriseId));
		leaveQueueEvent.put(AmiAction.VARIABLE_QID, event.getQueue());

		amiEventPublisher.publish(leaveQueueEvent);

		cacheService.removeQueueCall(channel.getId());// 移除来电呼叫状态

		final AsteriskQueueImpl queue = getQueueByName(event.getQueue());
		if (queue == null) {
			logger.error("Ignored LeaveEvent for unknown queue " + event.getQueue());
			return;
		}
		final AsteriskQueueEntryImpl existingQueueEntry = queue.getEntry(event.getChannel());
		if (existingQueueEntry == null) {
			logger.error("Ignored leave event for non existing queue entry in queue " + event.getQueue()
					+ " for channel " + event.getChannel());
			return;
		}

		queue.removeEntry(existingQueueEntry, event.getDateReceived());
		queueMonitorService.deleteQueueWaiting(event.getQueue(),event.getChannel());
	}

	/**
	 * Challange a QueueMemberStatusEvent. Called from AsteriskServerImpl
	 * whenever a member state changes.
	 *
	 * @param event that was triggered by Asterisk server.
	 */
	public void handleQueueMemberStatusEvent(QueueMemberStatusEvent event) {
		CtiAgent ctiAgent = ctiAgentService.get(event.getName());
		if (ctiAgent != null) {
			String deviceStatus;
			Integer enterpriseId;
			String loginStatus;
			String pauseDescription;
			Integer ctiId;
			String location;
			Integer wrapup;
			String cno;
			String crmId;
			String busyDescription;
			String tel; // 座席绑定电话
			String customerNumber;
			String customerNumberType;// 电话类型
			Integer callType; // 呼叫类型
			String curQueue;
			String taskId;
			String transferCno;
			String consulterCno;
			String uniqueId;
			deviceStatus = ctiAgent.getDeviceStatus();
			loginStatus = ctiAgent.getLoginStatus();
			pauseDescription = ctiAgent.getPauseDescription();
			busyDescription = ctiAgent.getBusyDescription();
			customerNumber = ctiAgent.getCustomerNumber();
			enterpriseId = ctiAgent.getEnterpriseId();
			ctiId = ctiAgent.getCtiId();
			location = ctiAgent.getLocation();
			wrapup = ctiAgent.getWrapup();
			cno = ctiAgent.getCno();
			crmId = ctiAgent.getCrmId();
			tel = ctiAgent.getTel();
			customerNumberType = ctiAgent.getCustomerNumberTpye();
			callType = ctiAgent.getCallType();
			curQueue = ctiAgent.getCurQueue();
			taskId = ctiAgent.getTaskId();
			uniqueId = ctiAgent.getUniqueId();
			transferCno = ctiAgent.getTransferCno();
			consulterCno = ctiAgent.getConsulterCno();

			logger.info(new Date().toString() + " 座席:" + ctiAgent.getName() + " 登陆状态:" + loginStatus + " 置忙原因:"
					+ pauseDescription + " 设备状态:" + deviceStatus);

			String newDeviceStatus = AmiUtil.transferDeviceStatus(event.getStatus());
			logger.info(new Date().toString() + " event:" + newDeviceStatus + " queue:" + event.getQueue());
			if (!deviceStatus.equals(newDeviceStatus) ) {
				// 增加日志记录状态变化
				Map<String, String> statusEvent = new HashMap<String, String>();
				statusEvent.put(AmiAction.VARIABLE_NAME, AmiEvent.STATUS);
				statusEvent.put(AmiAction.VARIABLE_ENTERPRISE_ID, enterpriseId.toString());
				statusEvent.put(AmiAction.VARIABLE_CNO, cno);
				statusEvent.put(AmiAction.VARIABLE_CRM_ID, crmId == null ? "" : crmId);
				statusEvent.put(AmiAction.VARIABLE_BIND_TEL, tel);

				Boolean needClear = false;

				// 接听后挂机并且有整理开始，如果之前是忙的，现在状态变为空闲的
				if (newDeviceStatus.equals(CtiAgent.IDLE) && deviceStatus.equals(CtiAgent.BUSY)
						&& loginStatus.equals(CtiAgent.PAUSE)
						&& pauseDescription.equals(CtiAgent.PAUSE_DESCRIPTION_WRAPUP)) {
					needClear = true;
					int thisWrapup = wrapup;

					// 预览外呼整理时间使用任务配置的
//					if (callType == Const.CDR_CALL_TYPE_PREVIEW_OB && StringUtil.isNotEmpty(taskId)) {
//						thisWrapup = cacheService.getWrapupByTaskId(Integer.parseInt(taskId));
//					} else
					if ((callType == Const.CDR_CALL_TYPE_IB || callType == Const.CDR_CALL_TYPE_OB_WEBCALL)
							&& StringUtils.isNotEmpty(curQueue)) {
						Queue queue = redisService.get(String.format(CacheKey.QUEUE_ENTERPRISE_QNO,
								enterpriseId, curQueue), Queue.class);
						thisWrapup = queue.getWrapupTime();
					}

					// 加入到整理队列中
					statusEvent.put(AmiAction.VARIABLE_MEMBER_DEVICE_STATUS, newDeviceStatus); // 设备状态
					statusEvent.put(AmiAction.VARIABLE_MEMBER_LOGIN_STATUS, CtiAgent.PAUSE); // 座席状态
					statusEvent.put(AmiAction.VARIABLE_PAUSE_DESCRIPTION, CtiAgent.PAUSE_DESCRIPTION_WRAPUP);
					statusEvent.put(AmiAction.VARIABLE_WRAPUP_TIME, String.valueOf(thisWrapup));

					AmiWrapup wrapupEvent = new AmiWrapup(location);
					wrapupEvent.setWrapup(thisWrapup);
					wrapupEvent.setWrapupStart(new Date());

					amiWrapupEngine.pushWrapupEvent(wrapupEvent);

					// 记录Ami日志
					amiLogQueueEngine.insertLogQueue(null, new Date(), curQueue, enterpriseId + cno, "WRAPUP",
							String.valueOf(callType), null, null, null, null);

				}
				// 接听后挂机没有整理 或者响铃后挂机，如果之前是振铃或者忙的，现在变为空闲的
				else if (newDeviceStatus.equals(CtiAgent.IDLE)
						&& (deviceStatus.equals(CtiAgent.BUSY) || deviceStatus.equals(CtiAgent.RINGING))) {
					needClear = true;
					// 置忙外呼、挂机
					if (deviceStatus.equals(CtiAgent.BUSY) && loginStatus.equals(CtiAgent.PAUSE)) {
						if (callType == Const.CDR_CALL_TYPE_OB || callType == Const.CDR_CALL_TYPE_PREVIEW_OB
								|| callType == Const.CDR_CALL_TYPE_ORDER_CALL_BACK) {
							if (StringUtils.isNotEmpty("" + ctiAgent.getPreviewOutcallStart())) {
								long callEndTime = new Date().getTime() / 1000;
								long d = callEndTime - ctiAgent.getPreviewOutcallStart();
								if (d < 3600 * 24) { // 时长小于24小时
									// 记录Ami日志
									amiLogQueueEngine.insertLogQueue(null, new Date(), null, enterpriseId + cno,
											"PAUSEOUTCALL", ctiAgent.getPreviewOutcallStart() + "", d + "", null, null,
											null);
								}
							}
						}
					}

					statusEvent.put(AmiAction.VARIABLE_MEMBER_DEVICE_STATUS, newDeviceStatus); // 设备状态
					statusEvent.put(AmiAction.VARIABLE_MEMBER_LOGIN_STATUS, loginStatus);// 座席状态
				}
				// 座席响铃
				else if (newDeviceStatus.equals(CtiAgent.RINGING) && deviceStatus.equals(CtiAgent.IDLE)) {
					ctiAgent.getBargeChannel();
					// AsteriskChannel memberChannel =
					// server.getChannelByName(event.getName()) ;
					AsteriskChannel memberChannel = null;
					// TODO: 第一种方式取不到数据，这个测试用
					String channelName = "SIP/" + event.getName();
					Iterator<AsteriskChannel> cs = server.getChannels().iterator();
					while (cs.hasNext()) {
						AsteriskChannel c = cs.next();
						if (c.getName().startsWith(channelName)) {
							memberChannel = c;
							break;
						}
					}

					if (memberChannel != null) {
						try {
							String channelCallType = memberChannel.getVariable(Const.CDR_CALL_TYPE);
							if (!StringUtils.isEmpty(channelCallType)) {
								if (channelCallType.equals(Const.CDR_CALL_TYPE_IB + "") // 呼入
										|| channelCallType.equals(Const.CDR_CALL_TYPE_OB_WEBCALL + "") // 晚上400呼入
										|| channelCallType.equals(Const.CDR_CALL_TYPE_OB + "") // 点击外呼呼座席的通道
										|| channelCallType.equals(Const.CDR_CALL_TYPE_DIRECT_OB + "") // 直接外呼座席呼入的通道
										|| channelCallType.equals(Const.CDR_CALL_TYPE_PREVIEW_OB + "") // 预览外呼呼座席的通道
										|| channelCallType.equals(Const.CDR_CALL_TYPE_PREDICTIVE_OB + "")// 预测外呼呼客户的通道
										|| channelCallType.equals(Const.CDR_CALL_TYPE_ORDER_CALL_BACK + "")) {// 预约回呼的通道
									// 发送事件
									String channelCustomerNumber = "";
									String channelCustomerNumberType = "";
									String channelCustomerAreaCode = "";
									try {
										channelCustomerNumber = memberChannel.getVariable(Const.CDR_CUSTOMER_NUMBER);
										channelCustomerNumberType = memberChannel
												.getVariable(Const.CDR_CUSTOMER_NUMBER_TYPE);
										channelCustomerAreaCode = memberChannel
												.getVariable(Const.CDR_CUSTOMER_AREA_CODE);
									} catch (Exception e) {

									}
									String channelQid = "";
									try {
										channelQid = memberChannel.getVariable(Const.CUR_QUEUE);
									} catch (Exception e) {
									}
									if (channelQid == null || channelQid.equals("null")) {
										channelQid = "";
									}
									String channelNumberTrunk = "";
									try {
										channelNumberTrunk = memberChannel.getVariable(Const.CDR_NUMBER_TRUNK);
									} catch (Exception e) {
									}
									if (channelNumberTrunk == null || channelNumberTrunk.equals("null")) {
										channelNumberTrunk = "";
									}
									String channelHotline = "";
									if (StringUtils.isNotEmpty(channelNumberTrunk)) {
										EnterpriseHotline enterpriseHotline =redisService.get(String.format(CacheKey.ENTERPRISE_HOTLINE_ENTERPRISE_ID_NUMBER_TRUNK,
												enterpriseId, channelNumberTrunk), EnterpriseHotline.class);
										channelHotline = enterpriseHotline.getHotline();
									}
									String channelConsulterCno = "";
									try {
										// 从咨询过来的ringing
										channelConsulterCno = memberChannel.getVariable(Const.CONSULTER_CNO);
									} catch (Exception e) {
									}
									// 从转移过来的ringing
									String channelTransferCno = "";
									String channelTransferChannel = "";
									try {
										channelTransferChannel = memberChannel.getVariable(Const.TRANSFER_CHANNEL);
									} catch (Exception e) {
									}
									if (StringUtils.isNotEmpty(channelTransferChannel)
											&& !channelTransferChannel.equals("null")) {
										channelTransferCno = memberChannel.getVariable(
												"SHARED(" + Const.TRANSFER_CNO + "," + channelTransferChannel + ")");
										if (channelTransferCno == null || channelTransferCno.equals("null")) {
											channelTransferCno = "";
										}
									}
									// 主channel
									String channelMainChannel = "";
									String channelUniqueId = "";
									if (channelCallType.equals(Const.CDR_CALL_TYPE_PREVIEW_OB + "")
											|| channelCallType.equals(Const.CDR_CALL_TYPE_DIRECT_OB + "")
											|| channelCallType.equals(Const.CDR_CALL_TYPE_OB + "")
											|| channelCallType.equals(Const.CDR_CALL_TYPE_ORDER_CALL_BACK + "")) {
										try {
											channelUniqueId = memberChannel.getVariable(Const.CDR_MAIN_UNIQUE_ID);
											channelMainChannel = memberChannel.getName();
										} catch (Exception e) {
										}
										if (StringUtils.isEmpty(channelUniqueId)) {// 当是外呼咨询或转移座席时虽然calltype进来，但是也要从通道cdr_main_unique_id中取
											channelUniqueId = memberChannel.getId();
										}
										// 先看是不是转移过的如果是转移过有transfer_channel变量
										if (StringUtils.isNotEmpty(channelTransferChannel)
												&& !channelTransferChannel.equals("null")) {
											channelMainChannel = channelTransferChannel;
										} else {
											// 再看有没有MASTER_CHANNEL(BRIDGEPEER),有的话是咨询过的
											String mainChannel = "";
											try {
												mainChannel = memberChannel.getVariable("MASTER_CHANNEL(BRIDGEPEER)");
											} catch (Exception e) {
											}
											if (StringUtils.isNotEmpty(mainChannel) && !mainChannel.equals("null")) {
												channelMainChannel = mainChannel;
											}
										}
									} else {
										try {
											channelUniqueId = memberChannel.getVariable(Const.CDR_MAIN_UNIQUE_ID);
											channelMainChannel = memberChannel.getVariable(Const.MAIN_CHANNEL);
										} catch (Exception e) {
										}
									}
									String channelTaskId = "";
									String channelTaskInventoryId = "";
									if (channelCallType.equals(Const.CDR_CALL_TYPE_PREVIEW_OB + "")
											|| channelCallType.equals(Const.CDR_CALL_TYPE_PREDICTIVE_OB + "")) {
										try {
											channelTaskId = memberChannel.getVariable(Const.CDR_TASK_ID);

											if (channelTaskId == null || channelTaskId.equals("null")) {
												channelTaskId = "";
											}
											channelTaskInventoryId = memberChannel
													.getVariable(Const.CDR_TASK_INVENTORY_ID);
											if (channelTaskInventoryId == null
													|| channelTaskInventoryId.equals("null")) {
												channelTaskInventoryId = "";
											}
										} catch (Exception e) {
										}
									}
									Map<String, String> ringingEvent = new HashMap<String, String>();
									ringingEvent.put(AmiAction.VARIABLE_NAME, AmiEvent.RINGING);
									ringingEvent.put(AmiAction.VARIABLE_ENTERPRISE_ID, enterpriseId.toString());
									ringingEvent.put(AmiAction.VARIABLE_CNO, cno);
									ringingEvent.put(AmiAction.VARIABLE_BIND_TEL, tel);
									// 把不加密的电话传入工具条和前台
									ringingEvent.put(AmiAction.VARIABLE_CRM_CUSTOMER_NUMBER, channelCustomerNumber);
									ringingEvent.put(AmiAction.VARIABLE_CUSTOMER_NUMBER, channelCustomerNumber);
									ringingEvent.put(AmiAction.VARIABLE_CUSTOMER_NUMBER_TYPE,
											channelCustomerNumberType);
									ringingEvent.put(AmiAction.VARIABLE_CUSTOMER_AREA_CODE, channelCustomerAreaCode);
									ringingEvent.put(AmiAction.VARIABLE_NUMBER_TRUNK, channelNumberTrunk);
									ringingEvent.put(AmiAction.VARIABLE_HOTLINE,
											channelHotline == null ? "" : channelHotline);
									ringingEvent.put(AmiAction.VARIABLE_CALL_TYPE, channelCallType);
									ringingEvent.put(AmiAction.VARIABLE_RINGING_TIME,
											DateUtil.format(new Date(), DateUtil.FMT_DATE_YYYY_MM_DD_HH_mm_ss));
									if (channelCallType.equals(Const.CDR_CALL_TYPE_PREVIEW_OB + "")
											|| channelCallType.equals(Const.CDR_CALL_TYPE_PREDICTIVE_OB + "")) {
										ringingEvent.put(AmiAction.VARIABLE_TASK_ID, channelTaskId);
										ringingEvent.put(AmiAction.VARIABLE_TASK_INVENTORY_ID, channelTaskInventoryId);
									}
									ringingEvent.put(AmiAction.VARIABLE_UNIQUEID, channelUniqueId);
									if (StringUtils.isNotEmpty(channelTransferCno)) {
										ringingEvent.put(AmiAction.VARIABLE_TRANSFER_CNO, channelTransferCno);
									}
									if (StringUtils.isNotEmpty(channelConsulterCno)) {
										ringingEvent.put(AmiAction.VARIABLE_CONSULTER_CNO, channelConsulterCno);
									}

									ringingEvent.put(AmiAction.VARIABLE_QID, channelQid);

									// 根据企业设置获取前台来电弹屏时传递的参数
									EnterpriseSetting entSetting = redisService.get(String.format(CacheKey.ENTERPRISE_SETTING_ENTERPRISE_ID_NAME,
											enterpriseId, Const.ENTERPRISE_SETTING_NAME_CRM_URL_POPUP_USER_FIELD), EnterpriseSetting.class);
									if (entSetting != null && entSetting.getId() != null) {
										if (StringUtils.isNotEmpty(entSetting.getProperty())) {
											String property[] = StringUtils.split(entSetting.getProperty(), ",");
											AsteriskChannel channel = server.getChannelByName(channelMainChannel);
											for (String var : property) {
												ringingEvent.put(var, channel.getNoCacheVariable(var));
											}
										}
									}
									String channelNoRinging = "";
									try {
										channelNoRinging = memberChannel.getVariable(AmiAction.VARIABLE_NO_RINGING);
									} catch (Exception e) {
									}
									if (null == channelNoRinging || channelNoRinging.equals("")) {
										amiEventPublisher.publish(ringingEvent);
										AsteriskChannel channel = this.channelManager
												.getChannelImplByName(channelMainChannel);

										// 来电推送
										int pushType = 0;
										int curlType = 0;

										Map<String, Object> filterMap = new HashMap<String, Object>();
										filterMap.put("enterpriseId", enterpriseId);
										if (channelCallType.equals(Const.CDR_CALL_TYPE_IB + "") // 呼入
												|| channelCallType.equals(Const.CDR_CALL_TYPE_OB_WEBCALL + "")) {
											pushType = Const.ENTERPRISE_PUSH_TYPE_RINGING_IB;
											curlType = Const.CURL_TYPE_RINGING_IB;
										} else {
											pushType = Const.ENTERPRISE_PUSH_TYPE_RINGING_OB;
											curlType = Const.CURL_TYPE_RINGING_OB;
										}

										Map<String, String> pushEvent = new HashMap<String, String>();
										for (String key : ringingEvent.keySet()) {
											pushEvent.put(key, ringingEvent.get(key));
										}

										// 增加crmId 和 pwd
										pushEvent.put(AmiAction.VARIABLE_CRM_ID, crmId == null ? "" : crmId);
										pushEvent.put(AmiAction.VARIABLE_CUSTOMER_NUMBER,
												EncryptUtil.decryp(enterpriseId, channelCustomerNumber));

										//不推送了, tcc如果需要自己去查
//										AreaCode areaCode = cacheService.getAreaCode(channelCustomerAreaCode);
//										pushEvent.put(AmiAction.VARIABLE_CUSTOMER_AREA_NAME,
//												areaCode.getProvince() + "/" + areaCode.getCity());

										// 根据企业设置推送Curl
										AmiUtil.pushCurl(channel, pushEvent, enterpriseId, pushType, curlType);
									} else {
										logger.debug("属于前台过来的咨询、电话按键过来的咨询、直接呼叫电话，不推送ringing事件");
									}

									/**
									 * 如果呼叫类型是ib或者web400_ib发送queuecall事件，
									 * 说明此通电话正在呼叫座席了
									 */
									if (StringUtils.isNotEmpty(channelQid)
											&& (channelCallType.equals(Const.CDR_CALL_TYPE_IB + ""))
											|| (channelCallType.equals(Const.CDR_CALL_TYPE_OB_WEBCALL + "")
													|| (channelCallType
															.equals(Const.CDR_CALL_TYPE_PREDICTIVE_OB + "")))) {
										Map<String, String> queueCallEvent = new HashMap<String, String>();
										queueCallEvent.put(AmiAction.VARIABLE_NAME, AmiEvent.QUEUE_CALL);
										queueCallEvent.put(AmiAction.VARIABLE_ENTERPRISE_ID, enterpriseId.toString());
										queueCallEvent.put(AmiAction.VARIABLE_CNO, cno);
										queueCallEvent.put(AmiAction.VARIABLE_CUSTOMER_NUMBER, channelCustomerNumber);
										queueCallEvent.put(AmiAction.VARIABLE_UNIQUEID, channelUniqueId);
										queueCallEvent.put(AmiAction.VARIABLE_QID, channelQid);
										amiEventPublisher.publish(queueCallEvent); // 发送QueueCall事件

										logger.info("-----------------发送QueueCall事件-----------------");

										HashMap<String, String> queueCallMap = new HashMap<String, String>();
										queueCallMap.put(AmiAction.VARIABLE_ENTERPRISE_ID,
												String.valueOf(enterpriseId));
										queueCallMap.put(AmiAction.VARIABLE_CNO, cno);
										queueCallMap.put(AmiAction.VARIABLE_CUSTOMER_NUMBER, channelCustomerNumber);
										queueCallMap.put(AmiAction.VARIABLE_UNIQUEID, channelUniqueId);
										queueCallMap.put(AmiAction.VARIABLE_QID, channelQid);
										cacheService.setQueueCall(queueCallMap);
									}

									// 保存在线座席状态
									RedisLock redisLock = RedisLockUtil.lock(CtiAgent.getLockKey(ctiAgent.getCid()));
									if (redisLock != null) {
										try {
											CtiAgent newAgent = ctiAgentService.get(ctiAgent.getCid());
											if (newAgent != null) {
												newAgent.setChannel(memberChannel.getName());
												newAgent.setMainChannel(channelMainChannel);
												newAgent.setCustomerNumber(channelCustomerNumber);// 通话电话号码
												newAgent.setCustomerNumberTpye(channelCustomerNumberType);// 电话类型
												newAgent.setCustomerAreaCode(channelCustomerAreaCode);// 电话区号
												newAgent.setCallType(Integer.parseInt(channelCallType));// 呼叫类型
												newAgent.setTaskId(channelTaskId); // 任务id
												newAgent.setNumberTrunk(channelNumberTrunk); // 中继号码
												newAgent.setCurQueue(channelQid); // 队列id
												newAgent.setConsulterCno(channelConsulterCno); // 咨询者的cno
												newAgent.setTransferCno(channelTransferCno); // 轉移者的cno
												newAgent.setTaskInventoryId(channelTaskInventoryId); //
												newAgent.setHotline(channelHotline);
												newAgent.setUniqueId(channelUniqueId);
												ctiAgentService.set(newAgent);
											}
										} finally {
											RedisLockUtil.unLock(redisLock);
										}
									}

								} // end if channelCallType
							} // end channelCallType is null
						} catch (Exception e) {
							logger.error(
									"!!!!!!发生错误 座席ringing事件没找到座席channel enterpriseId=" + enterpriseId + " cno=" + cno);
							e.printStackTrace();
						}
					} // end 没找到座席
					statusEvent.put(AmiAction.VARIABLE_MEMBER_DEVICE_STATUS, newDeviceStatus); // 设备状态
					statusEvent.put(AmiAction.VARIABLE_MEMBER_LOGIN_STATUS, loginStatus);// 座席状态
				} else {
					// statusEvent.put(AmiAction.VARIABLE_CALL_TYPE, callType);
					statusEvent.put(AmiAction.VARIABLE_MEMBER_DEVICE_STATUS, newDeviceStatus);// 设备状态
					statusEvent.put(AmiAction.VARIABLE_MEMBER_LOGIN_STATUS, loginStatus);// 座席状态
				}

				if (loginStatus.equals(CtiAgent.PAUSE)) {
					// status事件中推送的相应数据
					statusEvent.put(AmiAction.VARIABLE_PAUSE_DESCRIPTION, pauseDescription);
				}
				if (newDeviceStatus.equals(CtiAgent.BUSY)) {
					// status事件中推送的相应数据
					statusEvent.put(AmiAction.VARIABLE_BUSY_DESCRIPTION, busyDescription);

					statusEvent.put(AmiAction.VARIABLE_CUSTOMER_NUMBER, customerNumber);// 通话电话号码
					statusEvent.put(AmiAction.VARIABLE_CUSTOMER_NUMBER_TYPE, customerNumberType);// 通话电话类型
					statusEvent.put(AmiAction.VARIABLE_CALL_TYPE, callType.toString());// 呼入号码类型
					if (StringUtils.isNotEmpty(transferCno)) {
						statusEvent.put(AmiAction.VARIABLE_TRANSFER_CNO, transferCno);
					}
					if (StringUtils.isNotEmpty(consulterCno)) {
						statusEvent.put(AmiAction.VARIABLE_CONSULTER_CNO, consulterCno);
					}
					statusEvent.put(AmiAction.VARIABLE_UNIQUEID, uniqueId);
				}
				amiEventPublisher.publish(statusEvent);

				// 保存在线座席状态
				RedisLock redisLock = RedisLockUtil.lock(CtiAgent.getLockKey(ctiAgent.getCid()));
				if (redisLock != null) {
					try {
						CtiAgent newAgent = ctiAgentService.get(ctiAgent.getCid());
						if (newAgent != null) {
							newAgent.setDeviceStatus(newDeviceStatus);
							newAgent.setBusyDescription(busyDescription);

							// 如果needClear为真，则表示通话结束
							if (needClear) {
								newAgent.clearChannel();

								// 如果busyDescription状态为BUSY_DESCRIPTION_ONHOLD表示在置忙过程中挂机了，补一个unhold事件
								if (busyDescription.equals(CtiAgent.BUSY_DESCRIPTION_ONHOLD)) {
									// 记录Ami日志
									amiLogQueueEngine.insertLogQueue(null, new Date(), null, enterpriseId + cno,
											"UNHOLD", null, null, null, null, null);
								}
							}
							ctiAgentService.set(newAgent);
						}
					} finally {
						RedisLockUtil.unLock(redisLock);
					}
				}
			}
			// 队列监控
			queueMonitorService.setMemberCalls(event.getQueue(), ctiAgent.getCid(), event.getCallsTaken());

		} else {
			logger.error("handleQueueMemberStatusEvent 未找到座席 event= {}", event);
		}
	}

	public void handleQueueMemberPausedEvent(QueueMemberPausedEvent event) {
		logger.debug("handleQueueMemberPausedEvent : {}", event);
		CtiAgent ctiAgent = ctiAgentService.get(event.getMemberName());
		if (ctiAgent != null) {
			String pauseDescription;
			Integer enterpriseId;
			String cno;
			String crmId;
			String deviceStatus;
			String tel; // 座席绑定电话
			boolean pause;
			enterpriseId = ctiAgent.getEnterpriseId();
			cno = ctiAgent.getCno();
			crmId = ctiAgent.getCrmId();
			pause = ctiAgent.getPause();
			pauseDescription = ctiAgent.getPauseDescription();
			deviceStatus = ctiAgent.getDeviceStatus();
			tel = ctiAgent.getTel();

			logger.debug("座席:" + ctiAgent.getName() + " 当前登陆状态:" + ((pause == true) ? "置忙" : "空闲") + " 当前置忙原因:"
					+ pauseDescription);
			logger.debug("event:" + ((event.getPaused() == true) ? "置忙" : "空闲") + " queue:" + event.getQueue());

			// 多个队列置忙过滤掉
			if (event.getPaused() == pause && ((event.getPaused() == true && event.getReason().equals(pauseDescription))
					|| (event.getPaused() == false))) {
				logger.debug("当前座席状态已经置忙？" + pause);
				if (event.getPaused() == pause) {
					logger.debug("当前座席状态已经置忙？" + pause);
					return;
				}
				return;
			}

			// 更新在线座席状态
			if (event.getPaused() == true) {// 原先空闲，现在变为置忙
				RedisLock redisLock = RedisLockUtil.lock(CtiAgent.getLockKey(ctiAgent.getCid()));
				if (redisLock != null) {
					try {
						CtiAgent newAgent = ctiAgentService.get(ctiAgent.getCid());
						if (newAgent != null) {
							newAgent.setLoginStatus(CtiAgent.PAUSE);
							newAgent.setPauseDescription(event.getReason());
							ctiAgentService.set(newAgent);
						}
					} finally {
						RedisLockUtil.unLock(redisLock);
					}
				}

				if (event.getReason() != null && event.getReason().equals(CtiAgent.PAUSE_DESCRIPTION_WRAPUP)) {// 如果是整理不发status事件

				} else {
					// broadcast to all other jwsClients
					Map<String, String> wsEvent = new HashMap<String, String>();
					wsEvent.put(AmiAction.VARIABLE_NAME, AmiEvent.STATUS);
					wsEvent.put(AmiAction.VARIABLE_ENTERPRISE_ID, enterpriseId.toString());
					wsEvent.put(AmiAction.VARIABLE_CNO, cno);
					wsEvent.put(AmiAction.VARIABLE_CRM_ID, crmId == null ? "" : crmId);
					wsEvent.put(AmiAction.VARIABLE_BIND_TEL, tel);
					wsEvent.put(AmiAction.VARIABLE_MEMBER_LOGIN_STATUS, CtiAgent.PAUSE); // 座席状态
					wsEvent.put(AmiAction.VARIABLE_MEMBER_DEVICE_STATUS, deviceStatus); // 设备状态
					wsEvent.put(AmiAction.VARIABLE_PAUSE_DESCRIPTION, event.getReason());

					amiEventPublisher.publish(wsEvent);
				}
			} else {// 原先置忙，现在变为空闲
				RedisLock redisLock = RedisLockUtil.lock(CtiAgent.getLockKey(ctiAgent.getCid()));
				if (redisLock != null) {
					try {
						CtiAgent newAgent = ctiAgentService.get(ctiAgent.getCid());
						if (newAgent != null) {
							newAgent.setLoginStatus(CtiAgent.ONLINE);
							newAgent.setPauseDescription("");
							ctiAgentService.set(newAgent);
						}
					} finally {
						RedisLockUtil.unLock(redisLock);
					}
				}

				// broadcast to all other jwsClients
				Map<String, String> wsEvent = new HashMap<String, String>();
				wsEvent.put(AmiAction.VARIABLE_NAME, AmiEvent.STATUS);
				wsEvent.put(AmiAction.VARIABLE_ENTERPRISE_ID, enterpriseId.toString());
				wsEvent.put(AmiAction.VARIABLE_CNO, cno);
				wsEvent.put(AmiAction.VARIABLE_CRM_ID, crmId == null ? "" : crmId);
				wsEvent.put(AmiAction.VARIABLE_BIND_TEL, tel);
				wsEvent.put(AmiAction.VARIABLE_MEMBER_LOGIN_STATUS, CtiAgent.ONLINE); // 座席状态
				wsEvent.put(AmiAction.VARIABLE_MEMBER_DEVICE_STATUS, deviceStatus); // 设备状态
				amiEventPublisher.publish(wsEvent);
			}
		} else {
			logger.error("handleQueueMemberPausedEvent 未找到座席 event= {}", event);
		}

	}

	public void handleQueueMemberPenaltyEvent(QueueMemberPenaltyEvent event) {
		AsteriskQueueImpl queue = getQueueByName(event.getQueue());

		if (queue == null) {
			logger.error("Ignored QueueMemberStatusEvent for unknown queue " + event.getQueue());
			return;
		}

		AsteriskQueueMemberImpl member = queue.getMemberByLocation(event.getLocation());
		if (member == null) {
			logger.error("Ignored QueueMemberStatusEvent for unknown member " + event.getLocation());
			return;
		}

		member.penaltyChanged(event.getPenalty());
	}

	/**
	 * Retrieves a queue by its name.
	 *
	 * @param queueName name of the queue.
	 * @return the requested queue or <code>null</code> if there is no queue
	 *         with the given name.
	 */
	private AsteriskQueueImpl getQueueByName(String queueName) {
		AsteriskQueueImpl queue;

		synchronized (queues) {
			queue = queues.get(queueName);
		}
		if (queue == null) {
			logger.error("Requested queue '" + queueName + "' not found!");
		}
		return queue;
	}

	/**
	 * Challange a QueueMemberAddedEvent.
	 *
	 * @param event - the generated QueueMemberAddedEvent.
	 */
	public void handleQueueMemberAddedEvent(QueueMemberAddedEvent event) {
		final AsteriskQueueImpl queue = queues.get(event.getQueue());
		if (queue == null) {
			logger.error("Ignored QueueMemberAddedEvent for unknown queue " + event.getQueue());
			return;
		}

		AsteriskQueueMemberImpl member = queue.getMember(event.getLocation());
		if (member == null) {
			member = new AsteriskQueueMemberImpl(server, queue, event.getLocation(),
					QueueMemberState.valueOf(event.getStatus()), event.getPaused(), event.getPenalty(),
					event.getMembership());
		}

		queue.addMember(member);
	}

	/**
	 * Challange a QueueMemberRemovedEvent.
	 *
	 * @param event - the generated QueueMemberRemovedEvent.
	 */
	public void handleQueueMemberRemovedEvent(QueueMemberRemovedEvent event) {
		final AsteriskQueueImpl queue = queues.get(event.getQueue());
		if (queue == null) {
			logger.error("Ignored QueueMemberRemovedEvent for unknown queue " + event.getQueue());
			return;
		}

		final AsteriskQueueMemberImpl member = queue.getMember(event.getLocation());
		if (member == null) {
			logger.error("Ignored QueueMemberRemovedEvent for unknown agent name: " + event.getMemberName()
					+ " location: " + event.getLocation() + " queue: " + event.getQueue());
			return;
		}

		queue.removeMember(member);
	}
}
