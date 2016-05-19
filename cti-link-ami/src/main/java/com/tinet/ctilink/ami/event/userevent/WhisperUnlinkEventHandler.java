package com.tinet.ctilink.ami.event.userevent;

import java.util.HashMap;
import java.util.Map;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.WhisperUnlinkEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiUserEventHandler;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.ami.inc.AmiEventTypeConst;

/**
 * 耳语挂断事件
 * 
 * @author tianzp
 */
@Component
public class WhisperUnlinkEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {

	@Override
	public Class<?> getEventClass() {
		return WhisperUnlinkEvent.class;
	}

	@Override
	public void handle(ManagerEvent event) {
		logger.info("handle {} : {}.", this.getEventClass().getSimpleName(), event);

		String enterpriseId = ((WhisperUnlinkEvent) event).getEnterpriseId();
		String cno = ((WhisperUnlinkEvent) event).getCno();
		String whisperObject = ((WhisperUnlinkEvent) event).getWhisperObject();
		String objectType = ((WhisperUnlinkEvent) event).getObjectType();
		String whisperedCno = ((WhisperUnlinkEvent) event).getWhisperedCno();
				
		if (null != cno && !cno.equals("")) {
			Map<String, String> userEvent = new HashMap<String, String>();
			userEvent.put("type", AmiParamConst.VARIABLE_EVENT);
			userEvent.put(AmiParamConst.VARIABLE_NAME, AmiEventTypeConst.WHISPER_UNLINK);
			userEvent.put(AmiParamConst.VARIABLE_ENTERPRISE_ID, enterpriseId);
			userEvent.put(AmiParamConst.VARIABLE_CNO, cno);
			userEvent.put(AmiParamConst.VARIABLE_WHISPER_OBJECT, whisperObject);
			userEvent.put(AmiParamConst.VARIABLE_OBJECT_TYPE, objectType);
			userEvent.put(AmiParamConst.VARIABLE_WHISPERED_CNO, whisperedCno);
			publishEvent(userEvent);
		}

	}

}
