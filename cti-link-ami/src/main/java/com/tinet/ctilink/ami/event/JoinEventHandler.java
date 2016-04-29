package com.tinet.ctilink.ami.event;

import org.asteriskjava.live.internal.QueueManager;
import org.asteriskjava.manager.event.JoinEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiQueueEventHandler;

@Component
public class JoinEventHandler extends AbstractAmiEventHandler  implements AmiQueueEventHandler{
	@Override
	public Class<?> getEventClass() {
		return JoinEvent.class;
	}

	@Override
	public void handle(ManagerEvent event, QueueManager queueManager) {
		logger.info("queueManager.handleJoinEvent((JoinEvent) event)");
		queueManager.handleJoinEvent((JoinEvent) event);
	}
}
