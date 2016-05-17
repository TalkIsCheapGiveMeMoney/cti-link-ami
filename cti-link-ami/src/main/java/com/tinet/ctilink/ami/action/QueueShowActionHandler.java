package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.inc.AmiParamConst;

/**
 * 刷新队列状态
 * 
 * @author tianzp
 */
@Component
public class QueueShowActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiParamConst.QUEUE_SHOW;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.info("handle {} action : {}", this.getAction(), params);

		String queue = params.get(AmiParamConst.VARIABLE_QUEUE);
		
		if(queue.isEmpty()){
			return ERROR_BAD_PARAM;
		}

		String command = "queue show " + queue;

		if (sendCommand(command, 6000) == null) {
			return ERROR_EXCEPTION;
		}
		return SUCCESS;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> fixParams, Map<String, String> optionalParams) {
		// TODO Auto-generated method stub
		return null;
	}

}
