package com.tinet.ctilink.ami.webcall;

import java.io.Serializable;

/**
 * 同步调用WebCall的响应结果
 * 
 * @author Jiangsl
 *
 */
@SuppressWarnings("serial")
public class WebCallResponse implements Serializable {
	private String uniqueId;
	private Integer result;
	private String description;

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
