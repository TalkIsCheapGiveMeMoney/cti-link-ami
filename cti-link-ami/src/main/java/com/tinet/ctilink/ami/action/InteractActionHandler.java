package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.manager.action.AtxferAction;
import org.asteriskjava.manager.action.SetVarAction;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.inc.Const;
import com.tinet.ctilink.ami.online.CtiAgent;

/**
 * 交互
 * 
 * @author tianzp
 */
@Component
public class InteractActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiAction.INTERACT;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.info("handle {} action : {}", this.getAction(), params);

		CtiAgent ctiAgent = getCtiAgent(params);
		if (ctiAgent == null) {
			return ERROR_BAD_PARAM;
		}

		String interactObject = params.get(AmiAction.VARIABLE_INTERACT_OBJECT); // ivrId,ivrNode
		String extension = interactObject + "#";

		if (StringUtils.isEmpty(ctiAgent.getMainChannel())) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NO_CHANNEL, "no channel");
		}

		AtxferAction transferAction = new AtxferAction();
		transferAction.setChannel(ctiAgent.getMainChannel());
		transferAction.setContext(Const.DIALPLAN_CONTEXT_INTERACT);
		transferAction.setExten(extension);

		SetVarAction setVarChannelAction = null;
		SetVarAction setVarCnoAction = null;
		if (ctiAgent.getCallType().equals(Const.CDR_CALL_TYPE_PREVIEW_OB)
				|| ctiAgent.getCallType().equals(Const.CDR_CALL_TYPE_OB)) {
			setVarChannelAction = new SetVarAction(ctiAgent.getChannel(), "interact_channel", ctiAgent.getChannel());
			setVarCnoAction = new SetVarAction(ctiAgent.getChannel(), "interact_cno", ctiAgent.getCno());
		} else {
			setVarChannelAction = new SetVarAction(ctiAgent.getMainChannel(), "interact_channel",
					ctiAgent.getChannel());
			setVarCnoAction = new SetVarAction(ctiAgent.getMainChannel(), "interact_cno", ctiAgent.getCno());
		}

		if (sendAction(setVarChannelAction, 1000) == null || sendAction(setVarCnoAction, 1000) == null
				|| sendAction(transferAction, 30000) == null) {
			return ERROR_EXCEPTION;
		}

		return SUCCESS;
	}

}
