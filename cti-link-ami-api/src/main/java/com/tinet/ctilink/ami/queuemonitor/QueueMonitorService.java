package com.tinet.ctilink.ami.queuemonitor;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 队列状态查询接口
 * 
 * @author Jiangsl
 *
 */
public interface QueueMonitorService {

	/**
	 * 每天凌晨清理监控数据
	 */
	public void clearMonitorData();
	/**
	 * 获取一个队列的状态
	 * 
	 * @param queueName
	 * @return
	 */
	public QueueStatus getQueueStatus(String queueName);

	/**
	 * 获取多个队列的状态
	 * @param queueNames
	 * @return
	 */
	public List<QueueStatus> getQueueStatus(String... queueNames);

	/**
	 * 获取一个队列的排队
	 * @param ctiId
	 * @param queueName
	 * @return
	 */
	public List<QueueEntry> getQueueEntry(Integer ctiId, String queueName);
	
	/**
	 * 获取一个时间段内的队列统计数据
	 * @param queueName
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public List<QueueStat> getQueueStats(Integer ctiId, String queueName, Date fromDate, Date toDate);
	
	/**
	 * 获取队列等待channel数
	 * 
	 * @param queueName
	 */
	public Map<String,Date> getQueueWaitings(String queueName);
}
