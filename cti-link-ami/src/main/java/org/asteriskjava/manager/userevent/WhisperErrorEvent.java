package org.asteriskjava.manager.userevent;

import org.asteriskjava.manager.event.UserEvent;

@SuppressWarnings("serial")
public class WhisperErrorEvent extends UserEvent {
	private String cno;
	private String whisperedCno;
	private String whisperObject;
	private String objectType;

	public WhisperErrorEvent(Object source) {
		super(source);
	}

	public String getCno() {
		return cno;
	}

	public void setCno(String cno) {
		this.cno = cno;
	}

	public String getWhisperedCno() {
		return whisperedCno;
	}

	public void setWhisperedCno(String whisperedCno) {
		this.whisperedCno = whisperedCno;
	}

	public String getWhisperObject() {
		return whisperObject;
	}

	public void setWhisperObject(String whisperObject) {
		this.whisperObject = whisperObject;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
}
