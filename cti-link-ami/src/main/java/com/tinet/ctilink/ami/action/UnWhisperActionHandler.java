package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.manager.action.HangupAction;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.AmiAction;


/**
 * 耳语挂断
 * 
 * @author tianzp
 */
@Component
public class UnWhisperActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiAction.UNWHISPER;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.info("handle {} action : {}", this.getAction(), params);
/*
		CtiAgent ctiAgent = getCtiAgent(params);
		if (ctiAgent == null) {
			return ERROR_BAD_PARAM;
		}

		String whisperedCno = StringUtils.trimToEmpty(params.get(AmiAction.VARIABLE_WHISPERED_CNO));
		CtiAgent whisperedAgent = ctiAgentService.get(ctiAgent.getEnterpriseId() + whisperedCno);
		if (whisperedAgent == null) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NO_SUCH_CNO, "no such cno");
		}

		if (StringUtils.isEmpty(whisperedAgent.getWhisperChannel())
				|| CtiAgent.BUSY.equals(whisperedAgent.getDeviceStatus())) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NO_CHANNEL, "no whisper channel");
		}

		HangupAction hangupAction = new HangupAction(whisperedAgent.getWhisperChannel());
		hangupAction.setCause(new Integer(3));

		if (sendAction(hangupAction) == null) {
			return ERROR_EXCEPTION;
		}
*/
		return SUCCESS;
	}

}
