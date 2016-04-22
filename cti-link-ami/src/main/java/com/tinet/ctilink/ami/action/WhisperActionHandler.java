package com.tinet.ctilink.ami.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.manager.action.GetVarAction;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.response.GetVarResponse;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.ami.util.SipHeaderUtil;
import com.tinet.ctilink.inc.Const;
import com.tinet.ctilink.ami.online.CtiAgent;
import com.tinet.ctilink.util.LocalIpUtil;

/**
 * 耳语
 * 
 * @author tianzp
 */
@Component
public class WhisperActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiAction.WHISPER;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.info("handle {} action : {}", this.getAction(), params);

		String whisperObject = params.get(AmiAction.VARIABLE_WHISPER_OBJECT);
		String objectType = params.get(AmiAction.VARIABLE_OBJECT_TYPE);
		String destInterface = params.get(AmiAction.VARIABLE_DEST_INTERFACE);
		String gwIp = params.get(AmiAction.VARIABLE_GWIP);
		String clid = params.get(AmiAction.VARIABLE_CLID);

		if (destInterface.isEmpty()) {
			return ERROR_BAD_PARAM;
		}

		CtiAgent ctiAgent = getCtiAgent(params);
		if (ctiAgent == null) {
			return ERROR_BAD_PARAM;
		}

//		if (!CtiAgent.IDLE.equals(ctiAgent.getDeviceStatus())) {
//			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODR_BAD_DEVICE_STATUS,
//					"device status not idle");
//		}

		String whisperedCno = StringUtils.trimToEmpty(params.get(AmiAction.VARIABLE_WHISPERED_CNO));
		CtiAgent whisperedAgent = ctiAgentService.get(ctiAgent.getEnterpriseId() + whisperedCno);
		if (whisperedAgent == null) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NO_SUCH_CNO, "no such cno");
		}

		if (StringUtils.isEmpty(whisperedAgent.getChannel())
				|| !whisperedAgent.getDeviceStatus().equals(CtiAgent.BUSY)) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NO_CHANNEL, "no whisper channel");
		}

		if (!whisperedAgent.getMonitoredType().equals("")) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_ALREADY_MONITORED, "already monitored");
		}

		Integer callType = whisperedAgent.getCallType();
		if (callType == 0) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_CANNOT_MONITORE, "can't monitor");
		}

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

		GetVarAction getVarAction = new GetVarAction(whisperedAgent.getMainChannel(), Const.CDR_MAIN_UNIQUE_ID);
		GetVarResponse re = (GetVarResponse) sendAction(getVarAction, 1000);
		if (re == null) {
			return ERROR_EXCEPTION;
		}
		variables.put(Const.CDR_MAIN_UNIQUE_ID, re.getValue());
		variables.put("whisper_chan", whisperedAgent.getChannel());
		variables.put("__" + Const.ENTERPRISE_ID, String.valueOf(ctiAgent.getEnterpriseId()));
		variables.put(Const.WHISPER_CHAN, whisperedAgent.getChannel());
		variables.put(Const.WHISPER_CNO, ctiAgent.getCno());
		variables.put(Const.WHISPERED_CNO, whisperedCno);
		variables.put(Const.WHISPER_OBJECT, whisperObject);
		variables.put(Const.OBJECT_TYPE, objectType);
		variables.put(Const.CDR_ENTERPRISE_ID, String.valueOf(ctiAgent.getEnterpriseId()));
		variables.put(Const.CDR_START_TIME, String.valueOf(new Date().getTime() / 1000));
		variables.put(Const.CDR_DETAIL_CNO, ctiAgent.getCno());
		variables.put(Const.CDR_DETAIL_GW_IP, gwIp);
		if (callType == Const.CDR_CALL_TYPE_IB || callType == Const.CDR_CALL_TYPE_OB_WEBCALL) {// 呼入
			variables.put(Const.CDR_DETAIL_CALL_TYPE, String.valueOf(Const.CDR_CALL_TYPE_IB_WHISPER));
		} else {
			variables.put(Const.CDR_DETAIL_CALL_TYPE, String.valueOf(Const.CDR_CALL_TYPE_OB_WHISPER));
		}
		variables.put(Const.CDR_NUMBER_TRUNK, clid);
		variables.put(Const.CDR_STATUS, String.valueOf(Const.CDR_STATUS_DETAIL_CALL_FAIL));

		SipHeaderUtil.setAlternateRouter(variables, LocalIpUtil.getRealIp(), 0, "CCIC2", ctiAgent.getEnterpriseId(),
				variables.get(Const.CDR_DETAIL_CALL_TYPE), String.valueOf(routerClidType), 1);

		OriginateAction originateAction = new OriginateAction();
		originateAction.setChannel(destInterface);
		originateAction.setContext(Const.DIALPLAN_CONTEXT_WHISPER);
		originateAction.setExten(ctiAgent.getCid());
		originateAction.setVariables(variables);
		originateAction.setTimeout(30000l);
		originateAction.setPriority(new Integer(1)); // set
		originateAction.setCallerId(clid);

		if (sendAction(originateAction, 60000) == null) {
			return ERROR_EXCEPTION;
		}

		return SUCCESS;
	}

}
