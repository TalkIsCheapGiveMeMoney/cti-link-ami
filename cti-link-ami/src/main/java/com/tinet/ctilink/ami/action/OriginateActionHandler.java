package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.asteriskjava.manager.action.OriginateAction;
import org.springframework.stereotype.Component;

import com.github.pagehelper.StringUtil;
import com.tinet.ctilink.ami.action.callback.OriginateActionCallback;
import com.tinet.ctilink.ami.inc.AmiActionTypeConst;
import com.tinet.ctilink.ami.inc.AmiParamConst;

@Component
public class OriginateActionHandler extends AbstractActionHandler {


	@Override
	public String getAction() {
		return AmiActionTypeConst.ORIGINATE;
	}

	@Override
	public AmiActionResponse handle(Map<String, Object> params) {
		logger.info("The begin of OriginateActionHandler:handle");
		logger.info("handle {} action : {}", this.getAction(), params);
		
		OriginateAction originateAction;
		int timeout = 60; 
		Map<String, String> actionMap = null;
		Map<String, String> callbackMap = null;
		Map<String, String> chanvarMap = null;
		
		actionMap = (Map<String, String>)(params.get(AmiParamConst.ACTION_MAP));
		if(actionMap == null)
		{
			logger.error("Parameter name "+ AmiParamConst.ACTION_MAP + " is empty!!!!!");
			return ERROR;
		}		
		chanvarMap = (Map<String, String>)(params.get(AmiParamConst.VAR_MAP));	
		
		originateAction = new OriginateAction();			
		String dstChannel = actionMap.get(AmiParamConst.CHANNEL);
		if(StringUtil.isEmpty(dstChannel))
		{
			logger.error("Parameter name "+ AmiParamConst.CHANNEL + " is empty!!!!!");
			return ERROR;
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
		
		callbackMap = (Map<String, String>)(params.get(AmiParamConst.ACTION_EVENT));
		OriginateActionCallback observer = null;
		if(callbackMap!=null)
		{
			observer = new OriginateActionCallback();
			observer.setOriginateDataArray(callbackMap);
		}
		originateAsync(originateAction,observer);
		logger.info("The end of OriginateActionHandler:handle");
		return SUCCESS;
				
	}
}
