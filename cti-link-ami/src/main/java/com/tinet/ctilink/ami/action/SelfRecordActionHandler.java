package com.tinet.ctilink.ami.action;

import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.manager.action.OriginateAction;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.ami.util.SipHeaderUtil;
import com.tinet.ctilink.inc.Const;
import com.tinet.ctilink.util.LocalIpUtil;

/**
 * 
 * 
 * @author tianzp
 */
@Component
public class SelfRecordActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiAction.SELF_RECORD;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.info("handle {} action : {}", this.getAction(), params);

		Map<String, String> variables = new HashMap<String, String>();
		variables.put(Const.ENTERPRISE_ID, params.get(Const.ENTERPRISE_ID));
		variables.put(Const.CDR_ENTERPRISE_ID, params.get(Const.CDR_ENTERPRISE_ID));
		variables.put(Const.CDR_CUSTOMER_NUMBER, params.get(Const.CDR_CUSTOMER_NUMBER)); //客户号码
		variables.put(Const.CDR_CUSTOMER_NUMBER_TYPE, params.get(Const.CDR_CUSTOMER_NUMBER_TYPE)); //电话类型
		variables.put(Const.CDR_CUSTOMER_AREA_CODE, params.get(Const.CDR_CUSTOMER_AREA_CODE)); //区号
		variables.put(Const.CDR_START_TIME, params.get(Const.CDR_START_TIME)); //区号
		variables.put(Const.CDR_NUMBER_TRUNK, params.get(Const.CDR_NUMBER_TRUNK));
		variables.put(Const.CDR_GW_IP, params.get(Const.CDR_GW_IP));
		variables.put(Const.CDR_CALL_TYPE, String.valueOf(Const.CDR_CALL_TYPE_SELF_RECORD_OB));
		variables.put(Const.CDR_STATUS, String.valueOf(Const.CDR_STATUS_OB_CLIENT_NO_ANSWER));
		SipHeaderUtil.setAlternateRouter(variables, LocalIpUtil.getRealIp(), 0, "CCIC2", params.get(Const.ENTERPRISE_ID), String.valueOf(Const.CDR_CALL_TYPE_SELF_RECORD_OB), String.valueOf(params.get("routerClidType")), 1);
		
		OriginateAction originateAction;
		originateAction = new OriginateAction();
		originateAction.setChannel(params.get("destInterface")); // set channel
		originateAction.setContext(Const.DIALPLAN_CONTEXT_SELF_RECORD); // set
		// context
		originateAction.setExten(params.get("callerNumber"));
		originateAction.setCallerId(params.get("clid"));
		long timeout = 30000;
		originateAction.setTimeout(timeout);
		originateAction.setPriority(new Integer(1)); // set priority
		originateAction.setVariables(variables);
		
		if (sendAction(originateAction, 30000) == null) {
			return ERROR_EXCEPTION;
		}

		return SUCCESS;
	}

}
