package com.tinet.ctilink.ami.util;

import java.util.Map;

/**
 * SipHeader处理工具类
 * <p>
 * 文件名： AreaCodeUtil.java
 * <p>
 * Copyright (c) 2006-2010 T&I Net Communication CO.,LTD. All rights reserved.
 * 
 * @author 安静波
 * @since 1.0
 * @version 1.0
 */

public class SipHeaderUtil {

	private static String int2Str(int i) {
		if (i < 10) {
			return "0" + i;
		} else if (i < 100) {
			return "" + i;
		} else {
			return "";
		}
	}

	public static void setAlternateRouter(Map<String, String> variables, String jbeIp, Integer ccicId, String type, Integer enterpriseId, String callType, String routeType, Integer isAlternateRouter) {
		setAlternateRouter(variables, jbeIp, ccicId, type, String.valueOf(enterpriseId), callType, routeType,
				isAlternateRouter);
	}

	public static void setAlternateRouter(Map<String, String> variables, String jbeIp, Integer ccicId, String type, String enterpriseId, String callType, String routeType, Integer isAlternateRouter) {
		int i = 1;
		variables.put("SIPADDHEADER" + int2Str(i++), "X-Asterisk-PlatformIP:" + jbeIp);// 说明：必选，JBE平台ip，例如：172.16.203.189
		variables.put("SIPADDHEADER" + int2Str(i++), "X-Asterisk-PlatformID:" + ccicId);// 说明：可选，平台id，例如：1001
		variables.put("SIPADDHEADER" + int2Str(i++), "X-Asterisk-PlatformType:" + type);// 说明：可选，平台类型，例如：CCIC2
		variables.put("SIPADDHEADER" + int2Str(i++), "X-Asterisk-EnterpriseID:" + enterpriseId);// 说明：必选，企业账号
		variables.put("SIPADDHEADER" + int2Str(i++), "X-Asterisk-CallType:" + callType);// 说明：必选，呼叫类型，详见
																						// proxy请求备用路由信息接口文档
																						// 中的calltype参数
		variables.put("SIPADDHEADER" + int2Str(i++), "X-Asterisk-IsRouter:" + isAlternateRouter);// 说明：必选，是否开启备用路由功能，在企业业务信息中设置
		variables.put("SIPADDHEADER" + int2Str(i++), "X-Asterisk-RouteType:" + routeType);// 说明：必选，是否开启备用路由功能，在企业业务信息中设置
	}
}
