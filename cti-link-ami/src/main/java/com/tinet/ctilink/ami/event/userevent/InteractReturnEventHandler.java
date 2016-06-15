package com.tinet.ctilink.ami.event.userevent;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.InteractReturnEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiUserEventHandler;
import com.tinet.ctilink.ami.inc.AmiEventTypeConst;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.json.JSONObject;

/**
 * 咨询接回事件
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
		
		JSONObject userEvent=new JSONObject();		
		userEvent.put(AmiParamConst.EVENT, AmiEventTypeConst.INTERACT_RETURN);
		userEvent.put(AmiParamConst.ENTERPRISE_ID, enterpriseId);
		userEvent.put(AmiParamConst.CNO, cno);
		publishEvent(userEvent);
	}

}
