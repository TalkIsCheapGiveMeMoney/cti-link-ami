package com.tinet.ctilink.ami.event.userevent;

import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.SpyLinkEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiUserEventHandler;
import com.tinet.ctilink.ami.inc.AmiEventConst;

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

		String cid = enterpriseId + spiedCno;
		
//		if (ctiAgent != null) 
		{
			if (null != cno && !cno.equals("")) {
				Map<String, String> userEvent = new HashMap<String, String>();
				userEvent.put("type", AmiAction.VARIABLE_EVENT);
				userEvent.put(AmiAction.VARIABLE_NAME, AmiEventConst.SPY_LINK);
				userEvent.put(AmiAction.VARIABLE_ENTERPRISE_ID, enterpriseId);
				userEvent.put(AmiAction.VARIABLE_CNO, cno);
				userEvent.put(AmiAction.VARIABLE_SPY_OBJECT, spyObject);
				userEvent.put(AmiAction.VARIABLE_OBJECT_TYPE, objectType);
				userEvent.put(AmiAction.VARIABLE_SPIED_CNO, spiedCno);
				publishEvent(userEvent);
			}
		}

	}

}
