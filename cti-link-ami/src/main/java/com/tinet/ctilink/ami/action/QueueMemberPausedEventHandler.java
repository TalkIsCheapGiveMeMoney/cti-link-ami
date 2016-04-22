package com.tinet.ctilink.ami.action;

import org.asteriskjava.live.internal.QueueManager;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.QueueMemberPausedEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiQueueEventHandler;

@Component
public class QueueMemberPausedEventHandler extends AbstractAmiEventHandler  implements AmiQueueEventHandler{
	@Override
	public Class<?> getEventClass() {
		return QueueMemberPausedEvent.class;
	}

	@Override
	public void handle(ManagerEvent event, QueueManager queueManager) {
		logger.info("queueManager.handleQueueMemberPausedEvent((QueueMemberPausedEvent)event)");
		queueManager.handleQueueMemberPausedEvent((QueueMemberPausedEvent)event);
	}
}
