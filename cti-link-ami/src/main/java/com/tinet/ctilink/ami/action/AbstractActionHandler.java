package com.tinet.ctilink.ami.action;

import java.util.Map;

import com.tinet.ctilink.ami.AmiManager;
import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.live.OriginateCallback;
import org.asteriskjava.manager.action.CommandAction;
import org.asteriskjava.manager.action.ManagerAction;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.response.CommandResponse;
import org.asteriskjava.manager.response.ManagerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tinet.ctilink.ami.event.AmiEventPublisher;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.ami.log.AmiLogQueueEngine;


/**
 * AMI动作抽象基类
 * @author tianzp
 */
public abstract class AbstractActionHandler implements AmiActionHandler {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	protected AmiManager amiManager;

	@Autowired
	protected AmiEventPublisher amiEventPublisher;

	@Autowired
	protected AmiLogQueueEngine amiLogQueueEngine;



	protected final AmiActionResponse SUCCESS = AmiActionResponse.createSuccessResponse();
	protected final AmiActionResponse ERROR_BAD_PARAM = AmiActionResponse.createFailResponse(AmiParamConst.ERRORCODE_BAD_PARAM,"无效参数");
	protected final AmiActionResponse ERROR_EXCEPTION = AmiActionResponse.createFailResponse(AmiParamConst.ERRORCODE_EXCEPTION,"发生异常");

	/**
	 * 执行AMI特定动作
	 * @param action
	 * @return
	 */
	protected ManagerResponse sendAction(ManagerAction action) {
		return this.sendAction(action, 5000);
	}

	/**
	 * 执行AMI特定动作
	 * @param action
	 * @param timeout
	 * @return
	 */
	protected ManagerResponse sendAction(ManagerAction action, long timeout) {
		try {
			return amiManager.getManager().sendAction(action, timeout);
		} catch (Exception e) {
			logger.error("Send action error : {}", e);
		}
		return null;
	}
	
	/**
	 * 执行AMI特定动作
	 * @param originateAction
	 * @param cb
	 * @return
	 */
	protected void originateAsync(OriginateAction originateAction, OriginateCallback cb) {
		try {
			amiManager.getManager().getAsteriskServer().originateAsync(originateAction, cb);
		} catch (Exception e) {
			logger.error("Send action error : {}", e);
		}
	}
	/**
	 * 执行AMI指令
	 * @param command
	 * @return
	 */
	protected CommandResponse sendCommand(String command) {
		return (CommandResponse) sendAction(new CommandAction(command));
	}

	/**
	 * 执行AMI指令
	 * @param command
	 * @param timeout
	 * @return
	 */
	protected CommandResponse sendCommand(String command, long timeout) {
		return (CommandResponse) sendAction(new CommandAction(command), timeout);
	}

	/**
	 * 将Ami Event事件推送给Redis
	 * 
	 * @param event
	 */
	protected void publishEvent(final Map<String, String> event) {
		amiEventPublisher.publish(event);
	}

	/**
	 * 同步 Queue Member
	 * 
	 * @param queues
	 * @return
	 */
	protected boolean syncQueueMember(String... queues) {
		for (String queue : queues) {
			this.sendAction(new CommandAction("queue show " + queue), 6000);
		}
		return true;
	}

	
}
