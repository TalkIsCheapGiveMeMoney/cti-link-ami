package com.tinet.ctilink.ami.ivrmonitor;

import java.util.Date;
import java.util.List;


/**
 * ivr状态查询接口
 * 
 * @author mucw
 *
 */
public interface IvrMonitorService {
	/**
	 * 每天凌晨清理监控数据
	 */
	public void clearMonitorData();
	/**
	 * 获取一个时间段内的IVR统计数据
	 * @param enterpriseId
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public List<IvrStat> getIvrStats(String enterpriseId, String ivrId, Date fromDate, Date toDate);
	/**
	 * 获取ivr中的通话
	 * @return
	 */
	public List<IvrEntry> getIvrEntry(String enterpriseId, String ivrId);
		
}
