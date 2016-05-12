package com.tinet.ctilink.ami.event;

import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.BargeUnlinkEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.ami.inc.AmiEventConst;
import com.tinet.ctilink.ami.online.CtiAgent;
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
		CtiAgent ctiAgent = null;

		// 保存在线座席
		RedisLock redisLock = RedisLockUtil.lock(CtiAgent.getLockKey(cid));
		if (redisLock != null) {
			try {
				ctiAgent = ctiAgentService.get(cid);

				if (ctiAgent != null) {
					ctiAgent.setSpyChannel("");
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
				userEvent.put(AmiAction.VARIABLE_NAME, AmiEventConst.BARGE_UNLINK);
				userEvent.put(AmiAction.VARIABLE_ENTERPRISE_ID, enterpriseId);
				userEvent.put(AmiAction.VARIABLE_CNO, cno);
				userEvent.put(AmiAction.VARIABLE_SPY_OBJECT, bargeObject);
				userEvent.put(AmiAction.VARIABLE_OBJECT_TYPE, objectType);
				userEvent.put(AmiAction.VARIABLE_SPIED_CNO, bargedCno);
				publishEvent(userEvent);
			}
		}

	}

}
