package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.AmiAction;

/**
 * 刷新队列状态
 * 
 * @author tianzp
 */
@Component
public class QueueShowActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiAction.QUEUE_SHOW;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.info("handle {} action : {}", this.getAction(), params);

		String queue = params.get(AmiAction.VARIABLE_QUEUE);
		
		if(queue.isEmpty()){
			return ERROR_BAD_PARAM;
		}

		String command = "queue show " + queue;

		if (sendCommand(command, 6000) == null) {
			return ERROR_EXCEPTION;
		}
		return SUCCESS;
	}

}
