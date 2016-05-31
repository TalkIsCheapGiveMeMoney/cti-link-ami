package com.tinet.ctilink.ami.event.userevent;

import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.BargeLinkEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiUserEventHandler;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.json.JSONObject;
import com.tinet.ctilink.ami.inc.AmiEventTypeConst;

/**
 * 强插事件
 * 
 * @author tianzp
 */
@Component
public class BargeLinkEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {

	@Override
	public Class<?> getEventClass() {
		return BargeLinkEvent.class;
	}

	@Override
	public void handle(ManagerEvent event) {
		logger.info("handle {} : {}.", this.getEventClass().getSimpleName(), event);

		String channel = ((BargeLinkEvent) event).getChannel();
		String enterpriseId = ((BargeLinkEvent) event).getEnterpriseId();
		String cno = ((BargeLinkEvent) event).getCno();
		String bargeObject = ((BargeLinkEvent) event).getBargeObject();
		String bargedCno = ((BargeLinkEvent) event).getBargedCno();
		String objectType = ((BargeLinkEvent) event).getObjectType();

		JSONObject userEvent=new JSONObject();		
		userEvent.put(AmiParamConst.EVENT, AmiEventTypeConst.BARGE_LINK);
		userEvent.put(AmiParamConst.ENTERPRISE_ID, enterpriseId);
		userEvent.put(AmiParamConst.CNO, cno);
		userEvent.put(AmiParamConst.CHANNEL, channel);
		userEvent.put(AmiParamConst.BARGE_OBJECT, bargeObject);
		userEvent.put(AmiParamConst.BARGED_CNO, bargedCno);
		userEvent.put(AmiParamConst.OBJECT_TYPE, objectType);
		publishEvent(userEvent);
		

	}

}
