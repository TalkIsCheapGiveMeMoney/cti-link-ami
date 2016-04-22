package org.asteriskjava.manager.userevent;

import org.asteriskjava.manager.event.UserEvent;

@SuppressWarnings("serial")
public class BargeUnlinkEvent extends UserEvent {
	private String cno;
	private String bargedCno;
	private String bargeObject;
	private String objectType;

	public BargeUnlinkEvent(Object source) {
		super(source);
	}
	
	public String getCno() {
		return cno;
	}

	public void setCno(String cno) {
		this.cno = cno;
	}

	public String getBargedCno() {
		return bargedCno;
	}

	public void setBargedCno(String bargedCno) {
		this.bargedCno = bargedCno;
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
