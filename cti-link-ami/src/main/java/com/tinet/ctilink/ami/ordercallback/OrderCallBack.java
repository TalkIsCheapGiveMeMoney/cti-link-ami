package com.tinet.ctilink.ami.ordercallback;

import java.util.Date;

public class OrderCallBack implements java.io.Serializable {
	private Integer enterpriseId;
	private String mainUniqueId;
	private String qno;
	private String queueName;
	private String tel;
	private Integer isCall;
	private String cno;
	private Date orderTime;
	private Date callBackTime;
	private Date createTime;
	private String area;
	private String areaCode;

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getMainUniqueId() {
		return mainUniqueId;
	}

	public void setMainUniqueId(String mainUniqueId) {
		this.mainUniqueId = mainUniqueId;
	}

	public String getQno() {
		return qno;
	}

	public void setQno(String qno) {
		this.qno = qno;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public Integer getIsCall() {
		return isCall;
	}

	public void setIsCall(Integer isCall) {
		this.isCall = isCall;
	}

	public String getCno() {
		return cno;
	}

	public void setCno(String cno) {
		this.cno = cno;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Date getCallBackTime() {
		return callBackTime;
	}

	public void setCallBackTime(Date callBackTime) {
		this.callBackTime = callBackTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
}
