package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.inc.AmiParamConst;

/**
 * 三方邀请
 * 
 * @author tianzp
 */
@Component
public class ThreewayActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiParamConst.THREEWAY;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.info("handle {} action : {}", this.getAction(), params);

		String threewayCno = StringUtils.trimToEmpty(params.get(AmiParamConst.VARIABLE_THREEWAYED_CNO)); // 正在通话的座席
		String threewayObject = StringUtils.trimToEmpty(params.get(AmiParamConst.VARIABLE_THREEWAY_OBJECT)); // 管理员进行、发起三方通话
		String objectType = StringUtils.trimToEmpty(params.get(AmiParamConst.VARIABLE_OBJECT_TYPE)); // 0：普通电话1：座席号
																									// 2：分机
																									// 3：IVR节点
																									// 4：IVR
		String routerClidType = params.get(AmiParamConst.VARIABLE_ROUTER_CLID_TYPE);																							// Id
		String destInterface = params.get(AmiParamConst.VARIABLE_DEST_INTERFACE);
		String gwIp = params.get(AmiParamConst.VARIABLE_GWIP);
		String clid = params.get(AmiParamConst.VARIABLE_CLID);

		if (destInterface.isEmpty()) {
			return ERROR_BAD_PARAM;
		}
/*
		CtiAgent ctiAgent = getCtiAgent(params);
		if (ctiAgent == null) {
			return ERROR_BAD_PARAM;
		}

		CtiAgent threewayAgent = ctiAgentService.get(ctiAgent.getEnterpriseId() + threewayCno);
		if (threewayAgent == null) {
			return ERROR_BAD_PARAM;
		}

		if (StringUtils.isEmpty(threewayAgent.getChannel()) || !threewayAgent.getDeviceStatus().equals(CtiAgent.BUSY)) {
			return AmiActionResponse.createFailResponse(8,"坐席不在通话中");
		}

		if (!threewayAgent.getMonitoredType().equals("") || threewayAgent.getCallType() == 0) {
			return AmiActionResponse.createFailResponse(9,"坐席已经在三方中");
		}

		Integer callType = threewayAgent.getCallType();

		Map<String, String> variables = new HashMap<String, String>();

		GetVarAction getVarAction = new GetVarAction(ctiAgent.getMainChannel(), Const.CDR_MAIN_UNIQUE_ID);
		GetVarResponse re = (GetVarResponse) sendAction(getVarAction, 1000);
		if (re == null) {
			return ERROR_EXCEPTION;
		}
		variables.put(Const.CDR_MAIN_UNIQUE_ID, re.getValue());
		variables.put("__" + Const.ENTERPRISE_ID, String.valueOf(threewayAgent.getEnterpriseId()));
		variables.put(Const.THREEWAY_CHAN, threewayAgent.getChannel());
		variables.put(Const.THREEWAYED_CNO, threewayCno);
		variables.put(Const.THREEWAY_OBJECT, threewayObject);
		variables.put(Const.OBJECT_TYPE, objectType);
		variables.put(Const.CDR_ENTERPRISE_ID, String.valueOf(threewayAgent.getEnterpriseId()));
		variables.put(Const.CDR_START_TIME, String.valueOf(new Date().getTime() / 1000));
		variables.put(Const.CDR_DETAIL_GW_IP, gwIp);
		variables.put(Const.CDR_NUMBER_TRUNK, clid);
		variables.put(Const.CDR_STATUS, String.valueOf(Const.CDR_STATUS_DETAIL_CALL_FAIL));

		if (callType == Const.CDR_CALL_TYPE_IB || callType == Const.CDR_CALL_TYPE_OB_WEBCALL) {// 呼入
			variables.put(Const.CDR_DETAIL_CALL_TYPE, String.valueOf(Const.CDR_CALL_TYPE_IB_THREEWAY));
		} else {
			variables.put(Const.CDR_DETAIL_CALL_TYPE, String.valueOf(Const.CDR_CALL_TYPE_OB_THREEWAY));
		}

		SipHeaderUtil.setAlternateRouter(variables, LocalIpUtil.getRealIp(), 0, "CCIC2",
				threewayAgent.getEnterpriseId(), variables.get(Const.CDR_DETAIL_CALL_TYPE),
				String.valueOf(routerClidType), 1);

		OriginateAction originateAction = new OriginateAction();
		originateAction.setChannel(destInterface);
		originateAction.setContext(Const.DIALPLAN_CONTEXT_THREEWAY);
		originateAction.setExten(threewayAgent.getCid());
		originateAction.setVariables(variables);
		originateAction.setTimeout(30000L);
		originateAction.setPriority(new Integer(1));
		originateAction.setCallerId(clid);

		if (sendAction(originateAction, 60000) == null) {
			return ERROR_EXCEPTION;
		}
*/
		return SUCCESS;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> fixParams, Map<String, String> optionalParams) {
		// TODO Auto-generated method stub
		return null;
	}

}
