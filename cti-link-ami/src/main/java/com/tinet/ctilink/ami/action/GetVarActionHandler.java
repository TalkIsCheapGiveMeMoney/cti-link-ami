package com.tinet.ctilink.ami.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.manager.action.GetVarAction;
import org.asteriskjava.manager.response.ManagerResponse;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.inc.AmiActionTypeConst;
import com.tinet.ctilink.ami.inc.AmiParamConst;


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
		return AmiActionTypeConst.GET_VAR;
	}

	@Override
	public AmiActionResponse handle(Map<String, Object> params) {
		logger.info("handle {} action : {}", this.getAction(), params);
		
		String channel =(String) params.get(AmiParamConst.CHANNEL);	
		if(StringUtils.isEmpty(channel))
		{
			return AmiActionResponse.createFailResponse(AmiParamConst.ERROR_CODE, "no channel");
		}
		AmiActionResponse actionResponse = AmiActionResponse.createSuccessResponse();
		actionResponse.setValues(new HashMap<String,Object>());
		
		@SuppressWarnings("unchecked")
		Map<String, String> varMap = (Map<String, String>)(params.get(AmiParamConst.VAR_MAP));			
		for(String varName: varMap.keySet()){
				
			GetVarAction getVarAction = new GetVarAction();
			getVarAction.setChannel(channel);
			getVarAction.setVariable(varName);
					
			ManagerResponse response = sendAction(getVarAction);
			if (response != null)
			{
				actionResponse.getValues().put(varMap.get(varName).toString(), response.getAttribute("Value"));
			}
		}

		return actionResponse;
	}



}
