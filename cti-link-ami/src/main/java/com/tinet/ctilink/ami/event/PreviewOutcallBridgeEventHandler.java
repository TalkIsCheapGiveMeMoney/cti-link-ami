package com.tinet.ctilink.ami.event;

import java.util.HashMap;
import java.util.Map;

import com.tinet.ctilink.ami.util.AmiUtil;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.UserEvent;
import org.asteriskjava.manager.userevent.PreviewOutcallBridgeEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.ami.inc.AmiEventConst;
import com.tinet.ctilink.ami.online.CtiAgent;
import com.tinet.ctilink.util.RedisLock;
import com.tinet.ctilink.util.RedisLockUtil;
import com.tinet.ctilink.inc.Const;

/**
 * 预览外呼接通
 * 
 * @author tianzp
 */
@Component
public class PreviewOutcallBridgeEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {

	@Override
	public Class<?> getEventClass() {
		return PreviewOutcallBridgeEvent.class;
	}

	@Override
	public void handle(ManagerEvent event) {
		logger.info("handle {} : {}.", this.getEventClass().getSimpleName(), event);

		String enterpriseId = ((PreviewOutcallBridgeEvent) event).getEnterpriseId();
		String callType = ((PreviewOutcallBridgeEvent) event).getCallType();
		String customerNumber = ((PreviewOutcallBridgeEvent) event).getCustomerNumber();
		String customerNumberType = ((PreviewOutcallBridgeEvent) event).getCustomerNumberType();
		String customerAreaCode = ((PreviewOutcallBridgeEvent) event).getCustomerAreaCode();
		String cno = ((PreviewOutcallBridgeEvent) event).getCno();
		String mainUniqueId = ((PreviewOutcallBridgeEvent) event).getMainUniqueId();

		Map<String, String> userEvent = new HashMap<String, String>();
		userEvent.put("type", AmiAction.VARIABLE_EVENT);
		userEvent.put(AmiAction.VARIABLE_NAME, AmiEventConst.PREVIEW_OUTCALL_BRIDGE);
		userEvent.put(AmiAction.VARIABLE_ENTERPRISE_ID, enterpriseId);
		userEvent.put(AmiAction.VARIABLE_CALL_TYPE, callType);
		userEvent.put(AmiAction.VARIABLE_CUSTOMER_NUMBER, customerNumber);
		userEvent.put(AmiAction.VARIABLE_CUSTOMER_NUMBER_TYPE, customerNumberType);
		userEvent.put(AmiAction.VARIABLE_CUSTOMER_AREA_CODE, customerAreaCode);
		userEvent.put(AmiAction.VARIABLE_UNIQUEID, mainUniqueId);
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
					ctiAgent.setMainChannel(((PreviewOutcallBridgeEvent) event).getChannel());
					ctiAgentService.set(ctiAgent);
				}
			} finally {
				RedisLockUtil.unLock(redisLock);
			}
		}

		// 根据企业设置推送Curl
		AmiUtil.pushCurl(((UserEvent) event).getAsteriskChannel(), userEvent, Integer.parseInt(enterpriseId),
				Const.ENTERPRISE_PUSH_TYPE_BRIDGE_OB, Const.CURL_TYPE_BRIDGE_OB);
	}

}
