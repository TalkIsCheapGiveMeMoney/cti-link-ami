package com.tinet.ctilink.ami.event.userevent;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.OrderCallBackEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiUserEventHandler;
import com.tinet.ctilink.ami.inc.AmiEventTypeConst;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.json.JSONObject;

/**
 * 预约回呼事件
 * 
 * @author tianzp
 */
@Component
public class OrderCallBackEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {


	@Override
	public Class<?> getEventClass() {
		return OrderCallBackEvent.class;
	}

	@Override
	public void handle(ManagerEvent event) {
		logger.info("handle {} : {}.", this.getEventClass().getSimpleName(), event);

		String enterpriseId = ((OrderCallBackEvent) event).getEnterpriseId();
		String customerNumber = ((OrderCallBackEvent) event).getCustomerNumber();
		String customerAreaCode = ((OrderCallBackEvent) event).getCustomerAreaCode();
		String qno = ((OrderCallBackEvent) event).getQno();
		String orderTime = ((OrderCallBackEvent) event).getOrderTime();
		
		JSONObject userEvent=new JSONObject();
		userEvent.put(AmiParamConst.EVENT,AmiEventTypeConst.ORDER_CALL_BACK);		
		userEvent.put(AmiParamConst.ENTERPRISE_ID, enterpriseId);
		userEvent.put(AmiParamConst.QNO, qno);
		userEvent.put(AmiParamConst.CUSTOMER_NUMBER, customerNumber);
		userEvent.put(AmiParamConst.CUSTOMER_AREA_CODE,customerAreaCode );
		userEvent.put(AmiParamConst.ORDER_TIME,orderTime );
		publishEvent(userEvent);

	}

}
