package org.asteriskjava.manager.userevent;

import org.asteriskjava.manager.event.UserEvent;

@SuppressWarnings("serial")
public class IncomingEvent extends UserEvent {
	private String callType;
	private String customerNumber;
	private String customerNumberType;
	private String customerAreaCode;
	private String ivrId;
	private String enterpriseIdIvrIdCount;
	private String channel;

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public IncomingEvent(Object source) {
		super(source);
	}

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}
	
	public void setIvrId(String ivrId) {
		this.ivrId = ivrId;
	}
	
	public String getIvrId() {
		return ivrId;
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

	public String getEnterpriseIdIvrIdCount() {
		return enterpriseIdIvrIdCount;
	}

	public void setEnterpriseIdIvrIdCount(String enterpriseIdIvrIdCount) {
		this.enterpriseIdIvrIdCount = enterpriseIdIvrIdCount;
	}
	
}
