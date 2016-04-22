package com.tinet.ctilink.ami.ivrmonitor;

import java.io.Serializable;

/**
 * IVR统计
 * 
 * @author mucw
 *
 */
@SuppressWarnings("serial")
public class IvrStat implements Serializable{
	/**
	 * 企业id
	 */
	private String enterpriseId;

	/**
	 * ivr id
	 */
	private String ivrId;
	/**
	 * 队列统计周期，根据其在一天中的时间，以30秒为采样，值范围为0-2879，计算公式为hour*60*2+minute*2
	 */
	private int period;

	/**
	 * 队列中呼入数
	 */
	private String incomings ;


	public String getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	
	public void setIvrId(String ivrId) {
		this.ivrId = ivrId;
	}
	
	public String getIvrId() {
		return ivrId;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	/**
	 * 获取呼入数
	 * 
	 * @return
	 */
	public String getIncomings() {
		return incomings;
	}

	/**
	 * 设置呼入数
	 * 
	 * @return
	 */
	public void setIncomings(String enterpriseIdIvrIdCount) {
		incomings = enterpriseIdIvrIdCount;
	}
	
}
