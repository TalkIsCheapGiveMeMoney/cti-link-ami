package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.asteriskjava.manager.action.IndicateAction;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.inc.AmiActionTypeConst;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.inc.StringUtil;

/**
 * 电话保持
 * 
 * @author hongzk
 */
@Component
public class IndicateActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiActionTypeConst.INDICATE;
	}

	@Override
	public AmiActionResponse handle(Map<String, Object> params) {
		logger.debug("handle {} action : {}", this.getAction(), params);
		
		String channel =(String) params.get(AmiParamConst.CHANNEL);	
		if(StringUtil.isEmpty(channel))
		{
			return AmiActionResponse.createFailResponse(AmiParamConst.ERRORCODE_NO_CHANNEL, "no channel");
		}
		
		int code = 0;
		String holdOpCode = (String)params.get(AmiParamConst.VARIABLE_OP_CODE);
		if(StringUtil.isNotEmpty(holdOpCode))
		{
			try{
				code = Integer.parseInt((String)params.get(AmiParamConst.VARIABLE_OP_CODE));
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			if(code!=16&&code!=17) // 16是保持的指令代码 17是取消保持的指令代码
			{
				return AmiActionResponse.createFailResponse(AmiParamConst.ERRORCODE_BAD_PARAM, "Hold's opCode is wrong!");
			}
		}
		
		IndicateAction holdAction = new IndicateAction();
		holdAction.setChannel(channel);
		holdAction.setIndicate(code);

		if (sendAction(holdAction) == null) {
			return ERROR_EXCEPTION;
		}
		
		return SUCCESS;
	}


}
