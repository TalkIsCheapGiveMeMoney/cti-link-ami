package com.tinet.ctilink.ami;

import java.util.concurrent.atomic.AtomicInteger;

import org.asteriskjava.live.AsteriskServer;
import org.asteriskjava.live.DefaultAsteriskServer;
import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.live.internal.ManagerConnectionPool;
import org.asteriskjava.manager.DefaultManagerConnection;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ResponseEvents;
import org.asteriskjava.manager.action.EventGeneratingAction;
import org.asteriskjava.manager.action.ManagerAction;
import org.asteriskjava.manager.response.ManagerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AMI Server 实现类，底层与前台之间沟通桥梁
 * <p>
 * 文件名： AmiManager.java
 * <p>
 * Copyright (c) 2006-2010 T&I Net Communication CO.,LTD. All rights reserved.
 * 
 * @author 安静波
 * @since 1.0
 * @version 1.0
 */
public class AmiListener {
	private final Logger logger = LoggerFactory.getLogger(AmiListener.class.getName());
	private AsteriskServer asteriskServer;
	/**
	 * A pool of manager connections to use for sending actions to Asterisk.
	 */
	private ManagerConnectionPool actionConnectionPool;
	private int actionConnectionPoolCount = 20;// 单服务器调整成20；HA时调整为10
	private AtomicInteger dedicateConnectionCount = new AtomicInteger(0);

	private String host;
	private String managerUser;
	private int port;
	private String managerPwd;
	private String actionUser;
	private String actionPwd;

	public AsteriskServer getAsteriskServer() {
		return asteriskServer;
	}

	public ManagerResponse sendAction(ManagerAction action) throws Exception {
		if (getAvalibleConnectionCount() > 0) {
			return sendActionPool(action);
		} else {
			logger.error("已经使用独立的ami connection，请注意。。。。。。");
			return sendActionNew(action);
		}
	}
	
	public ManagerResponse sendAction(ManagerAction action, long timeout) throws Exception {
		if (getAvalibleConnectionCount() > 0) {
			return sendActionPool(action, timeout);
		} else {
			logger.error("已经使用独立的ami connection，请注意。。。。。。");
			return sendActionNew(action, timeout);
		}
	}

	public ResponseEvents sendEventGenerateAction(EventGeneratingAction action) throws Exception {
		if (getAvalibleConnectionCount() > 0) {
			return sendActionPool(action);
		} else {
			logger.error("已经使用独立的ami connection，请注意。。。。。。");
			return sendActionNew(action);
		}
	}

	public ResponseEvents sendEventGenerateAction(EventGeneratingAction action, long timeout) throws Exception {
		if (getAvalibleConnectionCount() > 0) {
			return sendActionPool(action, timeout);
		} else {
			logger.error("已经使用独立的ami connection，请注意。。。。。。");
			return sendActionNew(action, timeout);
		}
	}

	private ManagerResponse sendActionPool(ManagerAction action) throws Exception {
		return this.actionConnectionPool.sendAction(action);
	}

	private ManagerResponse sendActionPool(ManagerAction action, long timeout) throws Exception {
		return this.actionConnectionPool.sendAction(action, timeout);
	}
	
	private ResponseEvents sendActionPool(EventGeneratingAction action) throws Exception {
		return this.actionConnectionPool.sendEventGeneratingAction(action);
	}

	private ResponseEvents sendActionPool(EventGeneratingAction action, long timeout) throws Exception {
		return this.actionConnectionPool.sendEventGeneratingAction(action, timeout);
	}

	private ManagerResponse sendActionNew(ManagerAction action) throws Exception {
		ManagerConnection connection = new DefaultManagerConnection(host, port, actionUser, actionPwd);
		ManagerResponse res = null;
		try {
			dedicateConnectionCount.incrementAndGet();
			connection.login();
			res = connection.sendAction(action);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dedicateConnectionCount.decrementAndGet();
			connection.logoff();
		}
		return res;
	}
	
	private ManagerResponse sendActionNew(ManagerAction action, long timeout) throws Exception {
		ManagerConnection connection = new DefaultManagerConnection(host, port, actionUser, actionPwd);
		ManagerResponse res = null;
		try {
			dedicateConnectionCount.incrementAndGet();
			connection.login();
			res = connection.sendAction(action, timeout);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dedicateConnectionCount.decrementAndGet();
			connection.logoff();
		}
		return res;
	}

	private ResponseEvents sendActionNew(EventGeneratingAction action) throws Exception {
		ManagerConnection connection = new DefaultManagerConnection(host, port, actionUser, actionPwd);
		ResponseEvents res = null;
		try {
			dedicateConnectionCount.incrementAndGet();
			connection.login();
			res = connection.sendEventGeneratingAction(action);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dedicateConnectionCount.decrementAndGet();
			connection.logoff();
		}
		return res;
	}

	private ResponseEvents sendActionNew(EventGeneratingAction action, long timeout) throws Exception {
		ManagerConnection connection = new DefaultManagerConnection(host, port, actionUser, actionPwd);
		ResponseEvents res = null;
		try {
			dedicateConnectionCount.incrementAndGet();
			connection.login();
			res = connection.sendEventGeneratingAction(action, timeout);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dedicateConnectionCount.decrementAndGet();
			connection.logoff();
		}
		return res;
	}

	public AmiListener(String host, int port, String managerUser, String managerPwd, String actionUser, String actionPwd) {
		this.host = host;
		this.port = port;
		this.managerUser = managerUser;
		this.managerPwd = managerPwd;
		this.actionUser = actionUser;
		this.actionPwd = actionPwd;
		this.actionConnectionPool = new ManagerConnectionPool(actionConnectionPoolCount);
	}

	public Integer getAvalibleConnectionCount() {
		return actionConnectionPool.getConnectionCount();
	}

	public Integer getDedicateConnectionCount() {
		return dedicateConnectionCount.get();
	}

	public String getHost() {
		return this.host;
	}

	public synchronized void start() throws ManagerCommunicationException {
		logger.info("AmiListener:" + host + ":start!");
		/**
		 * 通过AmiListener构造的对象拿到相应参数，构造一个asteriskServer对象，然后initialize这个对象
		 */
		asteriskServer = new DefaultAsteriskServer(host, port, managerUser, managerPwd);
		
		actionConnectionPool.clear();
		for (int i = 0; i < actionConnectionPoolCount; i++) {
			ManagerConnection connection = new DefaultManagerConnection(host, port, actionUser, actionPwd);
			actionConnectionPool.add(connection);
		}

		asteriskServer.initialize();
		asteriskServer.setActionConnectionPool(actionConnectionPool);

	}

	public synchronized void stop() {
		actionConnectionPool.close();
		asteriskServer.shutdown();
	}

}
