package org.asteriskjava.manager.userevent;

import org.asteriskjava.manager.event.UserEvent;

@SuppressWarnings("serial")
public class ConsultThreewayUnlinkEvent extends UserEvent {
	private String cno;

	public ConsultThreewayUnlinkEvent(Object source) {
		super(source);
	}

	public String getCno() {
		return cno;
	}

	public void setCno(String cno) {
		this.cno = cno;
	}

}
