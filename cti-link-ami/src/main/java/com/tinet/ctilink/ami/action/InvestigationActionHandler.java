package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.asteriskjava.manager.action.HangupAction;
import org.asteriskjava.manager.action.SetVarAction;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.inc.Const;


/**
 * 满意度调查
 * 
 * @author tianzp
 */
@Component
public class InvestigationActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiAction.INVESTIGATION;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.info("handle {} action : {}", this.getAction(), params);

/*		CtiAgent ctiAgent = getCtiAgent(params);
		if (ctiAgent == null) {
			return ERROR_BAD_PARAM;
		}

		if (StringUtils.isEmpty(ctiAgent.getMainChannel())) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NO_CHANNEL, "no main channel");
		}

		if (StringUtils.isEmpty(ctiAgent.getChannel())) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NO_CHANNEL, "no channel");
		}

		SetVarAction setVarAction = new SetVarAction(ctiAgent.getMainChannel(), Const.IS_INVESTIGATION, "1");
		HangupAction hangupAction = new HangupAction(ctiAgent.getChannel());
		hangupAction.setCause(new Integer(16));

		if (sendAction(setVarAction) == null || sendAction(hangupAction) == null) {
			return ERROR_EXCEPTION;
		}
*/
		return SUCCESS;
	}

}
