package com.tinet.ctilink.ami.event.userevent;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.ConsultThreewayLinkEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiUserEventHandler;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.json.JSONObject;
import com.tinet.ctilink.ami.inc.AmiEventTypeConst;


/**
 * 咨询三方事件
 * 
 * @author tianzp
 */
@Component
public class ConsultThreewayLinkEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {

	@Override
	public Class<?> getEventClass() {
		return ConsultThreewayLinkEvent.class;
	}

	@Override
	public void handle(ManagerEvent event) {
		logger.info("handle {} : {}.", this.getEventClass().getSimpleName(), event);

		String enterpriseId = ((ConsultThreewayLinkEvent) event).getEnterpriseId();
		String consulteeCno = ((ConsultThreewayLinkEvent) event).getCno(); // 被咨询的座席
		String consulterCno = ((ConsultThreewayLinkEvent) event).getConsulterCno(); // 发起咨询三方的座席
		
		JSONObject userEvent=new JSONObject();
		userEvent.put(AmiParamConst.VARIABLE_EVENT, AmiEventTypeConst.CONSULT_THREEWAY);
		userEvent.put(AmiParamConst.VARIABLE_ENTERPRISE_ID, enterpriseId);
		userEvent.put(AmiParamConst.VARIABLE_CNO, consulteeCno);
		userEvent.put(AmiParamConst.VARIABLE_CONSULTER_CNO, consulterCno);
		publishEvent(userEvent);
		
/*
		// 发送事件到被咨询的那个人，通知被咨询那个人是谁咨询的他，所以需要发送给前台，前台需要显示谁咨询的。
		if (StringUtils.isNotEmpty(consulteeCno)) {
			JSONObject userEvent=new JSONObject();
			userEvent.put(AmiParamConst.VARIABLE_EVENT, AmiEventTypeConst.CONSULT_THREEWAY);
			userEvent.put(AmiParamConst.VARIABLE_ENTERPRISE_ID, enterpriseId);
			userEvent.put(AmiParamConst.VARIABLE_CNO, consulteeCno);
			userEvent.put(AmiParamConst.VARIABLE_CONSULTER_CNO, consulterCno);
			publishEvent(userEvent);
		}

		//发送事件到咨询发起者的那个人
		JSONObject userEvent=new JSONObject();
		userEvent.put(AmiParamConst.VARIABLE_EVENT, AmiEventTypeConst.CONSULT_THREEWAY);
		userEvent.put(AmiParamConst.VARIABLE_ENTERPRISE_ID, enterpriseId);
		userEvent.put(AmiParamConst.VARIABLE_CNO, consulterCno);
		publishEvent(userEvent);
*/
	}

}
