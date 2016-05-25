package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.asteriskjava.manager.action.RedirectAction;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.inc.AmiActionTypeConst;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.inc.StringUtil;


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
		if(StringUtil.isEmpty(channel))
		{
			return AmiActionResponse.createFailResponse(AmiParamConst.ERRORCODE_NO_CHANNEL, "no channel");
		}
		
		String context = (String)params.get(AmiParamConst.DIALPLAN_CONTEXT);			
		String extension = (String)params.get(AmiParamConst.EXTENSION);	
		
			
				
		RedirectAction redirectAction = new RedirectAction();
		redirectAction.setChannel(channel);
		redirectAction.setContext(context);
		redirectAction.setExten(extension);
		
		String extraChannel =(String) params.get(AmiParamConst.EXTRA_CHANNEL);
		if(StringUtil.isNotEmpty(extraChannel))
		{
			String extraContext = (String)params.get(AmiParamConst.EXTRA_CONTEXT);			
			String extraExtension = (String)params.get(AmiParamConst.EXTRA_EXTEN);
			
			redirectAction.setExtraChannel(extraChannel);
			redirectAction.setExtraContext(extraContext);
			redirectAction.setExtraExten(extraExtension);
			String extraPriority = (String)params.get(AmiParamConst.EXTRA_PRIORITY);
			if(StringUtil.isNotEmpty(extraPriority))
			{
				int priority = 1;
				try{
					priority = Integer.parseInt(extraPriority);
				}catch(Exception e)
				{
					
				}
				redirectAction.setExtraPriority(priority);
			}			
		}

		if (sendAction(redirectAction, 30000) == null) {
			return ERROR_EXCEPTION;
		}

		return SUCCESS;
	}



}
