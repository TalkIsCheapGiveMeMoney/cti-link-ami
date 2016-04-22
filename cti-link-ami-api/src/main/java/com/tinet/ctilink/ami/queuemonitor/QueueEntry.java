package com.tinet.ctilink.ami.queuemonitor;

import java.io.Serializable;
import java.util.Date;

/**
 * 队列排队对象
 * 
 * @author Jiangsl
 *
 */
@SuppressWarnings("serial")
public class QueueEntry implements Serializable{

	/**
	 * 通道Id
	 */
	private String uniqueId;

	/**
	 * 排队客户位置
	 */
	private Integer position;

	/**
	 * 排队客户加入时间
	 */
	private Date joinTime;

	/**
	 * 进入系统时间
	 */
	private String startTime;

	/**
	 * 等待时间（秒）
	 */
	private Long waitTime;

	/**
	 * 排队客户VIP级别
	 */
	private Integer priority;

	/**
	 * 溢出次数
	 */
	private String overflow;

	/**
	 * 呼入号码
	 */
	private String customerNumber;

	/**
	 * 来电客户的level
	 */
	private String customerVip;

	/**
	 * 通话状态
	 */
	private String callStatus;

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public Date getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(Date joinTime) {
		this.joinTime = joinTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public Long getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(Long waitTime) {
		this.waitTime = waitTime;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getOverflow() {
		return overflow;
	}

	public void setOverflow(String overflow) {
		this.overflow = overflow;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getCustomerVip() {
		return customerVip;
	}

	public void setCustomerVip(String customerVip) {
		this.customerVip = customerVip;
	}

	public String getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(String callStatus) {
		this.callStatus = callStatus;
	}
	
	
}
