package com.tinet.ctilink.ami.ordercallback;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.tinet.ctilink.model.AreaCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource(objectName = "Ctilink:type=OrderCallBackEngine,app=ami", description = "OrderCallBackEngine统计信息")
public class OrderCallBackEngine extends Thread {

	//TODO 不用存数据库了, 可能没用了, 改成推出去


	private static boolean terminated = false;
	private static Logger logger = LoggerFactory.getLogger(OrderCallBackEngine.class);
	private static LinkedBlockingQueue<OrderCallBack> logEvent = new LinkedBlockingQueue<OrderCallBack>();

	public void saveEvent(OrderCallBack order) {
		if (logEvent != null) {
			try {
				logEvent.put(order);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		logger.info("正在启动预约回呼处理引擎 ...");
		terminated = false;
		while (!terminated) {
			try {
				OrderCallBack order = (OrderCallBack) logEvent.poll(10, TimeUnit.SECONDS);
				if (order != null) {

//					if (StringUtils.isEmpty(order.getQueueName())) {
//						String queueName = queueService
//								.findTextBySql("select description from queue where enterprise_id="
//										+ order.getEnterpriseId() + " and qno='" + order.getQno() + "' limit 1");
//						order.setQueueName(queueName);
//					}
//					if (StringUtils.isEmpty(order.getArea())) {
//						AreaCode areaCode = areaCodeService.getAreaCodeByAreaCode(order.getAreaCode());
//						order.setArea(areaCode.getProvince() + "/" + areaCode.getCity());
//					}
//					orderCallBackService.saveOrUpdate(order);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Error accours when polling OrderCallBackEngine event!");
			}
		} // end while
		logger.error("OrderCallBackEngine engine terminated!");
	}

	public void shutDown() {
		logger.error("shuting down the OrderCallBackEngine engine!!!");
		terminated = true;
	}

	@ManagedAttribute(description = "队列任务数")
	public Integer getWaitCount() {
		return logEvent.size();
	}
}
