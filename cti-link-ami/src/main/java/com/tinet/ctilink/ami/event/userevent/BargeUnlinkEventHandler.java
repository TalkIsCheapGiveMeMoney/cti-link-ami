package com.tinet.ctilink.ami.event.userevent;

import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.BargeUnlinkEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiUserEventHandler;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.json.JSONObject;
import com.tinet.ctilink.ami.inc.AmiEventTypeConst;
import com.tinet.ctilink.util.RedisLock;
import com.tinet.ctilink.util.RedisLockUtil;

/**
 * 强插挂断事件
 * 
 * @author tianzp
 */
@Component
public class BargeUnlinkEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {

	@Override
	public Class<?> getEventClass() {
		return BargeUnlinkEvent.class;
	}

	@Override
	public void handle(ManagerEvent event) {
		logger.info("handle {} : {}.", this.getEventClass().getSimpleName(), event);

		String enterpriseId = ((BargeUnlinkEvent) event).getEnterpriseId();
		String cno = ((BargeUnlinkEvent) event).getCno();
		String bargeObject = ((BargeUnlinkEvent) event).getBargeObject();
		String objectType = ((BargeUnlinkEvent) event).getObjectType();
		String bargedCno = ((BargeUnlinkEvent) event).getBargedCno();
		String cid = enterpriseId + bargedCno;

//		if (ctiAgent != null) 
		{
			if (null != cno && !cno.equals("")) {
				JSONObject userEvent=new JSONObject();		
				userEvent.put(AmiParamConst.VARIABLE_EVENT, AmiEventTypeConst.BARGE_UNLINK);
				userEvent.put(AmiParamConst.VARIABLE_ENTERPRISE_ID, enterpriseId);
				userEvent.put(AmiParamConst.VARIABLE_CNO, cno);
				userEvent.put(AmiParamConst.VARIABLE_SPY_OBJECT, bargeObject);
				userEvent.put(AmiParamConst.VARIABLE_OBJECT_TYPE, objectType);
				userEvent.put(AmiParamConst.VARIABLE_SPIED_CNO, bargedCno);
				publishEvent(userEvent);
			}
		}

	}

}
