package com.tinet.ctilink.ami.event;

import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.ThreewayLinkEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.ami.AmiEvent;
import com.tinet.ctilink.ami.online.CtiAgent;
import com.tinet.ctilink.util.RedisLock;
import com.tinet.ctilink.util.RedisLockUtil;

/**
 * 管理员功能---三方通话
 * 
 * @author tianzp
 */
@Component
public class ThreewayLinkEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {

	@Override
	public Class<?> getEventClass() {
		return ThreewayLinkEvent.class;
	}

	@Override
	public void handle(ManagerEvent event) {
		logger.info("handle {} : {}.", this.getEventClass().getSimpleName(), event);

		String channel = ((ThreewayLinkEvent) event).getChannel();
		String enterpriseId = ((ThreewayLinkEvent) event).getEnterpriseId();
		String cno = ((ThreewayLinkEvent) event).getCno();
		String threewayObject = ((ThreewayLinkEvent) event).getThreewayObject();
		String objectType = ((ThreewayLinkEvent) event).getObjectType();
		String threewayedCno = ((ThreewayLinkEvent) event).getThreewayedCno();
		String cid = enterpriseId + threewayedCno;
		CtiAgent ctiAgent = null;
		
		// 保存在线座席
		RedisLock redisLock = RedisLockUtil.lock(CtiAgent.getLockKey(cid));
		if (redisLock != null) {
			try {
				ctiAgent = ctiAgentService.get(cid);

				if (ctiAgent != null) {
					ctiAgent.setThreewayChannel(channel);
					ctiAgent.setMonitoredObject(threewayObject);
					ctiAgent.setMonitoredObjectType(objectType);
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
				userEvent.put(AmiAction.VARIABLE_NAME, AmiEvent.THREEWAY_LINK);
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
