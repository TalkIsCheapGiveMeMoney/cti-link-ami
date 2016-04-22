package com.tinet.ctilink.ami.event;

import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.ConsultThreewayUnlinkEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.ami.AmiEvent;

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
		
		Map<String, String> userEvent = new HashMap<String, String>();
		userEvent.put("type", AmiAction.VARIABLE_EVENT);
		userEvent.put(AmiAction.VARIABLE_NAME, AmiEvent.CONSULT_THREEWAY_UNLINK);
		userEvent.put(AmiAction.VARIABLE_ENTERPRISE_ID, enterpriseId);
		userEvent.put(AmiAction.VARIABLE_CNO, cno);
		publishEvent(userEvent);

	}
}
