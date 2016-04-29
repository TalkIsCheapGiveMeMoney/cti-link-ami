package com.tinet.ctilink.ami.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.manager.action.QueuePauseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.ami.AmiEvent;
import com.tinet.ctilink.ami.wrapup.AmiWrapupEngine;
import com.tinet.ctilink.ami.online.CtiAgent;

/**
 * 座席登录
 * 
 * @author tianzp
 */
@Component
public class LoginActionHandler extends AbstractActionHandler {

	@Autowired
	private AmiWrapupEngine amiWrapupEngine;

	@Override
	public String getAction() {
		return AmiAction.LOGIN;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.info("handle login action : {}", params);

		// 解析传递的参数
		int clientId = Integer.valueOf(params.get(AmiAction.VARIABLE_CLIENT_ID));
		int power = Integer.valueOf(params.get(AmiAction.VARIABLE_POWER));
		Integer enterpriseId = Integer.valueOf(params.get(AmiAction.VARIABLE_ENTERPRISE_ID));
		String hotline = StringUtils.trimToEmpty(params.get(AmiAction.VARIABLE_HOTLINE));
		String cno = StringUtils.trimToEmpty(params.get(AmiAction.VARIABLE_CNO));
		String initStatus = StringUtils.trimToEmpty(params.get(AmiAction.VARIABLE_INIT_STATUS));
		String name = params.get(AmiAction.VARIABLE_AGENT_NAME);
		int wraupTime = Integer.parseInt(params.get(AmiAction.VARIABLE_WRAPUP_TIME));
		int bindType = Integer.valueOf(params.get(AmiAction.VARIABLE_BIND_TYPE));
		int obRecord = Integer.valueOf(params.get(AmiAction.VARIABLE_OB_RECORD));
		String tel = StringUtils.trimToEmpty(params.get(AmiAction.VARIABLE_BIND_TEL));
		String location = StringUtils.trimToEmpty(params.get(AmiAction.VARIABLE_AGENT_LOCATION));
		String pauseDescription = StringUtils.trimToEmpty(params.get(AmiAction.VARIABLE_PAUSE_DESCRIPTION));
		String obClid = params.get(AmiAction.VARIABLE_OB_CLID);
		String crmId = params.get(AmiAction.VARIABLE_CRM_ID);
		String isOutCall = params.get(AmiAction.VARIABLE_IS_OUT_CALL);
		String obSmsTail = params.get(AmiAction.VARIABLE_OB_SMS_TAIL);
		String isInvestigationAuto = params.get(AmiAction.VARIABLE_IS_INVESTIGATION_AUTO);
		String[] queues = StringUtils.split(params.get(AmiAction.VARIABLE_AGENT_QUEUE), ",");
		obSmsTail = StringUtils.isEmpty(obSmsTail)?"0":obSmsTail;
		// 在线座席
		CtiAgent ctiAgent = new CtiAgent(enterpriseId, cno);

		if (initStatus.equals(CtiAgent.PAUSE)) {
			ctiAgent.setLoginStatus(CtiAgent.PAUSE);
			ctiAgent.setPauseDescription(pauseDescription);

			// 更新队列状态
			QueuePauseAction queuePauseAction = new QueuePauseAction(location, true, pauseDescription);
			if (sendAction(queuePauseAction) == null) {
				return ERROR_EXCEPTION;
			}
		} else if (initStatus.equals(CtiAgent.ONLINE)) {
			ctiAgent.setLoginStatus(CtiAgent.ONLINE);

			// 更新队列状态
			QueuePauseAction queuePauseAction = new QueuePauseAction(location, false, "");
			if (sendAction(queuePauseAction) == null) {
				return ERROR_EXCEPTION;
			}
		}

		// 更新座席整理状态
		amiWrapupEngine.removeWrapup(location);

		// 记录Ami日志
		amiLogQueueEngine.insertLogQueue(null, new Date(), null, enterpriseId + cno, "LOGIN", tel, null, null, null,
				null);

		// 保存在线座席
		ctiAgent.setClientId(clientId);
		ctiAgent.setName(name); // 座席姓名
		ctiAgent.setWrapup(wraupTime); // 座席整理时间
		ctiAgent.setHotline(hotline);// 热线号码
		ctiAgent.setObClid(obClid);
		ctiAgent.setPreviewOutcallLocked(false);// 初始化外呼锁
		ctiAgent.setCrmId(crmId);
		ctiAgent.setBindType(bindType);
		ctiAgent.setTel(tel); // 绑定电话
		ctiAgent.setLocation(location);
		ctiAgent.setLoginStartTime(new Date().getTime());
		ctiAgent.setDeviceStatus(CtiAgent.IDLE);// FIXME 座席登录时可能设备正忙，是否需要检测状态？
		ctiAgent.setOutCall(isOutCall != null && isOutCall.equals("1"));
		ctiAgent.setObRecord(obRecord);
		ctiAgent.setObSmsTail(obSmsTail);
		ctiAgent.setIsInvestigationAuto(isInvestigationAuto);
		ctiAgentService.set(ctiAgent);
		
		// 同步队列状态
		syncQueueMember(queues);

		// 广播座席状态
		Map<String, String> event = new HashMap<String, String>();
		event.put(AmiAction.VARIABLE_NAME, AmiEvent.STATUS); // 状态发生改变
		event.put(AmiAction.VARIABLE_ENTERPRISE_ID, enterpriseId.toString()); // 企业id
		event.put(AmiAction.VARIABLE_CNO, cno);// 座席工号
		event.put(AmiAction.VARIABLE_CRM_ID, crmId==null?"":crmId);
		event.put(AmiAction.VARIABLE_BIND_TEL, tel);
		event.put(AmiAction.VARIABLE_MEMBER_DEVICE_STATUS, ctiAgent.getDeviceStatus());
		event.put(AmiAction.VARIABLE_MEMBER_LOGIN_STATUS, ctiAgent.getLoginStatus());// 座席状态

		if (!ctiAgent.getLoginStatus().equals(CtiAgent.ONLINE)) {
			event.put(AmiAction.VARIABLE_PAUSE_DESCRIPTION, pauseDescription);
			if (pauseDescription.equals(CtiAgent.PAUSE_DESCRIPTION_WRAPUP)) {
				event.put(AmiAction.VARIABLE_MEMBER_LOGIN_STATUS, CtiAgent.PAUSE);// 座席状态
				event.put(AmiAction.VARIABLE_WRAPUP_TIME, amiWrapupEngine.getMemberWrapupLeft(location) + "");
			}
		}

		publishEvent(event);

		return SUCCESS;
	}
}
