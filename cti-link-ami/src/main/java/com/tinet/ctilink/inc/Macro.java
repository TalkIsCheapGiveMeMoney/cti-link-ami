package com.tinet.ctilink.inc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.tinet.ctilink.conf.model.SystemSetting;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;


public class Macro extends PropertyPlaceholderConfigurer {

	// begin:SystemSetting设置项
	public static int ANTIHARASS_TIME;
	public static String ANTIHARASS_UNIT;
	public static int LOCK_IP_TIME;
	public static String LOCK_IP_UNIT;
	public static int LOCK_COOKIENAME_TIME;
	public static String LOCK_COOKIENAME_UNIT;
	public static int LOCK_TEL_TIME;
	public static String LOCK_TEL_UNIT;
	public static int LOCK_LOGIN_TIME;
	public static String LOCK_LOGIN_UNIT;
	public static int CALL_FAILURE_MAX_TIMES;
	public static String CALL_FAILURE_MAX_UNIT;
	public static int CALL_MAX_TIMES;
	public static String CALL_MAX_UNIT;
	public static int SMS_MAX_TIMES;
	public static String SMS_MAX_UNIT;
	public static int LOGIN_FAILED_WHEN_EMAIL_TIMES;
	public static String LOGIN_FAILED_WHEN_EMAIL_UNIT;
	public static int LOGIN_FAILED_FORBIDDEN_TIMES;
	public static String LOGIN_FAILED_FORBIDDEN_UNIT;
	public static int RADIUS_SERVER_AUTH_PORT;
	public static int RADIUS_SERVER_AUTH;
	public static int RADIUS_SERVER_ACCT_PORT;
	public static int RADIUS_SERVER_ACCT;
	public static String INTERFACE_AUTH_USERNAME;
	public static String INTERFACE_AUTH_PASSWORD;
	public static int AGI_SERVER_PORT;
	public static int DEFAULT_ROUTER;
	public static int IB_CALL_REMEMBER_TIME;
	public static String IB_CALL_REMEMBER_UNIT;
	public static int OB_CALL_REMEMBER_TIME;
	public static String OB_CALL_REMEMBER_UNIT;
	public static int AMI_RESPONSE_TIMEOUT;
	public static String AMI_RESPONSE_UNIT;
	public static String BOSS_DB_INFO;
	public static String BOSS_URL_INFO;
	public static String BOSS_INTERFACE_URL;
	public static String BOSS_CONN_POOL;
	public static String STANDBY_DB_INFO;
	public static String STANDBY_CONN_POOL;
	public static String SMS_URL;
	public static String SMS_MOBILE_NAME;
	public static String SMS_MESSAGE_NAME;
	public static String SMS_CELL_NAME;
	public static String TIMED_TASK_EMAIL_ADDRESS;
	public static String MOD_AGI;
	public static String MOD_AMI;
	public static String MOD_RADIUS;
	public static String MOD_WEBSOCKET;
	public static String MOD_SBC;
	public static String MOD_MONITOR;// JBE模块监控管理器
	public static String MOD_MONITOR_QUEUE;// 队列监控
	public static String MOD_MONITOR_IVR;// IVR监控
	public static String MOD_MONITOR_TRUNK;// 中继监控
	public static String MOD_AGENDA_SEND;// 日程表监控
	public static String MOD_CURL;// 挂机模块
	public static Integer MOD_CURL_THREAD;// curl推送线程数
	public static Integer MOD_CURL_MAX_THREAD;// curl推送最大线程数
	public static String MOD_WEBCALL;// webcall引擎模块
	public static Integer WEBCALL_CAPS;// webcall引擎 caps设置 默认3
	public static String WEBRTC_BREAKER;// webbreaker开关
	public static String WEBRTC_STUN_SERVER;// webrtc stun server
	public static String TAOBAO_APP_KEY;// 淘宝app对接参数
	public static String TAOBAO_APP_SECRET;
	public static String WEBRTC_WEBSOCKET_URL;
	public static int MONEY_CALL_ALERT_THRESHOLD;
	public static int CDR_EXPIRE_MONTH;// 企业客户数据过期时长定义
	public static int CDR_SMS_EXPIRE_MONTH;
	public static int LOG_ACTION_EXPIRE_MONTH;
	public static int LOG_CLIENT_EXPIRE_MONTH;
	public static int LOG_CRONTAB_EXPIRE_MONTH;
	public static int LOG_EMAIL_EXPIRE_MONTH;
	public static int LOG_LOGIN_EXPIRE_MONTH;
	public static int LOG_WEB400_CALL_EXPIRE_MONTH;
	public static int REPORT_DAY_EXPIRE_MONTH;
	public static int REPORT_MONTH_EXPIRE_MONTH;
	public static int REPORT_WEEK_EXPIRE_MONTH;
	public static int STATISTIC_CALL_IB_DAY_EXPIRE_MONTH;
	public static int STATISTIC_CALL_IB_DAY_AREA_EXPIRE_MONTH;
	public static int STATISTIC_CALL_OB_DAY_EXPIRE_MONTH;
	public static int STATISTIC_CLIENT_WORKLOAD_DAY_EXPIRE_MONTH;
	public static int STATISTIC_INVESTIGATION_DAY_EXPIRE_MONTH;
	public static int STATISTIC_IVR_DAY_EXPIRE_MONTH;
	public static int STATISTIC_QUEUE_HOUR_EXPIRE_MONTH;
	public static int STATISTIC_SMS_DAY_EXPIRE_MONTH;
	public static int STATISTIC_WEB400_DAY_EXPIRE_MONTH;
	public static int WEB400_VISITOR_EXPIRE_MONTH;
	public static int MONITOR_SERVER_EXPIRE_MONTH;
	public static int CASE_LIB_EXPIRE_MONTH;
	public static int SERVICE_LEVEL;
	public static String PREDICTIVE_OUT_CALL_MODE;
	public static String CCIC_JSP_TITLE; // 后台管理界面title设置
	public static Integer PREDICTIVE_CHANNEL_LIMIT;
	public static Integer PREDICTIVE_CAPS;
	public static String FAXC_URL;
	public static String OUTBOUND_PROXY;
	public static Integer MOD_PREDICTIVE = 0;
	public static Integer MOD_SPEECH = 1;
	public static Integer MOD_SPEECH_THREAD;
	public static String SPEECH_ENGINE;
	public static String IFLYTEK_ICS_URL;
	public static Integer AGI_SHELL_PORT;
	public static String CCIC2_PINGTAI_NAME;
	public static String DIANHUABANG_APIKEY;
	public static String DIANHUABANG_PWD;
	public static String DIANHUABNAG_INTERFACE_URL;
	public static String DIANHUABNAG_SIG;
	public static String IVR_TO_DIANHUABANG;
	public static String DIANHUABNAG_INTERFACE_RESULT_URL;
	public static String DIANHUABNAG_UID;
	public static String SYSTEM_START_TIME_MILLIS;
	public static String CURL_AGAIN;
	public static String STATISTIC_REPORT_URL;// 报表中心地址
	public static String MONITOR_URL; // 监控中心地址
	public static String[] UNICOM_SEGMENT;
	public static String[] TELECOM_SEGMENT;
	public static String[] MOBILE_SEGMENT;
	// end:SystemSetting设置项

	// 应用程序配置项
	private static Map<String, String> properties;

	/**
	 * 重写Spring的方法，将Properties加载至本地内存
	 */
	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) {
		super.processProperties(beanFactory, props);

		properties = new HashMap<String, String>();
		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			properties.put(keyStr, props.getProperty(keyStr));
		}
	}

	/**
	 * 读取应用程序配置项
	 * 
	 * @param key properties文件中的配置项名称
	 * @return properties文件中的配置项值
	 */
	public static String getProperty(String key) {
		return properties.get(key);
	}

	/**
	 * 初始化系统设置数据，将数据库SystemSetting表中的全局设置载入到内存
	 * 
	 * @param systemSetting
	 */
	public static void loadSystemSettings(SystemSetting systemSetting) {
//		if (systemSetting.getName().equals(Const.SYSTEM_SETTING_NAME_DEFAULT_ROUTER)) {
//			Macro.DEFAULT_ROUTER = Integer.parseInt(systemSetting.getValue());
//		} else if (systemSetting.getName().equals(Const.SYSTEM_SETTING_NAME_IB_CALL_REMEMBER_TIME)) {
//			Macro.IB_CALL_REMEMBER_TIME = Integer.parseInt(systemSetting.getValue());
//			Macro.IB_CALL_REMEMBER_UNIT = systemSetting.getProperty();
//		} else if (systemSetting.getName().equals(Const.SYSTEM_SETTING_NAME_OB_CALL_REMEMBER_TIME)) {
//			Macro.OB_CALL_REMEMBER_TIME = Integer.parseInt(systemSetting.getValue());
//			Macro.OB_CALL_REMEMBER_UNIT = systemSetting.getProperty();
//		} else if (systemSetting.getName().equals(Const.SYSTEM_SETTING_NAME_AMI_RESPONSE_TIMEOUT)) {
//			Macro.AMI_RESPONSE_TIMEOUT = Integer.parseInt(systemSetting.getValue());
//			Macro.AMI_RESPONSE_UNIT = systemSetting.getProperty();
//		}  else if (systemSetting.getName().equals(Const.SYSTEM_SETTING_NAME_TIMED_TASK_EMAIL_ADDRESS)) {
//			Macro.TIMED_TASK_EMAIL_ADDRESS = systemSetting.getValue();
//		} else if (systemSetting.getName().equals(Const.SYSTEM_SETTING_NAME_SMS_URL)) {
//			Macro.SMS_URL = systemSetting.getValue();
//			String[] infoArr = systemSetting.getProperty().split(",");
//			if (infoArr.length == 3) {
//				Macro.SMS_MOBILE_NAME = infoArr[0];
//				Macro.SMS_MESSAGE_NAME = infoArr[1];
//				Macro.SMS_CELL_NAME = infoArr[2];
//			}
//		} else if (systemSetting.getName().equals(Const.SYSTEM_SETTING_NAME_SERVICE_LEVEL)) {
//			Macro.SERVICE_LEVEL = Integer.parseInt(systemSetting.getValue());
//		} else if (systemSetting.getName().equals(Const.SYSTEM_SETTING_NAME_UNICOM_SEGMENT)) {
//			Macro.UNICOM_SEGMENT = StringUtils.split(systemSetting.getValue(), ",");
//		} else if (systemSetting.getName().equals(Const.SYSTEM_SETTING_NAME_TELECOM_SEGMENT)) {
//			Macro.TELECOM_SEGMENT = StringUtils.split(systemSetting.getValue(), ",");
//		} else if (systemSetting.getName().equals(Const.SYSTEM_SETTING_NAME_MOBILE_SEGMENT)) {
//			Macro.MOBILE_SEGMENT = StringUtils.split(systemSetting.getValue(), ",");
//		} else if (systemSetting.getName().equals(Const.SYSTEM_SETTING_NAME_WEBRTC_WEBSOCKET_URL)) {
//			Macro.WEBRTC_BREAKER = systemSetting.getValue();
//			Macro.WEBRTC_WEBSOCKET_URL = systemSetting.getProperty();
//		} else if (systemSetting.getName().equals(Const.SYSTEM_SETTING_NAME_WEBRTC_STUN_SERVER)) {
//			Macro.WEBRTC_STUN_SERVER = systemSetting.getValue();
//		} else if (systemSetting.getName().equals(Const.SYSTEM_SETTING_NAME_SPEECH_ENGINE)) {
//			Macro.SPEECH_ENGINE = systemSetting.getValue();
//		} 
	}

	/**
	 * 初始化系统设置数据，将数据库SystemSetting表中的全局设置载入到内存
	 * 
	 * @param systemSettings
	 */
	public static void loadSystemSettings(List<SystemSetting> systemSettings) {
//		for (SystemSetting systemSetting : systemSettings) {
//			loadSystemSettings(systemSetting);
//		}
	}
}
