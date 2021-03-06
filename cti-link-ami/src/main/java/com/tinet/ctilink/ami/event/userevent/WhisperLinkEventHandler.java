package com.tinet.ctilink.ami.event.userevent;

import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.WhisperLinkEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiUserEventHandler;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.json.JSONObject;
import com.tinet.ctilink.ami.inc.AmiEventTypeConst;

/**
 * 耳语事件
 * 
 * @author tianzp
 */
@Component
public class WhisperLinkEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {

	@Override
	public Class<?> getEventClass() {
		return WhisperLinkEvent.class;
	}

	@Override
	public void handle(ManagerEvent event) {
		logger.info("handle {} : {}.", this.getEventClass().getSimpleName(), event);

		String channel = ((WhisperLinkEvent) event).getChannel();
		String enterpriseId = ((WhisperLinkEvent) event).getEnterpriseId();
		String cno = ((WhisperLinkEvent) event).getCno();
		String whisperObject = ((WhisperLinkEvent) event).getWhisperObject();
		String objectType = ((WhisperLinkEvent) event).getObjectType();
		String whisperedCno = ((WhisperLinkEvent) event).getWhisperedCno();
		
		JSONObject userEvent=new JSONObject();		
		userEvent.put(AmiParamConst.EVENT, AmiEventTypeConst.WHISPER_LINK);
		userEvent.put(AmiParamConst.ENTERPRISE_ID, enterpriseId);
		userEvent.put(AmiParamConst.CNO, cno);
		userEvent.put(AmiParamConst.WHISPER_OBJECT, whisperObject);
		userEvent.put(AmiParamConst.OBJECT_TYPE, objectType);
		userEvent.put(AmiParamConst.WHISPERED_CNO, whisperedCno);
		publishEvent(userEvent);
	}

}
