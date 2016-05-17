package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.inc.AmiParamConst;


/**
 * 拒接
 * 
 * @author tianzp
 */
@Component
public class RefuseActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiParamConst.REFUSE;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.info("handle {} action : {}", this.getAction(), params);
/*
		CtiAgent ctiAgent = getCtiAgent(params);
		if (ctiAgent == null) {
			return ERROR_BAD_PARAM;
		}

		if (!CtiAgent.RINGING.equals(ctiAgent.getDeviceStatus())) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_BAD_REFUSE, "bad refuse");
		}

		HangupAction hangupAction = new HangupAction(ctiAgent.getChannel());
		hangupAction.setCause(new Integer(16));

		try {
			if (sendAction(hangupAction) == null) {
				return ERROR_EXCEPTION;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_EXCEPTION, e.toString());
		}
*/
		return SUCCESS;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> fixParams, Map<String, String> optionalParams) {
		// TODO Auto-generated method stub
		return null;
	}

}
