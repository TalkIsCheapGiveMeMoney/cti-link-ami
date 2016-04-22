package com.tinet.ctilink.ami.action;

import org.asteriskjava.live.internal.QueueManager;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.QueueMemberAddedEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiQueueEventHandler;

@Component
public class QueueMemberAddedEventHandler extends AbstractAmiEventHandler  implements AmiQueueEventHandler{
	@Override
	public Class<?> getEventClass() {
		return QueueMemberAddedEvent.class;
	}

	@Override
	public void handle(ManagerEvent event, QueueManager queueManager) {
		logger.info("queueManager.handleQueueMemberAddedEvent((QueueMemberAddedEvent)event)");
		queueManager.handleQueueMemberAddedEvent((QueueMemberAddedEvent)event);
	}
}
