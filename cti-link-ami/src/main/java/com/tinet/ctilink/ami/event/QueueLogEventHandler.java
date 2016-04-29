package com.tinet.ctilink.ami.event;

import java.util.Date;

import org.asteriskjava.live.internal.QueueManager;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.QueueLogEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.log.AmiLogQueueEngine;
import com.tinet.ctilink.ami.queuemonitor.QueueMonitorServiceImp;
import com.tinet.ctilink.ami.online.CtiAgent;

@Component
public class QueueLogEventHandler extends AbstractAmiEventHandler  implements AmiQueueEventHandler {
	@Autowired
	private AmiLogQueueEngine amiLogQueueEngine ;
	@Autowired
	private QueueMonitorServiceImp queueMonitorService;
	@Override
	public Class<?> getEventClass() {
		return QueueLogEvent.class;
	}

	@Override
	public void handle(ManagerEvent event, QueueManager queueManager) {
		QueueLogEvent queueLogEvent = (QueueLogEvent) event;

		if ("EXITWITHEMPTY".equals(queueLogEvent.getLogEvent())) {
			queueLogEvent.setLogEvent("ENTERQUEUE");
			amiLogQueueEngine.insertLogQueue(queueLogEvent);
			queueLogEvent.setLogEvent("EXITEMPTY");
		} else if ("EXITWITHFULL".equals(queueLogEvent.getLogEvent())) {
			queueLogEvent.setLogEvent("ENTERQUEUE");
			amiLogQueueEngine.insertLogQueue(queueLogEvent);
			queueLogEvent.setLogEvent("FULL");
		} else if ("PAUSE".equals(queueLogEvent.getLogEvent())
				&& CtiAgent.PAUSE_DESCRIPTION_OUTCALLING.equals(queueLogEvent.getData1())) { // 外呼pauseOutCalling不入库
			amiLogQueueEngine.saveLogQueueLog(queueLogEvent);
		} else {
			// 队列接通数统计
			if ("CONNECT".equals(queueLogEvent.getLogEvent())) {
				queueMonitorService.increaseQueueCompleted(queueLogEvent.getQueueName(), new Date());
			}
			amiLogQueueEngine.insertLogQueue(queueLogEvent); // 自定义ManagerEvent事件QueueLogEvent
		}
	}
}
