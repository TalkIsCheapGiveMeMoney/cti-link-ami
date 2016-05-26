package com.tinet.ctilink.ami.event.userevent;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.PreviewOutcallBridgeEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiUserEventHandler;
import com.tinet.ctilink.ami.inc.AmiEventTypeConst;
import com.tinet.ctilink.ami.inc.AmiParamConst;
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

		JSONObject userEvent=new JSONObject();
		userEvent.put(AmiParamConst.VARIABLE_EVENT, AmiEventTypeConst.PREVIEW_OUTCALL_BRIDGE);
		userEvent.put(AmiParamConst.VARIABLE_ENTERPRISE_ID, enterpriseId);
		userEvent.put(AmiParamConst.VARIABLE_CALL_TYPE, callType);
		userEvent.put(AmiParamConst.VARIABLE_CUSTOMER_NUMBER, customerNumber);
		userEvent.put(AmiParamConst.VARIABLE_CUSTOMER_NUMBER_TYPE, customerNumberType);
		userEvent.put(AmiParamConst.VARIABLE_CUSTOMER_AREA_CODE, customerAreaCode);
		userEvent.put(AmiParamConst.VARIABLE_UNIQUEID, mainUniqueId);
		userEvent.put(AmiParamConst.VARIABLE_CNO, cno);
		publishEvent(userEvent);
	}

}
