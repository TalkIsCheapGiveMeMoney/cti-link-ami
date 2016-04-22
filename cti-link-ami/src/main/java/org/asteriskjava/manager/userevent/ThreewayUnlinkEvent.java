package org.asteriskjava.manager.userevent;

import org.asteriskjava.manager.event.UserEvent;

@SuppressWarnings("serial")
public class ThreewayUnlinkEvent extends UserEvent {
	private String cno;
	private String threewayedCno;
	private String threewayObject;
	private String objectType;

	public ThreewayUnlinkEvent(Object source) {
		super(source);
	}

	public String getCno() {
		return cno;
	}

	public void setCno(String cno) {
		this.cno = cno;
	}

	public String getThreewayedCno() {
		return threewayedCno;
	}

	public void setThreewayedCno(String threewayedCno) {
		this.threewayedCno = threewayedCno;
	}

	public String getThreewayObject() {
		return threewayObject;
	}

	public void setThreewayObject(String threewayObject) {
		this.threewayObject = threewayObject;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
}
