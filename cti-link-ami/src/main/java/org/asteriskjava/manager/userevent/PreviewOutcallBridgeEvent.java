package org.asteriskjava.manager.userevent;

import org.asteriskjava.manager.event.UserEvent;

@SuppressWarnings("serial")
public class PreviewOutcallBridgeEvent extends UserEvent {
	private String callType;
	private String customerNumber;
	private String customerNumberType;
	private String customerAreaCode;
	private String mainUniqueId;
	private String cno;

	public PreviewOutcallBridgeEvent(Object source) {
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

	public String getCustomerNumberType() {
		return customerNumberType;
	}

	public void setCustomerNumberType(String customerNumberType) {
		this.customerNumberType = customerNumberType;
	}

	public String getCustomerAreaCode() {
		return customerAreaCode;
	}

	public void setCustomerAreaCode(String customerAreaCode) {
		this.customerAreaCode = customerAreaCode;
	}

	public String getCno() {
		return cno;
	}

	public void setCno(String cno) {
		this.cno = cno;
	}

	public String getMainUniqueId() {
		return mainUniqueId;
	}

	public void setMainUniqueId(String mainUniqueId) {
		this.mainUniqueId = mainUniqueId;
	}

}
