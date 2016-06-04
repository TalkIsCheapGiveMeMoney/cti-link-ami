package com.tinet.ctilink.ami.event;

import org.asteriskjava.live.internal.ChannelManager;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.UnlinkEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiChannelEventHandler;

@Component
public class UnlinkEventHandler   extends AbstractAmiEventHandler  implements AmiChannelEventHandler {
	@Override
	public Class<?> getEventClass() {
		return UnlinkEvent.class;
	}

	@Override
	public void handle(ManagerEvent event, ChannelManager channelManager) {
		channelManager.handleBridgeEvent((UnlinkEvent) event);
	}
}
