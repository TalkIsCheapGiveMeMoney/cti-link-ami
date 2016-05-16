package com.tinet.ctilink.ami.event.userevent;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.tinet.ctilink.ami.ordercallback.OrderCallBack;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.OrderCallBackEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiUserEventHandler;
import com.tinet.ctilink.ami.inc.AmiEventConst;
import com.tinet.ctilink.ami.ordercallback.OrderCallBackEngine;

/**
 * 预约回呼事件
 * 
 * @author tianzp
 */
@Component
public class OrderCallBackEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {

	@Autowired
	private OrderCallBackEngine orderCallBackEngine;

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
		String queueName = ((OrderCallBackEvent) event).getQueueName();
		String orderTime = ((OrderCallBackEvent) event).getOrderTime();

		OrderCallBack orderCallBack = new OrderCallBack();
		orderCallBack.setEnterpriseId(Integer.parseInt(enterpriseId));
		orderCallBack.setQno(queueName.substring(7));
		orderCallBack.setTel(customerNumber);
		orderCallBack.setIsCall(0);
		orderCallBack.setOrderTime(new Date(Long.valueOf(orderTime) * 1000));
		orderCallBack.setCreateTime(new Date());
		orderCallBack.setAreaCode(customerAreaCode);
		orderCallBackEngine.saveEvent(orderCallBack);

		Map<String, String> userEvent = new HashMap<String, String>();
		userEvent.put("type", AmiAction.VARIABLE_EVENT);
		userEvent.put(AmiAction.VARIABLE_NAME, AmiEventConst.ORDER_CALL_BACK);
		userEvent.put(AmiAction.VARIABLE_ADD_OR_REDUCE, "1");
		userEvent.put(AmiAction.VARIABLE_ENTERPRISE_ID, enterpriseId);
		userEvent.put(AmiAction.VARIABLE_QID, queueName);
		userEvent.put(AmiAction.VARIABLE_AGENT_QUEUE, queueName);
		publishEvent(userEvent);

	}

}
