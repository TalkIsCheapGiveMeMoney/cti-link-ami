package com.tinet.ctilink.ami.event;

import org.asteriskjava.live.internal.QueueManager;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.QueueMemberPenaltyEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiQueueEventHandler;

@Component
public class QueueMemberPenaltyEventHandler extends AbstractAmiEventHandler  implements AmiQueueEventHandler {
	@Override
	public Class<?> getEventClass() {
		return QueueMemberPenaltyEvent.class;
	}

	@Override
	public void handle(ManagerEvent event, QueueManager queueManager) {
		logger.info("queueManager.handleQueueMemberPenaltyEvent((QueueMemberPenaltyEvent)event)");
		queueManager.handleQueueMemberPenaltyEvent((QueueMemberPenaltyEvent)event);
	}
}
