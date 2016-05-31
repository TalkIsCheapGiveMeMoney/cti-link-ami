package com.tinet.ctilink.ami.action;

import java.util.HashMap;
import java.util.Map;

/**
 * 调用Action返回结果类
 * 
 * @author tianzp
 */
public class AmiActionResponse {

	private int code;
	private String msg;
	private Map<String, String> values;

	public AmiActionResponse() {
		this.code = 0;
		this.msg = "ok";
		values = new HashMap<String, String>();
	}

	/**
	 * @param code
	 * @param msg
	 */
	public AmiActionResponse(int code, String msg) {
		this();
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map<String, String> getValues() {
		return values;
	}

	public void setValues(Map<String, String> values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return "AmiActionReponse [code=" + code + ", msg=" + msg + ",values="+values+"]";
	}
	

	public static AmiActionResponse createFailResponse(int code, String msg) {
		AmiActionResponse response = new AmiActionResponse();
		response.setCode(code);
		response.setMsg(msg);
		return response;
	}

	public static AmiActionResponse createSuccessResponse() {
		AmiActionResponse response = new AmiActionResponse();
		response.setCode(0);
		response.setMsg("ok");
		return response;
	}
}
