package org.asteriskjava.manager.userevent;

import org.asteriskjava.manager.event.UserEvent;

@SuppressWarnings("serial")
public class HangupActionEvent extends UserEvent {
	private String actionId;
	private String actionParams;
	private String callType;

	public HangupActionEvent(Object source) {
		super(source);
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getActionParams() {
		return actionParams;
	}

	public void setActionParams(String actionParams) {
		this.actionParams = actionParams;
	}

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	
}
