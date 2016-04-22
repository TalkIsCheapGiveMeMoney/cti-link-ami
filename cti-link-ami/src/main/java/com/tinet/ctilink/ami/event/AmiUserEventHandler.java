package com.tinet.ctilink.ami.event;

import org.asteriskjava.manager.event.ManagerEvent;

/**
 * AMI用户事件处理器接口
 * 
 * @author Jiangsl
 *
 */
public interface AmiUserEventHandler {
	
	/**
	 * 获取处理事件类型
	 * 
	 * @return
	 */
	Class<?> getEventClass();
	
	/**
	 * 处理Ami 事件
	 * 
	 * @param mEvent
	 */
	void handle(ManagerEvent event);
}
