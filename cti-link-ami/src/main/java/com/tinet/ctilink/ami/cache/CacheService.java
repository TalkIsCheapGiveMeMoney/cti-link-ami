package com.tinet.ctilink.ami.cache;

import java.util.HashMap;
import java.util.Map;

import com.tinet.ctilink.ami.AmiAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * 本地缓存
 * 
 * @author tianzp
 */
@Component
public class CacheService {
	private final Logger logger = LoggerFactory.getLogger(CacheService.class);

	private static final Map<String, Map<String, String>> queueCallMap = new HashMap<String, Map<String, String>>();

	/**
	 * 获取来电呼叫状态
	 * @param uniqueId
	 */
	public Map<String, String> getQueueCall(String uniqueId) {
		return queueCallMap.get(uniqueId);
	}
	
	/**
	 * 存储来电呼叫状态
	 * @param map
	 */
	public void setQueueCall(HashMap<String, String> map) {
		String uniqueId = map.get(AmiAction.VARIABLE_UNIQUEID);
		queueCallMap.put(uniqueId, map);
	}
	
	/**
	 * 移除来电呼叫状态
	 * @param uniqueId
	 */
	public void removeQueueCall(String uniqueId) {
		queueCallMap.remove(uniqueId);
	}
}
