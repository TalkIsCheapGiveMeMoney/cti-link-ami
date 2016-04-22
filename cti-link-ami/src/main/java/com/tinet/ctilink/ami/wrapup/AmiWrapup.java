package com.tinet.ctilink.ami.wrapup;

import java.util.Date;

/**
 * 与呼叫相关的事件封装。
 * <p>
 * 文件名： Event.java
 * <p>
 * Copyright (c) 2006-2010 T&I Net Communication CO.,LTD. All rights reserved.
 * 
 * @author 安静波
 * @since 1.0
 * @version 1.0
 */

public class AmiWrapup {

	private String location;
	private Integer wrapup;
	private Date wrapupStart;

	public AmiWrapup(String location) {
		super();
		this.location = location;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getWrapup() {
		return wrapup;
	}

	public void setWrapup(Integer wrapup) {
		this.wrapup = wrapup;
	}

	public Date getWrapupStart() {
		return wrapupStart;
	}

	public void setWrapupStart(Date wrapupStart) {
		this.wrapupStart = wrapupStart;
	}

	public String toString() {
		return "{location=" + location + ",wrapup=" + wrapup + ",wrapupStart=" + wrapupStart
				.toString() + "}";
	}
}
