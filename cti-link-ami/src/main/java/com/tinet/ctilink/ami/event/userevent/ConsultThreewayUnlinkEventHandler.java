package com.tinet.ctilink.ami.event.userevent;

import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.ConsultLinkEvent;
import org.asteriskjava.manager.userevent.ConsultThreewayUnlinkEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiUserEventHandler;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.json.JSONObject;
import com.tinet.ctilink.ami.inc.AmiEventTypeConst;


/**
 * 咨询第三方挂断事件
 * 
 * @author tianzp
 */
@Component
public class ConsultThreewayUnlinkEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {

	@Override
	public Class<?> getEventClass() {
		return ConsultThreewayUnlinkEvent.class;
	}

	@Override
	public void handle(ManagerEvent event) {
		logger.info("handle {} : {}.", this.getEventClass().getSimpleName(), event);
		
		String enterpriseId = ((ConsultThreewayUnlinkEvent) event).getEnterpriseId();
		String cno = ((ConsultThreewayUnlinkEvent) event).getCno();
		String consultObject = ((ConsultLinkEvent) event).getConsultObject();
		String objectType = ((ConsultLinkEvent) event).getObjectType();
		
		JSONObject userEvent=new JSONObject();
		userEvent.put(AmiParamConst.EVENT, AmiEventTypeConst.CONSULT_THREEWAY_UNLINK);
		userEvent.put(AmiParamConst.ENTERPRISE_ID, enterpriseId);
		userEvent.put(AmiParamConst.CNO, cno);
		userEvent.put(AmiParamConst.CONSULT_OBJECT, consultObject);
		userEvent.put(AmiParamConst.OBJECT_TYPE, objectType);
		publishEvent(userEvent);

	}
}
