package com.tinet.ctilink.ami.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.manager.action.IndicateAction;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.ami.inc.AmiEventConst;
import com.tinet.ctilink.ami.online.CtiAgent;
import com.tinet.ctilink.util.RedisLock;
import com.tinet.ctilink.util.RedisLockUtil;

/**
 * 取消保持
 * 
 * @author tianzp
 */
@Component
public class UnHoldActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiAction.UNHOLD;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.debug("handle {} action : {}", this.getAction(), params);

		CtiAgent ctiAgent = getCtiAgent(params);
		if (ctiAgent == null) {
			return ERROR_BAD_PARAM;
		}

		if (StringUtils.isEmpty(ctiAgent.getChannel())) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NO_CHANNEL, "no channel");
		}

		if (!CtiAgent.BUSY_DESCRIPTION_ONHOLD.equals(ctiAgent.getBusyDescription())) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_ALREADY_ONHOLD, "not onhold");
		}

		IndicateAction holdAction = new IndicateAction();
		holdAction.setChannel(ctiAgent.getChannel());
		holdAction.setIndicate(17);// 16是保持的指令代码 17是取消保持的指令代码

		if (sendAction(holdAction) == null) {
			return ERROR_EXCEPTION;
		}

		// 保存在线座席
		RedisLock redisLock = RedisLockUtil.lock(CtiAgent.getLockKey(ctiAgent.getCid()));
		if (redisLock != null) {
			try {
				CtiAgent newAgent = ctiAgentService.get(ctiAgent.getCid());
				newAgent.setBusyDescription("");
				ctiAgentService.set(newAgent);
			} finally {
				RedisLockUtil.unLock(redisLock);
			}
		}

		// 广播座席状态
		Map<String, String> event = new HashMap<String, String>();
		event.put(AmiAction.VARIABLE_TYPE, AmiAction.VARIABLE_EVENT);
		event.put(AmiAction.VARIABLE_NAME, AmiEventConst.STATUS);
		event.put(AmiAction.VARIABLE_ENTERPRISE_ID, String.valueOf(ctiAgent.getEnterpriseId()));
		event.put(AmiAction.VARIABLE_CNO, ctiAgent.getCno());
		event.put(AmiAction.VARIABLE_CRM_ID, ctiAgent.getCrmId());
		event.put(AmiAction.VARIABLE_BIND_TEL, ctiAgent.getTel());
		event.put(AmiAction.VARIABLE_MEMBER_DEVICE_STATUS, CtiAgent.BUSY);// 设备状态
		event.put(AmiAction.VARIABLE_MEMBER_LOGIN_STATUS, CtiAgent.ONLINE);

		publishEvent(event);

		// 记录Ami日志
		amiLogQueueEngine.insertLogQueue(null, new Date(), null, ctiAgent.getCid(), "UNHOLD", null, null, null, null,
				null);

		return SUCCESS;
	}

}
