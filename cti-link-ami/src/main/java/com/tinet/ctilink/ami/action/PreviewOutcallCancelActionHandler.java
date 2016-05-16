package com.tinet.ctilink.ami.action;

import java.util.Map;

import com.tinet.ctilink.ami.AmiAction;
import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.manager.action.HangupAction;
import org.springframework.stereotype.Component;


/**
 * 取消预览式外呼
 * 
 * @author tianzp
 */
@Component
public class PreviewOutcallCancelActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiAction.PREVIEW_OUTCALL_CANCEL;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.debug("handle {} action : {}", this.getAction(), params);
	/*	
		CtiAgent ctiAgent = getCtiAgent(params);
		if (ctiAgent == null) {
			return ERROR_BAD_PARAM;
		}

		if (StringUtils.isEmpty(ctiAgent.getChannel())) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NO_CHANNEL, "没有正确拿到此座席外呼的channel");
		}

		// 构造HangupAction对象，传入consultCancelChannel值
		HangupAction hangupAction = new HangupAction(ctiAgent.getChannel()); 
		hangupAction.setCause(new Integer(99));

		if(sendAction(hangupAction)==null){
			return ERROR_EXCEPTION;
		}
*/		
		return SUCCESS;
	}

}
