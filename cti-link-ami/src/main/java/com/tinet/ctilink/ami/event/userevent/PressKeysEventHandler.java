package com.tinet.ctilink.ami.event.userevent;

import java.util.HashMap;
import java.util.Map;

import com.tinet.ctilink.ami.util.AmiUtil;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.UserEvent;
import org.asteriskjava.manager.userevent.PressKeysEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiUserEventHandler;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.ami.inc.AmiEventTypeConst;
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
		String keys = ((PressKeysEvent) event).getKeys();
		String callType = ((PressKeysEvent) event).getCallType();

		Map<String, String> userEvent = new HashMap<String, String>();
		userEvent.put(AmiParamConst.EVENT, AmiEventTypeConst.PRESS_KEYS);
		userEvent.put(AmiParamConst.ENTERPRISE_ID, enterpriseId);
		userEvent.put(AmiParamConst.CUSTOMER_NUMBER, customerNumber);
		userEvent.put(AmiParamConst.IVR_ID, ivrId);
		userEvent.put(AmiParamConst.IVR_NODE, ivrNode);
		userEvent.put(AmiParamConst.TIME, time);
		userEvent.put(AmiParamConst.KEYS, keys);
		userEvent.put(AmiParamConst.CALL_TYPE, callType);

		// 根据企业设置推送Curl
		AmiUtil.pushCurl(((UserEvent) event).getAsteriskChannel(), userEvent, Integer.parseInt(enterpriseId),
				Const.ENTERPRISE_PUSH_TYPE_PRESS_KEY, Const.CURL_TYPE_PRESS_KEY);
	}

}
