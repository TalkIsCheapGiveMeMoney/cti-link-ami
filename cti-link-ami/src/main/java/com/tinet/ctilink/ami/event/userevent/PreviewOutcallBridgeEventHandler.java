package com.tinet.ctilink.ami.event.userevent;

import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.UserEvent;
import org.asteriskjava.manager.userevent.PreviewOutcallBridgeEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiUserEventHandler;
import com.tinet.ctilink.ami.inc.AmiEventTypeConst;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.ami.util.AmiUtil;
import com.tinet.ctilink.inc.Const;
import com.tinet.ctilink.json.JSONObject;

/**
 * 预览外呼接通
 * 
 * @author tianzp
 */
@Component
public class PreviewOutcallBridgeEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {

	@Override
	public Class<?> getEventClass() {
		return PreviewOutcallBridgeEvent.class;
	}

	@Override
	public void handle(ManagerEvent event) {
		logger.info("handle {} : {}.", this.getEventClass().getSimpleName(), event);

		String enterpriseId = ((PreviewOutcallBridgeEvent) event).getEnterpriseId();
		String callType = ((PreviewOutcallBridgeEvent) event).getCallType();
		String customerNumber = ((PreviewOutcallBridgeEvent) event).getCustomerNumber();
		String customerNumberType = ((PreviewOutcallBridgeEvent) event).getCustomerNumberType();
		String customerAreaCode = ((PreviewOutcallBridgeEvent) event).getCustomerAreaCode();
		String cno = ((PreviewOutcallBridgeEvent) event).getCno();
		String mainUniqueId = ((PreviewOutcallBridgeEvent) event).getMainUniqueId();
		String destChannel = ((PreviewOutcallBridgeEvent) event).getDestChan();
		String channel = ((PreviewOutcallBridgeEvent) event).getChannel();
		
		Map<String, String> userEvent = new HashMap<String, String>();
		
		userEvent.put(AmiParamConst.EVENT, AmiEventTypeConst.PREVIEW_OUTCALL_BRIDGE);
		userEvent.put(AmiParamConst.ENTERPRISE_ID, enterpriseId);
		userEvent.put(AmiParamConst.CALL_TYPE, callType);
		userEvent.put(AmiParamConst.CUSTOMER_NUMBER, customerNumber);
		userEvent.put(AmiParamConst.CUSTOMER_NUMBER_TYPE, customerNumberType);
		userEvent.put(AmiParamConst.CUSTOMER_AREA_CODE, customerAreaCode);
		userEvent.put(AmiParamConst.MAIN_UNIQUE_ID, mainUniqueId);
		userEvent.put(AmiParamConst.CNO, cno);
		userEvent.put(AmiParamConst.CHANNEL, channel);
		userEvent.put(AmiParamConst.DEST_CHANNEL, destChannel);
		
		JSONObject pushEvent=new JSONObject();
		pushEvent.putAll(userEvent);
		publishEvent(pushEvent);
		
		
		AmiUtil.pushCurl(((UserEvent) event).getAsteriskChannel(), userEvent, Integer.parseInt(enterpriseId),
				Const.ENTERPRISE_PUSH_TYPE_BRIDGE_OB, Const.CURL_TYPE_BRIDGE_OB);
	}

}
