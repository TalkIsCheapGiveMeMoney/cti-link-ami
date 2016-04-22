package com.tinet.ctilink.ami.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * AMI操作的实现
 * 
 * @author Jiangsl
 */
@Service
public class AmiActionServiceImpl implements AmiActionService {

	private static Logger logger = LoggerFactory.getLogger(AmiActionServiceImpl.class);

	private Map<String, AmiActionHandler> handlerMap;

	@Autowired
	private List<AmiActionHandler> handlers;

	/**
	 * 根据操作类型获取对应的处理器
	 * 
	 * @param action
	 * @return
	 */
	private AmiActionHandler getHandler(String action) {
		if (handlerMap == null) {
			handlerMap = new HashMap<String, AmiActionHandler>();

			for (AmiActionHandler handler : handlers) {
				handlerMap.put(handler.getAction(), handler);
			}
		}

		AmiActionHandler handler = handlerMap.get(action);

		if (handler == null) {
			logger.error("AmiActionHandler for Action: " + action + " not found.");
			throw new UnsupportedOperationException("AmiActionHandler for Action: " + action + " not found.");
		}

		return handler;
	}

	@Override
	public AmiActionResponse handleAction(String action, Map<String, String> params) {
		logger.debug("AmiAction : {}", action);

		return this.getHandler(action).handle(params);
	}
}
