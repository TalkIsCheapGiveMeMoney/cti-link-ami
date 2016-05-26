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

//		String enterpriseId = ((PredictiveBridgeEvent) event).getEnterpriseId();
//		String taskId = ((PredictiveBridgeEvent) event).getTaskId();

//		JSONObject userEvent=new JSONObject();
//		userEvent.put(AmiParamConst.VARIABLE_EVENT, AmiEventTypeConst.Pre);
//		userEvent.put(AmiParamConst.VARIABLE_ENTERPRISE_ID, enterpriseId);
//		userEvent.put(AmiParamConst.VARIABLE_TASK_ID, taskId);
//		publishEvent(userEvent);
		throw new UnsupportedOperationException("预测外呼不再支持");
	}

}
