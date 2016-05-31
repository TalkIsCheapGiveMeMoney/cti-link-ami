package org.asteriskjava.manager.userevent;

import org.asteriskjava.manager.event.UserEvent;

@SuppressWarnings("serial")
public class AgentCallTryingEvent extends UserEvent {
	private String enterpriseId;
	private String cno;
	private String callType;
	private String consulterCno;
	private String transferCno;

	public AgentCallTryingEvent(Object source) {
		super(source);
	}

	public String getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getCno() {
		return cno;
	}

	public void setCno(String cno) {
		this.cno = cno;
	}

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public String getConsulterCno() {
		return consulterCno;
	}

	public void setConsulterCno(String consulterCno) {
		this.consulterCno = consulterCno;
	}

	public String getTransferCno() {
		return transferCno;
	}

	public void setTransferCno(String transferCno) {
		this.transferCno = transferCno;
	}	

	
}
