package com.tinet.ctilink.ami.action;

import java.util.Map;
import org.asteriskjava.manager.action.AtxferAction;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.inc.AmiActionTypeConst;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.inc.StringUtil;


/**
 * 咨询
 * 
 * @author hongzk
 */
@Component
public class ConsultActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiActionTypeConst.CONSULT;
	}

	@Override
	public AmiActionResponse handle(Map<String, Object> params) {
		logger.info("handle {} action : {}", this.getAction(), params);
		
		String channel =(String) params.get(AmiParamConst.CHANNEL);	
		if(StringUtil.isEmpty(channel))
		{
			return AmiActionResponse.createFailResponse(AmiParamConst.ERRORCODE_NO_CHANNEL, "no channel");
		}
		
		String context = (String)params.get(AmiParamConst.DIALPLAN_CONTEXT);			
		String extension = (String)params.get(AmiParamConst.EXTENSION);		
//		String consultObject = params.get(AmiAction.VARIABLE_CONSULT_OBJECT); // 电话号码
//		// 座席号
//		// 分机号
//		String objectType = params.get(AmiAction.VARIABLE_OBJECT_TYPE); // 0.电话
//		// 1.座席号
//		// 2.分机
		//String extension = objectType + consultObject + "#";
		

				
		AtxferAction transferAction = new AtxferAction();
		transferAction.setChannel(channel);
		transferAction.setContext(context);
		transferAction.setExten(extension);

		if (sendAction(transferAction, 30000) == null) {
			return ERROR_EXCEPTION;
		}

		return SUCCESS;
	}



}
