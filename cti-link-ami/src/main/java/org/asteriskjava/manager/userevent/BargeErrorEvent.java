package org.asteriskjava.manager.userevent;

import org.asteriskjava.manager.event.UserEvent;

@SuppressWarnings("serial")
public class BargeErrorEvent extends UserEvent {
	private String cno;
	private String bargeObject;
	private String objectType;

	public BargeErrorEvent(Object source) {
		super(source);
	}	

	public String getCno() {
		return cno;
	}

	public void setCno(String cno) {
		this.cno = cno;
	}

	public String getBargeObject() {
		return bargeObject;
	}

	public void setBargeObject(String bargeObject) {
		this.bargeObject = bargeObject;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

}
