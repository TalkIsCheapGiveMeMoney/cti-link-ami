package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.manager.action.FeatureAction;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.inc.AmiActionTypeConst;
import com.tinet.ctilink.ami.inc.AmiParamConst;


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
		if(StringUtils.isEmpty(channel))
		{
			return AmiActionResponse.createFailResponse(AmiParamConst.ERROR_CODE, "no channel");
		}
		
		String context = (String)params.get(AmiParamConst.DIALPLAN_CONTEXT);			
		String extension = (String)params.get(AmiParamConst.EXTENSION);		
		String feature = (String)params.get(AmiParamConst.FEATURE);		
		
		FeatureAction transferAction = new FeatureAction();
		transferAction.setChannel(channel);
		transferAction.setContext(context);
		transferAction.setExtension(extension);
		transferAction.setFeature(feature);

		if (sendAction(transferAction) == null) {
			return ERROR;
		}
		
		return SUCCESS;
	}



}
