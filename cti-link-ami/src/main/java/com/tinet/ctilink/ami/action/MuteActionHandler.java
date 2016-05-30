package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.manager.action.MuteAudioAction;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.inc.AmiActionTypeConst;
import com.tinet.ctilink.ami.inc.AmiParamConst;



/**
 * 静音
 * 
 * @author hongzk
 */
@Component
public class MuteActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiActionTypeConst.MUTE;
	}

	@Override
	public AmiActionResponse handle( Map<String, Object> params) {
		logger.info("handle {} action : {}", this.getAction(), params);
		
		String memberChannel = (String)params.get(AmiParamConst.CHANNEL);		
		if(StringUtils.isEmpty(memberChannel))
		{
			return AmiActionResponse.createFailResponse(AmiParamConst.ERRORCODE_NO_CHANNEL, "no channel");
		}
		
		String direction = (String)params.get(AmiParamConst.VARIABLE_MUTE_DIRECTION);
		String state = (String)params.get(AmiParamConst.VARIABLE_MUTE_STATE);
		MuteAudioAction muteAudioAction = new MuteAudioAction(memberChannel);
		muteAudioAction.setDirection(direction);
		muteAudioAction.setState(state);

		if (sendAction(muteAudioAction) == null) {
			return ERROR_EXCEPTION;
		}
		return SUCCESS;
	}



}
