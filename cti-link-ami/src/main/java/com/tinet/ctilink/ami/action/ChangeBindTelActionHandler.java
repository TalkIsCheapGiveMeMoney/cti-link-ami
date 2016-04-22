package com.tinet.ctilink.ami.action;

import java.util.Date;
import java.util.Map;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.ami.online.CtiAgent;
import com.tinet.ctilink.util.RedisLock;
import com.tinet.ctilink.util.RedisLockUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;


/**
 * 修改绑定电话
 * 
 * @author tianzp
 */
@Component
public class ChangeBindTelActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiAction.CHANGE_BIND_TEL;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.debug("handle {} action : {}", this.getAction(), params);

		String bindTel = params.get(AmiAction.VARIABLE_BIND_TEL);// 绑定电话
		String location = params.get(AmiAction.VARIABLE_AGENT_LOCATION);
		String[] queues = StringUtils.split(params.get(AmiAction.VARIABLE_AGENT_QUEUE), ",");
		String cid = StringUtils.trimToEmpty(params.get(AmiAction.VARIABLE_CID));
		if (StringUtils.isEmpty(cid)) {
			String enterpriseId = StringUtils.trimToEmpty(params.get(AmiAction.VARIABLE_ENTERPRISE_ID));
			String cno = StringUtils.trimToEmpty(params.get(AmiAction.VARIABLE_CNO));
			cid = enterpriseId + cno;
		}

		CtiAgent ctiAgent = null;
		String oldTel = "";
		// 保存在线座席
		RedisLock redisLock = RedisLockUtil.lock(CtiAgent.getLockKey(cid));
		if (redisLock != null) {
			try {
				ctiAgent = ctiAgentService.get(cid);
				if (ctiAgent == null) {
					return ERROR_BAD_PARAM;
				}
				oldTel = ctiAgent.getTel();
				// 通话中、整理状态不能修改绑定电话
				if (!CtiAgent.IDLE.equals(ctiAgent.getDeviceStatus())
						|| (CtiAgent.PAUSE.equals(ctiAgent.getLoginStatus())
								&& CtiAgent.PAUSE_DESCRIPTION_WRAPUP.equals(ctiAgent.getPauseDescription()))) {
					return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_AGENT_BUDY, "座席正忙，不能修改绑定电话");
				}

				ctiAgent.setTel(bindTel);
				ctiAgent.setLocation(location);
				ctiAgentService.set(ctiAgent);
			} finally {
				RedisLockUtil.unLock(redisLock);;
			}
		}

		// 同步队列状态
		for (String queue : queues) {
			this.syncQueueMember(queue);
		}

		// 记录Ami日志
		amiLogQueueEngine.insertLogQueue(null, new Date(), null, ctiAgent.getCid(), "BINDTEL", bindTel,
				oldTel, null, null, null);

		return SUCCESS;
	}

}
