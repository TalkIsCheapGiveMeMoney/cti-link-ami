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
	public static final String TRYING = "1"; // 事件类型：试呼 中，0
	public static final String RING = "2"; // 事件类型：振铃 ，1
	public static final String UP = "3"; // 事件类型：通道占用，2
	public static final String IDLE = "4"; // 事件类型：空闲
	
	private static final String[] channelStateArray={TRYING,RING,UP,IDLE};

	public static String ChannelStateToString(int channelState)
	{
		if(channelState<0||channelState>7)
			return IDLE;
		if(channelState == 3||channelState == 4)
			return channelStateArray[1];
		if(channelState == 5)
			return channelStateArray[2];
		if(channelState == 6)
			return channelStateArray[3];
		else
			return channelStateArray[4];
	}
}
