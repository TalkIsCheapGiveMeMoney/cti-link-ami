package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.manager.action.RedirectAction;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.inc.AmiActionTypeConst;
import com.tinet.ctilink.ami.inc.AmiParamConst;


/**
 * 呼叫改发
 * 
 * @author hongzk
 */
@Component
public class RedirectActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiActionTypeConst.REDIRECT;
	}

	@Override
	public AmiActionResponse handle(Map<String, Object> params) {
		logger.info("handle {} action : {}", this.getAction(), params);
		
		String channel =(String) params.get(AmiParamConst.CHANNEL);	
		if(StringUtils.isEmpty(channel))
		{
			return AmiActionResponse.createFailResponse(AmiParamConst.ERROR_CODE, "no channel");
		}
		
		String context = (String)params.get(AmiParamConst.DIALPLAN_CONTEXT);			
		String extension = (String)params.get(AmiParamConst.EXTENSION);	
		Integer priority = (Integer)params.get(AmiParamConst.PRIORITY);	
				
		RedirectAction redirectAction = new RedirectAction();
		redirectAction.setChannel(channel);
		redirectAction.setContext(context);
		redirectAction.setExten(extension);
		redirectAction.setPriority(priority);
		
		String extraChannel =(String) params.get(AmiParamConst.EXTRA_CHANNEL);
		if(StringUtils.isNotEmpty(extraChannel))
		{
			String extraContext = (String)params.get(AmiParamConst.EXTRA_CONTEXT);			
			String extraExtension = (String)params.get(AmiParamConst.EXTRA_EXTEN);
			
			redirectAction.setExtraChannel(extraChannel);
			redirectAction.setExtraContext(extraContext);
			redirectAction.setExtraExten(extraExtension);
			Integer extraPriority = (Integer)params.get(AmiParamConst.EXTRA_PRIORITY);
		}

		if (sendAction(redirectAction, 30000) == null) {
			return ERROR;
		}

		return SUCCESS;
	}



}
