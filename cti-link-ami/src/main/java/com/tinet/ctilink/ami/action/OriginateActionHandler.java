package com.tinet.ctilink.ami.action;

import java.util.Date;
import java.util.Map;

import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.response.ManagerResponse;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.action.callback.OriginateActionCallback;
import com.tinet.ctilink.ami.inc.AmiActionTypeConst;
import com.tinet.ctilink.ami.inc.AmiParamConst;

@Component
public class OriginateActionHandler extends AbstractActionHandler {


	@Override
	public String getAction() {
		// TODO Auto-generated method stub
		return AmiActionTypeConst.ORIGINATE;
	}

	@Override
	public AmiActionResponse handle(Map<String, Object> params) {
		// TODO Auto-generated method stub
		
		
		OriginateAction originateAction;
		ManagerResponse originateResponse = null;
		int timeout = 60; 
		Map<String, String> actionMap = null;
		Map<String, String> callbackMap = null;
		Map<String, String> chanvarMap = null;
		
		actionMap = (Map<String, String>)(params.get(AmiParamConst.ACTION_MAP));
		if(actionMap == null)
		{
			logger.error("Parameter name "+ AmiParamConst.ACTION_MAP + " is empty!!!!!");
			return ERROR_BAD_PARAM;
		}
		
		chanvarMap = (Map<String, String>)(params.get(AmiParamConst.CHANNEL_VAR_MAP));
		
		originateAction = new OriginateAction();			
		String dstChannel = actionMap.get(AmiParamConst.DEST_CHANNEL);
		if(dstChannel.isEmpty())
		{
			logger.error("Parameter name "+ AmiParamConst.DEST_CHANNEL + " is empty!!!!!");
			return ERROR_BAD_PARAM;
		}
		String context = actionMap.get(AmiParamConst.DIALPLAN_CONTEXT);			
		String clid = actionMap.get(AmiParamConst.CLID);
		String extension = actionMap.get(AmiParamConst.EXTENSION);
		String otherChannelId = actionMap.get(AmiParamConst.OTHER_CHANNEL_ID);
		try{
			timeout = Integer.parseInt(actionMap.get(AmiParamConst.ORIGINATE_TIMEOUT));
		}catch(NumberFormatException e)
		{
			timeout = 60;
			e.printStackTrace();
		}
		originateAction.setChannel(dstChannel); //set channel	
		originateAction.setOtherChannelId(otherChannelId);
		originateAction.setContext(context); // set
		originateAction.setExten(extension); //set extension
		originateAction.setTimeout((long)timeout * 1000); //set timeout
		originateAction.setPriority(new Integer(1)); //set priority 1		
		originateAction.setCallerId(clid);		
		originateAction.setVariables(chanvarMap);
		
		callbackMap = (Map<String, String>)(params.get(AmiParamConst.CALLBACK_MAP));
		OriginateActionCallback observer = null;
		if(callbackMap!=null)
		{
			observer = new OriginateActionCallback();
			observer.setOriginateDataArray(callbackMap);
		}
		originateAsync(originateAction,observer);
		return SUCCESS;
				
	}
}
