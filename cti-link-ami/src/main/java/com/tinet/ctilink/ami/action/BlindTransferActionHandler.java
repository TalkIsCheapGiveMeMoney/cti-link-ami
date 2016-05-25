package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.asteriskjava.manager.action.FeatureAction;
import org.asteriskjava.manager.action.RedirectAction;
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
public class BlindTransferActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiActionTypeConst.TRANSFER;
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
				
		FeatureAction transferAction = new FeatureAction();
		transferAction.setChannel(channel);
		transferAction.setContext(context);
		transferAction.setExtension(extension);
		transferAction.setFeature("blindxfer");

		if (sendAction(transferAction) == null) {
			return ERROR_EXCEPTION;
		}
		
		return SUCCESS;
	}



}
