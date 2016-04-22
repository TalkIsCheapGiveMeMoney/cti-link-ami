package com.tinet.ctilink.ami.event;

import org.asteriskjava.live.internal.ChannelManager;
import org.asteriskjava.manager.event.ManagerEvent;

/**
 * AMI通道事件处理器接口
 * 
 * @author Jiangsl
 *
 */
public interface AmiChannelEventHandler {

	/**
	 * 获取处理事件类型
	 * 
	 * @return
	 */
	Class<?> getEventClass();

	/**
	 * 处理Ami 事件
	 * 
	 * @param event
	 * @param channelManager
	 */
	void handle(ManagerEvent event, ChannelManager channelManager);
}
