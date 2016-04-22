package com.tinet.ctilink.ami.webcall;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @author Jiangsl
 *
 */
@SuppressWarnings("serial")
public class WebCall implements Serializable {

	private Integer enterpriseId;
	private String subtel;
	private String tel;
	private String callerIp;
	private String uniqueId;
	private String callbackUrl;
	private Integer sync;
	private Integer timeout;
	private Integer delay;
	private Integer multiTelDelay;
	private Integer multiTelPush;
	private String clidNumber;
	private Integer amd;   //是否开启amd, 1：开启  0：不开启 默认为0
	private Map<String, String> callbackParams;
	private String ivrId;
	private Map<String, String> params;
	private Map<String, Integer> paramTypes;
	private Integer result;
	private String webCallResultMessage;
	private Date requestTime;
	private Date startTime;
	private Date callTime;
	private Date answerTime = null;
	private Date endTime = null;
	private Integer inboundCallLimit;
	private int concurrency = 0;
	private int ttsDuration = 0;
	private int callStatus = -1;
	private String cdrUniqueId = "";

	public Integer getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	public String getSubtel() {
		return subtel;
	}
	public void setSubtel(String subtel) {
		this.subtel = subtel;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getCallerIp() {
		return callerIp;
	}
	public void setCallerIp(String callerIp) {
		this.callerIp = callerIp;
	}
	public String getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	public String getCallbackUrl() {
		return callbackUrl;
	}
	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}
	public Integer getSync() {
		return sync;
	}
	public void setSync(Integer sync) {
		this.sync = sync;
	}
	public Integer getTimeout() {
		return timeout;
	}
	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}
	public Integer getDelay() {
		return delay;
	}
	public void setDelay(Integer delay) {
		this.delay = delay;
	}
	public Integer getMultiTelDelay() {
		return multiTelDelay;
	}
	public void setMultiTelDelay(Integer multiTelDelay) {
		this.multiTelDelay = multiTelDelay;
	}
	public Integer getMultiTelPush() {
		return multiTelPush;
	}
	public void setMultiTelPush(Integer multiTelPush) {
		this.multiTelPush = multiTelPush;
	}
	public String getClidNumber() {
		return clidNumber;
	}
	public void setClidNumber(String clidNumber) {
		this.clidNumber = clidNumber;
	}
	public Integer getAmd() {
		return amd;
	}
	public void setAmd(Integer amd) {
		this.amd = amd;
	}
	public Map<String, String> getCallbackParams() {
		return callbackParams;
	}
	public void setCallbackParams(Map<String, String> callbackParams) {
		this.callbackParams = callbackParams;
	}
	public String getIvrId() {
		return ivrId;
	}
	public void setIvrId(String ivrId) {
		this.ivrId = ivrId;
	}
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	public Map<String, Integer> getParamTypes() {
		return paramTypes;
	}
	public void setParamTypes(Map<String, Integer> paramTypes) {
		this.paramTypes = paramTypes;
	}
	public Integer getResult() {
		return result;
	}
	public void setResult(Integer result) {
		this.result = result;
	}
	public String getWebCallResultMessage() {
		return webCallResultMessage;
	}
	public void setWebCallResultMessage(String webCallResultMessage) {
		this.webCallResultMessage = webCallResultMessage;
	}
	public Date getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
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
	public Integer getInboundCallLimit() {
		return inboundCallLimit;
	}
	public void setInboundCallLimit(Integer inboundCallLimit) {
		this.inboundCallLimit = inboundCallLimit;
	}
	public int getConcurrency() {
		return concurrency;
	}
	public void setConcurrency(int concurrency) {
		this.concurrency = concurrency;
	}
	public int getTtsDuration() {
		return ttsDuration;
	}
	public void setTtsDuration(int ttsDuration) {
		this.ttsDuration = ttsDuration;
	}
	public int getCallStatus() {
		return callStatus;
	}
	public void setCallStatus(int callStatus) {
		this.callStatus = callStatus;
	}
	public String getCdrUniqueId() {
		return cdrUniqueId;
	}
	public void setCdrUniqueId(String cdrUniqueId) {
		this.cdrUniqueId = cdrUniqueId;
	}
	
	
}
