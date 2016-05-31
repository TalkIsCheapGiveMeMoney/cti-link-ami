package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.manager.action.SetVarAction;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.inc.AmiActionTypeConst;
import com.tinet.ctilink.ami.inc.AmiParamConst;


/**
 * 设置通道变量
 * 
 * @author hongzk
 */
@Component
public class SetVarActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiActionTypeConst.SET_VAR;
	}

	@Override
	public AmiActionResponse handle(Map<String, Object> params) {
		logger.info("handle {} action : {}", this.getAction(), params);
		
		String channel =(String) params.get(AmiParamConst.CHANNEL);	
		if(StringUtils.isEmpty(channel))
		{
			return AmiActionResponse.createFailResponse(AmiParamConst.ERROR_CODE, "no channel");
		}
		
		AsteriskChannel asteriskChannel = amiManager.getManager().getAsteriskServer().getChannelByName(channel);
		if(asteriskChannel == null){
			return AmiActionResponse.createFailResponse(AmiParamConst.ERROR_CODE, "no channel");
		}
		
		@SuppressWarnings("unchecked")
		Map<String, String> varMap = (Map<String, String>)(params.get(AmiParamConst.VAR_MAP));			
		for(String varName: varMap.keySet()){
			String varValue = varMap.get(varName);
			
			
			SetVarAction setVarAction = new SetVarAction();
			setVarAction.setChannel(channel);
			setVarAction.setVariable(varName);
			setVarAction.setValue(varValue);
			
			if (sendAction(setVarAction) == null)
			{
				return ERROR;
			}
			asteriskChannel.setVariable(varName, varValue);
		}

		return SUCCESS;
	}



}
