package com.tinet.ctilink.ami.action;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.manager.action.RedirectAction;
import org.asteriskjava.manager.action.SetVarAction;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.inc.Const;

/**
 * ivr 回调
 * 
 * @author tianzp
 */
@Component
public class IvrCallbackActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiAction.IVR_CALLBACK;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		String uniqueId = StringUtils.trimToEmpty(params.get(AmiAction.VARIABLE_UNIQUEID));
		String paramNames = StringUtils.trimToEmpty(params.get(AmiAction.VARIABLE_PARAM_NAMES));
		String paramValues = StringUtils.trimToEmpty(params.get(AmiAction.VARIABLE_PARAM_VALUES));

		if (StringUtils.isEmpty(uniqueId)) {
			return ERROR_BAD_PARAM;
		}

		AsteriskChannel chan = amiManager.getManager().getAsteriskServer().getChannelById(uniqueId);
		if (chan != null && !chan.getState().equals(ChannelState.HUNGUP)) {
			String ivrWaitStatus = chan.getNoCacheVariable(Const.IVR_WAIT_STATUS);
			if ("1".equals(ivrWaitStatus)) {
				SetVarAction setVarAction = new SetVarAction(uniqueId, Const.IVR_WAIT_DONE, "1");
				if (sendAction(setVarAction) == null) {
					return AmiActionResponse.createFailResponse(6,"其他错误");
				}
				String[] names = paramNames.split(",");
				String[] values = paramValues.split(",");
				for (int i = 0; i < names.length; i++) {
					if (StringUtils.isNotEmpty(names[i]) && StringUtils.isNotEmpty(values[i])) {
						setVarAction = new SetVarAction(uniqueId, names[i], values[i]);
						if (sendAction(setVarAction) == null) {
							return AmiActionResponse.createFailResponse(6,"其他错误");
						}
					}
				}
				RedirectAction redirectAction = new RedirectAction();
				redirectAction.setChannel(uniqueId);
				redirectAction.setContext(Const.DIALPLAN_CONTEXT_IVR_WAIT);
				redirectAction.setExten("~~s~~");
				redirectAction.setPriority(1);
				if (sendAction(redirectAction) == null) {
					return AmiActionResponse.createFailResponse(6,"其他错误");
				}
				return SUCCESS;
			}else{
               return AmiActionResponse.createFailResponse(5,"回调已超时");
			}
		}else{
			return AmiActionResponse.createFailResponse(4,"通道已不存在");
		}
		
	}

}
