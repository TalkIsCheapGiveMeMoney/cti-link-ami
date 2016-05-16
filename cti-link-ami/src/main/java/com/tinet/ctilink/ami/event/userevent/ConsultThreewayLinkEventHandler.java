package com.tinet.ctilink.ami.event.userevent;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.ConsultThreewayLinkEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiUserEventHandler;
import com.tinet.ctilink.ami.inc.AmiEventConst;


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

		// 发送事件到被咨询的那个人，通知被咨询那个人是谁咨询的他，所以需要发送给前台，前台需要显示谁咨询的。
		if (StringUtils.isNotEmpty(consulteeCno)) {
			Map<String, String> userEvent = new HashMap<String, String>();
			userEvent.put("type", AmiAction.VARIABLE_EVENT);
			userEvent.put(AmiAction.VARIABLE_NAME, AmiEventConst.CONSULT_THREEWAY);
			userEvent.put(AmiAction.VARIABLE_ENTERPRISE_ID, enterpriseId);
			userEvent.put(AmiAction.VARIABLE_CNO, consulteeCno);
			userEvent.put(AmiAction.VARIABLE_CONSULTER_CNO, consulterCno);
			publishEvent(userEvent);
		}

		//发送事件到咨询发起者的那个人
		Map<String, String> userEvent = new HashMap<String, String>();
		userEvent.put("type", AmiAction.VARIABLE_EVENT);
		userEvent.put(AmiAction.VARIABLE_NAME, AmiEventConst.CONSULT_THREEWAY);
		userEvent.put(AmiAction.VARIABLE_ENTERPRISE_ID, enterpriseId);
		userEvent.put(AmiAction.VARIABLE_CNO, consulterCno);
		publishEvent(userEvent);

	}

}