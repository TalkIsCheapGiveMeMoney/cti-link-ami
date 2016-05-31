package com.tinet.ctilink.ami.event.userevent;

import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.ThreewayUnlinkEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiUserEventHandler;
import com.tinet.ctilink.ami.inc.AmiEventTypeConst;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.json.JSONObject;
import com.tinet.ctilink.util.RedisLock;
import com.tinet.ctilink.util.RedisLockUtil;

/**
 * 管理员功能---三方通话挂断
 * 
 * @author tianzp
 */
@Component
public class ThreewayUnlinkEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {

	@Override
	public Class<?> getEventClass() {
		return ThreewayUnlinkEvent.class;
	}

	@Override
	public void handle(ManagerEvent event) {
		logger.info("handle {} : {}.", this.getEventClass().getSimpleName(), event);

		String enterpriseId = ((ThreewayUnlinkEvent) event).getEnterpriseId();
		String cno = ((ThreewayUnlinkEvent) event).getCno();
		String threewayObject = ((ThreewayUnlinkEvent) event).getThreewayObject();
		String objectType = ((ThreewayUnlinkEvent) event).getObjectType();
		String threewayedCno = ((ThreewayUnlinkEvent) event).getThreewayedCno();
		
		if (null != cno && !cno.equals("")) {
			JSONObject userEvent=new JSONObject();		
			userEvent.put(AmiParamConst.EVENT, AmiEventTypeConst.THREEWAY_UNLINK);
			userEvent.put(AmiParamConst.ENTERPRISE_ID, enterpriseId);
			userEvent.put(AmiParamConst.CNO, cno);
			userEvent.put(AmiParamConst.THREEWAY_OBJECT, threewayObject);
			userEvent.put(AmiParamConst.OBJECT_TYPE, objectType);
			userEvent.put(AmiParamConst.THREEWAYED_CNO, threewayedCno);
			publishEvent(userEvent);
		
		}
		

	}

}
