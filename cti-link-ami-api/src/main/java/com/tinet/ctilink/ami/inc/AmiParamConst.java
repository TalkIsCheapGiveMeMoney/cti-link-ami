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
	public static final String ACTION_MAP = "actionMap"; 			// 客户号码
	public static final String CALLBACK_MAP = "callbackMap"; 	// 客户号码类型
	public static final String CHANNEL_VAR_MAP = "channelVarMap"; 		// 客户区号
	
	//used parameter name
	public static final String CUSTOMER_NUMBER = "customerNumber"; 			// 客户号码
	public static final String CUSTOMER_NUMBER_TYPE = "customerNumberType"; 	// 客户号码类型
	public static final String CUSTOMER_NUMBER_AREA_CODE = "customerNumberAreaCode"; 		// 客户区号
	public static final String CALL_TYPE = "callType"; 						// 呼叫类型
	public static final String CNO = "cno"; 											// 坐席号
	public static final String CDR_CALLEE_NUMBER = "cdr_callee_number"; 				// 被叫号码
	public static final String CHANNEL = "channel"; // 事件参数：通道名
	public static final String CHANNELSTATE = "status"; // 事件参数：通道状态
	public static final String UNIQUEID = "uniqueId"; // 事件参数：当前通道唯一Id
//	public static final String CDR_MAIN_UNIQUE_ID = "cdr_main_unique_id"; // 事件参数：主通道唯一Id
	public static final String ENTERPRISEID = "enterpriseId"; // 事件参数：企业Id
	public static final String VARIABLE_HOTLINE = "hotline"; // action/event字段：热线号码
	public static final String VARIABLE_QUEUE = "queue";
	public static final String VARIABLE_CNO = "cno"; // action/event字段：座席号 2000
	public static final String DETAIL_CALL_TYPE="detailCallType";
	public static final String VARIABLE_NUMBER_TRUNK = "numberTrunk";
	public static final String VARIABLE_BRIDGED_CHANNEL = "bridgedChannel";
	public static final String VARIABLE_BRIDGED_UNIQUEID = "bridgedUniqueId";
	public static final String VARIABLE_STATUS_VARIABLES = "variables";
	
	
    public static final Integer INDICATE_HOLD = 16;
    public static final Integer INDICATE_UNHOLD = 17;
    
    public static final String MUTE_DIRECTION_IN = "in";
    public static final String MUTE_DIRECTION_OUT = "out";
    public static final String MUTE_DIRECTION_ALL = "all";
    
    public static final String MUTE_STATE_ON = "on";
    public static final String MUTE_STATE_OFF = "off";  
	
    //used const parameter value
    public static final String ORIGINATE_RESPONSE_RESULT_ERROR = "error";
	public static final String ORIGINATE_RESPONSE_RESULT_SUCCESS = "success";
	
	//event param

	
	public static final String REFUSE = "refuse"; // 拒绝
	public static final String LINK = "link"; // 接听
	public static final String UNLINK = "unlink"; // 挂断或者取消呼叫
	public static final String HOLD = "hold"; // 保持
	public static final String UNHOLD = "unhold"; // 保持挂断
	public static final String CONSULT = "consult"; // 咨询 长动作
	public static final String CONSULTCANCEL = "consultCancel";// 咨询取消
	public static final String UNCONSULT = "unconsult"; // 咨询挂断
	public static final String CONSULT_TRANSFER = "consultTransfer"; // 咨询转接
	public static final String CONSULT_THREEWAY = "consultThreeway"; // 咨询三方
	public static final String TRANSFER = "transfer"; // 转移 长动作
	public static final String INVESTIGATION = "investigation"; // 满意度调查
	public static final String SPY = "spy"; // 监听 长动作
	public static final String UNSPY = "unspy"; // 监听挂断
	public static final String WHISPER = "whisper"; // 耳语 长动作
	public static final String UNWHISPER = "unwhisper"; // 耳语挂断
	public static final String THREEWAY = "threeway";
	public static final String UNTHREEWAY = "unthreeway";
	public static final String BARGE = "barge"; // 强插 长动作
	public static final String UNINSERT = "uninsert"; // 强插挂断
	public static final String DISCONNECT = "disconnect"; // 前拆
	public static final String GET_STATUS = "getStatus"; // 获取自己状态
	public static final String PING = "ping"; // 心跳
	public static final String NWAY_INIT = "threewayInit"; // 会议邀请
	public static final String NWAY_OK = "threewayOk"; // 会议进入
	public static final String PREVIEW_OUTCALL = "previewOutCall"; // 点击外呼
	public static final String PREVIEW_OUTCALL_CANCEL = "previewOutcallCancel"; // 点击取消
	public static final String QUEUE_STATUS = "queueStatus"; // 获取队列状态
	public static final String QUEUE_SHOW = "queueShow"; // 刷新队列状态
	public static final String CHANGE_BIND_TEL = "changeBindTel"; // 修改
	public static final String PICKUP = "pickup"; // 抢线
	public static final String CHANNELHANGUP = "channelHangUp"; // 抢线
	public static final String CALLLOCAL = "callLocal";
	public static final String IVR_CALLBACK = "ivrCallback";
	/** 聊天系统--web_chat--Action */
	public static final String WEBCHAT = "webchat";
	public static final String INTERACT = "interact";
	/** 坐席Debug信息采集--Action */
	public static final String AGENT_DEBUG = "agentDebug";
	public static final String MUTE = "mute";
	public static final String DIRECT_CALL_START = "directCallStart";
	public static final String WEB_400_CALL = "web400call";
	public static final String SELF_RECORD = "selfRecord"; // 咨询 长动作

	/** 验证绑定电话 action */
	public static final String VERIFY_BINDING_TEL = "verifyBindingTel";

	public static final int ERRORCODE_BAD_USERNAME_PASSWORD = 1; // 用户名密码错误
	public static final int ERRORCODE_BAD_CONNECTION = 2; // 连接已中断
	public static final int ERRORCODE_TIMEOUT = 3; // 超时
	public static final int ERRORCODE_NO_QUEUE = 4; // 座席没有在队列中
	public static final int ERRORCODE_NO_CHANNEL = 5; // 咨询失败，座席不在通话中
	public static final int ERRORCODE_BAD_PARAM = 6; // 参数错误
	public static final int ERRORCODE_BAD_BIND_TEL = 7; // 绑定的电话错误
	public static final int ERRORCODE_BAD_PICKUP = 8; // 抢线失败
	public static final int ERRORCODE_BAD_REFUSE = 9; // 拒接失败
	public static final int ERRORCODR_BAD_CANCEL = 10; // 取消外呼失败
	public static final int ERRORCODR_BAD_PAREM = 10; // 咨询或转移或三方通话参数错误
	public static final int ERRORCODR_BAD_DEVICE_STATUS = 11; // 监听耳语强插、外呼等操作时当前状态不为空闲
	public static final int ERRORCODE_NO_SUCH_CNO = 12; // 没有这个坐席号
	public static final int ERRORCODE_BAD_TEL = 13; // 外呼号码格式错误
	public static final int ERRORCODE_BAD_CLID = 14; // 外显号码设置错误
	public static final int ERRORCODE_NOT_ONLINE = 15; // 座席不在线
	public static final int ERRORCODE_NOT_LOGIN = 16; // 座席未登录平台
	public static final int ERRORCODE_ALREADY_ONHOLD = 17; // 座席已经是保持状态
	public static final int ERRORCODE_NOT_ONHOLD = 18; // 座席不在保持状态
	public static final int ERRORCODE_NOT_LOGIN_FRONTEND = 19; // 座席是后台登录
	public static final int ERRORCODE_PERMISSION_DENIED = 20; // 没有权限
	public static final int ERRORCODE_ALREADY_MONITORED = 21; // 座席已经被监控
	public static final int ERRORCODE_CANNOT_MONITORE = 22; // 班长不能监控正在做监控的其他班长座席
	public static final int ERRORCODE_DEFAULT_PAUSE = 23;// 座席默认自定义置忙状态配置错误
	public static final int ERRORCODE_PAUSE_OR_BUSY = 24; // 咨询失败，座席置忙或者busy
	public static final int ERRORCODE_BLACK_TEL = 25; // 号码为黑名单
	public static final int ERRORCODE_PERMISSION_OUTCALL = 26; // 座席没有外呼权限
	public static final int ERRORCODE_NO_MONEY = 27; // 余额不足
	public static final int ERRORCODE_NO_ROUTER = 28; // 没有路由
	public static final int ERRORCODE_CLIENT_WEB_LIMIT = 29; // 座席在线数超过最大并发限制
	public static final int ERRORCODE_AGENT_LOGIN = 30; // 此座席工号已经绑定别的电话号码或分机登录
	public static final int ERRORCODE_AGENT_BUDY = 31; // 座席busy 不能更改绑定电话
	public static final int ERRORCODE_AGENT_OFFLINE = 34;// 错误的断线重连，座席已经下线
	public static final int ERRORCODE_AGENT_NOT_ACTIVE = 32;// 座席未激活 不能登录电话
	public static final int ERRORCODE_AGENT_IS_OUTCALLING = 33; // 座席正在使用外呼
	public static final int ERRORCODE_DEAD_NUMBER = 35; // 外呼号码可能是空号
	public static final int ERRORCODE_CALL_ALREADY = 36; // 已经被执行
	public static final int ERRORCODE_CALL_BY_OTHER_NOW = 37; // 正在被别的坐席执行
	public static final int ERRORCODE_BLACK_IP = 38; // IP黑名单
	public static final int ERRORCODE_AGENT_WEB_LOGIN_FORBIDDEN = 39; // 非电脑座席
	public static final int ERRORCODE_EXCEPTION = 999; // 发生异常

	public static final String VARIABLE_TYPE = "type"; // action/event字段：类型
	public static final String VARIABLE_RES_TYPE = "reqType"; // action/event字段：响应类型
	public static final String VARIABLE_UTID = "utid"; // action/event字段：unique
														// id标识每一个请求
	public static final String VARIABLE_SESSION_ID = "sessionId"; // action/event字段：session
																	// id标识每一个websocket客户端
	public static final String VARIABLE_SESSION_TIMEOUT = "sessionTimeout";// action/event字段：session
																			// 超时时间
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
	
	
	
	public static final String VARIABLE_CID = "cid"; // action/event字段：座席id
	public static final String VARIABLE_CLIENT_ID = "clientId"; // client 的id //
																// 10000762000
	public static final String VARIABLE_CNAME = "cname"; // action/event字段：座席姓名
															// 张三
	public static final String VARIABLE_CRM_ID = "crmId"; // action/event字段：crm
															// id 9A0021C
	public static final String VARIABLE_POWER = "power"; // action/event字段：座席权限
															// 0为普通座席；1为班长席
	public static final String VARIABLE_QNO = "qno"; // action/event字段：队列号 001
	public static final String VARIABLE_QID = "qid"; // action/event字段：队列号
														// 1000076001
	public static final String VARIABLE_TRANSFER_CNO = "transferCno"; // action/event字段：队列号
																		// 1000076001
	public static final String VARIABLE_QNAME = "qname"; // action/event字段：队队列名称
															// 咨询队列
	public static final String VARIABLE_DURATION = "duration"; // action/event字段:座席状态持续时间
	public static final String VARIABLE_CALLSTAKEN = "callstaken"; // action/event字段:座席状态持续时间
	public static final String VARIABLE_PWD = "pwd"; // action/event字段：密码
														// pwd密文=md5(pwd明文+sessionId)
	public static final String VARIABLE_LOGIN_TYPE = "loginType"; // action/event字段：登录类型
																	// 后台/前台
																	// frontend/backend
	public static final String VARIABLE_HOLD_TYPE = "holdType"; // action/event字段:
																// 保持类型
																// 直接保持/咨询转移保持
	/** RemoveBinding 解除绑定 */
	public static final String VARIABLE_REMOVE_BINDING = "RemoveBinding"; // action/event字段：登录类型
																			// 后台/前台
																			// frontend/backend
	public static final String VARIABLE_INIT_STATUS = "initStatus"; // action/event字段：idle或者pause登录后状态
	public static final String VARIABLE_PAUSE_DESCRIPTION = "pauseDescription"; // action/event字段：置忙的描述
	public static final String VARIABLE_BUSY_DESCRIPTION = "busyDescription"; // action/event字段：通话中的描述
																				// 比如onhold
	public static final String VARIABLE_WRAPUP_TIME = "wrapupTime"; // action/event字段:
																	// 整理时长
	public static final String VARIABLE_PAUSE_QUEUE = "pauseQueueName"; // action/event字段：置忙的队列
	public static final String VARIABLE_ENTERPRISE_ID = "enterpriseId"; // action/event字段：企业id
	public static final String VARIABLE_BIND_TEL = "bindTel"; // action/event字段：绑定电话
	public static final String VARIABLE_BIND_TYPE = "bindType"; // 绑定电话类型
	/** 绑定电话类型 默认0不记住 1 记住 */
	public static final String VARIABLE_IS_TEL_REMEMBER = "is_tel_remember"; // 绑定电话类型
	public static final String VARIABLE_IS_OUT_CALL = "isOutCall"; // 是否有外呼权限
	public static final String VARIABLE_OB_RECORD = "obRecord"; // 外呼是否录音
	public static final String VARIABLE_IS_INVESTIGATION_AUTO = "isInvestigationAuto";
	public static final String VARIABLE_OB_SMS_TAIL = "obSmsTail";

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

	public static final String VARIABLE_WEBCHAT_SENDER_CNO = "webchatSenderCno"; // webchat发送方
	public static final String VARIABLE_WEBCHAT_SENDER_CNAME = "webchatSenderCname"; // webchat发送方
	public static final String VARIABLE_WEBCHAT_TARGET_CNO = "webchatTargetCno"; // webchat发送方
	public static final String VARIABLE_WEBCHAT_TARGET_CNAME = "webchatTargetCname"; // webchat发送方

	public static final String VARIABLE_IS_AGENT_DEBUG = "isAgentDebug"; // webchat发送方

	public static final String VARIABLE_OB_CLID_LEFT_NUMBER = "obClidLeftNumber";

	public static final String VARIABLE_START_TIME = "startTime"; // 进入系统时间

	public static final String VARIABLE_RINGING_TIME = "ringingTime"; // 进入系统时间
	public static final String VARIABLE_IVR_ID = "ivrId"; // ivrId
	public static final String VARIABLE_IVR_NODE = "ivrNode"; // ivrNode
	public static final String VARIABLE_TIME = "time"; // time
	public static final String VARIABLE_USER_FIELD = "userField"; // userField
	public static final String VARIABLE_KEYS = "keys"; // keys
	public static final String VARIABLE_BRIDGE_TIME = "bridgeTime"; // bridgeTime
	public static final String VARIABLE_CALLEE_NUMBER = "calleeNumber"; // calleeNumber
	public static final String VARIABLE_DETAIL_CALLTYPE = "detailCallType"; // detailCallType
	public static final String VARIABLE_MAIN_UNIQUE_ID = "mainUniqueId"; // mainUniqueId

	public static final String VARIABLE_DEFAULT_PAUSE = "defaultPause";
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
	

	/** 聊天系统--web_chat--消息 */
	public static final String VARIABLE_WEBCHAT_MSG = "webchatMsg";
	/** 聊天系统--web_chat--发送类型 */
	public static final String VARIABLE_WEBCHAT_TYPE = "webchatType";
	/** 聊天系统--web_chat--消息组id */
	public static final String VARIABLE_WEBCHAT_GROUP_ID = "groupId";
	/** 聊天系统--web_chat--消息组id */
	public static final String VARIABLE_SMS_MSG = "smsMsg";
	/** 聊天系统--web_chat--消息组id */
	public static final String VARIABLE_SMS_CUSTOMER_NAME = "smsCustomerName";

	/** 任务系统--taskId--任务ID */
	public static final String VARIABLE_TASK_ID = "taskId";
	/** 任务系统--taskInventoryId--任务清单ID */
	public static final String VARIABLE_TASK_INVENTORY_ID = "taskInventoryId";
	/** 任务系统--taskName--任务名称 */
	public static final String VARIABLE_TASK_NAME = "taskName";
	/** 任务系统--wrapupTime--任务整理时长 */
	public static final String VARIABLE_TASK_WRAPUP_TIME = "wrapupTime";
	/** 任务系统--wrapupTime--属于此座席的号码数量 */
	public static final String VARIABLE_TASK_COUNT = "count";

	/** 预约提醒--subject--预约主题 */
	public static final String VARIABLE_AGENDA_SUBJECT = "agenda_subject";
	/** 预约提醒--customer_name--预约客户姓名 */
	public static final String VARIABLE_AGENDA_CUSTOMER_NAME = "agenda_customer_name";
	/** 日历提醒的任务类型 */
	public static final String VARIABLE_AGEND_TYPE = "agendType";
	/** 预约提醒--customer_phone_number--预约客户电话号码 */
	public static final String VARIABLE_AGENDA_CUSTOMER_PHONE_NUMBER = "agenda_customer_phone_number";
	/** 预约提醒--customer_comment--备注 */
	public static final String VARIABLE_AGENDA_COMMENT = "agenda_comment";
	/** 预约提醒--customer_bookingTime--预约时间 */
	public static final String VARIABLE_AGENDA_BOOKINGTIME = "agenda_bookingTime";

	// public static final String VARIABLE_CURRENT_CHANNEL = "currentChannel";
	// //action/event字段: 当前channel
	// public static final String VARIABLE_BRIDGED_CHANNEL = "bridgedChannel";
	// //action/event字段: 桥接端channel
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

	/** mute相关参数 */
	public static final String VARIABLE_MUTE_DIRECTION = "direction";
	public static final String VARIABLE_MUTE_STATE = "state";
	/** 座席名称 */
	public static final String VARIABLE_AGENT_NAME = "agentName";
	public static final String VARIABLE_OB_CLID = "obClid";
	public static final String VARIABLE_AGENT_LOCATION = "agentLocation";
	public static final String VARIABLE_AGENT_QUEUE = "agentQueue";
	public static final String VARIABLE_CTIID = "ctiId";
	
	
	//固定参数
		public static final String DEST_CHANNEL = "dest_channel";
		public static final String CLID = "clid";
		public static final String EXTENSION = "exten";
		public static final String DIALPLAN_CONTEXT = "context";
		public static final String PRIORITY = "priority";
		public static final String ORIGINATE_TIMEOUT = "originateTimeout";
		
	//action parameter
	//可变参数																		
	
		public static final String MAIN_CHANNEL = "main_channel";		
		public static final String CDR_ENTERPRISE_ID = "cdr_enterprise_id";
//		public static final String CDR_MAIN_UNIQUE_ID = "cdr_main_unique_id";
		
		public static final String CDR_DETAIL_GW_IP = "cdr_detail_gw_ip";
		public static final String CDR_GW_IP = "cdr_gw_ip";
//		public static final String CDR_CUSTOMER_NUMBER = "cdr_customer_number";
//		public static final String CDR_CUSTOMER_NUMBER_TYPE = "cdr_customer_number_type";
//		public static final String CDR_CUSTOMER_AREA_CODE = "cdr_customer_area_code";
		public static final String CDR_NUMBER_TRUNK = "cdr_number_trunk";
//		public static final String CDR_CALL_TYPE = "cdr_call_type";
//		public static final String CDR_CALLEE_NUMBER = "cdr_callee_number";
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
		
		public static final String ENTERPRISE_ID="enterprise_id";
		public static final String WEBCALL_IVR_ID = "webcall_ivr_id";

		
		
}
