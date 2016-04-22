package com.tinet.ctilink.ami.queuemonitor;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 队列统计
 * 
 * @author Jiangsl
 *
 */
@SuppressWarnings("serial")
public class QueueStat implements Serializable {

	/**
	 * 队列名称
	 */
	private String queueName;

	/**
	 * 队列统计周期，根据其在一天中的时间，以10分钟为采样，值范围为0-143，计算公式为hour*6+minute%10
	 */
	private int period;

	/**
	 * 队列中呼入数
	 */
	private AtomicInteger incomings = new AtomicInteger(0);

	/**
	 * 队列中接通数
	 */
	private AtomicInteger completed = new AtomicInteger(0);

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	/**
	 * 递增呼入数
	 * 
	 * @return
	 */
	public int increaseIncomings() {
		return incomings.incrementAndGet();
	}

	/**
	 * 递增接通数
	 * 
	 * @return
	 */
	public int increaseCompleted() {
		return completed.incrementAndGet();
	}

	/**
	 * 获取呼入数
	 * 
	 * @return
	 */
	public int getIncomings() {
		return incomings.get();
	}

	/**
	 * 获取接通数
	 * 
	 * @return
	 */
	public int getCompleted() {
		return completed.get();
	}

	/**
	 * 获取接通率
	 * 
	 * @return
	 */
	public double getCompletedRate() {
		if (getIncomings() == 0) {
			return 0d;
		} else {
			return  (getCompleted() *100.0) / getIncomings();
		}
	}
}
