package com.tinet.ctilink.ami.webcall;

/**
 * WebCall同步调用服务接口
 * 
 * @author Jiangsl
 *
 */
public interface WebCallService {

	/**
	 * 同步调用发起WebCall呼叫
	 * 
	 * @param webCall
	 * @return
	 */
	public WebCallResponse callSync(WebCall webCall);
	
	/**
	 * 异步调用发起WebCall呼叫
	 * 
	 * @param webCall
	 */
	public WebCallResponse callAysnc(WebCall webCall);
}
