package org.asteriskjava.manager.userevent;

import org.asteriskjava.manager.event.UserEvent;

@SuppressWarnings("serial")
public class ConsultStartEvent extends UserEvent {
	private String cno;

	public ConsultStartEvent(Object source) {
		super(source);
	}	

	public String getCno() {
		return cno;
	}

	public void setCno(String cno) {
		this.cno = cno;
	}

}
