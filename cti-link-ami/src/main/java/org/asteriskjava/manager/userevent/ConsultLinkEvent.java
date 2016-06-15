package org.asteriskjava.manager.userevent;

import org.asteriskjava.manager.event.UserEvent;

@SuppressWarnings("serial")
public class ConsultLinkEvent extends UserEvent {
	private String cno;
	private String consultObject;
	private String objectType;

	public ConsultLinkEvent(Object source) {
		super(source);
	}	

	public String getCno() {
		return cno;
	}

	public void setCno(String cno) {
		this.cno = cno;
	}

	public String getConsultObject() {
		return consultObject;
	}

	public void setConsultObject(String consultObject) {
		this.consultObject = consultObject;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

}
