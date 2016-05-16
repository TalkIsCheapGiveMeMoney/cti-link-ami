package com.tinet.ctilink.ami.event.userevent;

import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.AnswerEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiUserEventHandler;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.ami.inc.AmiEventConst;
import com.tinet.ctilink.json.JSONObject;


/**
 * 系统应答事件
 * 
 * @author tianzp
 */
@Component
public class AnswerEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler{

	@Override
	public Class<?> getEventClass() {
		return AnswerEvent.class;
	}

	@Override
	public void handle(ManagerEvent event) {
		logger.info("handle {} : {}.", this.getEventClass().getSimpleName(), event);
		
		String enterpriseId = ((AnswerEvent) event).getEnterpriseId();
		String callType = ((AnswerEvent) event).getCallType();
		String customerNumber = ((AnswerEvent) event).getCustomerNumber();
		String customerNumberType = ((AnswerEvent) event).getCustomerNumberType();
		String customerAreaCode = ((AnswerEvent) event).getCustomerAreaCode();
		
		JSONObject userEvent=new JSONObject();
		userEvent.put("type", AmiParamConst.VARIABLE_EVENT);
		userEvent.put(AmiParamConst.VARIABLE_NAME, AmiEventConst.ANSWER);
		userEvent.put(AmiParamConst.VARIABLE_ENTERPRISE_ID, enterpriseId);
		userEvent.put(AmiParamConst.VARIABLE_CALL_TYPE, callType);
		userEvent.put(AmiParamConst.VARIABLE_CUSTOMER_NUMBER, customerNumber);
		userEvent.put(AmiParamConst.VARIABLE_CUSTOMER_NUMBER_TYPE, customerNumberType);
		userEvent.put(AmiParamConst.VARIABLE_CUSTOMER_AREA_CODE, customerAreaCode);

		publishEvent(userEvent);
	}

}
