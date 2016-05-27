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
	public static final String ORIGINATE = "originate"; // 发起呼叫
	public static final String HANGUP = "hangup"; // 释放呼叫
	public static final String MUTE = "mute"; // 静音
	public static final String INDICATE = "indicate"; // 保持
	public static final String CONSULT = "consult"; // 咨询 长动作
	public static final String REDIRECT = "redirect"; // 
	public static final String TRANSFER = "transfer"; // 转移 长动作
	public static final String SET_VAR = "setVar";
	public static final String GET_VAR = "getVar";
	public static final String SORCERY_EXPIRE = "sorceryExpire";

}
