package com.tinet.ctilink.ami.inc;

/**
 * AMI相关的事件类型。
 * <p>
 * 文件名： AmiEventConst.java
 * <p>
 * Copyright (c) 2006-2010 T&I Net Communication CO.,LTD. All rights reserved.
 * 
 * @author 洪志奎
 * @since 1.0
 * @version 1.0
 * 
 */

public class AmiEventConst {
	

	public static final String INCOMING = "incoming"; // 事件类型：来电
	public static final String ANSWER = "answer"; // 事件类型：系统应答
	public static final String UNANSWER = "unanswer"; // 事件类型：未接来电	
	public static final String RINGING = "ringing"; // 事件类型：响铃事件
	public static final String BRIDGED = "bridged"; // 事件类型：坐席桥接
	
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
	public static final String PREVIEW_OUTCALL_ERROR = "previewOutcallError"; // 事件类型:外呼错误
	public static final String PREVIEW_OUTCALL_BRIDGE = "previewOutcallBridge"; // 事件类型：预览外呼客户接听
	public static final String ORDER_CALL_BACK = "orderCallBack"; // 事件类型：预约回呼
	public static final String PRESS_KEYS = "pressKeys"; // 事件类型：按键
	public static final String INTERACT_ERROR = "interactError"; // 事件类型: 交互错误
	public static final String INTERACT_RETURN = "interactReturn"; // 事件类型: 交互返回


}
