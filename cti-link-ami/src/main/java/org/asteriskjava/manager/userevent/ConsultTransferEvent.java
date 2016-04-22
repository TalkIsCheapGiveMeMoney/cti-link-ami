package org.asteriskjava.manager.userevent;

import org.asteriskjava.manager.event.UserEvent;

@SuppressWarnings("serial")
public class ConsultTransferEvent extends UserEvent {
	private String cno;
	private String consulterCno;

	public ConsultTransferEvent(Object source) {
		super(source);
	}
	
	public String getCno() {
		return cno;
	}

	public void setCno(String cno) {
		this.cno = cno;
	}

	public String getConsulterCno() {
		return consulterCno;
	}

	public void setConsulterCno(String consulterCno) {
		this.consulterCno = consulterCno;
	}

}
