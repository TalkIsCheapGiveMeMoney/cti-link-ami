package com.tinet.ctilink.ami.event;

import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.InteractReturnEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.ami.AmiEvent;
import com.tinet.ctilink.ami.online.CtiAgent;

/**
 * 交互返回
 * 
 * @author tianzp
 */
@Component
public class InteractReturnEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {

	@Override
	public Class<?> getEventClass() {
		return InteractReturnEvent.class;
	}

	@Override
	public void handle(ManagerEvent event) {
		logger.info("handle {} : {}.", this.getEventClass().getSimpleName(), event);
		
		String enterpriseId = ((InteractReturnEvent) event).getEnterpriseId();
		String cno = ((InteractReturnEvent) event).getCno();

		CtiAgent member = ctiAgentService.get(enterpriseId + cno);
		if (member != null) {
			Map<String, String> userEvent = new HashMap<String, String>();
			userEvent.put("type", AmiAction.VARIABLE_EVENT);
			userEvent.put(AmiAction.VARIABLE_NAME, AmiEvent.INTERACT_RETURN);
			userEvent.put(AmiAction.VARIABLE_ENTERPRISE_ID, enterpriseId);
			userEvent.put(AmiAction.VARIABLE_CNO, cno);
			publishEvent(userEvent);
		}

	}

}
