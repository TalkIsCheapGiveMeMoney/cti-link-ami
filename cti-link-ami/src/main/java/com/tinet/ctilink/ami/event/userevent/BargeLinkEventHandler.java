package com.tinet.ctilink.ami.event.userevent;

import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.BargeLinkEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiUserEventHandler;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.json.JSONObject;
import com.tinet.ctilink.ami.inc.AmiEventTypeConst;

/**
 * 强插事件
 * 
 * @author tianzp
 */
@Component
public class BargeLinkEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {

	@Override
	public Class<?> getEventClass() {
		return BargeLinkEvent.class;
	}

	@Override
	public void handle(ManagerEvent event) {
		logger.info("handle {} : {}.", this.getEventClass().getSimpleName(), event);

		String channel = ((BargeLinkEvent) event).getChannel();
		String enterpriseId = ((BargeLinkEvent) event).getEnterpriseId();
		String cno = ((BargeLinkEvent) event).getCno();
		String bargeObject = ((BargeLinkEvent) event).getBargeObject();
		String bargedCno = ((BargeLinkEvent) event).getBargedCno();
		String cid = enterpriseId + cno;
//		CtiAgent ctiAgent = null;

		// 保存在线座席
//		RedisLock redisLock = RedisLockUtil.lock(CtiAgent.getLockKey(cid));
//		if (redisLock != null) {
//			try {
//				ctiAgent = ctiAgentService.get(cid);
//
//				if (ctiAgent != null) {
//					ctiAgent.setBargeChannel(channel);
//					ctiAgentService.set(ctiAgent);
//				}
//			} finally {
//				RedisLockUtil.unLock(redisLock);
//			}
//		}

		// 发送事件给被强插者
//		if (ctiAgent != null)
		{
			JSONObject userEvent=new JSONObject();		
			userEvent.put(AmiParamConst.VARIABLE_EVENT, AmiEventTypeConst.BARGE_LINK);
			userEvent.put(AmiParamConst.VARIABLE_ENTERPRISE_ID, enterpriseId);
			userEvent.put(AmiParamConst.VARIABLE_CNO, bargedCno);
			userEvent.put(AmiParamConst.VARIABLE_BARGER_CNO, cno);
			publishEvent(userEvent);
		}

		// 发送事件给发起者
		if (null != cno && !cno.equals("")) {
			Map<String, String> userEvent = new HashMap<String, String>();
			userEvent.put("type", AmiParamConst.VARIABLE_EVENT);
			userEvent.put(AmiParamConst.VARIABLE_NAME, AmiEventTypeConst.BARGE_LINK);
			userEvent.put(AmiParamConst.VARIABLE_ENTERPRISE_ID, enterpriseId);
			userEvent.put(AmiParamConst.VARIABLE_CNO, cno);
			userEvent.put(AmiParamConst.VARIABLE_BARGE_OBJECT, bargeObject);
			userEvent.put(AmiParamConst.VARIABLE_OBJECT_TYPE, bargeObject);
			userEvent.put(AmiParamConst.VARIABLE_BARGED_CNO, bargedCno);
			publishEvent(userEvent);
		}

	}

}
