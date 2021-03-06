package com.tinet.ctilink.ami.event.userevent;

import java.util.HashMap;
import java.util.Map;

import com.tinet.ctilink.ami.util.AmiUtil;
import com.tinet.ctilink.inc.Const;
import com.tinet.ctilink.json.JSONObject;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.UserEvent;
import org.asteriskjava.manager.userevent.CallBridgeEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiUserEventHandler;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.ami.inc.AmiEventTypeConst;


/**
 *
 * @author tianzp
 */
@Component
public class BridgeEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {

	@Override
	public Class<?> getEventClass() {
		return CallBridgeEvent.class;
	}
	
	@Override
	public void handle(ManagerEvent event) {
		logger.debug("handle {} : {}", getEventClass().getTypeName(), event);

		String enterpriseId = ((CallBridgeEvent) event).getEnterpriseId();
		String customerNumber = ((CallBridgeEvent) event).getCustomerNumber();
		String cno = ((CallBridgeEvent) event).getCno();
		String bridgeTime = ((CallBridgeEvent) event).getBridgeTime();
		String calleeNumber = ((CallBridgeEvent) event).getCalleeNumber();
		String detailCallType = ((CallBridgeEvent) event).getDetailCallType();
		String callType = ((CallBridgeEvent) event).getCallType();

		
		Map<String, String> userEvent = new HashMap<String, String>();
		userEvent.put(AmiParamConst.EVENT, AmiEventTypeConst.BRIDGED);
		userEvent.put(AmiParamConst.ENTERPRISE_ID, enterpriseId);
		userEvent.put(AmiParamConst.CUSTOMER_NUMBER, customerNumber);
		userEvent.put(AmiParamConst.CNO, cno);
		userEvent.put(AmiParamConst.BRIDGE_TIME, bridgeTime);
		userEvent.put(AmiParamConst.CALLEE_NUMBER, calleeNumber);
		userEvent.put(AmiParamConst.DETAIL_CALLTYPE, detailCallType);
		userEvent.put(AmiParamConst.CALL_TYPE, callType);
		
		JSONObject pushEvent=new JSONObject();
		pushEvent.putAll(userEvent);		

		// 根据企业设置推送Curl
		AmiUtil.pushCurl(((UserEvent) event).getAsteriskChannel(), userEvent, Integer.parseInt(enterpriseId),
				Const.ENTERPRISE_PUSH_TYPE_BRIDGE_IB, Const.CURL_TYPE_BRIDGE_IB);
	}

	

}
