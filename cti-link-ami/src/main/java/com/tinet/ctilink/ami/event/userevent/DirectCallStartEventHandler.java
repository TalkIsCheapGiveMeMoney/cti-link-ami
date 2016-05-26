package com.tinet.ctilink.ami.event.userevent;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiUserEventHandler;
import com.tinet.ctilink.ami.inc.AmiEventTypeConst;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.inc.Const;
import com.tinet.ctilink.json.JSONObject;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.DirectCallStartEvent;
import org.springframework.stereotype.Component;


import com.tinet.ctilink.util.RedisLock;
import com.tinet.ctilink.util.RedisLockUtil;

/**
 * 直接外呼开始事件
 * 
 * @author tianzp
 */
@Component
public class DirectCallStartEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {

	@Override
	public Class<?> getEventClass() {
		return DirectCallStartEvent.class;
	}
	
	@Override
	public void handle(ManagerEvent event) {
		logger.info("handle {} : {}.", this.getEventClass().getSimpleName(), event);
		
		String enterpriseId = ((DirectCallStartEvent) event).getEnterpriseId();
		String cno = ((DirectCallStartEvent) event).getCno();
		String chan = ((DirectCallStartEvent) event).getChannel();
		String exten = ((DirectCallStartEvent) event).getExten();
		String cid = enterpriseId + cno;
		
		JSONObject userEvent=new JSONObject();
		userEvent.put(AmiParamConst.VARIABLE_EVENT,AmiEventTypeConst.DIRECT_CALL_START);
		userEvent.put(AmiParamConst.VARIABLE_ENTERPRISE_ID, enterpriseId);
		userEvent.put(AmiParamConst.VARIABLE_CNO, cno);
		userEvent.put(AmiParamConst.EXTENSION, exten);
		
		publishEvent(userEvent);

	}
}
