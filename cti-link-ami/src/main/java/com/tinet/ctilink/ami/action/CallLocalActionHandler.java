package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.manager.action.OriginateAction;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.action.AmiActionResponse;
import com.tinet.ctilink.ami.inc.AmiParamConst;

/**
 * 呼叫内部分机
 * 
 * @author tianzp
 */
@Component
public class CallLocalActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiParamConst.CALLLOCAL;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		String context = StringUtils.trimToEmpty(params.get("context"));
		String extension = StringUtils.trimToEmpty(params.get("extension"));
		String targetContext = StringUtils.trimToEmpty(params.get("targetContext"));
		String targetExtension = StringUtils.trimToEmpty(params.get("targetExtension"));
		params.remove("context");
		params.remove("extension");
		params.remove("targetContext");
		params.remove("targetExtension");

		OriginateAction originateAction = new OriginateAction();
		originateAction.setChannel("Local/" + extension + "@" + context);
		originateAction.setCallerId("");
		originateAction.setContext(targetContext);
		originateAction.setExten(targetExtension);
		originateAction.setVariables(params);
		originateAction.setTimeout((long) 5000);
		originateAction.setPriority(new Integer(1)); // set priority
		originateAction.setAsync(true);

		if (sendAction(originateAction, 60000) == null) {
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
