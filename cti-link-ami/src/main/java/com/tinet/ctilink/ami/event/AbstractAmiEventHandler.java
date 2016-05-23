package com.tinet.ctilink.ami.event;

import java.util.Map;

import com.tinet.ctilink.ami.cache.CacheService;

import com.tinet.ctilink.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


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
	protected CacheService cacheService;
	/**
	 * 推送AMI事件
	 * 
	 * @param event
	 */
	protected void publishEvent(final Map<String, String> event) {
		amiEventPublisher.publish(event);
	}
	
	public void publishEvent(final JSONObject event) {
		logger.info(" published event: " + event.toString());
		amiEventPublisher.publish(event);
	}

	
}
