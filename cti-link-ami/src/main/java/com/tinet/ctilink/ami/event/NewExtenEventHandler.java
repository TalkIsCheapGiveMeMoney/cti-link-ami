package com.tinet.ctilink.ami.event;

import org.asteriskjava.live.internal.ChannelManager;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.NewExtenEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiChannelEventHandler;

@Component
public class NewExtenEventHandler extends AbstractAmiEventHandler  implements AmiChannelEventHandler {
	@Override
	public Class<?> getEventClass() {
		return NewExtenEvent.class;
	}

	@Override
	public void handle(ManagerEvent event, ChannelManager channelManager) {
		channelManager.handleNewExtenEvent((NewExtenEvent) event);
	}
}
