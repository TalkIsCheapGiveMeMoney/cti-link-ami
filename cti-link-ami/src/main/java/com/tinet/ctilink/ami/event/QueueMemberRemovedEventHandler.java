package com.tinet.ctilink.ami.event;

import org.asteriskjava.live.internal.QueueManager;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.QueueMemberRemovedEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiQueueEventHandler;

@Component
public class QueueMemberRemovedEventHandler extends AbstractAmiEventHandler  implements AmiQueueEventHandler{
	@Override
	public Class<?> getEventClass() {
		return QueueMemberRemovedEvent.class;
	}

	@Override
	public void handle(ManagerEvent event, QueueManager queueManager) {
		logger.info("queueManager.handleQueueMemberRemovedEvent((QueueMemberRemovedEvent)event)");
		queueManager.handleQueueMemberRemovedEvent((QueueMemberRemovedEvent)event);
	}
}
