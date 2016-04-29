package com.tinet.ctilink.ami.entity;
// default package

import java.util.Date;

/**
 * 网上400呼叫日志表
 * <p>
 * 文件名： LogWeb400Call.java
 * <p>
 * Copyright (c) 2006-2010 T&I Net Communication CO.,LTD. All rights reserved.
 * 
 * @author MyEclipse Persistence Tools
 * @since 1.0
 * @version 1.0
 */
public class LogWeb400Call implements java.io.Serializable {

	// Fields

	private Integer enterpriseId;
	private String hotline;
	private String uniqueid;
	private String ip;
	private String cookie;
	private String tel;
	private Integer result;
	private Date createTime;

	// Constructors

	/** default constructor */
	public LogWeb400Call() {
		this.createTime = new Date();
	}

	/** minimal constructor */
	public LogWeb400Call(Integer enterpriseId, String cookie, Integer result) {
		this.enterpriseId = enterpriseId;
		this.cookie = cookie;
		this.result = result;
		this.createTime = new Date();
	}

	/** full constructor */
	public LogWeb400Call(Integer enterpriseId, String hotline, String uniqueid, String ip, String cookie, String tel, Integer result, Date createTime) {
		this.enterpriseId = enterpriseId;
		this.hotline = hotline;
		this.uniqueid = uniqueid;
		this.ip = ip;
		this.cookie = cookie;
		this.tel = tel;
		this.result = result;
		this.createTime = createTime;
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getHotline() {
		return hotline;
	}

	public void setHotline(String hotline) {
		this.hotline = hotline;
	}

	public String getUniqueid() {
		return uniqueid;
	}

	public void setUniqueid(String uniqueid) {
		this.uniqueid = uniqueid;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}