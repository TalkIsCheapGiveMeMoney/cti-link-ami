package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.manager.action.HangupAction;

import com.tinet.ctilink.ami.inc.AmiActionTypeConst;
import com.tinet.ctilink.ami.inc.AmiParamConst;

public class HangupActionHandler extends AbstractActionHandler   {

	@Override
	public String getAction() {
		return AmiActionTypeConst.HANGUP;
	}
	

	@Override
	public AmiActionResponse handle(Map<String, Object> params) {
		// TODO Auto-generated method stub
		
		logger.debug("handle {} action : {}", this.getAction(), params);
		
		String channelName = (String)params.get(AmiParamConst.CHANNEL);
		Integer cause = (Integer)params.get(AmiParamConst.HANGUP_CAUSE);
		if(StringUtils.isEmpty(channelName))
		{
			return AmiActionResponse.createFailResponse(AmiParamConst.ERRORCODE_NO_CHANNEL, "no channel");
		}
		// 构造HangupAction对象，传入consultCancelChannel值
		HangupAction hangupAction = new HangupAction(channelName);
		hangupAction.setCause(cause);

		if(sendAction(hangupAction)==null){
			return ERROR_EXCEPTION;
		}
		
		return SUCCESS;
	}

}
