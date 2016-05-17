package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.inc.AmiParamConst;


/**
 * 咨询取消
 * 
 * @author tianzp
 */
@Component
public class ConsultCancelActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiParamConst.CONSULTCANCEL;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.info("handle {} action : {}", this.getAction(), params);

//		CtiAgent ctiAgent = getCtiAgent(params);
//		if (ctiAgent == null) {
//			return ERROR_BAD_PARAM;
//		}
/*
		if (StringUtils.isEmpty(ctiAgent.getConsultChannel())) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NO_CHANNEL, "没有正确拿到此座席咨询取消的channel");
		}

		// 构造HangupAction对象，传入consultCancelChannel值
		HangupAction hangupAction = new HangupAction(ctiAgent.getConsultChannel()); 
		hangupAction.setCause(new Integer(99));
		
		// 给咨询取消的时候设定一个通道变量consultCancel值
		SetVarAction setVarAction = new SetVarAction(Const.CONSULT_CANCEL, AmiStaticParameters.CONSULT_CANCEL);

		// 在执行咨询取消的时候设定一个通道值
		// 执行AMI命令，取消咨询
		if (sendAction(hangupAction) == null || sendAction(setVarAction) == null) {
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
