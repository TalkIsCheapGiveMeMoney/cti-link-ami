package com.tinet.ctilink.ami.action;

import java.util.Map;

/**
 * AMI操作接口
 * 
 * @author Jiangsl
 *
 */
public interface AmiActionService {

	/**
	 * 执行一个AMI动作，只用于Dubbo接口，不允许调用（因为AMI是有状态的，不同通过Dubbo集群访问）
	 * 
	 * @param action
	 * @param params
	 * @return
	 */
	public AmiActionResponse handleAction(String action, Map<String, Object> params);
	
	
//	/**
//	 * 执行一个AMI动作,参数分为两个部分
//	 * @param action
//	 * @param fixedParams
//	 * @param OptionalParams
//	 * @return
//	 */
//	public AmiActionResponse handleAction(String action, Map<String,String> fixedParams, Map<String, String> OptionalParams);
}
