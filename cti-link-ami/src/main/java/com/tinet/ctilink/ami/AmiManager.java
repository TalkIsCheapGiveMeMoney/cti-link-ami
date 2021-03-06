package com.tinet.ctilink.ami;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import org.asteriskjava.manager.ManagerConnectionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.inc.AmiConfOption;
import com.tinet.ctilink.json.JSONObject;

/**
 * AMI Server 管理，获取AMI Server 状态、启动与停止。 Copyright (c) 2006-2010 T&I Net
 * Communication CO.,LTD. All rights reserved.
 * 
 * @author 安静波
 * @since 1.0
 * @version 1.0
 */
@Component
@ManagedResource(objectName = "Ctilink:type=AmiManager,app=ami", description = "AmiManager统计信息")
public class AmiManager {
	private static Logger logger = LoggerFactory.getLogger(AmiManager.class);

	private static AmiListener amiListener;

	/**
	 * 远程执行控制AMI server 命令
	 *
	 * @param cmd 命令类型
	 * @return 命令执行结果
	 */
	public String command(String cmd) {
		if (cmd.equalsIgnoreCase("status")) {
			return status();
		} else if (cmd.equalsIgnoreCase("start")) {
			start();
			return "1";
		} else if (cmd.equalsIgnoreCase("stop")) {
			stop();
			return "1";
		} else if (cmd.equalsIgnoreCase("restart")) {
			stop();
			start();
			return "1";
		} else {
			return "0";
		}
	}

	/**
	 * 根据id返回相应的AMI监听服务器
	 *
	 * @return 返回AMI 实体
	 */
	public AmiListener getManager() {
		return amiListener;
	}

	/**
	 * 启动AMI模块，在cti表中查询active=1的数据，然后传递参数构建AmiListener对象
	 */
	private String start() {
		logger.info("正在启动AmiListenerManager");
		amiListener = new AmiListener(AmiConfOption.ASTERISK_AMI_HOST, AmiConfOption.ASTERISK_AMI_PORT, AmiConfOption.ASTERISK_AMI_MANAGER_USER,
				AmiConfOption.ASTERISK_AMI_MANAGER_PWD, AmiConfOption.ASTERISK_AMI_ACTION_USER, AmiConfOption.ASTERISK_AMI_ACTION_PWD);
		try {
			amiListener.start();
		} catch (Exception e) {
			logger.error("failed to start AmiListener,", e);
		}

		return "1";
	}

	private String stop() {
		if (amiListener != null) {
			logger.debug(amiListener.getAsteriskServer().getManagerConnection().getHostname() + ":stop!");
			try {
				amiListener.stop();
			} catch (Exception e) {
				logger.error("failed to stop AmiServer, ", e);
			}
		}
		return "1";
	}

	public static String status(){
		JSONObject object = new JSONObject();

		if(amiListener != null){
			object.put("ctiIp", AmiConfOption.ASTERISK_AMI_HOST);
			String status;
			if(amiListener.getAsteriskServer().getManagerConnection().getState().equals(ManagerConnectionState.CONNECTED)){
				status = "1";
			}else{
				status = "0";
			}
			object.put("status", status);
			object.put("avalibleConnectionCount", amiListener.getAvalibleConnectionCount());
			object.put("channels", amiListener.getAsteriskServer().getChannels().size());
			object.put("dedicateConnectionCount", amiListener.getDedicateConnectionCount());
		}else{
			object.put("ctiIp", AmiConfOption.ASTERISK_AMI_HOST);
			object.put("status", "0");
		}

		return object.toString();
	}

	@ManagedAttribute(description = "通道数")
	public int getChannelCount() {
		int sum = 0;
		if (amiListener != null) {
			sum = amiListener.getAsteriskServer().getChannels().size();
		}
		return sum;
	}

	@ManagedAttribute(description = "有效连接数")
	public int getAvalibleConnectionCount() {
		int sum = 0;
		if (amiListener != null) {
			sum = amiListener.getAvalibleConnectionCount();
		}
		return sum;
	}

	@ManagedAttribute(description = "专用连接数")
	public int getDedicateConnectionCount() {
		int sum = 0;
		if (amiListener != null) {
			sum = amiListener.getDedicateConnectionCount();
		}
		return sum;
	}
}
