package com.tinet.ctilink.ami.event;

import java.util.HashMap;
import java.util.Map;

import com.tinet.ctilink.ami.util.AmiUtil;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.UserEvent;
import org.asteriskjava.manager.userevent.PressKeysEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.ami.inc.AmiEventConst;
import com.tinet.ctilink.inc.Const;

/**
 * 选择或收号节点按键
 * 
 * @author tianzp
 */
@Component
public class PressKeysEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {

	@Override
	public Class<?> getEventClass() {
		return PressKeysEvent.class;
	}

	@Override
	public void handle(ManagerEvent event) {
		String enterpriseId = ((PressKeysEvent) event).getEnterpriseId();
		String customerNumber = ((PressKeysEvent) event).getCustomerNumber();
		String ivrId = ((PressKeysEvent) event).getIvrId();
		String ivrNode = ((PressKeysEvent) event).getIvrNode();
		String time = ((PressKeysEvent) event).getTime();
		String userField = ((PressKeysEvent) event).getUserField();
		String keys = ((PressKeysEvent) event).getKeys();
		String callType = ((PressKeysEvent) event).getCallType();

		Map<String, String> userEvent = new HashMap<String, String>();
		userEvent.put(AmiAction.VARIABLE_NAME, AmiEventConst.PRESS_KEYS);
		userEvent.put(AmiAction.VARIABLE_ENTERPRISE_ID, enterpriseId);
		userEvent.put(AmiAction.VARIABLE_CUSTOMER_NUMBER, customerNumber);
		userEvent.put(AmiAction.VARIABLE_IVR_ID, ivrId);
		userEvent.put(AmiAction.VARIABLE_IVR_NODE, ivrNode);
		userEvent.put(AmiAction.VARIABLE_TIME, time);
		userEvent.put(AmiAction.VARIABLE_USER_FIELD, userField);
		userEvent.put(AmiAction.VARIABLE_KEYS, keys);
		userEvent.put(AmiAction.VARIABLE_CALL_TYPE, callType);

		// 根据企业设置推送Curl
		AmiUtil.pushCurl(((UserEvent) event).getAsteriskChannel(), userEvent, Integer.parseInt(enterpriseId),
				Const.ENTERPRISE_PUSH_TYPE_PRESS_KEY, Const.CURL_TYPE_PRESS_KEY);
	}

}
