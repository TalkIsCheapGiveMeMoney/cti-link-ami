package com.tinet.ctilink.ami.event.userevent;

import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.UserEvent;
import org.asteriskjava.manager.userevent.IncomingEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiUserEventHandler;
import com.tinet.ctilink.ami.inc.AmiEventTypeConst;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.ami.util.AmiUtil;
import com.tinet.ctilink.cache.RedisService;
import com.tinet.ctilink.inc.Const;
import com.tinet.ctilink.json.JSONObject;

/**
 * 来电事件处理
 * 
 * @author tianzp
 */
@Component
public class IncomingEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {

	@Autowired
	private RedisService redisService;
	@Override
	public Class<?> getEventClass() {
		return IncomingEvent.class;
	}
	
	@Override
	public void handle(ManagerEvent event) {
		logger.info("handle {} : {}.", this.getEventClass().getSimpleName(), event);
		
		String enterpriseId = ((IncomingEvent) event).getEnterpriseId();
		String callType = ((IncomingEvent) event).getCallType();
		String customerNumber = ((IncomingEvent) event).getCustomerNumber();
		String customerNumberType = ((IncomingEvent) event).getCustomerNumberType();
		String customerAreaCode = ((IncomingEvent) event).getCustomerAreaCode();
		String ivrId =  ((IncomingEvent) event).getIvrId();

		
		Map<String, String> userEvent = new HashMap<String, String>();
		userEvent.put(AmiParamConst.VARIABLE_EVENT,AmiEventTypeConst.INCOMING);
		userEvent.put(AmiParamConst.VARIABLE_ENTERPRISE_ID, enterpriseId);
		userEvent.put(AmiParamConst.VARIABLE_CALL_TYPE, callType);
		userEvent.put(AmiParamConst.VARIABLE_CUSTOMER_NUMBER, customerNumber);
		userEvent.put(AmiParamConst.VARIABLE_CUSTOMER_NUMBER_TYPE, customerNumberType);
		userEvent.put(AmiParamConst.VARIABLE_CUSTOMER_AREA_CODE, customerAreaCode);

		JSONObject pushEvent=new JSONObject();
		pushEvent.putAll(userEvent);
		publishEvent(pushEvent);

		// 根据企业设置推送Curl
		AmiUtil.pushCurl(((UserEvent) event).getAsteriskChannel(), userEvent, Integer.parseInt(enterpriseId), Const.ENTERPRISE_PUSH_TYPE_INCOMING_IB,
				Const.CURL_TYPE_INCOMING);
		
	}
}
