package com.tinet.ctilink.ami.action;

import org.asteriskjava.live.internal.QueueManager;
import org.asteriskjava.manager.event.LeaveEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiQueueEventHandler;

@Component
public class LeaveEventHandler extends AbstractAmiEventHandler  implements AmiQueueEventHandler{
	
	@Override
	public Class<?> getEventClass() {
		return LeaveEvent.class;
	}

	@Override
	public void handle(ManagerEvent event, QueueManager queueManager) {
		logger.info("queueManager.handleLeaveEvent((LeaveEvent) event)");
		queueManager.handleLeaveEvent((LeaveEvent) event);
	}
}
