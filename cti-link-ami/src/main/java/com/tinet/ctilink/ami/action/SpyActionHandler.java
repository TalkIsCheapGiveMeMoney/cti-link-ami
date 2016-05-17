package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.inc.AmiParamConst;

/**
 * 监听
 * 
 * @author tianzp
 */
@Component
public class SpyActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiParamConst.SPY;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.info("handle {} action : {}", this.getAction(), params);

		String clid = params.get(AmiParamConst.VARIABLE_CLID);
		String gwIp = params.get(AmiParamConst.VARIABLE_GWIP);
		String spyObject = StringUtils.trimToEmpty(params.get(AmiParamConst.VARIABLE_SPY_OBJECT)); // 监听者
		String objectType = StringUtils.trimToEmpty(params.get(AmiParamConst.VARIABLE_OBJECT_TYPE)); // 0：普通电话1：座席号
																								// 2：IVR节点
																								// 3：IVR
																								// id
		String destInterface = params.get("destInterface");
/*
		CtiAgent ctiAgent = getCtiAgent(params);
		if (ctiAgent == null) {
			return ERROR_BAD_PARAM;
		}

		if (destInterface.isEmpty()) {
			return ERROR_BAD_PARAM;
		}

//		if (!CtiAgent.IDLE.equals(ctiAgent.getDeviceStatus())) {
//			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODR_BAD_DEVICE_STATUS,
//					"device status not idle");
//		}

		String spiedCno = StringUtils.trimToEmpty(params.get(AmiAction.VARIABLE_SPIED_CNO));
		CtiAgent spiedCtiAgent = ctiAgentService.get(ctiAgent.getEnterpriseId() + spiedCno);
		if (spiedCtiAgent == null) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NO_SUCH_CNO, "no such cno");
		}

		if (StringUtils.isEmpty(spiedCtiAgent.getChannel()) || !CtiAgent.BUSY.equals(spiedCtiAgent.getDeviceStatus())) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NO_CHANNEL, "no spied channel");
		}

		if (!spiedCtiAgent.getMonitoredType().isEmpty()) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_ALREADY_MONITORED, "already monitored");
		}

		if (spiedCtiAgent.getCallType() == 0) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_CANNOT_MONITORE, "can't monitor");
		}

		Integer callType = spiedCtiAgent.getCallType();
		int routerClidType = 0;
		if (callType == Const.CDR_CALL_TYPE_IB || callType == Const.CDR_CALL_TYPE_OB_WEBCALL) {// 呼入
			routerClidType = Const.ROUTER_CLID_CALL_TYPE_IB_RIGHT;
		} else if (callType == Const.CDR_CALL_TYPE_OB || callType == Const.CDR_CALL_TYPE_DIRECT_OB
				|| callType == Const.CDR_CALL_TYPE_PREVIEW_OB) {// 点击外呼
			routerClidType = Const.ROUTER_CLID_CALL_TYPE_OB_RIGHT;
		} else if (callType == Const.CDR_CALL_TYPE_PREDICTIVE_OB) {// 预测外呼
			routerClidType = Const.ROUTER_CLID_CALL_TYPE_OB_RIGHT;
		}

		long timeout = 30000;

		Map<String, String> variables = new HashMap<String, String>();

		GetVarAction getVarAction = new GetVarAction(spiedCtiAgent.getMainChannel(), Const.CDR_MAIN_UNIQUE_ID);
		GetVarResponse re = (GetVarResponse) sendAction(getVarAction);
		if (re == null) {
			return ERROR_EXCEPTION;
		}
		variables.put(Const.CDR_MAIN_UNIQUE_ID, re.getValue());
		variables.put("__" + Const.ENTERPRISE_ID, String.valueOf(ctiAgent.getEnterpriseId()));
		variables.put(Const.SPY_CHAN, spiedCtiAgent.getChannel());
		variables.put(Const.SPYER_CNO, ctiAgent.getCno());
		variables.put(Const.SPIED_CNO, spiedCno);
		variables.put(Const.SPY_OBJECT, spyObject);
		variables.put(Const.OBJECT_TYPE, objectType);
		variables.put(Const.CDR_ENTERPRISE_ID, String.valueOf(ctiAgent.getEnterpriseId()));
		variables.put(Const.CDR_START_TIME, String.valueOf(new Date().getTime() / 1000));
		variables.put(Const.CDR_DETAIL_CNO, ctiAgent.getCno());
		variables.put(Const.CDR_DETAIL_GW_IP, gwIp);
		if (callType == Const.CDR_CALL_TYPE_IB || callType == Const.CDR_CALL_TYPE_OB_WEBCALL) {// 呼入
			variables.put(Const.CDR_DETAIL_CALL_TYPE, String.valueOf(Const.CDR_CALL_TYPE_IB_SPY));
		} else {
			variables.put(Const.CDR_DETAIL_CALL_TYPE, String.valueOf(Const.CDR_CALL_TYPE_OB_SPY));
		}
		variables.put(Const.CDR_NUMBER_TRUNK, clid);
		variables.put(Const.CDR_STATUS, String.valueOf(Const.CDR_STATUS_DETAIL_CALL_FAIL));

		SipHeaderUtil.setAlternateRouter(variables, LocalIpUtil.getRealIp(), 0, "CCIC2", ctiAgent.getEnterpriseId(),
				variables.get(Const.CDR_DETAIL_CALL_TYPE), String.valueOf(routerClidType), 1);

		OriginateAction originateAction = new OriginateAction();
		originateAction.setChannel(destInterface);
		originateAction.setContext(Const.DIALPLAN_CONTEXT_SPY);
		originateAction.setExten(ctiAgent.getCid());
		originateAction.setVariables(variables);
		originateAction.setTimeout(timeout);
		originateAction.setPriority(new Integer(1));
		originateAction.setCallerId(clid);

		ManagerResponse managerResponse = sendAction(originateAction, 60000);
		if (managerResponse == null) {
			return ERROR_EXCEPTION;
		}*/
		return SUCCESS;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> fixParams, Map<String, String> optionalParams) {
		// TODO Auto-generated method stub
		return null;
	}

}
