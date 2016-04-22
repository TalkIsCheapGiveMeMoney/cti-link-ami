package org.asteriskjava.manager.userevent;

import org.asteriskjava.manager.event.ManagerEvent;

@SuppressWarnings("serial")
public class QueueLogEvent extends ManagerEvent {
	private String callId = "";
	private String time;
	private String agent = "";
	private String queueName = "";
	private String logEvent;
	private String data1 = "";
	private String data2 = "";
	private String data3 = "";
	private String data4 = "";
	private String data5 = "";

	public QueueLogEvent(Object source) {
		super(source);
	}

	public String getCallId() {
		return callId;
	}

	public void setCallId(String callId) {
		if (callId == null) {
			this.callId = "";
		} else {
			this.callId = callId;
		}
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return the queue
	 */
	public String getQueueName() {
		return queueName;
	}

	/**
	 * @param queue
	 *            the queue to set
	 */
	public void setQueueName(String queueName) {
		if (queueName == null) {
			this.queueName = "";
		} else {
			this.queueName = queueName;
		}
	}

	public String getLogEvent() {
		return logEvent;
	}

	public void setLogEvent(String logEvent) {
		this.logEvent = logEvent;
	}

	/**
	 * @return the data1
	 */
	public String getData1() {
		return data1;
	}

	/**
	 * @param data1
	 *            the data1 to set
	 */
	public void setData1(String data1) {
		if (data1 == null) {
			this.data1 = "";
		} else {
			this.data1 = data1;
		}
	}

	/**
	 * @return the data2
	 */
	public String getData2() {
		return data2;
	}

	/**
	 * @param data2
	 *            the data2 to set
	 */
	public void setData2(String data2) {
		if (data2 == null) {
			this.data2 = "";
		} else {
			this.data2 = data2;
		}
	}

	/**
	 * @return the data3
	 */
	public String getData3() {
		return data3;
	}

	/**
	 * @param data3
	 *            the data3 to set
	 */
	public void setData3(String data3) {
		if (data3 == null) {
			this.data3 = "";
		} else {
			this.data3 = data3;
		}
	}

	/**
	 * @return the data4
	 */
	public String getData4() {
		return data4;
	}

	/**
	 * @param data4
	 *            the data4 to set
	 */
	public void setData4(String data4) {
		if (data4 == null) {
			this.data4 = "";
		} else {
			this.data4 = data4;
		}
	}

	/**
	 * @return the data5
	 */
	public String getData5() {
		return data5;
	}

	/**
	 * @param data5
	 *            the data5 to set
	 */
	public void setData5(String data5) {
		if (data5 == null) {
			this.data5 = "";
		} else {
			this.data5 = data5;
		}
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		if (agent == null) {
			this.agent = "";
		} else {
			this.agent = agent;
		}
	}

}
