package com.tinet.ctilink.ami.event;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.PredictiveBridgeEvent;
import org.springframework.stereotype.Component;

/**
 * 预测外呼
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

		throw new UnsupportedOperationException("预测外呼不再支持");

	}
}
