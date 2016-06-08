package com.tinet.ctilink.ami.event;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.inc.AmiEventTypeConst;
import com.tinet.ctilink.cache.RedisService;
import com.tinet.ctilink.inc.Const;
import com.tinet.ctilink.json.JSONObject;


/**
 * AMI事件推送器
 * 
 * @author Jiangsl
 *
 * @author Tianzp
 * 
 */
@Component
public class AmiEventPublisher {
	@Autowired
	private RedisService redisService;
	
	public void publish(JSONObject event) {
		redisService.lpush(Const.REDIS_DB_CTI_INDEX, AmiEventTypeConst.AMI_EVENT_LIST, event.toString());
	}

}
