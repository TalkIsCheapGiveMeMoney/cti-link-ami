package com.tinet.ctilink.ami.webcall;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

/**
 * WebCall日志记录引擎
 * 
 * @author Jiangsl
 *
 */
@Component
@ManagedResource(objectName = "Ctilink:type=WebCallLogEngine,app=ami", description = "WebCallLogEngine统计信息")
public class WebCallLogEngine extends Thread {
	//TODO 日志  不存数据库，改成存kv

	private Logger logger = LoggerFactory.getLogger(getClass());
	private static boolean terminated = false;
	private static LinkedBlockingQueue<LogWebCall> logWebCallQueue = new LinkedBlockingQueue<LogWebCall>();


	@Override
	public void run() {
		logger.info("正在启动WebCall日志记录引擎 ...");
		terminated = false;
		while (!terminated) {
			try {
				LogWebCall logWebCall = (LogWebCall) logWebCallQueue.poll(10, TimeUnit.SECONDS);
				if (logWebCall != null) {
					//logWebCallService.saveOrUpdate(logWebCall);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Error accours when polling WebCallLog event!");
			}
		}
		logger.error("WebCallLog terminated!");
	}

	public void insertWebCallLog(WebCall webCall) {
		// 日志入库
		LogWebCall lwc = new LogWebCall();
		String paramNamesStr = "";
		String paramTypesStr = "";
		String paramsStr = "";
		String callbackParamsStr = "";
		Set<String> paramkeySet = webCall.getParams().keySet();// 获取map的key值的集合，set集合
		for (String key : paramkeySet) {// 遍历key
			paramNamesStr += key + ",";
			paramTypesStr += webCall.getParamTypes().get(key) + ",";
			try {
				paramsStr += key + "=" + URLEncoder.encode(webCall.getParams().get(key), "UTF8") + ",";
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		// 添加自定义回调参数
		Set<String> callbackParamkeySet = webCall.getCallbackParams().keySet();// 获取map的key值的集合，set集合
		for (String key : callbackParamkeySet) {// 遍历key
			try {
				callbackParamsStr += key + "=" + URLEncoder.encode(webCall.getCallbackParams().get(key), "UTF8") + ",";
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		if (paramNamesStr.endsWith(",")) {
			paramNamesStr = paramNamesStr.substring(0, paramNamesStr.length() - 1);
		}
		if (paramTypesStr.endsWith(",")) {
			paramTypesStr = paramTypesStr.substring(0, paramTypesStr.length() - 1);
		}
		if (paramsStr.endsWith(",")) {
			paramsStr = paramsStr.substring(0, paramsStr.length() - 1);
		}
		if (callbackParamsStr.endsWith(",")) {
			callbackParamsStr = callbackParamsStr.substring(0, callbackParamsStr.length() - 1);
		}

		lwc.setEnterpriseId(webCall.getEnterpriseId());
		lwc.setCallerIp(webCall.getCallerIp());
		lwc.setTel(webCall.getTel());
		lwc.setSubtel(webCall.getSubtel());
		lwc.setUniqueId(webCall.getUniqueId());
		lwc.setUserField(webCall.getUniqueId());
		lwc.setSync(webCall.getSync());
		lwc.setCallbackUrl(webCall.getCallbackUrl());
		lwc.setCallbackParams(callbackParamsStr);
		lwc.setIvrId(Integer.parseInt(webCall.getIvrId()));
		lwc.setClid(webCall.getClidNumber());
		lwc.setDelay(webCall.getDelay());
		lwc.setTimeout(webCall.getTimeout());
		lwc.setAmd(webCall.getAmd());
		lwc.setParamNames(paramNamesStr);
		lwc.setParamTypes(paramTypesStr);
		lwc.setParam(paramsStr);
		lwc.setResult(webCall.getResult());
		lwc.setCallStatus(webCall.getCallStatus());
		lwc.setRequestTime(webCall.getRequestTime());
		lwc.setConcurrency(webCall.getConcurrency());
		lwc.setTtsDuration(webCall.getTtsDuration());
		lwc.setStartTime(webCall.getStartTime());
		lwc.setCallTime(webCall.getCallTime());
		lwc.setAnswerTime(webCall.getAnswerTime());
		lwc.setEndTime(webCall.getAnswerTime());
		lwc.setCreateTime(new Date());

		if (logWebCallQueue != null) {
			try {
				logWebCallQueue.put(lwc);
			} catch (InterruptedException e) {
				logger.error("", e);
			}
		}

		logger.info(webCall.toString());
	}

	public void shutDown() {
		logger.info("正在关闭WebCall日志记录引擎 ...");
		terminated = true;
	}

	@ManagedAttribute(description = "队列任务数")
	public int getWaitCount() {
		return logWebCallQueue.size();
	}
}
