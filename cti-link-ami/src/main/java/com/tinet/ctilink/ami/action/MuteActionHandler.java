package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.inc.AmiParamConst;


/**
 * 静音
 * 
 * @author tianzp
 */
@Component
public class MuteActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiParamConst.MUTE;
	}

	@Override
	public AmiActionResponse handle( Map<String, Object> params) {
		logger.info("handle {} action : {}", this.getAction(), params);
		
//		String direction = params.get(AmiParamConst.VARIABLE_MUTE_DIRECTION);
//		String state = params.get(AmiParamConst.VARIABLE_MUTE_STATE);

//		CtiAgent ctiAgent = getCtiAgent(params);
//		if (ctiAgent == null) {
//			return ERROR_BAD_PARAM;
//		}
//
//		String memberChannel = null;
//		if (ctiAgent.getCallType() == Const.CDR_CALL_TYPE_OB
//				|| ctiAgent.getCallType() == Const.CDR_CALL_TYPE_PREVIEW_OB) {// 先呼座席
//			memberChannel = ctiAgent.getMainChannel();
//		} else {
//			memberChannel = ctiAgent.getChannel();
//		}
//
//		if (StringUtils.isEmpty(memberChannel) || !CtiAgent.BUSY.equals(ctiAgent.getDeviceStatus())) {
//			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NO_CHANNEL, "no channel to mute");
//		}
//
//		if (StringUtils.isEmpty(direction)
//				|| (!direction.equals("in") && !direction.equals("out") && !direction.equals("all"))) {
//			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NO_CHANNEL,
//					"direction should be in|out|all");
//		}
//
//		if (StringUtils.isEmpty(state) || (!state.equals("on") && !state.equals("off"))) {
//			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NO_CHANNEL, "state should be on|off");
//		}
//
//		MuteAudioAction muteAudioAction = new MuteAudioAction(memberChannel);
//		muteAudioAction.setDirection(direction);
//		muteAudioAction.setState(state);
//
//		if (sendAction(muteAudioAction) == null) {
//			return ERROR_EXCEPTION;
//		}
		return SUCCESS;
	}



}
