package com.tinet.ctilink.ami.log;

import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.asteriskjava.manager.userevent.QueueLogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

/**
 * Ami日志记录引擎
 *********************************************** 
 * @Pageage com.tinet.ccic.ami.logqueue
 * @author 罗尧 Email:j2ee.xiao@gmail.com
 * @since 1.0 创建时间 2012-3-31 下午2:43:19
 *********************************************** 
 */
@Component
@ManagedResource(objectName = "Ctilink:type=AmiLogQueueEngine,app=ami", description = "AmiLogQueueEngine统计信息")
public class AmiLogQueueEngine extends Thread {
	//TODO  日志记录，不存数据库，改成存kv

	private static Logger logger = LoggerFactory.getLogger(AmiLogQueueEngine.class);
	private static boolean terminated = false;
	private static LinkedBlockingQueue<LogQueue> logQueueEvent = new LinkedBlockingQueue<LogQueue>();

	/**
	 * 启动Ami日志引擎
	 */
	@Override
	public void run() {
		logger.info("正在启动Ami日志记录引擎 ...");
		terminated = false;
		while (!terminated) {
			try {
				LogQueue logQueue = (LogQueue) logQueueEvent.poll(10, TimeUnit.SECONDS);

				if (logQueue != null) {
					//logQueueService.saveOrUpdate(logQueue);

				} else {
					Thread.sleep(100);
				}
			} catch (Exception e) {
				logger.error("记录Ami日志时遇到错误", e);
			}
		}
	}

	/**
	 * 关闭Ami日志引擎
	 */
	public void shutdown() {
		logger.info("正在关闭Ami日志记录引擎 ...");
		terminated = true;
	}

	/**
	 * 保存到日志文件
	 * 
	 * @param queueLogEvent
	 */
	public void saveLogQueueLog(QueueLogEvent queueLogEvent) {
		if (logger.isDebugEnabled()) {

			LogQueue logQueue = new LogQueue();

			if (queueLogEvent.getCallId() == null) {
				logQueue.setUniqueId("");
			} else {
				logQueue.setUniqueId(queueLogEvent.getCallId());
			}

			if (queueLogEvent.getQueueName() == null) {
				logQueue.setQueue("");
			} else {
				logQueue.setQueue(queueLogEvent.getQueueName());
			}

			if (queueLogEvent.getAgent() == null) {
				logQueue.setAgent("");
			} else {
				logQueue.setAgent(queueLogEvent.getAgent());
			}

			if (queueLogEvent.getLogEvent() == null) {
				logQueue.setEvent("");
			} else {
				logQueue.setEvent(queueLogEvent.getLogEvent());
			}

			if (queueLogEvent.getData1() == null) {
				logQueue.setData1("");
			} else {
				logQueue.setData1(queueLogEvent.getData1());
			}

			if (queueLogEvent.getData2() == null) {
				logQueue.setData2("");
			} else {
				logQueue.setData2(queueLogEvent.getData2());
			}

			if (queueLogEvent.getData3() == null) {
				logQueue.setData3("");
			} else {
				logQueue.setData3(queueLogEvent.getData3());
			}

			if (queueLogEvent.getData4() == null) {
				logQueue.setData4("");
			} else {
				logQueue.setData4(queueLogEvent.getData4());
			}

			if (queueLogEvent.getData5() == null) {
				logQueue.setData5("");
			} else {
				logQueue.setData5(queueLogEvent.getData5());
			}

			logger.debug(logQueue.toString());
		}

	}

	/**
	 * 加入日志处理队列，同时记录到日志文件
	 * 
	 * @param uniqueId
	 * @param time 时间
	 * @param queue 队列号
	 * @param agent 座席号
	 * @param event 事件
	 * @param data1 数据1
	 * @param data2 数据2
	 * @param data3 数据3
	 * @param data4 数据4
	 * @param data5 数据5
	 */
	public void insertLogQueue(String uniqueId, Date time, String queue, String agent, String event, String data1, String data2, String data3, String data4, String data5) {

		if (uniqueId == null) {
			uniqueId = "";
		}
		if (queue == null) {
			queue = "";
		}
		if (agent == null) {
			agent = "";
		}
		if (data1 == null) {
			data1 = "";
		}
		if (data2 == null) {
			data2 = "";
		}
		if (data3 == null) {
			data3 = "";
		}
		if (data4 == null) {
			data4 = "";
		}
		if (data5 == null) {
			data5 = "";
		}

		LogQueue logQueue = new LogQueue();

		logQueue.setAgent(agent);
		logQueue.setEvent(event);
		logQueue.setUniqueId(uniqueId);
		logQueue.setTime(time);
		logQueue.setQueue(queue);
		logQueue.setData1(data1);
		logQueue.setData2(data2);
		logQueue.setData3(data3);
		logQueue.setData4(data4);
		logQueue.setData5(data5);

		try {
			logQueueEvent.put(logQueue);

			logger.debug(logQueue.toString());
		} catch (InterruptedException e) {
			logger.error("记录Ami日志时遇到错误", e);
		}
	}

	/**
	 * 加入日志处理队列，同时记录到日志文件
	 * 
	 * @param queueLogEvent
	 */
	public void insertLogQueue(QueueLogEvent queueLogEvent) {
		LogQueue logQueue = new LogQueue();

		if (queueLogEvent.getCallId() == null) {
			logQueue.setUniqueId("");
		} else {
			logQueue.setUniqueId(queueLogEvent.getCallId());
		}

		if (queueLogEvent.getQueueName() == null) {
			logQueue.setQueue("");
		} else {
			logQueue.setQueue(queueLogEvent.getQueueName());
		}

		if (queueLogEvent.getAgent() == null) {
			logQueue.setAgent("");
		} else {
			logQueue.setAgent(queueLogEvent.getAgent());
		}

		if (queueLogEvent.getLogEvent() == null) {
			logQueue.setEvent("");
		} else {
			logQueue.setEvent(queueLogEvent.getLogEvent());
		}

		if (queueLogEvent.getData1() == null) {
			logQueue.setData1("");
		} else {
			logQueue.setData1(queueLogEvent.getData1());
		}

		if (queueLogEvent.getData2() == null) {
			logQueue.setData2("");
		} else {
			logQueue.setData2(queueLogEvent.getData2());
		}

		if (queueLogEvent.getData3() == null) {
			logQueue.setData3("");
		} else {
			logQueue.setData3(queueLogEvent.getData3());
		}

		if (queueLogEvent.getData4() == null) {
			logQueue.setData4("");
		} else {
			logQueue.setData4(queueLogEvent.getData4());
		}

		if (queueLogEvent.getData5() == null) {
			logQueue.setData5("");
		} else {
			logQueue.setData5(queueLogEvent.getData5());
		}

		try {
			logQueueEvent.put(logQueue);

			logger.debug(logQueue.toString());
		} catch (InterruptedException e) {
			logger.error("记录Ami日志时遇到错误", e);
		}
	}

	@ManagedAttribute(description = "队列任务数")
	public int getWaitCount() {
		return logQueueEvent.size();
	}
}
