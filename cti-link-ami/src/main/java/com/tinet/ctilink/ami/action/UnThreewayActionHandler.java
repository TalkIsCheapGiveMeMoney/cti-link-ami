package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.inc.AmiParamConst;


/**
 * 三方挂断
 * 
 * @author tianzp
 */
@Component
public class UnThreewayActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiParamConst.UNTHREEWAY;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.info("handle {} action : {}", this.getAction(), params);

		String threewayCno = StringUtils.trimToEmpty(params.get("threewayedCno")); // 正在通话的座席
/*
		CtiAgent ctiAgent = getCtiAgent(params);
		if (ctiAgent == null) {
			return ERROR_BAD_PARAM;
		}

		CtiAgent thirdCtiAgent = ctiAgentService.get(ctiAgent.getEnterpriseId() + threewayCno);
		if (thirdCtiAgent == null) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NO_SUCH_CNO, "no such cno");
		}

		if (StringUtils.isEmpty(thirdCtiAgent.getChannel()) || !thirdCtiAgent.getDeviceStatus().equals(CtiAgent.BUSY)) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NO_CHANNEL, "no threeway channel");
		}

		HangupAction hangupAction = new HangupAction(thirdCtiAgent.getThreewayChannel());
		hangupAction.setCause(new Integer(3));

		if (sendAction(hangupAction) == null) {
			return ERROR_EXCEPTION;
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
