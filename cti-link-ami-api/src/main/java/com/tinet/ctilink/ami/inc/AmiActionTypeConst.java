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

public class AmiActionTypeConst {
	
	//Action Type
	
	public static final String ORIGINATE = "Originate"; // 发起呼叫
	public static final String HANGUP = "Hangup"; // 释放呼叫
	public static final String MUTEAUDIO = "MuteAudio"; // 静音
	public static final String CONSULTATIONCALL = "Atxfer"; // 咨询呼叫
	public static final String CONSULTATIONTRANSFER = "Redirect"; // 咨询转移
	

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
	
	public static final String PREVIEW_OUTCALL = "previewOutCall"; // 点击外呼
	public static final String PREVIEW_OUTCALL_CANCEL = "previewOutcallCancel"; // 点击取消
	
	public static final String PICKUP = "pickup"; // 抢线
	public static final String CHANNELHANGUP = "channelHangUp"; // 抢线
	public static final String CALLLOCAL = "callLocal";
	public static final String IVR_CALLBACK = "ivrCallback";

}
