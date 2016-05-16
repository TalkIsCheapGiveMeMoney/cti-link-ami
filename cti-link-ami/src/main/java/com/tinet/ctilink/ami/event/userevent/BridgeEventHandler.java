package com.tinet.ctilink.ami.event.userevent;

import java.util.HashMap;
import java.util.Map;

import com.tinet.ctilink.ami.util.AmiUtil;
import com.tinet.ctilink.inc.Const;
import com.tinet.ctilink.json.JSONObject;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.UserEvent;
import org.asteriskjava.manager.userevent.IbBridgeEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiUserEventHandler;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.ami.inc.AmiEventConst;


/**
 *
 * @author tianzp
 */
@Component
public class BridgeEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {

	@Override
	public Class<?> getEventClass() {
		return IbBridgeEvent.class;
	}
	
	@Override
	public void handle(ManagerEvent event) {
		logger.debug("handle {} : {}", getEventClass().getTypeName(), event);

		String enterpriseId = ((IbBridgeEvent) event).getEnterpriseId();
		String customerNumber = ((IbBridgeEvent) event).getCustomerNumber();
		String cno = ((IbBridgeEvent) event).getCno();
		String bridgeTime = ((IbBridgeEvent) event).getBridgeTime();
		String calleeNumber = ((IbBridgeEvent) event).getCalleeNumber();
		String userField = ((IbBridgeEvent) event).getUserField();
		String detailCallType = ((IbBridgeEvent) event).getDetailCallType();
		String callType = ((IbBridgeEvent) event).getCallType();

		JSONObject pushEvent=new JSONObject();
		pushEvent.put("type", AmiParamConst.VARIABLE_EVENT);
		pushEvent.put(AmiParamConst.VARIABLE_NAME, AmiEventConst.BRIDGED);
		pushEvent.put(AmiParamConst.VARIABLE_ENTERPRISE_ID, enterpriseId);
		pushEvent.put(AmiParamConst.VARIABLE_CUSTOMER_NUMBER, customerNumber);
		pushEvent.put(AmiParamConst.VARIABLE_CNO, cno);
		pushEvent.put(AmiParamConst.VARIABLE_BRIDGE_TIME, bridgeTime);
		pushEvent.put(AmiParamConst.VARIABLE_CALLEE_NUMBER, calleeNumber);
		pushEvent.put(AmiParamConst.VARIABLE_USER_FIELD, userField);
		pushEvent.put(AmiParamConst.VARIABLE_DETAIL_CALLTYPE, detailCallType);
		pushEvent.put(AmiParamConst.VARIABLE_CALL_TYPE, callType);
		publishEvent(pushEvent);

//		// 根据企业设置推送Curl
//		AmiUtil.pushCurl(((UserEvent) event).getAsteriskChannel(), pushEvent, Integer.parseInt(enterpriseId),
//				Const.ENTERPRISE_PUSH_TYPE_BRIDGE_IB, Const.CURL_TYPE_BRIDGE_IB);
	}

	

}
