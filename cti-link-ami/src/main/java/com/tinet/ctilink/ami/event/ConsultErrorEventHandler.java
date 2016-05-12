package com.tinet.ctilink.ami.event;

import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.ConsultErrorEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.ami.inc.AmiEventConst;
import com.tinet.ctilink.ami.online.CtiAgent;
import com.tinet.ctilink.util.RedisLock;
import com.tinet.ctilink.util.RedisLockUtil;

/**
 * 咨询失败事件
 * 
 * @author tianzp
 */
@Component
public class ConsultErrorEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {

	@Override
	public Class<?> getEventClass() {
		return ConsultErrorEvent.class;
	}

	@Override
	public void handle(ManagerEvent event) {
		logger.info("handle {} : {}.", this.getEventClass().getSimpleName(), event);

		String enterpriseId = ((ConsultErrorEvent) event).getEnterpriseId();
		String cno = ((ConsultErrorEvent) event).getCno();

		Map<String, String> userEvent = new HashMap<String, String>();
		userEvent.put("type", AmiAction.VARIABLE_EVENT);
		userEvent.put(AmiAction.VARIABLE_NAME, AmiEventConst.CONSULT_ERROR);
		userEvent.put(AmiAction.VARIABLE_ENTERPRISE_ID, enterpriseId);
		userEvent.put(AmiAction.VARIABLE_CNO, cno);
		publishEvent(userEvent);
		
		String cid = enterpriseId + cno;
		CtiAgent ctiAgent = null;
		
		// 保存在线座席
		RedisLock redisLock = RedisLockUtil.lock(CtiAgent.getLockKey(cid));
		if (redisLock != null) {
			try {
				ctiAgent = ctiAgentService.get(cid);

				if (ctiAgent != null) {
					ctiAgent.setConsultChannel("");
					ctiAgentService.set(ctiAgent);
				}
			} finally {
				RedisLockUtil.unLock(redisLock);
			}
		}
	}

}
