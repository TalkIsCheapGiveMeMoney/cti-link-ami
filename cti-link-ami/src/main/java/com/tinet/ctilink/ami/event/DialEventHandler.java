package com.tinet.ctilink.ami.event;

import org.asteriskjava.live.internal.ChannelManager;
import org.asteriskjava.manager.event.DialEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiChannelEventHandler;

@Component
public class DialEventHandler extends AbstractAmiEventHandler  implements AmiChannelEventHandler {
	@Override
	public Class<?> getEventClass() {
		return DialEvent.class;
	}

	@Override
	public void handle(ManagerEvent event, ChannelManager channelManager) {
		logger.info("channelManager.handleDialEvent((DialEvent) event)");
		channelManager.handleDialEvent((DialEvent) event);
	}
}