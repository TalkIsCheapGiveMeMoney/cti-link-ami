package com.tinet.ctilink.ami.inc;

/**
 * 与呼叫相关的事件封装。
 * <p>
 * 文件名： Event.java
 * <p>
 * Copyright (c) 2006-2010 T&I Net Communication CO.,LTD. All rights reserved.
 * 
 * @author 安静波
 * @since 1.0
 * @version 1.0
 * 
 * @author 罗尧
 * @since 1.1
 * @version 1.1
 * @since 更新时间：2012-1-12 下午04:50:02 *
 */

public class AmiEventConst {
	
//	public static final String INCOMING = "incoming"; // 事件类型：来电
//	public static final String SYSTEM_ANSWER = "answer"; // 事件类型：系统应答
//	public static final String SYSTEM_UNANSWER = "system_unanswer"; // 事件类型：未接来电
//	
//	public static final String AGENT_RINGING = "agent_ringing"; // 事件类型：坐席振铃
//	public static final String AGENT_ANSWER = "agent_answer"; // 事件类型：坐席应答
//	public static final String AGENT_UNANSWER = "agent_unanswer"; // 事件类型：坐席未应答
//	public static final String KEY_PRESSED = "key_pressed"; // 事件类型：按键

	
	
	public static final String INCOMING = "incoming"; // 事件类型：来电
	public static final String ANSWER = "answer"; // 事件类型：系统应答
	public static final String UNANSWER = "unanswer"; // 事件类型：未接来电	
	public static final String RINGING = "ringing"; // 事件类型：响铃事件
	public static final String BRIDGED = "bridged"; // 事件类型：坐席桥接
	
//	public static final String JOIN_QUEUE = "joinQueue"; // 事件类型：接入队列
//	public static final String LEAVE_QUEUE = "leaveQueue"; // 事件类型：离开队列
//	public static final String WELCOME = "welcome"; // 事件类型：websocket 进入
//	public static final String GOODBYE = "goodbye"; // 事件类型：websocket 离开
//	public static final String OPEN_AGENT_DEBUG = "openAgentDebug"; // 事件类型:开启坐席debug监控
//	public static final String CLOSE_AGENT_DEBUG = "closeAgentDebug"; // 事件类型:关闭坐席debug监控'
//	public static final String AGENT_SCREEN_SHOT = "agentScreenShot"; // 事件类型:座席截图
	
	
	public static final String STATUS = "status"; // 事件类型：状态改变
	public static final String SPY_LINK = "spyLink"; // 事件类型: 监听接听
	public static final String SPY_UNLINK = "spyUnlink"; // 事件类型: 监听挂断
	public static final String SPY_ERROR = "spyError"; // 事件类型: 监听错误
	public static final String CONSULT_LINK = "consultLink"; // 事件类型: 咨询接听
	public static final String UNCONSULT = "unconsult"; // 事件类型: 咨询挂断
	public static final String CONSULT_ERROR = "consultError"; // 事件类型: 咨询错误
	public static final String CONSULT_TRANSFER = "consultTransfer"; // 事件类型:咨询转接
	public static final String CONSULT_THREEWAY = "consultThreeway"; // 事件类型:咨询三方
	public static final String CONSULT_THREEWAY_UNLINK = "consultThreewayUnlink"; // 事件类型:咨询三方挂断
	public static final String TRANSFER_ERROR = "transferError"; // 事件类型: 转移错误
	public static final String WHISPER_LINK = "whisperLink"; // 事件类型: 耳语接听
	public static final String WHISPER_UNLINK = "whisperUnlink"; // 事件类型: 耳语挂断
	public static final String WHISPER_ERROR = "whisperError"; // 事件类型: 耳语错误
	public static final String THREEWAY_LINK = "threewayLink"; // 事件类型: 三方接听
	public static final String THREEWAY_UNLINK = "threewayUnlink"; // 事件类型: 三方挂断
	public static final String THREEWAY_ERROR = "threewayError"; // 事件类型: 三方错误
	public static final String BARGE_LINK = "bargeLink"; // 事件类型: 强插接听
	public static final String BARGE_UNLINK = "bargeUnlink"; // 事件类型: 强插挂断
	public static final String BARGE_ERROR = "bargeError"; // 事件类型: 强插错误
	public static final String DISCONNECT_UNLINK = "disconnectUnlink"; // 事件类型:强拆
//	public static final String NWAY_INIT_LINK = ""; // 事件类型: 会议邀请接听
//	public static final String NWAY_INIT_UNLINK = ""; // 事件类型: 会议邀请接听后对方挂断
//	public static final String NWAY_INIT_ERROR = ""; // 事件类型: 会议邀请失败
//	public static final String NWAY_JOIN = ""; // 事件类型: 会议邀请接听后加入会议
	public static final String PREVIEW_OUTCALL_ERROR = "previewOutcallError"; // 事件类型:外呼错误
	public static final String PREVIEW_OUTCALL_BRIDGE = "previewOutcallBridge"; // 事件类型：预览外呼客户接听
//	public static final String QUEUE_CALL = "queueCall"; // 事件类型: 来电在队列中开始呼叫座席了

	public static final String ORDER_CALL_BACK = "orderCallBack"; // 事件类型：预约回呼
	public static final String PRESS_KEYS = "pressKeys"; // 事件类型：按键
//	public static final String IB_BRIDGE = "ibBridge"; // 事件类型：来电接通

//	/** 事件类型: 踢人 */
//	public static final String KICKOUT = "kickout";
//	/** 事件类型: 后台下线事件 */
//	public static final String BACKEND_LOGOUT_EVENT = "backendLogoutEvent";
//	/** 事件类型: 预览时外呼--开始 */
//	public static final String PREVIEW_START = "previewStart";
//	/** 事件类型: 预览时外呼--暂停 */
//	public static final String PREVIEW_PAUSE = "previewPause";
//	/** 事件类型: 预览时外呼--回收完成 */
//	public static final String PREVIEW_COMPLETE = "previewComplete";
//	/** 事件类型: 预览时外呼--刷新 */
//	public static final String PREVIEW_REFRESH = "previewRefresh";
//	/** 事件类型: 聊天系统--消息事件 */
//	public static final String WEBCHAT_MSG = "webchatMsg";
//	/** 事件类型: 聊天系统--消息事件 */
//	public static final String SMS_RECEIVE = "smsReceive";
//	/** 事件类型: 计划表--消息提醒事件 */
//	public static final String AGENDA = "agenda";

	public static final String INTERACT_ERROR = "interactError"; // 事件类型: 交互错误
	public static final String INTERACT_RETURN = "interactReturn"; // 事件类型: 交互返回
//	/** 事件类型: 工单流转事件 */
//	public static final String VARIABLE_RECORD_COMMENT = "recordComment";

}
