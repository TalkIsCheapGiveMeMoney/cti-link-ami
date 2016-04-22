package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.manager.action.HangupAction;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.inc.Const;

/**
 * 通道挂断
 * 
 * @author tianzp
 */
@Component
public class ChannelHangUpActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiAction.CHANNELHANGUP;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.info("handle {} action : {}", this.getAction(), params);

		String uniqueId = params.get("uniqueId");

		AsteriskChannel chan = amiManager.getManager().getAsteriskServer().getChannelById(uniqueId);

		if (chan == null || chan.getState().equals(ChannelState.HUNGUP)) {
			return AmiActionResponse.createFailResponse(3, "通话不存在或已挂断");
		}

		HangupAction hangupAction = new HangupAction(chan.getName());
		hangupAction.setCause(Const.CDR_HANGUP_CAUSE_DISCONNECT);

		if (sendAction(hangupAction) == null) {
			return ERROR_EXCEPTION;
		}

		return SUCCESS;
	}
}
