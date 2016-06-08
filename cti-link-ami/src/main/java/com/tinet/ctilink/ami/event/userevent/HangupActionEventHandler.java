package com.tinet.ctilink.ami.event.userevent;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tinet.ctilink.ami.util.AmiUtil;
import com.tinet.ctilink.cache.CacheKey;
import com.tinet.ctilink.cache.RedisService;
import com.tinet.ctilink.conf.model.EnterprisePushAction;
import com.tinet.ctilink.inc.Const;
import com.tinet.ctilink.json.JSONObject;
import com.tinet.ctilink.util.ContextUtil;

import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.UserEvent;
import org.asteriskjava.manager.userevent.HangupActionEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiUserEventHandler;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.ami.inc.AmiEventTypeConst;


/**
 *
 * @author tianzp
 */
@Component
public class HangupActionEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {

	@Override
	public Class<?> getEventClass() {
		return HangupActionEvent.class;
	}
	
	@Override
	public void handle(ManagerEvent event) {
		logger.debug("handle {} : {}", getEventClass().getTypeName(), event);

		String enterpriseId = ((HangupActionEvent) event).getEnterpriseId();
		String actionParams = ((HangupActionEvent) event).getActionParams();
		String actionId = ((HangupActionEvent) event).getActionId();
		String callType = ((HangupActionEvent) event).getCallType();

		
		Map<String, String> userEvent = new HashMap<String, String>();
		userEvent.put(AmiParamConst.ENTERPRISE_ID, enterpriseId);
		userEvent.put(AmiParamConst.CALL_TYPE, callType);
		
		if(StringUtils.isNotEmpty(actionParams)){
			String[] params = actionParams.split("&");
			for(int i=0; i< params.length; i++){
				if(StringUtils.isNotEmpty(params[i])){
					String nameValue[] = params[i].split("=");
					if(nameValue.length == 2){
						try {
							userEvent.put(nameValue[0], URLDecoder.decode(nameValue[1], "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		

		// 根据企业设置推送Curl
		int curlType;
		if(callType.equals(String.valueOf(Const.CDR_CALL_TYPE_IB))){
			curlType = Const.CURL_TYPE_HANGUP_IB;
		}else{
			curlType = Const.CURL_TYPE_HANGUP_OB;
		}
		EnterprisePushAction pushAction = ContextUtil.getBean(RedisService.class).get(Const.REDIS_DB_CONF_INDEX
				, String.format(CacheKey.ENTERPRISE_PUSH_ACTION_ID, enterpriseId, Integer.parseInt(actionId)), EnterprisePushAction.class);
		if(pushAction != null){
			AmiUtil.pushOne(pushAction, null, userEvent, Integer.parseInt(enterpriseId), curlType);
		}
	}

	

}
