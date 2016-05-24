package com.tinet.ctilink.ami.event.userevent;

import java.util.HashMap;
import java.util.Map;

import com.tinet.ctilink.ami.ivrmonitor.IvrMonitorServiceImp;
import com.tinet.ctilink.ami.util.AmiUtil;
import com.tinet.ctilink.cache.CacheKey;
import com.tinet.ctilink.cache.RedisService;
import com.tinet.ctilink.conf.model.EnterpriseSetting;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.UserEvent;
import org.asteriskjava.manager.userevent.IncomingEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiUserEventHandler;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.ami.inc.AmiEventTypeConst;
import com.tinet.ctilink.inc.Const;
import com.tinet.ctilink.inc.EnterpriseSettingConst;
import com.tinet.ctilink.json.JSONObject;

/**
 * 来电事件处理
 * 
 * @author tianzp
 */
@Component
public class IncomingEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {
	@Autowired
	private IvrMonitorServiceImp ivrMonitorService;
	@Autowired
	private RedisService redisService;
	@Override
	public Class<?> getEventClass() {
		return IncomingEvent.class;
	}
	
	@Override
	public void handle(ManagerEvent event) {
		logger.info("handle {} : {}.", this.getEventClass().getSimpleName(), event);
		
		String enterpriseId = ((IncomingEvent) event).getEnterpriseId();
		String callType = ((IncomingEvent) event).getCallType();
		String customerNumber = ((IncomingEvent) event).getCustomerNumber();
		String customerNumberType = ((IncomingEvent) event).getCustomerNumberType();
		String customerAreaCode = ((IncomingEvent) event).getCustomerAreaCode();
		String ivrId =  ((IncomingEvent) event).getIvrId();

		JSONObject pushEvent=new JSONObject();
		pushEvent.put(AmiParamConst.VARIABLE_EVENT,AmiEventTypeConst.INCOMING);
//		pushEvent.put(AmiParamConst.VARIABLE_NAME, AmiEventConst.INCOMING);
		pushEvent.put(AmiParamConst.VARIABLE_ENTERPRISE_ID, enterpriseId);
		pushEvent.put(AmiParamConst.VARIABLE_CALL_TYPE, callType);
		pushEvent.put(AmiParamConst.VARIABLE_CUSTOMER_NUMBER, customerNumber);
		pushEvent.put(AmiParamConst.VARIABLE_CUSTOMER_NUMBER_TYPE, customerNumberType);
		pushEvent.put(AmiParamConst.VARIABLE_CUSTOMER_AREA_CODE, customerAreaCode);

		publishEvent(pushEvent);

		// 根据企业设置推送Curl
//		AmiUtil.pushCurl(((UserEvent) event).getAsteriskChannel(), pushEvent, Integer.parseInt(enterpriseId), Const.ENTERPRISE_PUSH_TYPE_INCOMING_IB,
//				Const.CURL_TYPE_INCOMING);
		// ivr来电数统计
		if(callType != null && (callType.equals(String.valueOf(Const.CDR_CALL_TYPE_IB)) || callType.equals(String.valueOf(Const.CDR_CALL_TYPE_OB_WEBCALL)) || callType.equals(String.valueOf(Const.CDR_CALL_TYPE_PREDICTIVE_OB)))){
			EnterpriseSetting enterpriseSetting = redisService.get(Const.REDIS_DB_CONF_INDEX, String.format(CacheKey.ENTERPRISE_SETTING_ENTERPRISE_ID_NAME,
					Integer.parseInt(enterpriseId), EnterpriseSettingConst.ENTERPRISE_SETTING_NAME_IVR_OBSERVER), EnterpriseSetting.class);
			if(enterpriseSetting != null && enterpriseSetting.getValue() != null && enterpriseSetting.getValue().equals("1") ){
				ivrMonitorService.setIvrIncomings(enterpriseId, ivrId, event.getDateReceived(),((IncomingEvent)event).getEnterpriseIdIvrIdCount());
			}
		}
	}
}
