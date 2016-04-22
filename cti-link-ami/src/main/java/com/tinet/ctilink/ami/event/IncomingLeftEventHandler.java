package com.tinet.ctilink.ami.event;


import com.tinet.ctilink.ami.cache.CacheService;
import com.tinet.ctilink.ami.ivrmonitor.IvrMonitorServiceImp;
import com.tinet.ctilink.cache.CacheKey;
import com.tinet.ctilink.cache.RedisService;
import com.tinet.ctilink.model.EnterpriseSetting;
import com.tinet.ctilink.util.ContextUtil;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.IncomingLeftEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.inc.Const;

/**
 * 来电结束事件处理
 * 
 * @author mucw
 */
@Component
public class IncomingLeftEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {
	@Autowired
	private IvrMonitorServiceImp ivrMonitorService;
	@Autowired
	private CacheService cacheService;
	@Override
	public Class<?> getEventClass() {
		return IncomingLeftEvent.class;
	}
	
	@Override
	public void handle(ManagerEvent event) {
		logger.info("handle {} : {}.", this.getEventClass().getSimpleName(), event);
		
		String enterpriseId = ((IncomingLeftEvent) event).getEnterpriseId();
		String ivrId =  ((IncomingLeftEvent) event).getIvrId();
		String callType = ((IncomingLeftEvent) event).getCallType();
		// ivr来电数统计
		if(callType != null && (callType.equals(String.valueOf(Const.CDR_CALL_TYPE_IB)) || callType.equals(String.valueOf(Const.CDR_CALL_TYPE_OB_WEBCALL)) || callType.equals(String.valueOf(Const.CDR_CALL_TYPE_PREDICTIVE_OB)))){
			EnterpriseSetting enterpriseSetting = ContextUtil.getBean(RedisService.class).get(String.format(CacheKey.ENTERPRISE_SETTING_ENTERPRISE_ID_NAME,
					Integer.parseInt(enterpriseId), Const.ENTERPRISE_SETTING_NAME_IVR_OBSERVER), EnterpriseSetting.class);
			if(enterpriseSetting != null && enterpriseSetting.getValue() != null && enterpriseSetting.getValue().equals("1") ){
				ivrMonitorService.setIvrIncomings(enterpriseId, ivrId, event.getDateReceived(),((IncomingLeftEvent)event).getEnterpriseIdIvrIdCount());
			}
		}
	}
}
