package com.tinet.ctilink.ami.event.userevent;

import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.UserEvent;
import org.asteriskjava.manager.userevent.AgentCallTryingEvent;
import org.asteriskjava.manager.userevent.IncomingEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiUserEventHandler;
import com.tinet.ctilink.ami.inc.AmiChannelStatusConst;
import com.tinet.ctilink.ami.inc.AmiEventTypeConst;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.ami.util.AmiUtil;
import com.tinet.ctilink.cache.RedisService;
import com.tinet.ctilink.inc.Const;
import com.tinet.ctilink.json.JSONObject;

/**
 * 来电事件处理
 * 
 * @author tianzp
 */
@Component
public class AgentCallTryingEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {

	@Autowired
	private RedisService redisService;
	@Override
	public Class<?> getEventClass() {
		return AgentCallTryingEvent.class;
	}
	
	@Override
	public void handle(ManagerEvent event) {
		logger.info("handle {} : {}.", this.getEventClass().getSimpleName(), event);
		
		String enterpriseId = ((IncomingEvent) event).getEnterpriseId();
		String cno =  ((AgentCallTryingEvent) event).getCno();
		Map<String, String> userEvent = new HashMap<String, String>();
		userEvent.put(AmiParamConst.VARIABLE_EVENT,AmiEventTypeConst.STATUS);
		userEvent.put(AmiParamConst.VARIABLE_ENTERPRISE_ID, enterpriseId);		
		userEvent.put(AmiParamConst.CNO, cno);
		String channelState = ((Integer)AmiChannelStatusConst.TRYING).toString();
		userEvent.put(AmiParamConst.CHANNELSTATE, channelState);
		JSONObject pushEvent=new JSONObject();
		pushEvent.putAll(userEvent);
		publishEvent(pushEvent);
		
	}
}
