package com.tinet.ctilink.ami.event;

import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.AnswerEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.ami.AmiEvent;

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
		
		Map<String, String> userEvent = new HashMap<String, String>();
		userEvent.put("type", AmiAction.VARIABLE_EVENT);
		userEvent.put(AmiAction.VARIABLE_NAME, AmiEvent.ANSWER);
		userEvent.put(AmiAction.VARIABLE_ENTERPRISE_ID, enterpriseId);
		userEvent.put(AmiAction.VARIABLE_CALL_TYPE, callType);
		userEvent.put(AmiAction.VARIABLE_CUSTOMER_NUMBER, customerNumber);
		userEvent.put(AmiAction.VARIABLE_CUSTOMER_NUMBER_TYPE, customerNumberType);
		userEvent.put(AmiAction.VARIABLE_CUSTOMER_AREA_CODE, customerAreaCode);

		publishEvent(userEvent);
	}

}
