package com.tinet.ctilink.ami.wrapup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.asteriskjava.manager.action.QueuePauseAction;
import org.asteriskjava.manager.response.ManagerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.AmiManager;
import com.tinet.ctilink.util.DateUtil;

/**
 * 座席状态整理引擎 Copyright (c) 2006-2010 T&I Net Communication CO.,LTD. All rights
 * reserved.
 * 
 * @author 安静波
 * @since 1.0
 * @version 1.0
 * 
 * @author tianzp
 * @since 4.0
 * @version 4.0 2015-10-27 14:28:00
 */
@Component
@ManagedResource(objectName = "Ctilink:type=AmiWrapupEngine,app=ami", description = "AmiWrapupEngine统计信息")
public class AmiWrapupEngine extends Thread {
	private Logger logger = LoggerFactory.getLogger(AmiWrapupEngine.class);
	private static boolean terminated = false;

	// 正在整理的座席队列
	private List<AmiWrapup> events = new ArrayList<AmiWrapup>();

	// 结束整理，需要释放的座席队列
	private List<AmiWrapup> wrapupEndEvents = new ArrayList<AmiWrapup>();

	@Autowired
	private AmiManager amiManager;

	/**
	 * 启动座席状态整理引擎
	 */
	@Override
	public void run() {
		logger.info("正在启动座席状态整理引擎 ...");
		terminated = false;
		while (!terminated) {
			try {
				wrapupEndEvents.clear();

				synchronized (events) {
					for (AmiWrapup wrapupEvent : events) {
						if (DateUtil.diffSecond(wrapupEvent.getWrapupStart(), new Date()) >= wrapupEvent.getWrapup()) {
							wrapupEndEvents.add(wrapupEvent);
						}
					}
				}

				if (wrapupEndEvents.size() > 0) {
					logger.debug("当前整理队列中全部个数：" + events.size() + "；当前整理队列中需要释放的个数：" + wrapupEndEvents.size());

					for (AmiWrapup wrapupEvent : wrapupEndEvents) {
						QueuePauseAction queuePauseAction = new QueuePauseAction(wrapupEvent.getLocation(), false,
								"pauseWrapup");

						try {
							// 尝试3次
							for (int retry = 0; retry < 3; retry++) {
								ManagerResponse res = amiManager.getManager().sendAction(queuePauseAction);

								if(res != null && "Success".equals(res.getResponse())){
									logger.debug("释放整理座席：" + wrapupEvent.toString());									
									break;
								}
								logger.error("尝试"+retry+"次释放整理座席失败：" + wrapupEvent.toString());
							}
							removeWrapup(wrapupEvent.getLocation());
						} catch (Exception e) {
							e.printStackTrace();
							logger.error("释放整理座席时遇到错误，被强制移除:"+wrapupEvent.toString()+":"+ e);
							removeWrapup(wrapupEvent.getLocation());
						}
					}
				}

				Thread.sleep(100);
			} catch (Exception e) {
				logger.error("释放整理座席时遇到错误:"+ e);
			}
		}
	}

	/**
	 * 关闭座席状态整理引擎
	 */
	public void shutdown() {
		logger.info("正在关闭座席状态整理引擎 ...");
		terminated = true;
	}

	/**
	 * 加入座席整理队列
	 * 
	 * @param wrapupEvent
	 */
	public void pushWrapupEvent(AmiWrapup wrapupEvent) {
		synchronized (events) {
			events.add(wrapupEvent);
			logger.debug("加入整理队列： " + wrapupEvent.toString() + "；队列排队数：" + events.size());
		}
	}

	/**
	 * 从座席整理队列中移除
	 * 
	 * @param location
	 */
	public void removeWrapup(String location) {
		List<AmiWrapup> removeEvents = new ArrayList<AmiWrapup>();

		synchronized (events) {
			for (AmiWrapup wrapupEvent : events) {
				if (wrapupEvent.getLocation().equals(location)) {
					removeEvents.add(wrapupEvent);
				}
			}

			if (removeEvents.size() > 0) {
				events.removeAll(removeEvents);
				logger.debug("移除整理：" + location + "；个数:" + removeEvents.size());
			}
		}
	}

	/**
	 * 获取座席整理队列中的剩余数
	 * 
	 * @param location
	 * @return
	 */
	public int getMemberWrapupLeft(String location) {
		synchronized (events) {
			for (AmiWrapup wrapupEvent : events) {
				if (wrapupEvent.getLocation().equals(location)) {
					int left = wrapupEvent.getWrapup() - DateUtil.diffSecond(wrapupEvent.getWrapupStart(), new Date());
					if (left > 0) {
						return left;
					} else {
						return 0;
					}
				}
			}
		}
		return 0;
	}

	@ManagedAttribute(description = "座席整理排队数")
	public Integer getWaitCount() {
		return events.size();
	}
}
