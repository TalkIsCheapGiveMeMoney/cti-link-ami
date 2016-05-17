package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.inc.AmiParamConst;


/**
 * 咨询接回，挂断被咨询方
 * 
 * @author tianzp
 */
@Component
public class UnConsultActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiParamConst.UNCONSULT;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.info("handle {} action : {}", this.getAction(), params);

//		CtiAgent ctiAgent = getCtiAgent(params);
//		if (ctiAgent == null) {
//			return ERROR_BAD_PARAM;
//		}
//
//		if (StringUtils.isEmpty(ctiAgent.getConsultChannel())) {
//			return ERROR_BAD_PARAM;
//		}
//
//		HangupAction hangupAction = new HangupAction(ctiAgent.getConsultChannel());
//		hangupAction.setCause(new Integer(3));
//
//		if (sendAction(hangupAction) == null) {
//			return ERROR_EXCEPTION;
//		}

		return SUCCESS;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> fixParams, Map<String, String> optionalParams) {
		// TODO Auto-generated method stub
		return null;
	}

}
