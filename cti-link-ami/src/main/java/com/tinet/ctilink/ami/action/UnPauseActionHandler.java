package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.manager.action.QueuePauseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.ami.wrapup.AmiWrapupEngine;
import com.tinet.ctilink.ami.online.CtiAgent;

/**
 * 置闲
 * 
 * @author tianzp
 */
@Component
public class UnPauseActionHandler extends AbstractActionHandler {

	@Autowired
	private AmiWrapupEngine amiWrapupEngine;

	@Override
	public String getAction() {
		return AmiAction.UNPAUSE;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.debug("handle {} action : {}", this.getAction(), params);

		String pauseQueueName = params.get(AmiAction.VARIABLE_QNAME);

		CtiAgent ctiAgent = getCtiAgent(params);
		if (ctiAgent == null) {
			return ERROR_BAD_PARAM;
		}

		QueuePauseAction queuePauseAction = null;
		if (StringUtils.isEmpty(pauseQueueName)) {
			queuePauseAction = new QueuePauseAction(ctiAgent.getLocation(), false, "");
		} else {
			queuePauseAction = new QueuePauseAction(ctiAgent.getLocation(), pauseQueueName, false, "");
		}

		if (sendAction(queuePauseAction) == null) {
			return ERROR_EXCEPTION;
		}

		if (ctiAgent.getPause()) {
			amiWrapupEngine.removeWrapup(ctiAgent.getLocation());
		}

		return SUCCESS;
	}

}
