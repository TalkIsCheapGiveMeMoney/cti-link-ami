package com.tinet.ctilink.ami.action;

import org.asteriskjava.live.internal.QueueManager;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.QueueMemberEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiQueueEventHandler;

@Component
public class QueueMemberEventHandler extends AbstractAmiEventHandler  implements AmiQueueEventHandler {
	@Override
	public Class<?> getEventClass() {
		return QueueMemberEvent.class;
	}

	@Override
	public void handle(ManagerEvent event, QueueManager queueManager) {
		logger.info("queueManager.handleQueueMemberEvent((QueueMemberEvent) event)");
		queueManager.handleQueueMemberEvent((QueueMemberEvent) event);
	}
}
