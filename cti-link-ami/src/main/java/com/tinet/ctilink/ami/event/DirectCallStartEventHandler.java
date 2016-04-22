package com.tinet.ctilink.ami.event;

import com.tinet.ctilink.inc.Const;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.DirectCallStartEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.online.CtiAgent;
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
		String cid = enterpriseId + cno;
		CtiAgent ctiAgent = null;
		
		// 保存在线座席
		RedisLock redisLock = RedisLockUtil.lock(CtiAgent.getLockKey(cid));
		if (redisLock != null) {
			try {
				ctiAgent = ctiAgentService.get(cid);

				if (ctiAgent != null) {
					ctiAgent.setCallType(Const.CDR_CALL_TYPE_DIRECT_OB);
					ctiAgent.setChannel(chan);
					ctiAgentService.set(ctiAgent);
				}
			} finally {
				RedisLockUtil.unLock(redisLock);
			}
		}
	}
}
