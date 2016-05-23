package com.tinet.ctilink.ami.inc;

/**
 * 通道状态描述
 * <p>
 * 文件名： AmiChannelStatusConst.java
 * <p>
 * Copyright (c) 2006-2016 T&I Net Communication CO.,LTD. All rights reserved.
 * 
 * @author hongzk
 * @since 1.0
 * @version 1.0
 * 
 */

public class AmiChannelStatusConst {
	
//	public static final String DOWN = "0"; // 事件类型：空闲 ，0
	public static final int TRYING = 1; // 事件类型：试呼 中，0
	public static final int RING = 2; // 事件类型：振铃 ，1
	public static final int UP = 3; // 事件类型：通道占用，2
	public static final int IDLE = 4; // 事件类型：空闲
	
	public static Integer ChannelStateToString(int channelState)
	{
		if(channelState<0||channelState>7)
			return IDLE;
		if(channelState == 3||channelState == 4)
			return TRYING;
		if(channelState == 5)
			return RING;
		if(channelState == 6)
			return UP;
		else
			return IDLE;
	}
}
