package org.asteriskjava.manager.userevent;

import org.asteriskjava.manager.event.UserEvent;

@SuppressWarnings("serial")
public class PressKeysEvent extends UserEvent {
	private String customerNumber;
	private String ivrId;
	private String ivrNode;
	private String time;
	private String keys;
	private String callType;

	public PressKeysEvent(Object source) {
		super(source);
	}

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getIvrId() {
		return ivrId;
	}

	public void setIvrId(String ivrId) {
		this.ivrId = ivrId;
	}

	public String getIvrNode() {
		return ivrNode;
	}

	public void setIvrNode(String ivrNode) {
		this.ivrNode = ivrNode;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getKeys() {
		return keys;
	}

	public void setKeys(String keys) {
		this.keys = keys;
	}

}
