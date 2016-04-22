package com.tinet.ctilink.ami.event;

import org.asteriskjava.live.internal.QueueManager;
import org.asteriskjava.manager.event.ManagerEvent;

/**
 * AMI队列事件处理器接口
 * 
 * @author Jiangsl
 *
 */
public interface AmiQueueEventHandler {

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
	 * @param queueManager
	 */
	void handle(ManagerEvent event, QueueManager queueManager);
}
