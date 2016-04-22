package com.tinet.ctilink.ami.event;

import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.ThreewayUnlinkEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.ami.AmiEvent;
import com.tinet.ctilink.ami.online.CtiAgent;
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
		String cid = enterpriseId + threewayedCno;
		CtiAgent ctiAgent = null;
		
		// 保存在线座席
		RedisLock redisLock = RedisLockUtil.lock(CtiAgent.getLockKey(cid));
		if (redisLock != null) {
			try {
				ctiAgent = ctiAgentService.get(cid);

				if (ctiAgent != null) {
					ctiAgent.setThreewayChannel("");
					ctiAgent.setMonitoredObject("");
					ctiAgent.setMonitoredObjectType("");
					ctiAgentService.set(ctiAgent);
				}
			} finally {
				RedisLockUtil.unLock(redisLock);
			}
		}

		if (ctiAgent != null) {
			if (null != cno && !cno.equals("")) {
				Map<String, String> userEvent = new HashMap<String, String>();
				userEvent.put("type", AmiAction.VARIABLE_EVENT);
				userEvent.put(AmiAction.VARIABLE_NAME, AmiEvent.THREEWAY_UNLINK);
				userEvent.put(AmiAction.VARIABLE_ENTERPRISE_ID, enterpriseId);
				userEvent.put(AmiAction.VARIABLE_CNO, cno);
				userEvent.put(AmiAction.VARIABLE_THREEWAY_OBJECT, threewayObject);
				userEvent.put(AmiAction.VARIABLE_OBJECT_TYPE, objectType);
				userEvent.put(AmiAction.VARIABLE_THREEWAYED_CNO, threewayedCno);
				publishEvent(userEvent);
			}
		}

	}

}
