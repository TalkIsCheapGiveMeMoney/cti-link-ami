/**
 * 
 */
package com.tinet.ctilink.ami.action;

import java.util.Map;

import com.tinet.ctilink.ami.action.AmiActionResponse;

/**
 * AMI动作接口
 * @author Jiangsl
 *
 */
public interface AmiActionHandler {
	public String getAction();

	public AmiActionResponse handle(Map<String, Object> params);
//	public AmiActionResponse handle(Map<String, String> fixParams,Map<String, String> optionalParams);
	

}
