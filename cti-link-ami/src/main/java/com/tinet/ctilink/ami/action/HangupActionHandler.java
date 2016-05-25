package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.asteriskjava.manager.action.HangupAction;

import com.tinet.ctilink.ami.action.AmiActionResponse;
import com.tinet.ctilink.ami.inc.AmiActionTypeConst;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.inc.StringUtil;

public class HangupActionHandler extends AbstractActionHandler   {

	@Override
	public String getAction() {
		// TODO Auto-generated method stub
//		return null;
		return AmiActionTypeConst.HANGUP;
	}
	

	@Override
	public AmiActionResponse handle(Map<String, Object> params) {
		// TODO Auto-generated method stub
		
		logger.debug("handle {} action : {}", this.getAction(), params);
		
		String channelName = (String)params.get(AmiParamConst.VARIABLE_CURRENT_CHANNEL);
		if(StringUtil.isEmpty(channelName))
		{
			return AmiActionResponse.createFailResponse(AmiParamConst.ERRORCODE_NO_CHANNEL, "no channel");
		}
		// 构造HangupAction对象，传入consultCancelChannel值
		HangupAction hangupAction = new HangupAction(channelName);
		hangupAction.setCause(new Integer(99));

		if(sendAction(hangupAction)==null){
			return ERROR_EXCEPTION;
		}
		
		return SUCCESS;
	}

}
