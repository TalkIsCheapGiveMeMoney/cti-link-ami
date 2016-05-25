package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.asteriskjava.manager.action.GetVarAction;
import org.asteriskjava.manager.action.RedirectAction;
import org.asteriskjava.manager.action.SetVarAction;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.inc.AmiActionTypeConst;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.inc.StringUtil;


/**
 * 获取通道变量值
 *
 * 
 * @author hongzk
 */
@Component
public class GetVarActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiActionTypeConst.SET_VAR;
	}

	@Override
	public AmiActionResponse handle(Map<String, Object> params) {
		logger.info("handle {} action : {}", this.getAction(), params);
		
		String channel =(String) params.get(AmiParamConst.CHANNEL);	
		if(StringUtil.isEmpty(channel))
		{
			return AmiActionResponse.createFailResponse(AmiParamConst.ERRORCODE_NO_CHANNEL, "no channel");
		}
		
		String channelVarName = (String)params.get(AmiParamConst.CHANNEL_VARIABLE_NAME);			
		String channelVarValue = (String)params.get(AmiParamConst.CHANNEL_VARIABLE_VALUE);	
				
		GetVarAction getVarAction = new GetVarAction();
		getVarAction.setChannel(channel);
		getVarAction.setVariable(channelVarName);
				
		if (sendAction(getVarAction) == null)
		{
			return ERROR_EXCEPTION;
		}

		return SUCCESS;
	}



}
