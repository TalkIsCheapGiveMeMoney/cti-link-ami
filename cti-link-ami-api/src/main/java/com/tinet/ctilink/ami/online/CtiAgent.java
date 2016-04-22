package com.tinet.ctilink.ami.online;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tinet.ctilink.inc.Const;
import com.tinet.ctilink.util.DateUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 呼叫中心坐席封装类。
 * <p>
 * 文件名： Event.java
 * <p>
 * Copyright (c) 2006-2010 T&I Net Communication CO.,LTD. All rights reserved.
 * 
 * @author 安静波
 * @since 1.0
 * @version 1.0
 */

public class CtiAgent implements Serializable {
	private static final long serialVersionUID = -868452981108376796L;

	public static final String ONLINE = "online";
	public static final String OFFLINE = "offline";
	public static final String PAUSE = "pause";
	public static final String NOCARE = "noCare";
	public static final String PAUSE_DESCRIPTION_WRAPUP = "pauseWrapup";
	public static final String PAUSE_DESCRIPTION_OUTCALLING = "pauseOutCalling";// 主动外呼中
	public static final String OFFHOOK = "offHook";
	public static final String IDLE = "idle";
	public static final String RINGING = "ringing";
	public static final String BUSY = "busy";
	public static final String UNAVAILABLE = "unavalible";
	public static final String BUSY_DESCRIPTION_ONHOLD = "busyOnhold";

	private String loginStatus = OFFLINE;
	private String deviceStatus = IDLE;
	private Integer enterpriseId;
	private Integer clientId;
	private String cno;
	private String location;
	private String name;
	private String channel; // 座席channel
	private String mainChannel; // 呼入客户的channel 或者外呼客户的channel
	private String consultChannel; // 咨询时对方的channel
	private String spyChannel; // 监听者的channel
	private String whisperChannel; // 耳语者channel
	private String bargeChannel; // 拦截者channel
	private String threewayChannel; // 三方channel
	private String monitoredObject; // 监听此座席的号码 01041005960/2002
	private String monitoredObjectType;// 监听此座席的号码类型 0:tel 1:cno
	private String customerNumber; //
	private String customerNumberTpye;
	private String numberTrunk;
	private Integer callType = 0;
	private String taskId;
	private String customerAreaCode;
	private String curQueue;
	private String tel; // can be changed during use.
	private Integer bindType;
	private Integer power;
	private int ctiId;
	private Date loginUpdateTime = new Date();
	private Date deviceUpdateTime = new Date();
	private Integer wrapup;
	private String hotline;
	private long loginStartTime;

	private boolean isOutCall;
	private String transferCno;
	private String consulterCno;
	private String taskInventoryId;
	private String uniqueId;
	private String obClid;
	private boolean previewOutcallLocked = false;
	private String crmId;
	private long previewOutcallStart;
	private String pauseDescription = "";
	private String busyDescription = "";
	private Integer obRecord = 0;// 录音是否录音
	private String isInvestigationAuto = "0";
	private String obSmsTail = "0";

	public String getDeviceStatus() {
		return deviceStatus;
	}

	public void setDeviceStatus(String deviceStatus) {
		if (!this.deviceStatus.equals(deviceStatus)) {
			this.deviceStatus = deviceStatus;
			this.deviceUpdateTime = new Date();
			this.loginUpdateTime = new Date();
		}
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public void setCno(String cno) {
		this.cno = cno;
	}

	public String getBusyDescription() {
		return busyDescription;
	}

	public void setBusyDescription(String busyDescription) {
		this.busyDescription = busyDescription;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public Integer getBindType() {
		return bindType;
	}

	public void setBindType(Integer bindType) {
		this.bindType = bindType;
	}

	public Integer getPower() {
		return power;
	}

	public void setPower(Integer power) {
		this.power = power;
	}

	public CtiAgent() {

	}

	public CtiAgent(Integer enterpriseId, String cno) {
		this.enterpriseId = enterpriseId;
		this.cno = cno;
	}

	@JsonIgnore
	public String getCid() {
		return enterpriseId + cno;
	}

	public String getCno() {
		return cno;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getLoginStatus() {
		return this.loginStatus;
	}

	public void setLoginStatus(String status) {
		if (!this.loginStatus.equals(status)) {
			this.loginStatus = status;
			this.loginUpdateTime = new Date();
		}
	}

	@JsonIgnore
	public boolean getPause() {
		return this.loginStatus.equals(PAUSE);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getCtiId() {
		return ctiId;
	}

	public void setCti(int ctiId) {
		this.ctiId = ctiId;
	}

	@JsonIgnore
	public void clearChannel() {
		bargeChannel = "";
		spyChannel = "";
		whisperChannel = "";
		threewayChannel = "";
		mainChannel = "";
		channel = "";
		consultChannel = "";
		customerNumber = "";
		busyDescription = "";
		customerNumber = "";
		customerNumberTpye = "";
		callType = 0;
		taskId = "";
		customerAreaCode = "";
		curQueue = "";
		taskInventoryId = "";
		consulterCno = "";
		transferCno = "";
		uniqueId = "";
	}

	public String getCurQueue() {
		return curQueue;
	}

	public void setCurQueue(String curQueue) {
		this.curQueue = curQueue;
	}

	public String getBargeChannel() {
		return bargeChannel;
	}

	public void setBargeChannel(String bargeChannel) {
		this.bargeChannel = bargeChannel;
	}

	public String getSpyChannel() {
		return spyChannel;
	}

	public void setSpyChannel(String spyChannel) {
		this.spyChannel = spyChannel;
	}

	public String getWhisperChannel() {
		return whisperChannel;
	}

	public void setWhisperChannel(String whisperChannel) {
		this.whisperChannel = whisperChannel;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getConsultChannel() {
		return consultChannel;
	}

	public void setConsultChannel(String consultChannel) {
		this.consultChannel = consultChannel;
	}

	public String getMainChannel() {
		return mainChannel;
	}

	public void setMainChannel(String mainChannel) {
		this.mainChannel = mainChannel;
	}

	public String getThreewayChannel() {
		return threewayChannel;
	}

	public void setThreewayChannel(String threewayChannel) {
		this.threewayChannel = threewayChannel;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public String getPauseDescription() {
		return pauseDescription;
	}

	public void setPauseDescription(String pauseDescription) {
		if (pauseDescription == null) {
			pauseDescription = "";
		}
		if (!this.pauseDescription.equals(pauseDescription)) {
			this.loginUpdateTime = new Date();
			this.pauseDescription = pauseDescription;
		}
	}

	public Integer getWrapup() {
		return wrapup;
	}

	public void setWrapup(Integer wrapup) {
		this.wrapup = wrapup;
	}

	public String getNumberTrunk() {
		return numberTrunk;
	}

	public void setNumberTrunk(String numberTrunk) {
		this.numberTrunk = numberTrunk;
	}

	/**
	 * @return the hotline
	 */
	public String getHotline() {
		return hotline;
	}

	/**
	 * @param hotline the hotline to set
	 */
	public void setHotline(String hotline) {
		this.hotline = hotline;
	}

	public Integer getCallType() {
		return callType;
	}

	public void setCallType(Integer callType) {
		this.callType = callType;
	}

	/**
	 * @return the customerAreaCode
	 */
	public String getCustomerAreaCode() {
		return customerAreaCode;
	}

	/**
	 * @param customerAreaCode the customerAreaCode to set
	 */
	public void setCustomerAreaCode(String customerAreaCode) {
		this.customerAreaCode = customerAreaCode;
	}

	/**
	 * @return the customerNumberTpye
	 */
	public String getCustomerNumberTpye() {
		return customerNumberTpye;
	}

	/**
	 * @param customerNumberTpye the customerNumberTpye to set
	 */
	public void setCustomerNumberTpye(String customerNumberTpye) {
		this.customerNumberTpye = customerNumberTpye;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * @return the transferCno
	 */
	public String getTransferCno() {
		return transferCno;
	}

	/**
	 * @param transferCno the transferCno to set
	 */
	public void setTransferCno(String transferCno) {
		this.transferCno = transferCno;
	}

	/**
	 * @return the consulterCno
	 */
	public String getConsulterCno() {
		return consulterCno;
	}

	/**
	 * @param consulterCno the consulterCno to set
	 */
	public void setConsulterCno(String consulterCno) {
		this.consulterCno = consulterCno;
	}

	/**
	 * @return the taskInventoryId
	 */
	public String getTaskInventoryId() {
		return taskInventoryId;
	}

	/**
	 * @param taskInventoryId the taskInventoryId to set
	 */
	public void setTaskInventoryId(String taskInventoryId) {
		this.taskInventoryId = taskInventoryId;
	}

	/**
	 * @return the uniqueId
	 */
	public String getUniqueId() {
		return uniqueId;
	}

	/**
	 * @param uniqueId the uniqueId to set
	 */
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	/**
	 * @return the obClid
	 */
	public String getObClid() {
		return obClid;
	}

	/**
	 * @param obClid the obClid to set
	 */
	public void setObClid(String obClid) {
		this.obClid = obClid;
	}

	/**
	 * @return the loginStartTime
	 */
	public long getLoginStartTime() {
		return loginStartTime;
	}

	/**
	 * @param loginStartTime the loginStartTime to set
	 */
	public void setLoginStartTime(long loginStartTime) {
		this.loginStartTime = loginStartTime;
	}

	public long getPreviewOutcallStart() {
		return previewOutcallStart;
	}

	public void setPreviewOutcallStart(long previewOutcallStart) {
		this.previewOutcallStart = previewOutcallStart;
	}

	public boolean isOutCall() {
		return isOutCall;
	}

	public void setOutCall(boolean isOutCall) {
		this.isOutCall = isOutCall;
	}

	public String getMonitoredObject() {
		return monitoredObject;
	}

	public void setMonitoredObject(String monitoredObject) {
		this.monitoredObject = monitoredObject;
	}

	public String getMonitoredObjectType() {
		return monitoredObjectType;
	}

	public void setMonitoredObjectType(String monitoredObjectType) {
		this.monitoredObjectType = monitoredObjectType;
	}

	public boolean isPreviewOutcallLocked() {
		return previewOutcallLocked;
	}

	public void setPreviewOutcallLocked(boolean previewOutcallLocked) {
		this.previewOutcallLocked = previewOutcallLocked;
	}

	public String getCrmId() {
		return crmId;
	}

	public void setCrmId(String crmId) {
		this.crmId = crmId;
	}

	public Date getLoginUpdateTime() {
		return loginUpdateTime;
	}

	public void setLoginUpdateTime(Date loginUpdateTime) {
		this.loginUpdateTime = loginUpdateTime;
	}

	public Date getDeviceUpdateTime() {
		return deviceUpdateTime;
	}

	public void setDeviceUpdateTime(Date deviceUpdateTime) {
		this.deviceUpdateTime = deviceUpdateTime;
	}

	public Integer getObRecord() {
		return obRecord;
	}

	public void setObRecord(Integer obRecord) {
		this.obRecord = obRecord;
	}

	public String getIsInvestigationAuto() {
		return isInvestigationAuto;
	}

	public void setIsInvestigationAuto(String isInvestigationAuto) {
		this.isInvestigationAuto = isInvestigationAuto;
	}

	public String getObSmsTail() {
		return obSmsTail;
	}

	public void setObSmsTail(String obSmsTail) {
		this.obSmsTail = obSmsTail;
	}

	/**
	 * 获取座席状态保持时长
	 * @return
	 */
	@JsonIgnore
	public int getDuration() {
		if (deviceStatus.equals(CtiAgent.OFFHOOK) || deviceStatus.equals(CtiAgent.RINGING)
				|| deviceStatus.equals(CtiAgent.BUSY)) {
			return DateUtil.diffSecond(deviceUpdateTime, new Date());
		} else {
			return DateUtil.diffSecond(loginUpdateTime, new Date());
		}
	}

	/**
	 * 获取监控类型
	 * 
	 * @return
	 */
	@JsonIgnore
	public String getMonitoredType() {
		if (StringUtils.isNotEmpty(spyChannel)) {
			return "spy";
		} else if (StringUtils.isNotEmpty(whisperChannel)) {
			return "whisper";
		} else if (StringUtils.isNotEmpty(threewayChannel)) {
			return "threeway";
		} else {
			return "";
		}
	}

	/**
	 * 获取用于分布式锁的key
	 * @return
	 */
	public static String getLockKey(String cid) {
		return Const.REDIS_CTIAGENT_LOCK + "#" + cid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cno == null) ? 0 : cno.hashCode());
		result = prime * result + ((enterpriseId == null) ? 0 : enterpriseId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CtiAgent other = (CtiAgent) obj;
		if (cno == null) {
			if (other.cno != null)
				return false;
		} else if (!cno.equals(other.cno))
			return false;
		if (enterpriseId == null) {
			if (other.enterpriseId != null)
				return false;
		} else if (!enterpriseId.equals(other.enterpriseId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CtiAgent [enterpriseId=" + enterpriseId + ", clientId=" + clientId + ", cno=" + cno + ", location="
				+ location + ", name=" + name + ", channel=" + channel + ", mainChannel=" + mainChannel
				+ ", consultChannel=" + consultChannel + ", spyChannel=" + spyChannel + ", whisperChannel="
				+ whisperChannel + ", bargeChannel=" + bargeChannel + ", threewayChannel=" + threewayChannel
				+ ", monitoredObject=" + monitoredObject + ", monitoredObjectType=" + monitoredObjectType
				+ ", customerNumber=" + customerNumber + ", customerNumberTpye=" + customerNumberTpye + ", numberTrunk="
				+ numberTrunk + ", callType=" + callType + ", taskId=" + taskId + ", customerAreaCode="
				+ customerAreaCode + ", curQueue=" + curQueue + ", tel=" + tel + ", bindType=" + bindType + ", power="
				+ power + ", ctiId=" + ctiId + ", loginUpdateTime=" + loginUpdateTime + ", deviceUpdateTime="
				+ deviceUpdateTime + ", wrapup=" + wrapup + ", hotline=" + hotline + ", loginStartTime="
				+ loginStartTime + ", transferCno=" + transferCno + ", consulterCno=" + consulterCno
				+ ", taskInventoryId=" + taskInventoryId + ", uniqueId=" + uniqueId + ", obClid=" + obClid
				+ ", previewOutcallLocked=" + previewOutcallLocked + ", crmId=" + crmId + ", previewOutcallStart="
				+ previewOutcallStart + ", loginStatus=" + loginStatus + ", deviceStatus=" + deviceStatus
				+ ", pauseDescription=" + pauseDescription + ", busyDescription=" + busyDescription + "]";
	}
}
