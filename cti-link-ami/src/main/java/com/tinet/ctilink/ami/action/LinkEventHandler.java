package com.tinet.ctilink.ami.action;

import org.asteriskjava.live.internal.ChannelManager;
import org.asteriskjava.manager.event.LinkEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiChannelEventHandler;

@Component
public class LinkEventHandler  extends AbstractAmiEventHandler  implements AmiChannelEventHandler {
	@Override
	public Class<?> getEventClass() {
		return LinkEvent.class;
	}

	@Override
	public void handle(ManagerEvent event, ChannelManager channelManager) {
		logger.info("channelManager.handleBridgeEvent((BridgeEvent) event)");
		channelManager.handleBridgeEvent((LinkEvent) event);
	}
}
