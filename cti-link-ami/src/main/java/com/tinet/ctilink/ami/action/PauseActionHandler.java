package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.manager.action.QueuePauseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.ami.action.AmiActionResponse;
import com.tinet.ctilink.ami.wrapup.AmiWrapupEngine;
import com.tinet.ctilink.ami.online.CtiAgent;

/**
 * 座席置忙
 * 
 * @author tianzp
 */
@Component
public class PauseActionHandler extends AbstractActionHandler {

	@Autowired
	AmiWrapupEngine amiWrapupEngine;

	@Override
	public String getAction() {
		return AmiAction.PAUSE;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.debug("handle {} action : {}", this.getAction(), params);

		String loginType = params.get(AmiAction.VARIABLE_LOGIN_TYPE);// 登录类型
		String pauseQueueName = params.get(AmiAction.VARIABLE_QNAME);// 队列名称
		String pauseDescription = params.get(AmiAction.VARIABLE_PAUSE_DESCRIPTION);
		
		CtiAgent ctiAgent = getCtiAgent(params);
		if (ctiAgent == null) {
			return ERROR_BAD_PARAM;
		}

		if (!ctiAgent.getDeviceStatus().equals(CtiAgent.IDLE) && !loginType.equals("backend")) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODR_BAD_DEVICE_STATUS, "bad device status");
		}

		QueuePauseAction queuePauseAction = null;

		// 如果没有队列名称，所有队列置忙
		if (StringUtils.isEmpty(pauseQueueName)) {
			queuePauseAction = new QueuePauseAction(ctiAgent.getLocation(), true, pauseDescription);
		}
		// 如果有，置忙所在队列的座席
		else {
			queuePauseAction = new QueuePauseAction(ctiAgent.getLocation(), pauseQueueName, true, pauseDescription);
		}

		if (sendAction(queuePauseAction) == null) {
			return ERROR_EXCEPTION;
		}

		// 如果是整理置忙
		if (ctiAgent.getPause() && ctiAgent.getPauseDescription().equals(CtiAgent.PAUSE_DESCRIPTION_WRAPUP)) {
			amiWrapupEngine.removeWrapup(ctiAgent.getLocation());
		}

		return SUCCESS;
	}

}
