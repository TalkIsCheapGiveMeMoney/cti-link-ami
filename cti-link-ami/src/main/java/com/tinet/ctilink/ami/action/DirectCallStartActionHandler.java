package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.manager.action.RedirectAction;
import org.asteriskjava.manager.action.SetVarAction;
import org.springframework.util.StringUtils;

import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.inc.Const;


/**
 * 摘机直呼送客户号码
 * 
 * @author tianzp
 */
public class DirectCallStartActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiParamConst.DIRECT_CALL_START;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.info("handle {} action : {}", this.getAction(), params);

/*		CtiAgent ctiAgent = getCtiAgent(params);
		if (ctiAgent == null) {
			return ERROR_BAD_PARAM;
		}

		if (StringUtils.isEmpty(ctiAgent.getChannel())) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NO_CHANNEL, "no channel");
		}

		AsteriskChannel channel = amiManager.getManager().getAsteriskServer()
				.getChannelByName(ctiAgent.getChannel());

		if (channel == null || ChannelState.HUNGUP.equals(channel.getState())) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NO_CHANNEL, "no such channel");
		}

		String directCallReadWaitStatus = channel.getNoCacheVariable(Const.DIRECT_CALL_READ_STATUS);
		if ("1".equals(directCallReadWaitStatus)) {
			SetVarAction setVarAction = new SetVarAction(ctiAgent.getChannel(), Const.DIRECT_CALL_READ_DONE, "1");

			sendAction(setVarAction);

			setVarAction = new SetVarAction(ctiAgent.getChannel(), Const.CDR_CUSTOMER_NUMBER,
					ctiAgent.getCustomerNumber());

			sendAction(setVarAction);

			RedirectAction redirectAction = new RedirectAction();
			redirectAction.setChannel(ctiAgent.getChannel());
			redirectAction.setContext(Const.DIALPLAN_CONTEXT_DIRECT_CALL_READ);
			redirectAction.setExten("~~s~~");
			redirectAction.setPriority(1);

			sendAction(redirectAction);
		} else {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_EXCEPTION, "回调已超时");
		}
*/
		return SUCCESS;
	}

}
