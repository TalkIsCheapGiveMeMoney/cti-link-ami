package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.asteriskjava.manager.action.QueuePauseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.ami.wrapup.AmiWrapupEngine;
import com.tinet.ctilink.ami.online.CtiAgent;

/**
 * 监控置忙
 * 
 * @author tianzp
 */
@Component
public class SetPauseActionHandler extends AbstractActionHandler {

	@Autowired
	private AmiWrapupEngine amiWrapupEngine;

	@Override
	public String getAction() {
		return AmiAction.SETPAUSE;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.info("handle {} action : {}", this.getAction(), params);

		String pauseDescription = params.get(AmiAction.VARIABLE_PAUSE_DESCRIPTION);

		CtiAgent ctiAgent = getCtiAgent(params);
		if (ctiAgent == null) {
			return ERROR_BAD_PARAM;
		}

		String monitoredCno = params.get(AmiAction.VARIABLE_MONITORED_CNO);
		CtiAgent monitoredMember = ctiAgentService.get(ctiAgent.getEnterpriseId() + monitoredCno);
		if (monitoredMember == null) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NO_SUCH_CNO, "no such cno");
		}

		if (!monitoredMember.getDeviceStatus().equals(CtiAgent.IDLE)) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODR_BAD_DEVICE_STATUS, "bad device status");
		}

		QueuePauseAction queuePauseAction = new QueuePauseAction(monitoredMember.getLocation(), true, pauseDescription);
		if (sendAction(queuePauseAction) == null) {
			return ERROR_EXCEPTION;
		}

		if (monitoredMember.getPause()
				&& CtiAgent.PAUSE_DESCRIPTION_WRAPUP.equals(monitoredMember.getPauseDescription())) {
			amiWrapupEngine.removeWrapup(monitoredMember.getLocation());
		}

		return SUCCESS;
	}

}
