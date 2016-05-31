package org.asteriskjava.manager.userevent;

import org.asteriskjava.manager.event.UserEvent;

@SuppressWarnings("serial")
public class CallBridgeEvent extends UserEvent {
	private String cno;
	private String callType;
	private String customerNumber;
	private String bridgeTime;
	private String calleeNumber;
	private String detailCallType;

	public CallBridgeEvent(Object source) {
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

	public String getCno() {
		return cno;
	}

	public void setCno(String cno) {
		this.cno = cno;
	}

	public String getBridgeTime() {
		return bridgeTime;
	}

	public void setBridgeTime(String bridgeTime) {
		this.bridgeTime = bridgeTime;
	}

	public String getCalleeNumber() {
		return calleeNumber;
	}

	public void setCalleeNumber(String calleeNumber) {
		this.calleeNumber = calleeNumber;
	}

	public String getDetailCallType() {
		return detailCallType;
	}

	public void setDetailCallType(String detailCallType) {
		this.detailCallType = detailCallType;
	}

}
