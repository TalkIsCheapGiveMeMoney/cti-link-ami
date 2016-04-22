package com.tinet.ctilink.ami.webcall;

import java.util.Date;

/**
 * 网上回呼日志表
 * <p>
 * 文件名： LogWebCall.java
 * <p>
 * Copyright (c) 2006-2010 T&I Net Communication CO.,LTD. All rights reserved.
 * 
 * @author MyEclipse Persistence Tools
 * @since 1.0
 * @version 1.0
 */
public class LogWebCall implements java.io.Serializable {

	// Fields
	/**
	 * 
	 */
	private Integer enterpriseId;
	private String callerIp;
	private String tel;
	private String subtel;
	private String uniqueId;
	private String userField;
	private String cookie;
	private String remoteIp;
	private Integer sync;
	private String callbackUrl;
	private String callbackParams;
	private Integer ivrId;
	private String clid;
	private Integer delay;
	private Integer timeout;
	private Integer amd;
	private String paramNames;
	private String paramTypes;
	private String param;
	private Integer result;
	private Integer callStatus;
	private Date requestTime;
	private Integer concurrency;
	private Integer ttsDuration;
	private Date startTime;
	private Date callTime;
	private Date answerTime;
	private Date endTime;
	private Date createTime;

	// Constructors

	/** default constructor */
	public LogWebCall() {
		this.createTime = new Date();
	}

	// Property accessors


	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getCallerIp() {
		return callerIp;
	}

	public void setCallerIp(String callerIp) {
		this.callerIp = callerIp;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getSubtel() {
		return subtel;
	}

	public void setSubtel(String subtel) {
		this.subtel = subtel;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getUserField() {
		return userField;
	}

	public void setUserField(String userField) {
		this.userField = userField;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	public Integer getSync() {
		return sync;
	}

	public void setSync(Integer sync) {
		this.sync = sync;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public String getCallbackParams() {
		return callbackParams;
	}

	public void setCallbackParams(String callbackParams) {
		this.callbackParams = callbackParams;
	}

	public Integer getIvrId() {
		return ivrId;
	}

	public void setIvrId(Integer ivrId) {
		this.ivrId = ivrId;
	}

	public String getClid() {
		return clid;
	}

	public void setClid(String clid) {
		this.clid = clid;
	}

	public Integer getDelay() {
		return delay;
	}

	public void setDelay(Integer delay) {
		this.delay = delay;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public Integer getAmd() {
		return amd;
	}

	public void setAmd(Integer amd) {
		this.amd = amd;
	}

	public String getParamNames() {
		return paramNames;
	}

	public void setParamNames(String paramNames) {
		this.paramNames = paramNames;
	}

	public String getParamTypes() {
		return paramTypes;
	}

	public void setParamTypes(String paramTypes) {
		this.paramTypes = paramTypes;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public Integer getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(Integer callStatus) {
		this.callStatus = callStatus;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	public Integer getConcurrency() {
		return concurrency;
	}

	public void setConcurrency(Integer concurrency) {
		this.concurrency = concurrency;
	}

	public Integer getTtsDuration() {
		return ttsDuration;
	}

	public void setTtsDuration(Integer ttsDuration) {
		this.ttsDuration = ttsDuration;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getCallTime() {
		return callTime;
	}

	public void setCallTime(Date callTime) {
		this.callTime = callTime;
	}

	public Date getAnswerTime() {
		return answerTime;
	}

	public void setAnswerTime(Date answerTime) {
		this.answerTime = answerTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}