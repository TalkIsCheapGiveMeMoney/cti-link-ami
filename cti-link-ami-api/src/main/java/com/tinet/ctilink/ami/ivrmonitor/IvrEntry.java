package com.tinet.ctilink.ami.ivrmonitor;

import java.io.Serializable;

/**
 * 进入ivr对象
 * 
 * @author mucw
 *
 */
@SuppressWarnings("serial")
public class IvrEntry implements Serializable{
	/**
	 * 客户号码
	 */
	private String tel;

	/**
	 * 呼入号
	 */
	private String numberTrunk;

	/**
	 * 客户进入ivr时间
	 */
	private String startTime;

	/**
	 * 持续时间（秒）
	 */
	private Integer duration;


	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getNumberTrunk() {
		return numberTrunk;
	}

	public void setNumberTrunk(String numberTrunk) {
		this.numberTrunk = numberTrunk;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}


	public Integer getDuration() {
		return duration;
	}

	public void setduration(Integer duration) {
		this.duration = duration;
	}
	
}
