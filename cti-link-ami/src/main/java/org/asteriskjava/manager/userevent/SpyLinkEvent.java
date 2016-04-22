package org.asteriskjava.manager.userevent;

import org.asteriskjava.manager.event.UserEvent;

@SuppressWarnings("serial")
public class SpyLinkEvent extends UserEvent {
	private String cno;
	private String spiedCno;
	private String spyObject;
	private String objectType;

	public SpyLinkEvent(Object source) {
		super(source);
	}

	public String getCno() {
		return cno;
	}

	public void setCno(String cno) {
		this.cno = cno;
	}

	public String getSpiedCno() {
		return spiedCno;
	}

	public void setSpiedCno(String spiedCno) {
		this.spiedCno = spiedCno;
	}

	public String getSpyObject() {
		return spyObject;
	}

	public void setSpyObject(String spyObject) {
		this.spyObject = spyObject;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

}
