package com.tinet.ctilink.ami.event;

import org.asteriskjava.live.internal.ChannelManager;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.RenameEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiChannelEventHandler;

@Component
public class RenameEventHandler extends AbstractAmiEventHandler  implements AmiChannelEventHandler {
	@Override
	public Class<?> getEventClass() {
		return RenameEvent.class;
	}

	@Override
	public void handle(ManagerEvent event, ChannelManager channelManager) {
		channelManager.handleRenameEvent((RenameEvent) event);
	}
}
