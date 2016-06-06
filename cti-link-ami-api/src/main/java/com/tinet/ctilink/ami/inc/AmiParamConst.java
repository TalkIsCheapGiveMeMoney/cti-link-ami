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
	

	//action top level param name
	public static final String ACTION_MAP = "actionMap"; 			//请求参数组
	public static final String ACTION_EVENT = "actionEvent"; 	// 请求事件参数组
	
	//used parameter name
	public static final String CUSTOMER_NUMBER = "customerNumber"; 			// 客户号码
	public static final String CUSTOMER_NUMBER_TYPE = "customerNumberType"; 	// 客户号码类型
	public static final String CUSTOMER_AREA_CODE = "customerAreaCode"; 		// 客户区号
	public static final String CALL_TYPE = "callType"; 						// 呼叫类型
	public static final String CNO = "cno"; 			// 坐席号
	public static final String CONSULTER_CNO = "consulterCno";
	public static final String TRANSFER_CNO = "transferCno";
	//event parameter	
	public static final String EVENT = "event"; // action/event字段：事件
	public static final String ENTERPRISE_ID = "enterpriseId"; // action/event字段：企业id
	public static final String QNO = "qno"; // action/event字段：队列号 001
	public static final String CHANNEL = "channel"; // 事件参数：通道名
	public static final String DEST_CHANNEL ="destChannel";
	public static final String CONSULT_OBJECT = "consultObject"; // action/event字段:咨询对象
	public static final String TRANSFER_OBJECT = "transferObject"; // action/event字段:
																			// 转移对象
	public static final String INTERACT_OBJECT = "interactObject"; // action/event字段:交互对象
	public static final String SPY_OBJECT = "spyObject"; // action/event字段:
																	// 监听者
	public static final String SPIED_CNO = "spiedCno"; // action/event字段:
																// 监听对象
	public static final String WHISPER_OBJECT = "whisperObject"; // action/event字段:
																			// 耳语者
	public static final String WHISPERED_CNO = "whisperedCno"; // action/event字段:
																		// 耳语对象
	public static final String THREEWAY_OBJECT = "threewayedObject"; // action/event字段:
																				// 三方者
	public static final String THREEWAYED_CNO = "threewayedCno"; // action/event字段:
																			// 三方对象

	public static final String BARGE_OBJECT = "bargeObject"; // action/event字段:
																		// 强插者
	public static final String BARGED_CNO = "bargedCno"; // action/event字段:
																	// 强插对象
	public static final String OBJECT_TYPE = "objectType"; // action/event字段:
	public static final String KEYS = "keys"; // keys
	public static final String BRIDGE_TIME = "bridgeTime"; // bridgeTime
	public static final String CALLEE_NUMBER = "calleeNumber"; // calleeNumber
	public static final String DETAIL_CALLTYPE = "detailCallType"; // detailCallType
	public static final String MAIN_UNIQUE_ID = "mainUniqueId"; // mainUniqueId
	public static final String EXTEN = "exten";
	public static final String ORDER_TIME = "orderTime";
	public static final String TIME = "time";
	
	public static final String IVR_ID = "ivrId"; // ivrId
	public static final String IVR_NODE = "ivrNode"; // ivrNode
	public static final String TASK_ID = "taskId";
	public static final String TEL = "tel";
	public static final String START_TIME = "startTime";
	public static final String NUMBER_TRUNK = "numberTrunk";
	public static final String RINGING_TIME = "ringingTime"; // 进入系统时间
	public static final String UNIQUE_ID = "uniqueId"; // 事件参数：当前通道唯一Id
	public static final String HOTLINE = "hotline"; // action/event字段：热线号码
	public static final String BRIDGED_CHANNEL = "bridgedChannel";
	public static final String BRIDGED_UNIQUE_ID = "bridgedUniqueId";
	
    public static final String EXTENSION = "exten";
    public static final String DIALPLAN_CONTEXT = "context";
    public static final String PRIORITY = "priority";
    public static final String ORIGINATE_TIMEOUT = "timeout";
    public static final String OTHER_CHANNEL_ID = "otherChannelId ";
    public static final String CLID = "clid";
    public static final String FEATURE = "feature";
    
    public static final String EXTRA_CHANNEL = "extraChannel";
    public static final String EXTRA_EXTEN = "extraExten ";
    public static final String EXTRA_CONTEXT = "extraContext";
    public static final String EXTRA_PRIORITY = "extraPriority";
    public static final String VAR_MAP = "varMap";
    public static final String HANGUP_CAUSE = "cause";
    public static final String INDICATE_CODE = "code";
    //indicate
    public static final Integer INDICATE_HOLD = 16;
    public static final Integer INDICATE_UNHOLD = 17;
    //mute
    public static final String MUTE_DIRECTION = "direction";
    public static final String MUTE_STATE = "state";
    public static final String MUTE_DIRECTION_IN = "in";
    public static final String MUTE_DIRECTION_OUT = "out";
    public static final String MUTE_DIRECTION_ALL = "all";
    public static final String MUTE_STATE_ON = "on";
    public static final String MUTE_STATE_OFF = "off";  
    public static final String STATUS = "status"; // 事件参数：通道状态
    
    public static final String ORIGINATE_RESPONSE_RESULT_ERROR = "error";
    public static final String ORIGINATE_RESPONSE_RESULT_SUCCESS = "success";
	
    //sorcery expire
    public static final String SORCERY_CACHE = "sorceryCache";
    public static final int ERROR_CODE = -1; // 发生异常
    public static final String DETAIL_CALL_TYPE = "detailCallType";
    public static final String VARIABLES = "variables";
	
	
		
}
