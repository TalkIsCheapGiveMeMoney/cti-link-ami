package com.tinet.ctilink.ami.event.userevent;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.ConsultStartEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiUserEventHandler;
import com.tinet.ctilink.ami.inc.AmiEventTypeConst;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.json.JSONObject;


/**
 * 咨询开始事件
 * 
 * @author tianzp
 */
@Component
public class ConsultStartEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {

	@Override
	public Class<?> getEventClass() {
		return ConsultStartEvent.class;
	}

	@Override
	public void handle(ManagerEvent event) {
		logger.info("handle {} : {}.", this.getEventClass().getSimpleName(), event);

		String enterpriseId = ((ConsultStartEvent) event).getEnterpriseId();
		String cno = ((ConsultStartEvent) event).getCno();
		String channel = ((ConsultStartEvent) event).getChannel();
		
		JSONObject userEvent=new JSONObject();
		userEvent.put(AmiParamConst.EVENT,AmiEventTypeConst.CONSULT_START);
		userEvent.put(AmiParamConst.ENTERPRISE_ID, enterpriseId);
		userEvent.put(AmiParamConst.CNO, cno);
		userEvent.put(AmiParamConst.CHANNEL, channel);
			
		publishEvent(userEvent);
		
	}

}
