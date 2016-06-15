package com.tinet.ctilink.ami.event.userevent;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.ConsultLinkEvent;
import org.asteriskjava.manager.userevent.ConsultTransferEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiUserEventHandler;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.json.JSONObject;
import com.tinet.ctilink.ami.inc.AmiEventTypeConst;


/**
 * 咨询转接事件
 * @author tianzp
 */
@Component
public class ConsultTransferEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {
	
	@Override
	public Class<?> getEventClass() {
		return ConsultTransferEvent.class;
	}

	@Override
	public void handle(ManagerEvent event) {
		logger.info("handle {} : {}.", this.getEventClass().getSimpleName(), event);
		
		String enterpriseId = ((ConsultTransferEvent) event).getEnterpriseId();
		String cno = ((ConsultTransferEvent) event).getCno();
		String consultObject = ((ConsultLinkEvent) event).getConsultObject();
		String objectType = ((ConsultLinkEvent) event).getObjectType();	
		
		JSONObject userEvent=new JSONObject();
		userEvent.put(AmiParamConst.EVENT, AmiEventTypeConst.CONSULT_TRANSFER);
		userEvent.put(AmiParamConst.ENTERPRISE_ID, enterpriseId);
		userEvent.put(AmiParamConst.CNO, cno);
		userEvent.put(AmiParamConst.CONSULT_OBJECT, consultObject);
		userEvent.put(AmiParamConst.OBJECT_TYPE, objectType);
		
		publishEvent(userEvent);
	}
}
