package com.tinet.ctilink.ami.action;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiChannelEventHandler;
import org.asteriskjava.live.internal.ChannelManager;
import org.asteriskjava.manager.event.BridgeEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.springframework.stereotype.Component;


@Component
public class BridgeEventHandler extends AbstractAmiEventHandler implements AmiChannelEventHandler {
	@Override
	public Class<?> getEventClass() {
		return BridgeEvent.class;
	}

	@Override
	public void handle(ManagerEvent event, ChannelManager channelManager) {
		logger.info("channelManager.handleBridgeEvent((BridgeEvent) event)");
		channelManager.handleBridgeEvent((BridgeEvent) event);
	}
}
