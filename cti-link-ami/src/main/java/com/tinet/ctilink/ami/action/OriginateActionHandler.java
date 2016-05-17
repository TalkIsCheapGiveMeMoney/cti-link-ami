package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.response.ManagerResponse;
import org.springframework.stereotype.Component;

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
	public AmiActionResponse handle(Map<String, String> params) {
		// TODO Auto-generated method stub
		logger.debug("handle {} action : {}", this.getAction(), params);
		
		OriginateAction originateAction;
		ManagerResponse originateResponse = null;
		originateAction = new OriginateAction();
		
//		String ActionId = params.get(AmiParamConst.);
		
		String dstChannel = params.get(AmiParamConst.DEST_CHANNEL);	
		String numberTrunk = params.get(AmiParamConst.CDR_NUMBER_TRUNK);
		String clid = params.get(AmiParamConst.CDR_OB_CLID);
		
//		originateAction.setActionId("dafsdfadsfafa01");
		originateAction.setChannel(dstChannel); //set channel		
		originateAction.setContext("webcall"); // set
		originateAction.setExten(numberTrunk); //set exten
		originateAction.setTimeout((long)60 * 1000); //set timeout
		originateAction.setPriority(new Integer(1)); //set priority 1		
		originateAction.setCallerId(clid);		
		originateAction.setVariables(params);
		originateAction.setAsync(true);
		ManagerResponse res = sendAction(originateAction);
		if(res==null){
			AmiActionResponse amiRes = AmiActionResponse.createFailResponse(-1, "Originate Failed!");
			return amiRes;
		}
		else{
			AmiActionResponse amiRes = AmiActionResponse.createSuccessResponse();			
			amiRes.setValues(res.getAttributes());
			return amiRes;
		}		
	}

	@Override
	public AmiActionResponse handle(Map<String, String> fixParams, Map<String, String> optionalParams) {
		// TODO Auto-generated method stub
		logger.debug("handle {} action : {}", this.getAction(), optionalParams);
		
		OriginateAction originateAction;
		ManagerResponse originateResponse = null;
		originateAction = new OriginateAction();
		
//		String ActionId = params.get(AmiParamConst.);
		
		String dstChannel = fixParams.get(AmiParamConst.DEST_CHANNEL);
		String context = fixParams.get(AmiParamConst.DIALPLAN_CONTEXT);
		
		String numberTrunk = optionalParams.get(AmiParamConst.CDR_NUMBER_TRUNK);
		
		String clid = fixParams.get(AmiParamConst.CDR_OB_CLID);
		
		
	
		
		
//		originateAction.setActionId("dafsdfadsfafa01");
		originateAction.setChannel(dstChannel); //set channel		
		originateAction.setContext(context); // set
		originateAction.setExten(numberTrunk); //set exten
		originateAction.setTimeout((long)60 * 1000); //set timeout
		originateAction.setPriority(new Integer(1)); //set priority 1		
		originateAction.setCallerId(clid);		
		originateAction.setVariables(optionalParams);
		originateAction.setAsync(true);
		ManagerResponse res = sendAction(originateAction);
		if(res==null){
			AmiActionResponse amiRes = AmiActionResponse.createFailResponse(-1, "Originate Failed!");
			return amiRes;
		}
		else{
			AmiActionResponse amiRes = AmiActionResponse.createSuccessResponse();			
			amiRes.setValues(res.getAttributes());
			return amiRes;
		}		
		
		
	}

}
