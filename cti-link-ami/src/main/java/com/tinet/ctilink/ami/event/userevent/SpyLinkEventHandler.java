package com.tinet.ctilink.ami.event.userevent;

import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.SpyLinkEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiUserEventHandler;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.json.JSONObject;
import com.tinet.ctilink.ami.inc.AmiEventTypeConst;

/**
 * 监听事件
 * 
 * @author tianzp
 */
@Component
public class SpyLinkEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {

	@Override
	public Class<?> getEventClass() {
		return SpyLinkEvent.class;
	}

	@Override
	public void handle(ManagerEvent event) {
		logger.info("handle {} : {}.", this.getEventClass().getSimpleName(), event);

		String channel = ((SpyLinkEvent) event).getChannel();
		String enterpriseId = ((SpyLinkEvent) event).getEnterpriseId();
		String cno = ((SpyLinkEvent) event).getCno();
		String spyObject = ((SpyLinkEvent) event).getSpyObject();
		String objectType = ((SpyLinkEvent) event).getObjectType();
		String spiedCno = ((SpyLinkEvent) event).getSpiedCno();

		JSONObject userEvent=new JSONObject();		
		userEvent.put(AmiParamConst.EVENT, AmiEventTypeConst.SPY_LINK);
		userEvent.put(AmiParamConst.ENTERPRISE_ID, enterpriseId);
		userEvent.put(AmiParamConst.CNO, cno);
		userEvent.put(AmiParamConst.SPY_OBJECT, spyObject);
		userEvent.put(AmiParamConst.OBJECT_TYPE, objectType);
		userEvent.put(AmiParamConst.SPIED_CNO, spiedCno);
		publishEvent(userEvent);
	}

}
