package com.tinet.ctilink.ami.log;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tinet.ctilink.inc.Const;
import org.apache.commons.lang3.StringUtils;

/**
 * 队列中日志表
 * <p>
 * 文件名： LogQueue.java
 * <p>
 * Copyright (c) 2006-2010 T&I Net Communication CO.,LTD. All rights reserved.
 * 
 * @author MyEclipse Persistence Tools
 * @since 1.0
 * @version 1.0
 */
public class LogQueue implements java.io.Serializable {

	// Fields

	private String uniqueId;
	private Date time;
	private String queue;
	private String agent;
	private String event;
	private String data1;
	private String data2;
	private String data3;
	private String data4;
	private String data5;

	/** default constructor */
	public LogQueue() {
		this.setTime(new Date());
	}

	/** full constructor */
	public LogQueue(String uniqueId, Integer enterpriseId, Date time, String queue, String agent, String event, String data1, String data2, String data3, String data4, String data5) {
		this.uniqueId = uniqueId;
		this.time = time;
		this.queue = queue;
		this.agent = agent;
		this.event = event;
		this.data1 = data1;
		this.data2 = data2;
		this.data3 = data3;
		this.data4 = data4;
		this.data5 = data5;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getData1() {
		return data1;
	}

	public void setData1(String data1) {
		this.data1 = data1;
	}

	public String getData2() {
		return data2;
	}

	public void setData2(String data2) {
		this.data2 = data2;
	}

	public String getData3() {
		return data3;
	}

	public void setData3(String data3) {
		this.data3 = data3;
	}

	public String getData4() {
		return data4;
	}

	public void setData4(String data4) {
		this.data4 = data4;
	}

	public String getData5() {
		return data5;
	}

	public void setData5(String data5) {
		this.data5 = data5;
	}

	@JsonIgnore
	public String getCno() {
		return StringUtils.isEmpty(agent) ? "" : agent.substring(Const.ENTERPRISE_ID_LEN);
	}

	public String toString() {
		return "{uniqueId:" + uniqueId + ",time:" + time + ",queue:" + queue + ",agent:" + agent + ",event:" + event + ",data1:" + data1 + ",data2:" + data2 + ",data3:" + data3 + ",data4:" + data4 + ",data5:" + data5 + "}";
	}
}