package com.tinet.ctilink.ami.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.ami.online.CtiAgent;
import com.tinet.ctilink.ami.util.SipHeaderUtil;
import com.tinet.ctilink.inc.Const;
import com.tinet.ctilink.util.LocalIpUtil;
import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.manager.action.GetVarAction;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.response.GetVarResponse;
import org.springframework.stereotype.Component;


/**
 * 强插
 * 
 * @author tianzp
 */
@Component
public class BargeActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiAction.BARGE;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.info("handle {} action : {}", this.getAction(), params);

		String bargeObject = params.get(AmiAction.VARIABLE_BARGE_OBJECT);
		String objectType = params.get(AmiAction.VARIABLE_OBJECT_TYPE);
		String bargedCno = StringUtils.trimToEmpty(params.get(AmiAction.VARIABLE_BARGED_CNO));
		String destInterface = params.get(AmiAction.VARIABLE_DEST_INTERFACE);
		String gwIp = params.get(AmiAction.VARIABLE_GWIP);
		String clid = params.get(AmiAction.VARIABLE_CLID);// 外显号码

		CtiAgent ctiAgent = getCtiAgent(params);
		if (ctiAgent == null) {
			return ERROR_BAD_PARAM;
		}

//		if (!CtiAgent.IDLE.equals(ctiAgent.getDeviceStatus())) {
//			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODR_BAD_DEVICE_STATUS,
//					"device status not idle");
//		}

		CtiAgent bargedAgent = ctiAgentService.get(ctiAgent.getEnterpriseId() + bargedCno);
		if (bargedAgent == null) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NO_SUCH_CNO, "no such cno");
		}

		if (StringUtils.isEmpty(bargedAgent.getMainChannel()) || !CtiAgent.BUSY.equals(bargedAgent.getDeviceStatus())) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NO_CHANNEL, "no barged channel");
		}

		Integer callType = bargedAgent.getCallType();
		int routerClidType = 0;
		if (callType == Const.CDR_CALL_TYPE_IB || callType == Const.CDR_CALL_TYPE_OB_WEBCALL) {// 呼入
			routerClidType = Const.ROUTER_CLID_CALL_TYPE_IB_RIGHT;
		} else if (callType == Const.CDR_CALL_TYPE_OB || callType == Const.CDR_CALL_TYPE_DIRECT_OB
				|| callType == Const.CDR_CALL_TYPE_PREVIEW_OB) {// 点击外呼
			routerClidType = Const.ROUTER_CLID_CALL_TYPE_OB_RIGHT;
		} else if (callType == Const.CDR_CALL_TYPE_PREDICTIVE_OB) {// 预测外呼
			routerClidType = Const.ROUTER_CLID_CALL_TYPE_OB_RIGHT;
		}

		Map<String, String> variables = new HashMap<String, String>();
		variables.put(Const.BARGE_CHAN, bargedAgent.getMainChannel());
		variables.put("__" + Const.CDR_CUSTOMER_NUMBER, bargedAgent.getCustomerNumber()); // 客户号码
		variables.put("__" + Const.CDR_CUSTOMER_NUMBER_TYPE, bargedAgent.getCustomerNumberTpye()); // 电话类型
		variables.put("__" + Const.CDR_CUSTOMER_AREA_CODE, bargedAgent.getCustomerAreaCode()); // 区号
		variables.put("__" + Const.CUR_QUEUE, bargedAgent.getCurQueue()); //
		variables.put("__" + Const.ENTERPRISE_ID, String.valueOf(ctiAgent.getEnterpriseId()));
		variables.put("__" + Const.BARGER_CNO, ctiAgent.getCno());
		variables.put("__" + Const.BARGED_CNO, bargedCno);
		variables.put("__" + Const.BARGER_INTERFACE, destInterface);
		variables.put(Const.CDR_DETAIL_GW_IP, gwIp);
		variables.put(Const.CDR_NUMBER_TRUNK, clid);
		variables.put(Const.CDR_STATUS, String.valueOf(Const.CDR_STATUS_DETAIL_CALL_FAIL));
		variables.put(Const.CDR_ENTERPRISE_ID, String.valueOf(ctiAgent.getEnterpriseId()));
		variables.put(Const.CDR_START_TIME, String.valueOf(new Date().getTime() / 1000));
		variables.put(Const.CDR_DETAIL_CNO, ctiAgent.getCno());
		variables.put(Const.BARGE_OBJECT, bargeObject);
		variables.put(Const.OBJECT_TYPE, objectType);

		if (routerClidType == Const.ROUTER_CLID_CALL_TYPE_IB_RIGHT) {
			variables.put(Const.CDR_DETAIL_CALL_TYPE, String.valueOf(Const.CDR_CALL_TYPE_IB_BARGE));
		} else {
			variables.put(Const.CDR_DETAIL_CALL_TYPE, String.valueOf(Const.CDR_CALL_TYPE_OB_BARGE));
		}

		GetVarAction getVarAction = new GetVarAction(bargedAgent.getMainChannel(), Const.CDR_MAIN_UNIQUE_ID);
		GetVarResponse re = (GetVarResponse) sendAction(getVarAction, 1000);
		if (re != null) {
			variables.put(Const.CDR_MAIN_UNIQUE_ID, re.getValue());
		}

		SipHeaderUtil.setAlternateRouter(variables, LocalIpUtil.getRealIp(), 0, "CCIC2", ctiAgent.getEnterpriseId(),
				variables.get(Const.CDR_DETAIL_CALL_TYPE), String.valueOf(routerClidType), 1);

		OriginateAction originateAction;
		originateAction = new OriginateAction();
		originateAction.setChannel(destInterface);
		originateAction.setContext(Const.DIALPLAN_CONTEXT_BARGE);
		originateAction.setExten(ctiAgent.getCid());
		originateAction.setVariables(variables);
		originateAction.setTimeout(30000l);
		originateAction.setPriority(new Integer(1));
		originateAction.setCallerId(clid);

		if (sendAction(originateAction, 60000) == null) {
			return ERROR_EXCEPTION;
		}

		return SUCCESS;
	}

}
