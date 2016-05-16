package com.tinet.ctilink.ami.event.userevent;

import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.WhisperLinkEvent;
import org.springframework.stereotype.Component;
import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiUserEventHandler;
import com.tinet.ctilink.ami.inc.AmiEventConst;

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
		String cid = enterpriseId + whisperedCno;
		
//		if (ctiAgent != null) 
		{
			if (null != cno && !cno.equals("")) {
				Map<String, String> userEvent = new HashMap<String, String>();
				userEvent.put("type", AmiAction.VARIABLE_EVENT);
				userEvent.put(AmiAction.VARIABLE_NAME, AmiEventConst.WHISPER_LINK);
				userEvent.put(AmiAction.VARIABLE_ENTERPRISE_ID, enterpriseId);
				userEvent.put(AmiAction.VARIABLE_CNO, cno);
				userEvent.put(AmiAction.VARIABLE_WHISPER_OBJECT, whisperObject);
				userEvent.put(AmiAction.VARIABLE_OBJECT_TYPE, objectType);
				userEvent.put(AmiAction.VARIABLE_WHISPERED_CNO, whisperedCno);
				publishEvent(userEvent);
			}
		}

	}

}