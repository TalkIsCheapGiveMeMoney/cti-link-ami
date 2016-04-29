package com.tinet.ctilink;

import com.tinet.ctilink.cache.CacheKey;
import com.tinet.ctilink.cache.RedisService;
import com.tinet.ctilink.inc.Const;
import com.tinet.ctilink.inc.Macro;
import com.tinet.ctilink.conf.model.SystemSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.AmiManager;
import com.tinet.ctilink.ami.log.AmiLogQueueEngine;
import com.tinet.ctilink.ami.ordercallback.OrderCallBackEngine;
import com.tinet.ctilink.ami.webcall.WebCallEngine;
import com.tinet.ctilink.ami.webcall.WebCallLogEngine;
import com.tinet.ctilink.ami.wrapup.AmiWrapupEngine;

import java.util.List;

/**
 * 应用程序启动器
 * 
 * @author Jiangsl
 *
 */
@Component
public class ApplicationStarter implements ApplicationListener<ContextRefreshedEvent> {
	private static Logger logger = LoggerFactory.getLogger(ApplicationStarter.class);

	@Autowired
	private AmiManager amiManager;

	@Autowired
	private AmiWrapupEngine amiWrapupEngine;

	@Autowired
	private OrderCallBackEngine orderCallBackEngine;

	@Autowired
	private AmiLogQueueEngine amiLogQueueEngine;

	@Autowired
	private WebCallEngine webCallEngine;

	@Autowired
	private WebCallLogEngine webCallLogEngine;

	@Autowired
	private RedisService redisService;

	@Override
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		
		// 设置JVM的DNS缓存时间
		// http://docs.amazonaws.cn/AWSSdkDocsJava/latest/DeveloperGuide/java-dg-jvm-ttl.html
		java.security.Security.setProperty("networkaddress.cache.ttl", "60");

		List<SystemSetting> systemSettings = redisService.getList(Const.REDIS_DB_CONF_INDEX, CacheKey.SYSTEM_SETTING, SystemSetting.class);
		Macro.loadSystemSettings(systemSettings);

		// 启动与Asterisk的AMI连接
		amiManager.command("start");

		// 启动座席状态整理引擎
		amiWrapupEngine.setName("AmiWrapupEngine");
		amiWrapupEngine.start();

		// 启动预约回呼处理引擎
		orderCallBackEngine.setName("OrderCallBackEngine");
		orderCallBackEngine.start();

		// 启动Ami日志记录引擎
		amiLogQueueEngine.setName("AmiLogQueueEngine");
		amiLogQueueEngine.start();

		// 启动WebCall引擎
		webCallEngine.setName("WebCallEngine");
		webCallEngine.start();

		// 启动WebCall日志记录引擎
		webCallLogEngine.setName("WebCallLogEngine");
		webCallLogEngine.start();

		logger.info("cti-link-ami启动成功");
		System.out.println("cti-link-ami启动成功");
	}
}