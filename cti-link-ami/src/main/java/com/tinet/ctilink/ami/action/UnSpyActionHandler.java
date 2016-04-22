package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.asteriskjava.manager.action.HangupAction;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.ami.online.CtiAgent;

/**
 * 监听挂断
 * 
 * @author tianzp
 */
@Component
public class UnSpyActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiAction.UNSPY;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.info("handle {} action : {}", this.getAction(), params);

		CtiAgent ctiAgent = getCtiAgent(params);
		if (ctiAgent == null) {
			return ERROR_BAD_PARAM;
		}

		String spiedCno = StringUtils.trimWhitespace(params.get(AmiAction.VARIABLE_SPIED_CNO));
		CtiAgent spiedCtiAgent = ctiAgentService.get(ctiAgent.getEnterpriseId() + spiedCno);
		if (spiedCtiAgent == null) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NO_SUCH_CNO, "no such cno");
		}

		if (StringUtils.isEmpty(spiedCtiAgent.getSpyChannel())
				|| !CtiAgent.BUSY.equals(spiedCtiAgent.getDeviceStatus())) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NO_CHANNEL, "no spy channel");
		}

		HangupAction hangupAction = new HangupAction(spiedCtiAgent.getSpyChannel());
		hangupAction.setCause(new Integer(3));

		if (sendAction(hangupAction) == null) {
			return ERROR_EXCEPTION;
		}
		return SUCCESS;
	}

}
