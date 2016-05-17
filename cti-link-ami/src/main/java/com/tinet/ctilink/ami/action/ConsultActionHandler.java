package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.asteriskjava.manager.action.AtxferAction;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.inc.Const;


/**
 * 咨询
 * 
 * @author tianzp
 */
@Component
public class ConsultActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiParamConst.CONSULT;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.info("handle {} action : {}", this.getAction(), params);

		String consultObject = params.get(AmiParamConst.VARIABLE_CONSULT_OBJECT); // 电话号码
																				// 座席号
																				// 分机号
		String objectType = params.get(AmiParamConst.VARIABLE_OBJECT_TYPE); // 0.电话
																		// 1.座席号
																		// 2.分机
/*
		CtiAgent ctiAgent = getCtiAgent(params);
		if (ctiAgent == null) {
			return ERROR_BAD_PARAM;
		}

		String extension = objectType + consultObject + "#";

		if (StringUtils.isEmpty(ctiAgent.getChannel())) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NO_CHANNEL, "no channel");
		}

		if ("1".equals(objectType)) {
			CtiAgent otherAgent = ctiAgentService.get(ctiAgent.getEnterpriseId() + consultObject);
			if (otherAgent != null) {
				if (CtiAgent.BUSY.equals(otherAgent.getDeviceStatus())
						|| CtiAgent.PAUSE.equals(otherAgent.getLoginStatus())) {
					return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_PAUSE_OR_BUSY,
							"座席忙或者座席已置忙，请稍后转移给此座席");
				}
			} else {
				return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NOT_ONLINE, "座席不在线，请稍后咨询此座席");
			}
		}
*/
		AtxferAction transferAction = new AtxferAction();
//		transferAction.setChannel(ctiAgent.getChannel());
		transferAction.setContext(Const.DIALPLAN_CONTEXT_CONSULT);
//		transferAction.setExten(extension);

		if (sendAction(transferAction, 30000) == null) {
			return ERROR_EXCEPTION;
		}

		return SUCCESS;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> fixParams, Map<String, String> optionalParams) {
		// TODO Auto-generated method stub
		return null;
	}

}
