package com.tinet.ctilink.ami.event.userevent;

import java.util.HashMap;
import java.util.Map;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.SpyUnlinkEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiUserEventHandler;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.json.JSONObject;
import com.tinet.ctilink.ami.inc.AmiEventTypeConst;

/**
 * 监听挂断事件
 * 
 * @author tianzp
 */
@Component
public class SpyUnlinkEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {

	@Override
	public Class<?> getEventClass() {
		return SpyUnlinkEvent.class;
	}

	@Override
	public void handle(ManagerEvent event) {
		logger.info("handle {} : {}.", this.getEventClass().getSimpleName(), event);

		String enterpriseId = ((SpyUnlinkEvent) event).getEnterpriseId();
		String cno = ((SpyUnlinkEvent) event).getCno();
		String spyObject = ((SpyUnlinkEvent) event).getSpyObject();
		String objectType = ((SpyUnlinkEvent) event).getObjectType();
		String spiedCno = ((SpyUnlinkEvent) event).getSpiedCno();
		String cid = enterpriseId + spiedCno;
		
//		if (ctiAgent != null) 
		{
			if (null != cno && !cno.equals("")) {
				JSONObject userEvent=new JSONObject();		
				userEvent.put(AmiParamConst.VARIABLE_EVENT, AmiEventTypeConst.SPY_UNLINK);
				userEvent.put(AmiParamConst.VARIABLE_ENTERPRISE_ID, enterpriseId);
				userEvent.put(AmiParamConst.VARIABLE_CNO, cno);
				userEvent.put(AmiParamConst.VARIABLE_SPY_OBJECT, spyObject);
				userEvent.put(AmiParamConst.VARIABLE_OBJECT_TYPE, objectType);
				userEvent.put(AmiParamConst.VARIABLE_SPIED_CNO, spiedCno);
				publishEvent(userEvent);
			}
		}

	}

}
