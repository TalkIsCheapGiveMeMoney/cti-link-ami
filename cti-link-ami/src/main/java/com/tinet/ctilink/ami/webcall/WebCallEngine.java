package com.tinet.ctilink.ami.webcall;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.tinet.ctilink.ami.AmiManager;
import com.tinet.ctilink.inc.Macro;
import com.tinet.ctilink.util.FileUtils;
import com.tinet.ctilink.util.SerializeUtil;
import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.manager.action.GetVarAction;
import org.asteriskjava.manager.response.GetVarResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;


/**
 * WebCall引擎
 * 
 * @author Jiangsl
 *
 */
@Component
@ManagedResource(objectName = "Ctilink:type=WebCallEngine,app=ami", description = "WebCallEngine统计信息")
public class WebCallEngine extends Thread implements DisposableBean {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private static boolean terminated = false;
	private static LinkedBlockingQueue<WebCall> webCallQueue = new LinkedBlockingQueue<WebCall>();
	private static Integer MAX_COUNT = 120;
	private static String unSendFile = "/var/log/clink/webcall.unsend";
	private ExecutorService executorService = Executors.newCachedThreadPool();

	@Autowired
	private WebCallExecutor webCallExecutor;

	@Autowired
	private AmiManager amiManager;

	@Override
	public void run() {
		logger.info("正在启动WebCall引擎 ...");
		terminated = false;

		try {
			init();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		while (!terminated) {
			try {
				WebCall webCall = webCallQueue.poll(10, TimeUnit.SECONDS);
				if (webCall != null) {
					if (webCall.getStartTime() != null
							&& (webCall.getDelay() > 0 && (new Date()).compareTo(webCall.getStartTime()) < 0)) {
						webCallQueue.put(webCall);
					} else {
						webCall.setStartTime(new Date());
						executorService.execute(new Runnable() {
							public void run() {
								webCallExecutor.execute(webCall);
							}
						});

						Thread.sleep(1000 / Macro.WEBCALL_CAPS);// caps=3
						SerializeUtil.writeObjectList(unSendFile, webCallQueue);
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Error accours when polling webCallQueue event!");
			}
		}
	}

	public synchronized boolean pushEvent(WebCall webCall) {
		if (webCallQueue.size() > MAX_COUNT) {
			return false;
		}
		// 查看是否超出并发限制
		int currentCall = 0;
		if (webCall.getInboundCallLimit() > 0) {
			WebCall[] list = (WebCall[]) webCallQueue.toArray(new WebCall[0]);
			for (WebCall w : list) {
				if (w.getEnterpriseId().equals(webCall.getEnterpriseId())) {
					currentCall++;
				}
			}
			// 请求asterisk看并发数多少，包括正在呼叫的和已经接通的
			GetVarAction action = new GetVarAction("GROUP_COUNT(" + webCall.getEnterpriseId() + "@enterprise)");
			try {
				GetVarResponse response = (GetVarResponse) amiManager.getManager().sendAction(action);
				if (StringUtils.isNotEmpty(response.getValue())) {
					currentCall += Integer.parseInt(response.getValue());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (currentCall >= webCall.getInboundCallLimit()) {
				return false;
			}
		}
		try {
			if (webCallQueue != null) {
				webCall.setConcurrency(currentCall);
				webCallQueue.put(webCall);
				SerializeUtil.writeObjectList(unSendFile, webCallQueue);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void init() throws UnsupportedEncodingException {
		LinkedBlockingQueue<WebCall> unSend = (LinkedBlockingQueue<WebCall>) SerializeUtil.readObjectList(unSendFile);
		if (unSend != null) {
			for (Object object : unSend) {
				try {
					webCallQueue.put((WebCall) object);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public synchronized void shutDown() {
		logger.info("正在关闭WebCall引擎 ...");
		FileUtils.rmFile(unSendFile);
		SerializeUtil.writeObjectList(unSendFile, webCallQueue);
		terminated = true;
	}

	@ManagedAttribute(description = "队列任务数（尚未进入线程池执行）")
	public int getWaitCount() {
		return webCallQueue.size();
	}

	@ManagedAttribute(description = "线程池的活动线程数")
	public int getActiveCount() {
		return ((ThreadPoolExecutor) executorService).getActiveCount();
	}

	@ManagedAttribute(description = "线程池的当前线程数")
	public int getPoolSize() {
		return ((ThreadPoolExecutor) executorService).getPoolSize();
	}

	@ManagedAttribute(description = "线程池曾经达到的线程数峰值")
	public int getLargestPoolSize() {
		return ((ThreadPoolExecutor) executorService).getLargestPoolSize();
	}
	
	@ManagedAttribute(description = "线程池已经执行完的任务数")
	public long getCompletedTaskCount() {
		return ((ThreadPoolExecutor) executorService).getCompletedTaskCount();
	}
}
