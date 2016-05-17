package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.inc.AmiParamConst;


/**
 * 强拆
 * 
 * @author tianzp
 */
@Component
public class DisconnectActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiParamConst.DISCONNECT;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.info("handle {} action : {}", this.getAction(), params);

/*		CtiAgent ctiAgent = getCtiAgent(params);
		if (ctiAgent == null) {
			return ERROR_BAD_PARAM;
		}

		String disconnectedCno = StringUtils.trimToEmpty(params.get(AmiAction.VARIABLE_DISCONNECTED_CNO));
		CtiAgent disconnectedAgent = ctiAgentService.get(ctiAgent.getEnterpriseId() + disconnectedCno);
		if (disconnectedAgent == null) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NO_SUCH_CNO, "no such cno");
		}

		String disconnectedChannel = null;
		String otherChannel = null;

		// 先呼座席
		if (disconnectedAgent.getCallType() == Const.CDR_CALL_TYPE_OB
				|| disconnectedAgent.getCallType() == Const.CDR_CALL_TYPE_PREVIEW_OB) {
			disconnectedChannel = disconnectedAgent.getChannel();
			otherChannel = disconnectedAgent.getMainChannel();
		} else {
			disconnectedChannel = disconnectedAgent.getMainChannel();
			otherChannel = disconnectedAgent.getChannel();
		}

		if (StringUtils.isEmpty(disconnectedChannel) || !disconnectedAgent.getDeviceStatus().equals(CtiAgent.BUSY)) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NO_SUCH_CNO, "no such cno");
		}

		if (StringUtils.isNotEmpty(otherChannel)) {
			SetVarAction setVarAction = new SetVarAction();
			setVarAction.setChannel(otherChannel);
			setVarAction.setVariable(Const.CDR_FORCE_DISCONNECT);
			setVarAction.setValue("1");

			sendAction(setVarAction);
		}

		HangupAction hangupAction = new HangupAction(disconnectedChannel);
		hangupAction.setCause(Const.CDR_HANGUP_CAUSE_DISCONNECT);

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
