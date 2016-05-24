package com.tinet.ctilink.ami.inc;

/**
 * AMI相关的事件类型。
 * <p>
 * 文件名： AmiEventConst.java
 * <p>
 * Copyright (c) 2006-2010 T&I Net Communication CO.,LTD. All rights reserved.
 * 
 * @author hongzk
 * @since 1.0
 * @version 1.0
 * 
 */

public class AmiEventTypeConst {
	
	public static final String AMI_EVENT_LIST = "cti-link.ami.event";	
	
	public static final String ORIGINATE_RESPONSE = "originateResponse"; // 事件类型：发起呼叫响应		
	public static final String INCOMING = "incoming"; // 事件类型：来电
	public static final String ANSWER = "answer"; // 事件类型：系统应答
	public static final String UNANSWER = "unanswer"; // 事件类型：未接来电	
	public static final String BRIDGED = "bridged"; // 事件类型：坐席桥接(只用于推送)
	public static final String STATUS = "status"; // 事件类型：状态改变
	public static final String SPY_LINK = "spyLink"; // 事件类型: 监听接听
	public static final String SPY_UNLINK = "spyUnlink"; // 事件类型: 监听挂断
	public static final String CONSULT_LINK = "consultLink"; // 事件类型: 咨询接听
	public static final String UNCONSULT = "unconsult"; // 事件类型: 咨询挂断
	public static final String CONSULT_ERROR = "consultError"; // 事件类型: 咨询错误
	public static final String CONSULT_TRANSFER = "consultTransfer"; // 事件类型:咨询转接
	public static final String CONSULT_THREEWAY = "consultThreeway"; // 事件类型:咨询三方
	public static final String CONSULT_THREEWAY_UNLINK = "consultThreewayUnlink"; // 事件类型:咨询三方挂断
	public static final String WHISPER_LINK = "whisperLink"; // 事件类型: 耳语接听
	public static final String WHISPER_UNLINK = "whisperUnlink"; // 事件类型: 耳语挂断
	public static final String THREEWAY_LINK = "threewayLink"; // 事件类型: 三方接听
	public static final String THREEWAY_UNLINK = "threewayUnlink"; // 事件类型: 三方挂断
	public static final String BARGE_LINK = "bargeLink"; // 事件类型: 强插接听
	public static final String BARGE_UNLINK = "bargeUnlink"; // 事件类型: 强插挂断
	public static final String PREVIEW_OUTCALL_BRIDGE = "previewOutcallBridge"; // 事件类型：预览外呼客户接听
	public static final String ORDER_CALL_BACK = "orderCallBack"; // 事件类型：预约回呼
	public static final String PRESS_KEYS = "pressKeys"; // 事件类型：按键
	public static final String INTERACT_RETURN = "interactReturn"; // 事件类型: 交互返回
	public static final String DIRECT_CALL_START = "directCallStart";

}
