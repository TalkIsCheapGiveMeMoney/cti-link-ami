package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.asteriskjava.manager.action.QueuePauseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.ami.wrapup.AmiWrapupEngine;
import com.tinet.ctilink.ami.online.CtiAgent;

/**
 * 监控置闲
 * 
 * @author tianzp
 */
@Component
public class SetUnPauseActionHandler extends AbstractActionHandler {

	@Autowired
	private AmiWrapupEngine amiWrapupEngine;

	@Override
	public String getAction() {
		return AmiAction.SETUNPAUSE;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.info("handle {} action : {}", this.getAction(), params);

		CtiAgent ctiAgent = getCtiAgent(params);
		if (ctiAgent == null) {
			return ERROR_BAD_PARAM;
		}

		String monitoredCno = params.get(AmiAction.VARIABLE_MONITORED_CNO);
		CtiAgent monitoredMember = ctiAgentService.get(ctiAgent.getEnterpriseId() + monitoredCno);
		if (monitoredMember == null) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NO_SUCH_CNO, "no such cno");
		}

		if (!CtiAgent.IDLE.equals(monitoredMember.getDeviceStatus())) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_AGENT_BUDY, "座席正忙，不能置闲");
		}

		if (monitoredMember.getPause()) {
			amiWrapupEngine.removeWrapup(monitoredMember.getLocation());
		}

		QueuePauseAction queuePauseAction = new QueuePauseAction(monitoredMember.getLocation(), false, "");
		if (sendAction(queuePauseAction) == null) {
			return ERROR_EXCEPTION;
		}

		return SUCCESS;
	}

}
