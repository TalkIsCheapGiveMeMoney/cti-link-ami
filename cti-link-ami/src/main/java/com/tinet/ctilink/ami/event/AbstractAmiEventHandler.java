package com.tinet.ctilink.ami.event;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tinet.ctilink.json.JSONObject;


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

	/**
	 * 推送AMI事件
	 * 
	 * @param event
	 */
	
	public void publishEvent(final JSONObject event) {
		logger.info(" published event: " + event.toString());
		amiEventPublisher.publish(event);
	}

	
}
