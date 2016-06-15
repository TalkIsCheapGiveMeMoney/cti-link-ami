package com.tinet.ctilink.ami.event.userevent;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.ConsultLinkEvent;
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
		String cno = ((ConsultThreewayLinkEvent) event).getCno(); // 被咨询的座席
		String consultObject = ((ConsultThreewayLinkEvent) event).getConsultObject();
		String objectType = ((ConsultThreewayLinkEvent) event).getObjectType();
		
		JSONObject userEvent=new JSONObject();
		userEvent.put(AmiParamConst.EVENT, AmiEventTypeConst.CONSULT_THREEWAY);
		userEvent.put(AmiParamConst.ENTERPRISE_ID, enterpriseId);
		userEvent.put(AmiParamConst.CNO, cno);
		userEvent.put(AmiParamConst.CONSULT_OBJECT, consultObject);
		userEvent.put(AmiParamConst.OBJECT_TYPE, objectType);
		publishEvent(userEvent);
		
	}

}
