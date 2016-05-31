package com.tinet.ctilink.ami.event.userevent;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.PredictiveBridgeEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiUserEventHandler;
import com.tinet.ctilink.ami.inc.AmiEventTypeConst;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.json.JSONObject;

/**
 * 预览外呼接通
 * 
 * @author tianzp
 */
@Component
public class PredictiveBridgeEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {

	@Override
	public Class<?> getEventClass() {
		return PredictiveBridgeEvent.class;
	}

	@Override
	public void handle(ManagerEvent event) {
		logger.info("handle {} : {}.", this.getEventClass().getSimpleName(), event);

		String enterpriseId = ((PredictiveBridgeEvent) event).getEnterpriseId();
		String taskId = ((PredictiveBridgeEvent) event).getTaskId();
		String tel = ((PredictiveBridgeEvent) event).getTel();
		
		JSONObject userEvent=new JSONObject();
		userEvent.put(AmiParamConst.EVENT, AmiEventTypeConst.PREVIEW_OUTCALL_BRIDGE);
		userEvent.put(AmiParamConst.ENTERPRISE_ID, enterpriseId);
		userEvent.put(AmiParamConst.TASK_ID, taskId);
		userEvent.put(AmiParamConst.TEL, tel);
		
		publishEvent(userEvent);
	}

}
