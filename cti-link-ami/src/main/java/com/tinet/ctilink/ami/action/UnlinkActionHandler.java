package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.manager.action.HangupAction;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.AmiAction;


/**
 * 挂断
 * 
 * @author tianzp
 */
@Component
public class UnlinkActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiAction.UNLINK;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.info("handle {} action : {}", this.getAction(), params);
/*
		CtiAgent ctiAgent = getCtiAgent(params);
		if (ctiAgent == null) {
			return ERROR_BAD_PARAM;
		}

		if (StringUtils.isEmpty(ctiAgent.getChannel())) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NO_CHANNEL, "no channel");
		}

		HangupAction hangupAction = new HangupAction(ctiAgent.getChannel());
		hangupAction.setCause(16);

		if (sendAction(hangupAction) == null) {
			return ERROR_EXCEPTION;
		}
*/
		return SUCCESS;
	}

}
