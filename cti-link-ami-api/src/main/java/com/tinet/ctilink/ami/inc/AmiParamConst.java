package com.tinet.ctilink.ami.inc;

/**
 * AMI相关的事件类型。
 * <p>
 * 文件名： AmiParamConst.java
 * <p>
 * Copyright (c) 2006-2010 T&I Net Communication CO.,LTD. All rights reserved.
 * 
 * @author hongzk
 * @since 1.0
 * @version 1.0
 * 
 */

public class AmiParamConst {
	
	public static final String CHANNEL = "channel"; // 事件参数：通道名
	public static final String UNIQUEID = "uniqueId"; // 事件参数：当前通道唯一Id
	public static final String CDR_MAIN_UNIQUE_ID = "cdr_main_unique_id"; // 事件参数：主通道唯一Id
	public static final String ENTERPRISEID = "enterpriseId"; // 事件参数：企业Id	
	
	public static final String CDR_CUSTOMER_NUMBER = "cdr_customer_number"; // 事件参数：客户号码
	public static final String CDR_CUSTOMER_NUMBER_TYPE = "cdr_customer_number_type"; // 事件参数：客户号码类型
	
	public static final String CDR_CUSTOMER_AREA_CODE = "cdr_customer_area_code"; // 事件参数：客户区号
	public static final String CDR_CALL_TYPE = "cdr_call_type"; // 事件参数: 呼叫类型
	
	public static final String CNO = "cno"; // 事件参数: 坐席号
	public static final String CDR_CALLEE_NUMBER = "cdr_callee_number"; // 事件参数: 被叫号码
	
	//copyed
	public static final String VARIABLE_TYPE = "type"; // action/event字段：类型
	public static final String VARIABLE_RES_TYPE = "reqType"; // action/event字段：响应类型
	public static final String VARIABLE_UTID = "utid"; // action/event字段：unique														
	public static final String VARIABLE_REASON = "reason"; // action/event字段：原因
	public static final String VARIABLE_EVENT = "event"; // action/event字段：事件
	public static final String VARIABLE_NAME = "name"; // action/event字段：描述event的name

	public static final String VARIABLE_CODE = "code"; // action/event字段：code
	public static final String VARIABLE_MSG = "msg"; // action/event字段：消息
	public static final String VARIABLE_CUSTOMER_NUMBER = "customerNumber"; // action/event字段：格式化后的号码
	public static final String VARIABLE_CRM_CUSTOMER_NUMBER = "crmCustomerNumber"; // action/event字段：格式化后的号码
	public static final String VARIABLE_CUSTOMER_NUMBER_TYPE = "customerNumberType"; // action/event字段:
																						// 号码类型
																						// 手机、固话
	public static final String VARIABLE_CUSTOMER_AREA_CODE = "customerAreaCode"; // action/event字段:
																					// 号码区号
	public static final String VARIABLE_CUSTOMER_AREA_NAME = "customerAreaName"; // action/event字段:
																					// 号码地址
	public static final String VARIABLE_NUMBER_TRUNK = "numberTrunk";
	public static final String VARIABLE_HOTLINE = "hotline"; // action/event字段：热线号码
	public static final String VARIABLE_CNO = "cno"; // action/event字段：座席号 2000
	public static final String VARIABLE_CID = "cid"; // action/event字段：座席id
	public static final String VARIABLE_CLIENT_ID = "clientId";	//client 的id												// 10000762000
	public static final String VARIABLE_CNAME = "cname"; // action/event字段：座席姓名
															// 张三
	
	public static final String VARIABLE_POWER = "power"; // action/event字段：座席权限
															// 0为普通座席；1为班长席
	
	public static final String VARIABLE_TRANSFER_CNO = "transferCno"; // action/event字段：队列号
																		// 1000076001
	
	public static final String VARIABLE_HOLD_TYPE = "holdType"; // action/event字段:
																// 保持类型
																// 直接保持/咨询转移保持

	
	
	public static final String VARIABLE_CONSULT_OBJECT = "consultObject"; // action/event字段:咨询对象
	public static final String VARIABLE_TRANSFER_OBJECT = "transferObject"; // action/event字段:
																			// 转移对象
	public static final String VARIABLE_INTERACT_OBJECT = "interactObject"; // action/event字段:交互对象
	public static final String VARIABLE_SPY_OBJECT = "spyObject"; // action/event字段:
																	// 监听者
	public static final String VARIABLE_SPIED_CNO = "spiedCno"; // action/event字段:
																// 监听对象
	public static final String VARIABLE_WHISPER_OBJECT = "whisperObject"; // action/event字段:
																			// 耳语者
	public static final String VARIABLE_WHISPERED_CNO = "whisperedCno"; // action/event字段:
																		// 耳语对象
	public static final String VARIABLE_THREEWAY_OBJECT = "threewayedObject"; // action/event字段:
																				// 三方者
	public static final String VARIABLE_THREEWAYED_CNO = "threewayedCno"; // action/event字段:
																			// 三方对象

	public static final String VARIABLE_BARGE_OBJECT = "bargeObject"; // action/event字段:
																		// 强插者
	public static final String VARIABLE_BARGED_CNO = "bargedCno"; // action/event字段:
																	// 强插对象
	public static final String VARIABLE_BARGER_CNO = "bargerCno"; // action/event字段:
																	// 强插者

	public static final String VARIABLE_DISCONNECTED_CNO = "disconnectedCno"; // action/event字段:
																				// 强拆对象
	public static final String VARIABLE_DISCONNECTER_CNO = "disconnecterCno"; // action/event字段:
																				// 强拆者

	public static final String VARIABLE_MONITOR_CNO = "monitorCno"; // action/event字段:
																	// 强拆对象
	public static final String VARIABLE_MONITORED_CNO = "monitoredCno"; // action/event字段:
																		// 强拆对象

	public static final String VARIABLE_THREEWAY_INIT_OBJECT = "threewayInitObject"; // action/event字段:
																						// 三方通话对象
	public static final String VARIABLE_OBJECT_TYPE = "objectType"; // action/event字段:
																	// 0：普通电话1：座席号
																	// 2：IVR节点
																	// 3：IVR id
	public static final String VARIABLE_PREVIEW_OUTCAL_TEL = "previewOutcallTel"; // action/event字段:
																					// 外呼电话
	public static final String VARIABLE_CUSTOMER_CRM_ID = "customerCrmId";
	
	
}
