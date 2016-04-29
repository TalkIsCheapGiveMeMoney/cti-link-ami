package com.tinet.ctilink.ami.action;

import java.util.*;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.ami.action.callback.PreviewOutcallCallback;
import com.tinet.ctilink.ami.log.AmiLogQueueEngine;
import com.tinet.ctilink.ami.online.CtiAgent;
import com.tinet.ctilink.ami.util.SipHeaderUtil;
import com.tinet.ctilink.cache.CacheKey;
import com.tinet.ctilink.cache.RedisService;
import com.tinet.ctilink.inc.Const;
import com.tinet.ctilink.conf.model.QueueMember;
import com.tinet.ctilink.util.LocalIpUtil;
import com.tinet.ctilink.util.RedisLock;
import com.tinet.ctilink.util.RedisLockUtil;
import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.response.ManagerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 预览式外呼
 * 
 * @author tianzp
 */
@Component
public class PreviewOutcallActionHandler extends AbstractActionHandler {

	@Autowired
	private PauseActionHandler pauseActionHandler;
	@Autowired
	private UnPauseActionHandler unPauseActionHandler;
	@Autowired
	private AmiLogQueueEngine amiLogQueueEngine;

	@Autowired
	private RedisService redisService;

	@Override
	public String getAction() {
		return AmiAction.PREVIEW_OUTCALL;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.debug("handle {} action : {}", this.getAction(), params);

		String previewOutcallTel = params.get(AmiAction.VARIABLE_PREVIEW_OUTCAL_TEL);// 外呼要拨打的电话号码
		String callType = params.get(AmiAction.VARIABLE_CALL_TYPE);// 外呼要拨打的电话号码类型
		String taskId = params.get(AmiAction.VARIABLE_TASK_ID); // 外呼任务id
		String taskInventoryId = params.get(AmiAction.VARIABLE_TASK_INVENTORY_ID);// 外呼任务清单id
		String orderCallBackId = params.get(AmiAction.VARIABLE_ORDER_CALL_BACK_ID);
		String customerCrmId = params.get(AmiAction.VARIABLE_CUSTOMER_CRM_ID);
		String isInvestigationAuto = params.get(AmiAction.VARIABLE_IS_INVESTIGATION_AUTO);
		String obSmsTail = params.get(AmiAction.VARIABLE_OB_SMS_TAIL);
		String clidRight = params.get(AmiAction.VARIABLE_CLID_RIGHT);// 呼叫座席侧外显号码
		String amiType = params.get(AmiAction.VARIABLE_AMI_TYPE);
		String gwIp = params.get(AmiAction.VARIABLE_GWIP);
		String obClid = params.get(AmiAction.VARIABLE_OB_CLID);
		String userField = params.get(AmiAction.VARIABLE_USER_FIELD);
		String callerNumber = params.get(AmiAction.VARIABLE_CALLER_NUMBER);
		String areaCode = params.get(AmiAction.VARIABLE_CUSTOMER_AREA_CODE);
		String telType = params.get(AmiAction.VARIABLE_TEL_TYPE);
		String sync = params.get(AmiAction.VARIABLE_SYNC);
		String loginStatus = null;
		String pauseDescription = null;

		// 检查外呼号码是否正确
		if (StringUtils.isEmpty(previewOutcallTel) || !StringUtils.isNumeric(previewOutcallTel)) {
			if (StringUtils.isEmpty(amiType)) {
				return ERROR_BAD_PARAM;
			}
		}

		String cid = StringUtils.trimToEmpty(params.get(AmiAction.VARIABLE_CID));
		if (StringUtils.isEmpty(cid)) {
			String enterpriseId = StringUtils.trimToEmpty(params.get(AmiAction.VARIABLE_ENTERPRISE_ID));
			String cno = StringUtils.trimToEmpty(params.get(AmiAction.VARIABLE_CNO));
			cid = enterpriseId + cno;
		}

		long callStartTime = new Date().getTime() / 1000;

		CtiAgent ctiAgent = null;
		RedisLock redisLock = RedisLockUtil.lock(CtiAgent.getLockKey(cid));
		if (redisLock != null) {
			try {
				ctiAgent = ctiAgentService.get(cid);
				if (ctiAgent == null) {
					return ERROR_BAD_PARAM;
				}
				loginStatus = ctiAgent.getLoginStatus();
				pauseDescription = ctiAgent.getPauseDescription();
				// 检查座席是否正在通话，防止多次调用
				if (ctiAgent.isPreviewOutcallLocked() || !ctiAgent.getDeviceStatus().equals(CtiAgent.IDLE)
						|| (ctiAgent.getLoginStatus().equals(CtiAgent.PAUSE)
								&& ctiAgent.getPauseDescription().equals(CtiAgent.PAUSE_DESCRIPTION_OUTCALLING))) {

					if (StringUtils.isEmpty(amiType)) {
						return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_AGENT_IS_OUTCALLING,
								"agent is outcalling");
					}
				}

				ctiAgent.setPreviewOutcallLocked(true);// 从这里锁住，防止后面频繁调用
				ctiAgent.setPreviewOutcallStart(callStartTime);// 保存呼叫开始时间
				ctiAgentService.set(ctiAgent);
			} finally {
				RedisLockUtil.unLock(redisLock);
			}
		}

		try {
			// 置忙外呼就不再置忙preivewoutcalling，如果在整理要调用置忙让整理结束
			if (ctiAgent.getLoginStatus().equals(CtiAgent.ONLINE) || (ctiAgent.getLoginStatus().equals(CtiAgent.PAUSE)
					&& ctiAgent.getPauseDescription().equals(CtiAgent.PAUSE_DESCRIPTION_WRAPUP))) {
				Map<String, String> pauseToken = new HashMap<String, String>();
				pauseToken.put(AmiAction.VARIABLE_ENTERPRISE_ID, String.valueOf(ctiAgent.getEnterpriseId()));
				pauseToken.put(AmiAction.VARIABLE_CNO, ctiAgent.getCno());
				pauseToken.put(AmiAction.VARIABLE_CID, ctiAgent.getCid());
				pauseToken.put(AmiAction.VARIABLE_PAUSE_DESCRIPTION, CtiAgent.PAUSE_DESCRIPTION_OUTCALLING);
				pauseToken.put(AmiAction.VARIABLE_LOGIN_TYPE, "backend");
				pauseActionHandler.handle(pauseToken);
			}

			Map<String, String> variables = new HashMap<String, String>();
			variables.put("dial_interface", ctiAgent.getLocation());
			variables.put("__" + Const.CDR_CUSTOMER_NUMBER, callerNumber); // 客户号码
			variables.put("__" + Const.CDR_CUSTOMER_NUMBER_TYPE, telType); // 电话类型
			variables.put("__" + Const.CDR_CUSTOMER_AREA_CODE, areaCode); // 区号
			variables.put("__" + Const.CDR_CUSTOMER_CRM_ID, customerCrmId); // crm_id
			variables.put("__" + Const.ENTERPRISE_ID, String.valueOf(ctiAgent.getEnterpriseId()));
			variables.put("__" + Const.CDR_ENTERPRISE_ID, String.valueOf(ctiAgent.getEnterpriseId()));
			variables.put("__" + Const.CDR_BRIDGED_CNO, ctiAgent.getCno());
			variables.put("__" + Const.CDR_CALL_TYPE, callType);
			variables.put("__" + Const.CDR_TASK_ID, taskId);
			variables.put("__" + Const.CDR_TASK_INVENTORY_ID, taskInventoryId);
			variables.put("__" + Const.CDR_NUMBER_TRUNK, obClid); // 座席侧外显号码
			variables.put("__" + Const.IS_INVESTIGATION_AUTO, isInvestigationAuto);
			variables.put("__" + Const.OB_SMS_TAIL, obSmsTail);
			variables.put(Const.IS_OB_RECORD, ctiAgent.getObRecord() + "");
			variables.put(Const.CDR_CLIENT_NUMBER, ctiAgent.getTel());
			variables.put(Const.CDR_START_TIME, String.valueOf(callStartTime));
			variables.put(Const.CDR_STATUS, String.valueOf(Const.CDR_STATUS_OB_CLIENT_NO_ANSWER));
			variables.put(Const.CDR_GW_IP, gwIp);
			variables.put(Const.PREVIEW_OUTCALL_LEFT_CLID, obClid); // 客户侧外显号码
			variables.put(Const.DIAL_TIMEOUT, "60"); // 外呼等待时长 60秒
			variables.put(Const.CDR_USER_FIELD, userField);

			if (ctiAgent.getBindType() != Const.BIND_TYPE_TEL) {
				variables.put(Const.CDR_EXTEN, ctiAgent.getTel());
			}

			// 置忙外呼就不再整理
			if (ctiAgent.getLoginStatus().equals(CtiAgent.ONLINE) || (ctiAgent.getLoginStatus().equals(CtiAgent.PAUSE)
					&& ctiAgent.getPauseDescription().equals(CtiAgent.PAUSE_DESCRIPTION_WRAPUP))) {
			} else {
				variables.put("paused", "1");
			}

			String routeType = "";
			if (ctiAgent.getBindType() == Const.BIND_TYPE_SOFT_PHONE) {
				routeType = "softphone";
			}
			SipHeaderUtil.setAlternateRouter(variables, LocalIpUtil.getRealIp(), 0, "CCIC2", ctiAgent.getEnterpriseId(),
					callType, routeType, 1);

			// 开始呼叫
			OriginateAction originate = new OriginateAction();
			originate.setChannel(ctiAgent.getLocation()); // set channel
			originate.setContext(Const.DIALPLAN_CONTEXT_PREVIEW_OUTCALL);
			originate.setCallerId(clidRight);
			originate.setVariables(variables);
			originate.setTimeout(45000L);
			originate.setPriority(1); // set priority
			originate.setExten(ctiAgent.getEnterpriseId() + ctiAgent.getCno());
			originate.setAccount(Integer.toString(ctiAgent.getEnterpriseId()));

			if ("0".equals(sync)) {
				originate.setAsync(true);
				PreviewOutcallCallback callback = new PreviewOutcallCallback(this, cid, callStartTime,
						orderCallBackId,loginStatus,pauseDescription);
				originateAsync(originate, callback);
			} else {
				ManagerResponse response = sendAction( originate,60000);
				HashMap<String, String> result = (HashMap<String, String>) handleResponse(response, cid,
						callStartTime, orderCallBackId,loginStatus,pauseDescription);
				AmiActionResponse res = null;
				if (result.get("result").equals("true")) {
					res = AmiActionResponse.createSuccessResponse();
				} else {
					res = AmiActionResponse.createFailResponse(1, "呼叫失败");
				}

				Map<String, Object> values = new HashMap<String, Object>();
				values.put("uniqueId", result.get("uniqueId"));
				res.setValues(values);
				return res;
			}
		} finally {
			redisLock = RedisLockUtil.lock(CtiAgent.getLockKey(cid));
			if (redisLock != null) {
				try {
					CtiAgent newAgent = ctiAgentService.get(cid);
					newAgent.setPreviewOutcallLocked(false);// 解锁
					ctiAgentService.set(newAgent);
				} finally {
					RedisLockUtil.unLock(redisLock);
				}
			}
		}

		return SUCCESS;
	}

	public Map<String, String> handleResponse(ManagerResponse response, String cid, Long callStartTime, String orderCallBackId,String loginStatus,String pauseDescription) {
		HashMap<String, String> res = new HashMap<String, String>();
		String uniqueId = "";
		CtiAgent ctiAgent = ctiAgentService.get(cid);
		if (null != response && response.getResponse().equals("Success")) {
			uniqueId = response.getMessage();

			if (CtiAgent.ONLINE.equals(loginStatus) || (CtiAgent.PAUSE.equals(loginStatus)
					&& CtiAgent.PAUSE_DESCRIPTION_WRAPUP.equals(pauseDescription))) {
				List<String> qList = new ArrayList<>();
				List<QueueMember> list = redisService.getList(Const.REDIS_DB_CONF_INDEX, String.format(CacheKey.QUEUE_MEMBER_ENTERPRISE_ID_CNO
						, ctiAgent.getEnterpriseId(), ctiAgent.getCno()), QueueMember.class);
				for (int i = 0; i < list.size(); i++) {
					qList.add(list.get(i).getQno());
				}
				Map<String, String> unpauseToken = new HashMap<String, String>();
				unpauseToken.put(AmiAction.VARIABLE_LOGIN_TYPE, "backend");
				unpauseToken.put(AmiAction.VARIABLE_ENTERPRISE_ID, String.valueOf(ctiAgent.getEnterpriseId()));
				unpauseToken.put(AmiAction.VARIABLE_CNO, ctiAgent.getCno());
				unpauseToken.put(AmiAction.VARIABLE_CID, cid);
				unpauseToken.put("queues", StringUtils.join(qList,","));
				unPauseActionHandler.handle(unpauseToken);
			}
			res.put("result", "true");
		} else {
			if (response != null) {
				uniqueId = response.getMessage();
			}

			if (CtiAgent.ONLINE.equals(loginStatus) || (CtiAgent.PAUSE.equals(loginStatus)
					&& CtiAgent.PAUSE_DESCRIPTION_WRAPUP.equals(pauseDescription))) {
				List<String> qList = new ArrayList<>();
				List<QueueMember> list = redisService.getList(Const.REDIS_DB_CONF_INDEX, String.format(CacheKey.QUEUE_MEMBER_ENTERPRISE_ID_CNO
						, ctiAgent.getEnterpriseId(), ctiAgent.getCno()), QueueMember.class);
				for (int i = 0; i < list.size(); i++) {
					qList.add(list.get(i).getQno());
				}
				Map<String, String> unpauseToken = new HashMap<String, String>();
				unpauseToken.put(AmiAction.VARIABLE_LOGIN_TYPE, "backend");
				unpauseToken.put(AmiAction.VARIABLE_ENTERPRISE_ID, String.valueOf(ctiAgent.getEnterpriseId()));
				unpauseToken.put(AmiAction.VARIABLE_CNO, ctiAgent.getCno());
				unpauseToken.put(AmiAction.VARIABLE_CID, cid);
				unpauseToken.put("queues", StringUtils.join(qList,","));
				unPauseActionHandler.handle(unpauseToken);
			} else { // 置忙外呼的情况
				if (StringUtils.isNotEmpty("" + callStartTime)) {
					long duration = (new Date().getTime() / 1000) - callStartTime;
					if (duration < 3600 * 24) { // 时长小于24小时
						// 数据库中插入一条
						amiLogQueueEngine.insertLogQueue(null, new Date(), null, cid, "PAUSEOUTCALL",
								callStartTime + "", duration + "", null, null, null);
					}
				}
			}
			res.put("result", "false");
		}

		res.put("uniqueId", uniqueId);
		return res;
	}
}
