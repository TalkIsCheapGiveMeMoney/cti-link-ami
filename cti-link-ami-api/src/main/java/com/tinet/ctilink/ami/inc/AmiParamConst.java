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
	public static final String CUSTOMER_NUMBER_AREA_CODE = "customerNumberAreaCode"; 		// 客户区号
	public static final String CALL_TYPE = "callType"; 						// 呼叫类型
	public static final String CNO = "cno"; 											// 坐席号
	public static final String CDR_CALLEE_NUMBER = "cdr_callee_number"; 				// 被叫号码
	public static final String CHANNEL = "channel"; // 事件参数：通道名
	public static final String HANGUP_CAUSE = "cause";
	public static final String INDICATE_CODE = "code";
	
	public static final String CHANNELSTATE = "status"; // 事件参数：通道状态
	public static final String UNIQUEID = "uniqueId"; // 事件参数：当前通道唯一Id
	public static final String ENTERPRISEID = "enterpriseId"; // 事件参数：企业Id

	public static final String VAR_MAP = "varMap";
	public static final String CHANNEL_VARIABLE_VALUE = "channelVariableValue";	
	public static final String MAIN_CHANNEL = "main_channel";
	
	public static final String VARIABLE_HOTLINE = "hotline"; // action/event字段：热线号码
	public static final String VARIABLE_QUEUE = "queue";
	public static final String VARIABLE_CNO = "cno"; // action/event字段：座席号 2000
	public static final String DETAIL_CALL_TYPE="detailCallType";
	public static final String VARIABLE_NUMBER_TRUNK = "numberTrunk";
	public static final String VARIABLE_BRIDGED_CHANNEL = "bridgedChannel";
	public static final String VARIABLE_BRIDGED_UNIQUEID = "bridgedUniqueId";
	public static final String VARIABLE_STATUS_VARIABLES = "variables";
	
	//OrderCallBack parameter	
	public static final String VARIABLE_ORDER_TIME = "orderTime";
	
	//indicate
	public static final Integer INDICATE_HOLD = 16;
	public static final Integer INDICATE_UNHOLD = 17;
	
	//mute
	public static final String VARIABLE_MUTE_DIRECTION = "direction";
	public static final String VARIABLE_MUTE_STATE = "state";
	public static final String MUTE_DIRECTION_IN = "in";
	public static final String MUTE_DIRECTION_OUT = "out";
	public static final String MUTE_DIRECTION_ALL = "all";
	public static final String MUTE_STATE_ON = "on";
	public static final String MUTE_STATE_OFF = "off";  
	
	//action固定参数		
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
	public static final String VARIABLE_CURRENT_CHANNEL = "currentChannel";
		
    //used const parameter value
    public static final String ORIGINATE_RESPONSE_RESULT_ERROR = "error";
	public static final String ORIGINATE_RESPONSE_RESULT_SUCCESS = "success";
	
	//event parameter	
	public static final String VARIABLE_EVENT = "event"; // action/event字段：事件
	public static final String VARIABLE_CUSTOMER_NUMBER = "customerNumber"; // action/event字段：格式化后的号码
	public static final String VARIABLE_CRM_CUSTOMER_NUMBER = "crmCustomerNumber"; // action/event字段：格式化后的号码
	public static final String VARIABLE_CUSTOMER_NUMBER_TYPE = "customerNumberType"; // action/event字段:
																						// 号码类型
																						// 手机、固话
	public static final String VARIABLE_CUSTOMER_AREA_CODE = "customerAreaCode"; // action/event字段:
																					// 号码区号
	//sorcery expire
	public static final String SORCERY_CACHE = "sorceryCache";
	public static final int ERROR_CODE = -1; // 发生异常
	public static final String VARIABLE_ENTERPRISE_ID = "enterpriseId"; // action/event字段：企业id
	public static final String VARIABLE_QNO = "qno"; // action/event字段：队列号 001
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
	public static final String VARIABLE_OBJECT_TYPE = "objectType"; // action/event字段:
	// 0：普通电话1：座席号
	// 2：IVR节点
	// 3：IVR id
	
	public static final String VARIABLE_RINGING_TIME = "ringingTime"; // 进入系统时间
	public static final String VARIABLE_TIME = "time";

	

	



	public static final String VARIABLE_DISCONNECTED_CNO = "disconnectedCno"; // action/event字段:
																				// 强拆对象
	public static final String VARIABLE_DISCONNECTER_CNO = "disconnecterCno"; // action/event字段:
																				// 强拆者
	public static final String VARIABLE_PREVIEW_OUTCAL_TEL = "previewOutcallTel"; // action/event字段:
		
	public static final String VARIABLE_OB_CLID_LEFT_NUMBER = "obClidLeftNumber";

	public static final String VARIABLE_START_TIME = "startTime"; // 进入系统时间

	
	public static final String VARIABLE_IVR_ID = "ivrId"; // ivrId
	public static final String VARIABLE_IVR_NODE = "ivrNode"; // ivrNode


	public static final String VARIABLE_KEYS = "keys"; // keys
	public static final String VARIABLE_BRIDGE_TIME = "bridgeTime"; // bridgeTime
	public static final String VARIABLE_CALLEE_NUMBER = "calleeNumber"; // calleeNumber
	public static final String VARIABLE_DETAIL_CALLTYPE = "detailCallType"; // detailCallType
	public static final String VARIABLE_MAIN_UNIQUE_ID = "mainUniqueId"; // mainUniqueId

	public static final String VARIABLE_QUEUES = "queues";
	public static final String VARIABLE_GWIP = "gwIp";
	public static final String VARIABLE_CALLER_NUMBER = "callerNumber";
	public static final String VARIABLE_CLID_RIGHT = "clidRight";
	public static final String VARIABLE_TEL_TYPE = "telType";
	public static final String VARIABLE_SYNC = "sync";
	public static final String VARIABLE_DEST_INTERFACE = "destInterface";
	public static final String VARIABLE_ROUTER_CLID_TYPE = "routerClidType";
	public static final String VARIABLE_CLID = "clid";
	public static final String VARIABLE_PARAM_NAMES = "paramNames";
	public static final String VARIABLE_PARAM_VALUES = "paramValues";
	public static final String VARIABLE_ORDER_CALL_BACK_ID = "orderCallBackId";
	public static final String VARIABLE_AMI_TYPE = "amiType";
	


	public static final String VARIABLE_CALL_TYPE = "callType"; // action/event字段:
																// 呼叫类型1呼入2外呼...
	public static final String VARIABLE_UNIQUEID = "uniqueId";
	public static final String VARIABLE_STATUS = "status"; // action/event字段:
															// 成员状态
	public static final String VARIABLE_MEMBER_STATUS = "memberStatus"; // action/event字段:
																		// 队列成员状态
	public static final String VARIABLE_QUEUE_ENTRY = "queueEntry"; // action/event字段:
																	// 队列等待客户
	public static final String VARIABLE_QUEUE_WAITING_COUNT = "queueWaitingCount"; // action/event字段:
	// 队列等待客户的数目
	public static final String VARIABLE_QUEUE_STATUS = "queueStatus"; // action/event字段:
																		// 队列信息
	public static final String VARIABLE_IVR_STATUS = "ivrStatus";
	public static final String VARIABLE_QUEUE_PARAMS = "queueParams"; // action/event字段:
																		// 队列信息
	public static final String VARIABLE_QUEUE_ENTRY_POSITION = "position"; // action/event字段:
																			// 排队客户位置
	public static final String VARIABLE_QUEUE_CALL_MAP = "queueCallMap"; // action/event字段:
																			// 存储来电的呼叫状态map
	public static final String VARIABLE_QUEUE_ENTRY_JOIN_TIME = "joinTime"; // action/event字段:
																			// 排队客户加入时间
	public static final String VARIABLE_QUEUE_ENTRY_WAIT_TIME = "waitTime"; // action/event字段:
																			// 排队客户等待时间
	public static final String VARIABLE_QUEUE_ENTRY_PRIORITY = "priority"; // action/event字段:
																			// 排队客户VIP级别
	public static final String VARIABLE_QUEUE_ENTRY_OVERFLOW = "overflow"; // action/event字段:
																			// 排队客户溢出次数
	public static final String VARIABLE_MEMBER_LOGIN_STATUS = "loginStatus"; // action/event字段:
																				// 座席登录状态
	public static final String VARIABLE_MEMBER_LOGIN_TIME = "loginTime"; // action/event字段:
																			// 座席登录状态
	public static final String VARIABLE_MEMBER_LOGIN_START_TIME = "loginStartTime"; // action/event字段:
																					// 座席登录状态
	public static final String VARIABLE_MEMBER_STATUS_START_TIME = "statusStartTime"; // action/event字段:
																						// 座席登录状态
	public static final String VARIABLE_MEMBER_DEVICE_STATUS = "deviceStatus"; // action/event字段:
																				// 座席设备状态
	public static final String VARIABLE_PICKUP_CNO = "pickupCno"; // action/event字段:
																	// 被抢线的座席
	public static final String VARIABLE_CONSULTER_CNO = "consulterCno";
	public static final String VARIABLE_CONSULTEE_CNO = "consulteeCno";

	public static final String VARIABLE_QUEUE_TIMEOUT = "queueTimeout"; // 队列超时时长
	public static final String VARIABLE_QUEUE_MEMBER_TIMEOUT = "memberTimeout"; // 队列中座席超时时长
	public static final String VARIABLE_QUEUE_WRAPUP_TIME = "wrapupTime"; // 队列默认整理时间
	public static final String VARIABLE_QUEUE_MAX = "max"; // 队列中最大等待座席数
	public static final String VARIABLE_QUEUE_STRATEGY = "strategy"; // 队列排队策略
	public static final String VARIABLE_QUEUE_CALLS = "calls"; // 队列当前等待电话数
	public static final String VARIABLE_QUEUE_HOLD_TIME = "holdTime"; // 队列中电话接通平均等待时长
	public static final String VARIABLE_QUEUE_TALK_TIME = "talkTime"; // 队列中电话接通平均通话时长
	public static final String VARIABLE_QUEUE_COMPLETED = "completed"; // 队列中接通电话数
	public static final String VARIABLE_QUEUE_ABANDONED = "abandoned"; // 队列中放弃电话数
	public static final String VARIABLE_QUEUE_SERVICE_LEVEL = "serviceLevel"; // 队列服务水平描述
	public static final String VARIABLE_QUEUE_SERVICE_LEVEL_PERF = "serviceLevelPerf";// 队列服务水平
	public static final String VARIABLE_QUEUE_WEIGHT = "weight"; // 队列优先级

	public static final String VARIABLE_MONITORED = "monitored"; // 是否被监控 0:未被监控
																	// 1:被监控
	public static final String VARIABLE_MONITORED_TYPE = "monitoredType"; // 被监控类型
																			// spy
																			// whisper
																			// threeway
	public static final String VARIABLE_MONITOR_OBJECT = "monitorObject"; // 监控者
																			// 01041005960或2001
	public static final String VARIABLE_MONITOR_OBJECT_TYPE = "monitorObjectType"; // 监控者类型
																					// 0:电话
																					// 1:座席号
	public static final String VARIABLE_NO_RINGING = "noringing";
	public static final String VARIABLE_ALL_CALL_BACK_COUNT = "allCallBackCount"; // 坐席所在队列所有预约回呼数
	public static final String VARIABLE_ADD_OR_REDUCE = "addORReduce"; // 增加或减少一个预约回呼

	public static final String VARIABLE_LIMIT_TIME_SECOND = "limitTimeSecond";
	public static final String VARIABLE_LIMIT_TIME_FILE = "limitTimeFile";
	public static final String VARIABLE_LIMIT_TIME_ALERT_SECOND = "limitTimeAlertSecond";
	/** 当前登录座席ip */
	public static final String VARIABLE_IP = "ip";

	
	/** 座席名称 */
	public static final String VARIABLE_AGENT_NAME = "agentName";
	public static final String VARIABLE_OB_CLID = "obClid";
	public static final String VARIABLE_AGENT_LOCATION = "agentLocation";
	public static final String VARIABLE_AGENT_QUEUE = "agentQueue";
	public static final String VARIABLE_CTIID = "ctiId";
	
	
	
		
		
		
	//action parameter
	//可变参数	
		public static final String CDR_DETAIL_GW_IP = "cdr_detail_gw_ip";
		public static final String CDR_GW_IP = "cdr_gw_ip";
		public static final String CDR_NUMBER_TRUNK = "cdr_number_trunk";
		public static final String CDR_CALLEE_AREA_CODE = "cdr_callee_area_code";
		public static final String CDR_STATUS = "cdr_status";
		public static final String RECORD_FILE = "record_file";
		public static final String CDR_RECORD_FILE = "cdr_record_file";
		public static final String CDR_IVR_ID = "cdr_ivr_id";
		public static final String CDR_IVR_FLOW = "cdr_ivr_flow";
		public static final String CDR_TELSET_NAME = "cdr_telset_name";
		public static final String CDR_IVR_FLOW_SEC = "cdr_ivr_flow_sec";
		public static final String CDR_USER_FIELD = "CDR(userfield)";
		public static final String CDR_SIP_CAUSE = "cdr_sip_cause";
		public static final String CDR_OB_CLID = "cdr_ob_clid";
		public static final String CDR_REQUEST_UNIQUE_ID = "cdr_request_unique_id";
		public static final String CDR_BRIDGE_TIME ="cdr_bridge_time";
		public static final String CDR_START_TIME = "cdr_start_time";
		public static final String CDR_START = "CDR(start)";
		public static final String CDR_ANSWER_TIME = "cdr_answer_time";
		public static final String CDR_END_TIME = "cdr_end_time";
		public static final String CDR_END = "CDR(end)";
		public static final String CDR_DIAL_TIME = "cdr_dial_time";
		public static final String CDR_REQUEST_TIME = "cdr_request_time";
		public static final String CDR_MONITOR_START_TIME = "cdr_monitor_start_time";
		public static final String CDR_MONITOR_STOP_TIME = "cdr_monitor_stop_time";
		public static final String CDR_MARK = "cdr_mark";
		public static final String CDR_MARK_DATA = "cdr_mark_data";
		public static final String CDR_HANGUP_CAUSE = "cdr_hangup_cause";
		public static final String CDR_DETAIL_CLID = "cdr_detail_clid";
		public static final String CDR_DETAIL_CALLEE_NUMBER="cdr_detail_callee_number";
		public static final String WEBCALL_TEL = "webcall_tel";
		public static final String IS_AMD_ON = "is_amd_on";
		public static final String SUBTEL = "subtel";
		public static final String NUMBER_TRUNK_AREA_CODE="number_trunk_area_code";
		public static final String WEBCALL_IVR_ID = "webcall_ivr_id";
		

		
		
}
