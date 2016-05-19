package com.tinet.ctilink.ami.event.userevent;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.UnanswerEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiUserEventHandler;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.ami.inc.AmiEventTypeConst;
import com.tinet.ctilink.json.JSONObject;



/**
 * 系统未接应答事件
 * 
 * @author tianzp
 */
@Component
public class UnanswerEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {

	@Override
	public Class<?> getEventClass() {
		return UnanswerEvent.class;
	}

	@Override
	public void handle(ManagerEvent event) {
		logger.info("handle {} : {}.", this.getEventClass().getSimpleName(), event);

		String enterpriseId = ((UnanswerEvent) event).getEnterpriseId();
		String callType = ((UnanswerEvent) event).getCallType();
		String customerNumber = ((UnanswerEvent) event).getCustomerNumber();
		String customerNumberType = ((UnanswerEvent) event).getCustomerNumberType();
		String customerAreaCode = ((UnanswerEvent) event).getCustomerAreaCode();
		String queue = ((UnanswerEvent) event).getQueue();
		String cno = ((UnanswerEvent) event).getCno();
		String startTime = ((UnanswerEvent) event).getStartTime();

		JSONObject userEvent=new JSONObject();
		userEvent.put(AmiParamConst.VARIABLE_EVENT,AmiEventTypeConst.UNANSWER);
//		userEvent.put("type", AmiParamConst.VARIABLE_EVENT);
//		userEvent.put(AmiParamConst.VARIABLE_NAME, AmiEventConst.UNANSWER);
		userEvent.put(AmiParamConst.VARIABLE_ENTERPRISE_ID, enterpriseId);
		userEvent.put(AmiParamConst.VARIABLE_CALL_TYPE, callType);
		userEvent.put(AmiParamConst.VARIABLE_CUSTOMER_NUMBER, customerNumber);
		userEvent.put(AmiParamConst.VARIABLE_CUSTOMER_NUMBER_TYPE, customerNumberType);
		userEvent.put(AmiParamConst.VARIABLE_CUSTOMER_AREA_CODE, customerAreaCode);
		userEvent.put(AmiParamConst.VARIABLE_START_TIME, startTime);

		if (!StringUtils.isEmpty(cno)) {
			userEvent.put(AmiParamConst.VARIABLE_CNO, cno);
		} else if (!StringUtils.isEmpty(queue)) {
			userEvent.put(AmiParamConst.VARIABLE_QNO, queue);
			userEvent.put(AmiParamConst.VARIABLE_AGENT_QUEUE, queue);
		}

		publishEvent(userEvent);
	}

}
