package org.asteriskjava.manager.userevent;

import org.asteriskjava.manager.event.UserEvent;

@SuppressWarnings("serial")
public class AgentTryingEvent extends UserEvent {
	private String enterpriseId;
	private String cno;

	public AgentTryingEvent(Object source) {
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

	
}
