package com.tinet.ctilink.ami.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.live.internal.ChannelManager;
import org.asteriskjava.live.internal.QueueManager;
import org.asteriskjava.manager.event.BridgeEvent;
import org.asteriskjava.manager.event.CdrEvent;
import org.asteriskjava.manager.event.DialEvent;
import org.asteriskjava.manager.event.DtmfEvent;
import org.asteriskjava.manager.event.HangupEvent;
import org.asteriskjava.manager.event.JoinEvent;
import org.asteriskjava.manager.event.LeaveEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.NewAccountCodeEvent;
import org.asteriskjava.manager.event.NewCallerIdEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
import org.asteriskjava.manager.event.NewExtenEvent;
import org.asteriskjava.manager.event.NewStateEvent;
import org.asteriskjava.manager.event.OriginateResponseEvent;
import org.asteriskjava.manager.event.QueueMemberAddedEvent;
import org.asteriskjava.manager.event.QueueMemberPausedEvent;
import org.asteriskjava.manager.event.QueueMemberPenaltyEvent;
import org.asteriskjava.manager.event.QueueMemberRemovedEvent;
import org.asteriskjava.manager.event.QueueMemberStatusEvent;
import org.asteriskjava.manager.event.QueueParamsEvent;
import org.asteriskjava.manager.event.RenameEvent;
import org.asteriskjava.manager.event.UserEvent;
import org.asteriskjava.manager.event.VarSetEvent;
import org.asteriskjava.manager.userevent.QueueLogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.inc.Const;

/**
 * 采用多线程的方式处理AMI Event事件，根据enterpriseId的尾号（0-9），共分配10个线程处理器并行处理
 * 对于同一个enterpriseId，其Event会被分配至固定的线程处理器排队处理，以保证处理的顺序
 * 每一个AMI连接都会有一个ManagerEventListenerExecutor实例，因此系统中的Event处理线程数=10*CTI数
 * 
 * @author tianzp
 */
@Component
public class AmiEventHandlerService {
	private static final Logger logger = LoggerFactory.getLogger(AmiEventHandlerService.class);

	@Autowired
	private List<AmiUserEventHandler> userEventHandlers;
	@Autowired
	private List<AmiChannelEventHandler> channelEventHandlers;
	@Autowired
	private List<AmiQueueEventHandler> queueEventHandlers;
	private Map<Class<?>, AmiUserEventHandler> userEventHandlerMap;
	private Map<Class<?>, AmiChannelEventHandler> channelEventHandlerMap;
	private Map<Class<?>, AmiQueueEventHandler> queueEventHandlerMap;
	private Map<String, String> enterpriseIds;
	private Map<String, ExecutorService> executors;

	public AmiEventHandlerService() {
		executors = new HashMap<String, ExecutorService>();
		enterpriseIds = new HashMap<String, String>();
	}

	/**
	 * 以多线程的方式处理UserEvent
	 * 
	 * @param event
	 */
	public void handleUserEvent(ManagerEvent event) {
		String enterpriseId = getEnterpriseId(event);
		if (StringUtils.isNotEmpty(enterpriseId)) {
			String tail = enterpriseId.substring(enterpriseId.length() - 1);

			getExecutor(tail).execute(new Runnable() {
				@Override
				public void run() {
					getUserEventHandler(event).handle(event);
				}
			});

		}
	}

	/**
	 * 以多线程的方式处理ChannelEvent
	 * 
	 * @param event
	 */
	public void handleChannelEvent(ManagerEvent event, ChannelManager channelManager) {
		String enterpriseId = getEnterpriseId(event);
		if (StringUtils.isNotEmpty(enterpriseId)) {
			String tail = enterpriseId.substring(enterpriseId.length() - 1);

			getExecutor(tail).execute(new Runnable() {
				@Override
				public void run() {
					getChannelEventHandler(event).handle(event, channelManager);
				}
			});

		}else{
			if(event instanceof NewChannelEvent){
				logger.info("channelManager.handleNewChannelEvent((NewChannelEvent) event)");
				channelManager.handleNewChannelEvent((NewChannelEvent) event);
			}
		}
	}

	/**
	 * 以多线程的方式处理QueueEvent
	 * 
	 * @param event
	 */
	public void handleQueueEvent(ManagerEvent event, QueueManager queueManager) {
		String enterpriseId = getEnterpriseId(event);
		if (StringUtils.isNotEmpty(enterpriseId)) {
			String tail = enterpriseId.substring(enterpriseId.length() - 1);

			getExecutor(tail).execute(new Runnable() {
				@Override
				public void run() {
					getQueueEventHandler(event).handle(event, queueManager);
				}
			});

		}
	}

	/**
	 *依据NewAccountCodeEvent获取enterpriseid
	 * 
	 * @param event
	 */
	public void handleNewAccountCodeEvent(ManagerEvent event) {
		logger.info("get enterprise based on accountcode");
		getEnterpriseId(event);
	}
	
	
	/**
	 * 根据enterpriseId的尾号获取特定的线程处理器
	 * 
	 * @param key
	 * @return
	 */
	private ExecutorService getExecutor(String key) {
		ExecutorService eventExecutor = executors.get(key);
		if (eventExecutor == null) {
			// 创建一个使用单个 线程的 Executor，以队列方式来运行分配给该线程的任务
			eventExecutor = Executors.newSingleThreadExecutor();
			executors.put(key, eventExecutor);
		}
		return eventExecutor;
	}

	/**
	 * 根据event类型获取对应的用户事件处理器
	 * 
	 * @param event
	 * @return
	 */
	private AmiUserEventHandler getUserEventHandler(ManagerEvent event) {
		if (userEventHandlerMap == null) {
			userEventHandlerMap = new HashMap<Class<?>, AmiUserEventHandler>();
			for (AmiUserEventHandler handler : userEventHandlers) {
				userEventHandlerMap.put(handler.getEventClass(), handler);
			}
		}

		AmiUserEventHandler handler = userEventHandlerMap.get(event.getClass());

		if (handler == null) {
			logger.error("AmiUserEventHandler for Event: " + event.getClass() + " not found.");
			throw new UnsupportedOperationException(
					"AmiUserEventHandler for Event: " + event.getClass() + " not found.");
		}

		return handler;
	}

	/**
	 * 根据event类型获取对应的通道事件处理器
	 * 
	 * @param event
	 * @return
	 */
	private AmiChannelEventHandler getChannelEventHandler(ManagerEvent event) {
		if (channelEventHandlerMap == null) {
			channelEventHandlerMap = new HashMap<Class<?>, AmiChannelEventHandler>();
			for (AmiChannelEventHandler handler : channelEventHandlers) {
				channelEventHandlerMap.put(handler.getEventClass(), handler);
			}
		}

		AmiChannelEventHandler handler = channelEventHandlerMap.get(event.getClass());

		if (handler == null) {
			logger.error("AmiChannelEventHandler for Event: " + event.getClass() + " not found.");
			throw new UnsupportedOperationException(
					"AmiChannelEventHandler for Event: " + event.getClass() + " not found.");
		}

		return handler;
	}

	/**
	 * 根据event类型获取对应的队列事件处理器
	 * 
	 * @param event
	 * @return
	 */
	private AmiQueueEventHandler getQueueEventHandler(ManagerEvent event) {
		if (queueEventHandlerMap == null) {
			queueEventHandlerMap = new HashMap<Class<?>, AmiQueueEventHandler>();
			for (AmiQueueEventHandler handler : queueEventHandlers) {
				queueEventHandlerMap.put(handler.getEventClass(), handler);
			}
		}

		AmiQueueEventHandler handler = queueEventHandlerMap.get(event.getClass());

		if (handler == null) {
			logger.error("AmiQueueEventHandler for Event: " + event.getClass() + " not found.");
			throw new UnsupportedOperationException(
					"AmiQueueEventHandler for Event: " + event.getClass() + " not found.");
		}

		return handler;
	}

	/**
	 * 从Event中解析企业Id
	 * 
	 * @param event
	 * @return enterpriseId
	 */
	private String getEnterpriseId(ManagerEvent event) {
		String enterpriseId = "";
		if (event instanceof NewAccountCodeEvent){
			NewAccountCodeEvent newAccountCode = (NewAccountCodeEvent)event;
			enterpriseId = newAccountCode.getAccountCode();
			if(StringUtils.isNotEmpty(enterpriseId)){
				enterpriseIds.put(newAccountCode.getChannel(),enterpriseId);
			}
		}
		// Handle Channel related events
		else if (event instanceof NewChannelEvent) {
			NewChannelEvent nEvent = (NewChannelEvent) event;
			enterpriseId = nEvent.getAccountCode();
			if (StringUtils.isNotEmpty(enterpriseId)) {
				enterpriseIds.put(nEvent.getChannel(), enterpriseId);
			}

		} else if (event instanceof NewExtenEvent) {
			enterpriseId = enterpriseIds.get(((NewExtenEvent) event).getChannel());
		} else if (event instanceof NewStateEvent) {
			enterpriseId = enterpriseIds.get(((NewStateEvent) event).getChannel());
		} else if (event instanceof NewCallerIdEvent) {
			enterpriseId = enterpriseIds.get(((NewCallerIdEvent) event).getChannel());
		} else if (event instanceof DialEvent) {
			enterpriseId = enterpriseIds.get(((DialEvent) event).getChannel());
		} else if (event instanceof BridgeEvent) {
			enterpriseId = enterpriseIds.get(((BridgeEvent) event).getChannel1());
		} else if (event instanceof RenameEvent) {
			enterpriseId = enterpriseIds.get(((RenameEvent) event).getChannel());
		} else if (event instanceof HangupEvent) {
			enterpriseId = enterpriseIds.get(((HangupEvent) event).getChannel());
			if (StringUtils.isNotEmpty(enterpriseId)) {
				enterpriseIds.remove(((HangupEvent) event).getChannel());
			}
		} else if (event instanceof CdrEvent) {
			enterpriseId = enterpriseIds.get(((CdrEvent) event).getChannel());
		} else if (event instanceof VarSetEvent) {
			enterpriseId = enterpriseIds.get(((VarSetEvent) event).getChannel());
		} else if (event instanceof DtmfEvent) {
			enterpriseId = enterpriseIds.get(((DtmfEvent) event).getChannel());
		}
		// End of parking related events
		// Handle queue related event
		else if (event instanceof QueueParamsEvent) {
			enterpriseId = getEnterpriseIdByQueue(((QueueParamsEvent) event).getQueue());
		} else if (event instanceof JoinEvent) {
			enterpriseId =  getEnterpriseIdByQueue(((JoinEvent) event).getQueue());
		} else if (event instanceof LeaveEvent) {
			enterpriseId = getEnterpriseIdByQueue(((LeaveEvent) event).getQueue());
		} else if (event instanceof QueueMemberStatusEvent) {
			enterpriseId = getEnterpriseIdByMember(((QueueMemberStatusEvent) event).getName());
		} else if (event instanceof QueueMemberPenaltyEvent) {
			enterpriseId = getEnterpriseIdByQueue(((QueueMemberPenaltyEvent) event).getQueue());
		} else if (event instanceof QueueMemberAddedEvent) {
			enterpriseId = getEnterpriseIdByMember(((QueueMemberAddedEvent) event).getMemberName());
		} else if (event instanceof QueueMemberRemovedEvent) {
			enterpriseId = getEnterpriseIdByMember(((QueueMemberRemovedEvent) event).getMemberName());
		} else if (event instanceof QueueMemberPausedEvent) {
			enterpriseId = getEnterpriseIdByMember(((QueueMemberPausedEvent) event).getMemberName());
		} else if (event instanceof QueueLogEvent) {
			QueueLogEvent queueLogEvent = (QueueLogEvent)event;
			enterpriseId = getEnterpriseIdByQueue(queueLogEvent.getQueueName());
			if (StringUtils.isEmpty(enterpriseId)) {
				enterpriseId = getEnterpriseIdByMember(queueLogEvent.getAgent());
			}
		} else if (event instanceof OriginateResponseEvent) {
			enterpriseId = enterpriseIds.get(((OriginateResponseEvent) event).getChannel());
		} else if (event instanceof UserEvent) {
			enterpriseId = ((UserEvent) event).getEnterpriseId();
		}
		return enterpriseId;
	}

	/**
	 * 根据MemberName 获取企业Id
	 * 
	 * @param memberName
	 * @return
	 */
	private String getEnterpriseIdByMember(String memberName) {
		if (StringUtils.isEmpty(memberName)) {
			return null;
		}
		if (memberName.length() > Const.ENTERPRISE_ID_LEN) {
			return memberName.substring(0, Const.ENTERPRISE_ID_LEN);
		}
		return null;
	}

	/**
	 * 根据Queue name 获取企业Id
	 * 
	 * @param queueName
	 * @return
	 */
	private String getEnterpriseIdByQueue(String queueName) {
		if (StringUtils.isEmpty(queueName)) {
			return null;
		}
		if (queueName.length() > Const.ENTERPRISE_ID_LEN) {
			return queueName.substring(0, Const.ENTERPRISE_ID_LEN);
		}
		return null;
	}
}
