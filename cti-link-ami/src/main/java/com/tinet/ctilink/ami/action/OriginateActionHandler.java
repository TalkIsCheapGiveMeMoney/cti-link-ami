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

	final int timeout = 60; 
	@Override
	public String getAction() {
		// TODO Auto-generated method stub
		return AmiActionTypeConst.ORIGINATE;
	}

	@Override
	public AmiActionResponse handle(Map<String, Object> params) {
		// TODO Auto-generated method stub
		logger.debug("handle {} action : {}", this.getAction(), params);
		
		OriginateAction originateAction;
		ManagerResponse originateResponse = null;
		
//		
//		Map<String, String> actionMap ;
//		actionMap =(Map<String, String>)(params.get(AmiParamConst.ACTION_MAP));
//		originateAction = new OriginateAction();
//		
//		
//		String dstChannel = params.remove(AmiParamConst.DEST_CHANNEL);
//		String context = params.remove(AmiParamConst.DIALPLAN_CONTEXT);			
//		String clid = params.remove(AmiParamConst.OB_CLID);
//		String extension = params.remove(AmiParamConst.EXTENSION);
//		String clientData = params.remove(AmiParamConst.REQUEST_USER_DATA);
//
//		originateAction.setChannel(dstChannel); //set channel		
//		originateAction.setContext(context); // set
//		originateAction.setExten(extension); //set extension
//		originateAction.setTimeout((long)timeout * 1000); //set timeout
//		originateAction.setPriority(new Integer(1)); //set priority 1		
//		originateAction.setCallerId(clid);		
//		originateAction.setVariables(params);
//		
//		OriginateActionCallback observer = new OriginateActionCallback();
//		observer.setOriginateClientData(clientData);
//		originateAsync(originateAction,observer);
//		AmiActionResponse amiRes = AmiActionResponse.createSuccessResponse();			
//		return amiRes;
		return SUCCESS;
				
	}

	
	

}
