package com.tinet.ctilink.ami.event;

import org.asteriskjava.live.internal.ChannelManager;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiChannelEventHandler;

@Component
public class NewChannelEventHandler extends AbstractAmiEventHandler  implements AmiChannelEventHandler {
	@Override
	public Class<?> getEventClass() {
		return NewChannelEvent.class;
	}

	@Override
	public void handle(ManagerEvent event, ChannelManager channelManager) {
		logger.info("channelManager.handleNewChannelEvent((NewChannelEvent) event)");
		System.out.println("cdr_main_unique_id:"+((NewChannelEvent) event).getChanVarialbe("cdr_main_unique_id"));
		channelManager.handleNewChannelEvent((NewChannelEvent) event);
	}
}
