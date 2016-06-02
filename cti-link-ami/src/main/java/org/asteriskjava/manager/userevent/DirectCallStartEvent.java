package org.asteriskjava.manager.userevent;

import org.asteriskjava.manager.event.UserEvent;

@SuppressWarnings("serial")
public class DirectCallStartEvent extends UserEvent {
	private String exten;

	public DirectCallStartEvent(Object source) {
		super(source);
	}


	public String getExten() {
		return exten;
	}

	public void setExten(String exten) {
		this.exten = exten;
	}

}
