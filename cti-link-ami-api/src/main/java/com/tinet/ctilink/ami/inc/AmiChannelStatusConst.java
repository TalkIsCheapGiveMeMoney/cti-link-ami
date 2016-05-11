package com.tinet.ctilink.ami.inc;

/**
 * 通道状态描述
 * <p>
 * 文件名： AmiChannelStatusConst.java
 * <p>
 * Copyright (c) 2006-2016 T&I Net Communication CO.,LTD. All rights reserved.
 * 
 * @author 洪志奎
 * @since 1.0
 * @version 1.0
 * 
 */

public class AmiChannelStatusConst {
	
	/*
		ChannelStateDesc
		Down
		Rsrvd
		OffHook
		Dialing
		Ring
		Ringing
		Up
		Busy
		Dialing Offhook
		Pre-ring
		Unknown
	 */
	
	public static final String DOWN = "Down"; // 事件类型：空闲 ，0
	public static final String RSRVD = "Rsrvd"; // 事件类型：空闲 ，1
	public static final String OFFHOOK = "OffHook"; // 事件类型：摘机，2
	public static final String DIALING = "Dialing"; // 事件类型：拨号中，3
	public static final String RING = "unanswer"; // 事件类型：回铃，4	
	public static final String RINGING = "ringing"; // 事件类型：振铃，5
	public static final String UP = "bridged"; // 事件类型：接通，6
	public static final String BUSY = "Busy"; // 事件类型：占线，7
	public static final String UNKNOWN = "Unknown"; // 事件类型：未知，7


}
