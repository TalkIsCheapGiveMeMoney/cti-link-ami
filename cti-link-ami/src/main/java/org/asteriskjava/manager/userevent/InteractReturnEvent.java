package org.asteriskjava.manager.userevent;

import org.asteriskjava.manager.event.UserEvent;

@SuppressWarnings("serial")
public class InteractReturnEvent extends UserEvent {
	private String cno;

	public InteractReturnEvent(Object source) {
		super(source);
	}

	public String getCno() {
		return cno;
	}

	public void setCno(String cno) {
		this.cno = cno;
	}

}
