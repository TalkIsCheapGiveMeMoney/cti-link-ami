package com.tinet.ctilink.ami.event;

import java.util.Map;

import com.tinet.ctilink.ami.cache.CacheService;
import com.tinet.ctilink.ami.online.CtiAgentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * AMI事件处理器抽象基类
 * 
 * @author Jiangsl
 *
 */
public abstract class AbstractAmiEventHandler {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	protected AmiEventPublisher amiEventPublisher;

	@Autowired
	protected CtiAgentService ctiAgentService;

	@Autowired
	protected CacheService cacheService;
	/**
	 * 推送AMI事件
	 * 
	 * @param event
	 */
	protected void publishEvent(final Map<String, String> event) {
//		amiEventPublisher.publish(event);
	}

	
}
