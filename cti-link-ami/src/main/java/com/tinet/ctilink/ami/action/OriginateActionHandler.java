package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.asteriskjava.manager.action.OriginateAction;
import org.springframework.stereotype.Component;

import com.github.pagehelper.StringUtil;
import com.tinet.ctilink.ami.action.callback.OriginateActionCallback;
import com.tinet.ctilink.ami.inc.AmiActionTypeConst;
import com.tinet.ctilink.ami.inc.AmiChanVarNameConst;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.json.JSONObject;

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

		Map<String, Object> actionMap = null;
		JSONObject actionEvent = null;
		Map<String, String> varMap = null;
		
		actionMap = (Map<String, Object>)(params.get(AmiParamConst.ACTION_MAP));
		if(actionMap == null)
		{
			logger.error("Parameter name "+ AmiParamConst.ACTION_MAP + " is empty!!!!!");
			return ERROR;
		}		
		varMap = (Map<String, String>)(params.get(AmiParamConst.VAR_MAP));	
		varMap.put(AmiChanVarNameConst.HANGUP_HANDLER_PUSH, "\""+ "hangup_handler,push_cdr,1"+ "\"");
		varMap.put(AmiChanVarNameConst.HANGUP_HANDLER_PUSH, "\""+ "hangup_handler,stop_monitor,1"+ "\"");
		
		originateAction = new OriginateAction();
		if(actionMap.get(AmiParamConst.CHANNEL) == null)
		{
			logger.error("Parameter name "+ AmiParamConst.CHANNEL  + " in ACTION_MAP is NULL!!!!!");
			return ERROR;
		}
		String dstChannel = actionMap.get(AmiParamConst.CHANNEL).toString();
		if(StringUtil.isEmpty(dstChannel))
		{
			logger.error("Parameter name "+ AmiParamConst.CHANNEL + "in ACTION_MAP is empty!!!!!");
			return ERROR;
		}
		String context = "";
		String clid = "";
		String extension = "";
		try{
			context = actionMap.get(AmiParamConst.DIALPLAN_CONTEXT).toString();			
			clid = actionMap.get(AmiParamConst.CLID).toString();
			extension = actionMap.get(AmiParamConst.EXTENSION).toString();
		}catch(Exception e)
		{
			logger.error("Parameter name " + AmiParamConst.DIALPLAN_CONTEXT + " or " + AmiParamConst.CLID + " or "+AmiParamConst.EXTENSION +" in ACTION_MAP is empty!!!!!");
			return ERROR;
		}
		if(actionMap.get(AmiParamConst.OTHER_CHANNEL_ID) != null)
		{
			String otherChannelId = actionMap.get(AmiParamConst.OTHER_CHANNEL_ID).toString();
			originateAction.setOtherChannelId(otherChannelId);
		}
		
		Integer timeout = (Integer)actionMap.get(AmiParamConst.ORIGINATE_TIMEOUT);
		if(timeout == null || timeout < 0 || timeout > 60){
			timeout = 60;
		}
		
		originateAction.setChannel(dstChannel); //set channel		
		originateAction.setContext(context); // set
		originateAction.setExten(extension); //set extension
		originateAction.setTimeout((long)timeout * 1000); //set timeout
		originateAction.setPriority(new Integer(1)); //set priority 1		
		originateAction.setCallerId(clid);		
		originateAction.setVariables(varMap);
		
		actionEvent = (JSONObject)(params.get(AmiParamConst.ACTION_EVENT));
		OriginateActionCallback observer = null;
		if(actionEvent!=null)
		{
			observer = new OriginateActionCallback();
			observer.setOriginateDataArray(actionEvent);
		}
		originateAsync(originateAction,observer);
		logger.info("The end of OriginateActionHandler:handle");
		return SUCCESS;
				
	}
}
