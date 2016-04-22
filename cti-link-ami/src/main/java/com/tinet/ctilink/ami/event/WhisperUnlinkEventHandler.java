package com.tinet.ctilink.ami.event;

import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.WhisperUnlinkEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.ami.AmiEvent;
import com.tinet.ctilink.ami.online.CtiAgent;
import com.tinet.ctilink.util.RedisLock;
import com.tinet.ctilink.util.RedisLockUtil;

/**
 * 耳语挂断事件
 * 
 * @author tianzp
 */
@Component
public class WhisperUnlinkEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {

	@Override
	public Class<?> getEventClass() {
		return WhisperUnlinkEvent.class;
	}

	@Override
	public void handle(ManagerEvent event) {
		logger.info("handle {} : {}.", this.getEventClass().getSimpleName(), event);

		String enterpriseId = ((WhisperUnlinkEvent) event).getEnterpriseId();
		String cno = ((WhisperUnlinkEvent) event).getCno();
		String whisperObject = ((WhisperUnlinkEvent) event).getWhisperObject();
		String objectType = ((WhisperUnlinkEvent) event).getObjectType();
		String whisperedCno = ((WhisperUnlinkEvent) event).getWhisperedCno();
		String cid = enterpriseId + whisperedCno;
		CtiAgent ctiAgent = null;
		
		// 保存在线座席
		RedisLock redisLock = RedisLockUtil.lock(CtiAgent.getLockKey(cid));
		if (redisLock != null) {
			try {
				ctiAgent = ctiAgentService.get(cid);

				if (ctiAgent != null) {
					ctiAgent.setWhisperChannel("");
					ctiAgent.setMonitoredObject("");
					ctiAgent.setMonitoredObjectType("");
					ctiAgentService.set(ctiAgent);
				}
			} finally {
				RedisLockUtil.unLock(redisLock);
			}
		}
		
		if (null != cno && !cno.equals("")) {
			Map<String, String> userEvent = new HashMap<String, String>();
			userEvent.put("type", AmiAction.VARIABLE_EVENT);
			userEvent.put(AmiAction.VARIABLE_NAME, AmiEvent.WHISPER_UNLINK);
			userEvent.put(AmiAction.VARIABLE_ENTERPRISE_ID, enterpriseId);
			userEvent.put(AmiAction.VARIABLE_CNO, cno);
			userEvent.put(AmiAction.VARIABLE_WHISPER_OBJECT, whisperObject);
			userEvent.put(AmiAction.VARIABLE_OBJECT_TYPE, objectType);
			userEvent.put(AmiAction.VARIABLE_WHISPERED_CNO, whisperedCno);
			publishEvent(userEvent);
		}

	}

}
