package com.tinet.ctilink.ami.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.manager.response.CommandResponse;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.ami.AmiEvent;
import com.tinet.ctilink.inc.Const;
import com.tinet.ctilink.ami.online.CtiAgent;

/**
 * 座席退出
 *
 * @author tianzp
 */
@Component
public class LogoutActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiAction.LOGOUT;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.debug("handle logout action:{}", params);

		String[] queues = StringUtils.split(params.get(AmiAction.VARIABLE_AGENT_QUEUE), ",");
		
		CtiAgent ctiAgent = getCtiAgent(params);
		if (ctiAgent == null) {
			return ERROR_BAD_PARAM;
		}
		
		if (queues == null || queues.length == 0) {
			return ERROR_BAD_PARAM;
		}

		// 检查座席是否在忙
		boolean busy = false;
		if (ctiAgent.getDeviceStatus() == null || (!CtiAgent.IDLE.equals(ctiAgent.getDeviceStatus())
				&& !CtiAgent.UNAVAILABLE.equals(ctiAgent.getDeviceStatus()))) {
			// 如果不是软电话判断底层状态，如果是软电话，直接退出
			if (ctiAgent.getBindType() != Const.BIND_TYPE_SOFT_PHONE) {
				CommandResponse res = this.sendCommand("queue show " + queues[0]);
				if (res != null && res.getResult() != null) {
					for (String msg : res.getResult()) {
						// 30100761111 (SIP/1113010076-0001@3010076-0001)
						// (realtime) (Not in use) has taken no calls yet
						if (msg.trim().startsWith(ctiAgent.getCid())) {
							// 底层忙不能退出
							if (msg.toLowerCase().indexOf(CtiAgent.BUSY) > -1
									|| msg.toLowerCase().indexOf(CtiAgent.RINGING) > -1) {

								busy = true;
							}
							break;
						}
					}
				}
			}
		}

		if (busy) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_AGENT_BUDY, "座席当前忙，不能强制下线");
		}

		// 同步队列状态
		syncQueueMember(queues);

		// 记录Ami日志
		amiLogQueueEngine.insertLogQueue(null, new Date(), null, ctiAgent.getCid(), "LOGOUT", null, null, null, null,
				null);

		// 删除在线座席
		ctiAgentService.remove(ctiAgent);

		// 广播座席状态
		Map<String, String> event = new HashMap<String, String>();
		event.put(AmiAction.VARIABLE_NAME, AmiEvent.STATUS);
		event.put(AmiAction.VARIABLE_ENTERPRISE_ID, String.valueOf(ctiAgent.getEnterpriseId()));
		event.put(AmiAction.VARIABLE_CNO, ctiAgent.getCno());
		event.put(AmiAction.VARIABLE_CRM_ID, ctiAgent.getCrmId());
		event.put(AmiAction.VARIABLE_BIND_TEL, ctiAgent.getTel());
		event.put(AmiAction.VARIABLE_MEMBER_LOGIN_STATUS, CtiAgent.OFFLINE);
		event.put(AmiAction.VARIABLE_MEMBER_DEVICE_STATUS, CtiAgent.IDLE);
		publishEvent(event);
		return SUCCESS;
	}
}
