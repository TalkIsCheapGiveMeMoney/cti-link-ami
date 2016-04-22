package com.tinet.ctilink.ami.queuemonitor;

import java.io.Serializable;
import java.util.Map;

/**
 * 队列状态
 * 
 * @author Jiangsl
 *
 */
@SuppressWarnings("serial")
public class QueueStatus implements Serializable {
	
	/**
	 * 队列名称
	 */
	private String queueName;

	/**
	 * 队列中最大等待座席数
	 */
	private Integer max;

	/**
	 * 队列排队策略
	 */
	private String strategy;

	/**
	 * 队列当前等待电话数
	 */
	private Integer calls;

	/**
	 * 队列中电话接通平均等待时长
	 */
	private Integer holdTime;

	/**
	 * 队列中电话接通平均通话时长
	 */
	private Integer talkTime;

	/**
	 * 队列中接通电话数
	 */
	private Integer completed;

	/**
	 * 队列中放弃电话数
	 */
	private Integer abandoned;

	/**
	 * 队列服务水平
	 */
	private Integer serviceLevel;

	/**
	 * 队列服务水平效率
	 */
	private Double serviceLevelPerf;

	/**
	 * 队列优先级
	 */
	private Integer weight;

	/**
	 * 队列成员的接听数，key是cid
	 */
	private Map<String, Integer> memberCalls;

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public Integer getCalls() {
		return calls;
	}

	public void setCalls(Integer calls) {
		this.calls = calls;
	}

	public Integer getHoldTime() {
		return holdTime;
	}

	public void setHoldTime(Integer holdTime) {
		this.holdTime = holdTime;
	}

	public Integer getTalkTime() {
		return talkTime;
	}

	public void setTalkTime(Integer talkTime) {
		this.talkTime = talkTime;
	}

	public Integer getCompleted() {
		return completed;
	}

	public void setCompleted(Integer completed) {
		this.completed = completed;
	}

	public Integer getAbandoned() {
		return abandoned;
	}

	public void setAbandoned(Integer abandoned) {
		this.abandoned = abandoned;
	}

	public Integer getServiceLevel() {
		return serviceLevel;
	}

	public void setServiceLevel(Integer serviceLevel) {
		this.serviceLevel = serviceLevel;
	}

	public Double getServiceLevelPerf() {
		return serviceLevelPerf;
	}

	public void setServiceLevelPerf(Double serviceLevelPerf) {
		this.serviceLevelPerf = serviceLevelPerf;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Map<String, Integer> getMemberCalls() {
		return memberCalls;
	}

	public void setMemberCalls(Map<String, Integer> memberCalls) {
		this.memberCalls = memberCalls;
	}

	
}
