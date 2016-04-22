package org.asteriskjava.manager.userevent;

import org.asteriskjava.manager.event.UserEvent;

@SuppressWarnings("serial")
public class PredictiveBridgeEvent extends UserEvent {
	private String taskId;

	public PredictiveBridgeEvent(Object source) {
		super(source);
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

}
