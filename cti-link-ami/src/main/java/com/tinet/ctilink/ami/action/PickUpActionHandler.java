package com.tinet.ctilink.ami.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.manager.action.GetVarAction;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.response.GetVarResponse;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.ami.util.SipHeaderUtil;
import com.tinet.ctilink.inc.Const;
import com.tinet.ctilink.util.LocalIpUtil;

/**
 * 抢线
 * 
 * @author tianzp
 */
@Component
public class PickUpActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiParamConst.PICKUP;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.info("handle {} action : {}", this.getAction(), params);

		String gwIp = params.get(AmiParamConst.VARIABLE_GWIP);
		String clid = params.get(AmiParamConst.VARIABLE_CLID);
		String pickupCno = params.get(AmiParamConst.VARIABLE_PICKUP_CNO);
		
//		CtiAgent ctiAgent = getCtiAgent(params);
//		if (ctiAgent == null) {
//			return ERROR_BAD_PARAM;
//		}
//
//		CtiAgent pickupMember = ctiAgentService.get(ctiAgent.getEnterpriseId() + pickupCno);
//		if (pickupMember == null || StringUtils.isEmpty(pickupMember.getChannel())
//				|| !pickupMember.getDeviceStatus().equals(CtiAgent.RINGING)) {
//			AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_BAD_PICKUP, "pickup fail");
//		}
//
//		Integer callType = pickupMember.getCallType();
//		Integer routerClidType = Const.ROUTER_CLID_CALL_TYPE_IB_RIGHT;
//		if (callType.equals(Const.CDR_CALL_TYPE_IB) || callType.equals(Const.CDR_CALL_TYPE_OB_WEBCALL)) {
//			routerClidType = Const.ROUTER_CLID_CALL_TYPE_IB_RIGHT;
//		} else if (callType.equals(Const.CDR_CALL_TYPE_PREDICTIVE_OB)) {
//			routerClidType = Const.ROUTER_CLID_CALL_TYPE_IB_RIGHT;
//		} else {
//			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_BAD_PARAM, "callType not allowed");
//		}
//
//		Map<String, String> variables = new HashMap<String, String>();
//		variables.put(Const.PICKUP_CHAN, pickupMember.getChannel());
//		variables.put("__" + Const.MAIN_CHANNEL, pickupMember.getMainChannel());
//		variables.put("__" + Const.ENTERPRISE_ID, String.valueOf(ctiAgent.getEnterpriseId()));
//		variables.put("__" + Const.CDR_CUSTOMER_NUMBER, pickupMember.getCustomerNumber());
//		variables.put("__" + Const.CDR_CUSTOMER_NUMBER_TYPE, pickupMember.getCustomerNumberTpye());
//		variables.put("__" + Const.CDR_CUSTOMER_AREA_CODE, pickupMember.getCustomerAreaCode());
//		variables.put("__" + Const.CDR_CALL_TYPE, String.valueOf(callType));
//		variables.put("__" + Const.CUR_QUEUE, pickupMember.getCurQueue());
//		variables.put("__" + Const.PICKUPER_CNO, ctiAgent.getCno());
//		variables.put("__" + Const.PICKUPER_INTERFACE, ctiAgent.getLocation());
//		variables.put(Const.CDR_DETAIL_CALL_TYPE, String.valueOf(Const.CDR_CALL_TYPE_IB_PICKUP));
//		variables.put(Const.CDR_NUMBER_TRUNK, clid);
//		variables.put(Const.CDR_STATUS, String.valueOf(Const.CDR_STATUS_DETAIL_CALL_FAIL));
//		variables.put(Const.CDR_ENTERPRISE_ID, String.valueOf(ctiAgent.getEnterpriseId()));
//		variables.put(Const.CDR_START_TIME, String.valueOf(new Date().getTime() / 1000));
//		variables.put(Const.CDR_DETAIL_CNO, ctiAgent.getCno());
//		variables.put(Const.CDR_DETAIL_GW_IP, gwIp);
//
//		GetVarAction getVarAction = new GetVarAction(pickupMember.getMainChannel(), Const.CDR_MAIN_UNIQUE_ID);
//		GetVarResponse re = (GetVarResponse) sendAction(getVarAction, 1000);
//		variables.put(Const.CDR_MAIN_UNIQUE_ID, re.getValue());
//
//		SipHeaderUtil.setAlternateRouter(variables, LocalIpUtil.getRealIp(), 0, "CCIC2", ctiAgent.getEnterpriseId(),
//				String.valueOf(Const.CDR_CALL_TYPE_IB_PICKUP), String.valueOf(routerClidType), 1);
//
//		OriginateAction originateAction = new OriginateAction();
//		originateAction.setChannel(ctiAgent.getLocation());
//		originateAction.setContext(Const.DIALPLAN_CONTEXT_PICKUP);
//		originateAction.setExten(ctiAgent.getEnterpriseId() + pickupCno);
//		originateAction.setVariables(variables);
//		originateAction.setTimeout(30000L);
//		originateAction.setPriority(new Integer(1)); // set priority
//		originateAction.setCallerId(clid);
//
//		if (sendAction(originateAction) == null) {
//			return ERROR_EXCEPTION;
//		}

		return SUCCESS;
	}

}
