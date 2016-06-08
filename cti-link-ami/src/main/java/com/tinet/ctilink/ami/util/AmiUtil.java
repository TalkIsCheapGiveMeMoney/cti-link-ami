package com.tinet.ctilink.ami.util;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.live.AsteriskChannel;

import com.tinet.ctilink.cache.CacheKey;
import com.tinet.ctilink.cache.RedisService;
import com.tinet.ctilink.conf.model.EnterprisePushAction;
import com.tinet.ctilink.conf.model.EnterpriseSetting;
import com.tinet.ctilink.curl.CurlData;
import com.tinet.ctilink.curl.CurlPushClient;
import com.tinet.ctilink.inc.Const;
import com.tinet.ctilink.inc.EnterpriseSettingConst;
import com.tinet.ctilink.util.ContextUtil;


/**
 * AMI工具类
 * 
 * @author tianzp
 */
public class AmiUtil {

	public static void pushCurl(AsteriskChannel channel, Map<String, String> event, int enterpriseId, int pushType, int curlType) {
		List<EnterprisePushAction> pushActionList = ContextUtil.getBean(RedisService.class).getList(Const.REDIS_DB_CONF_INDEX
				, String.format(CacheKey.ENTERPRISE_PUSH_ACTION_ENTERPRISE_ID_TYPE, enterpriseId, pushType), EnterprisePushAction.class);

		if (pushActionList != null) {
			for (EnterprisePushAction pushAction : pushActionList) {
				pushOne(pushAction, channel, event, enterpriseId, curlType);
			}
		}
	}

	public static void pushOne(EnterprisePushAction enterprisePushAction, AsteriskChannel channel,  Map<String, String> event,Integer enterpriseId, int curlType){
		CurlData curlData = new CurlData();
		curlData.setEnterpriseId(enterpriseId);
		String url = enterprisePushAction.getUrl();
		if (enterprisePushAction.getMethod() != null && enterprisePushAction.getMethod() == 1) {
			curlData.setMethod("GET");
		}
		String urlParams = null;
		// url支持 ?abc=a&bbb=c的格式
		if (url != null && url.indexOf('?') != -1) {
			String[] temp = url.split("\\?");
			url = temp[0];
			if (temp.length > 1) {
				urlParams = temp[1];
			}
		}
		curlData.setUrl(url);
		curlData.setTimeout(enterprisePushAction.getTimeout());
		curlData.setRetry(enterprisePushAction.getRetry());

		Map<String, String> nvParams = new HashMap<String, String>();
		String paramVariables = enterprisePushAction.getParamVariable();
		String paramNames = enterprisePushAction.getParamName();
		if (StringUtils.isNotEmpty(paramNames) && StringUtils.isNotEmpty(paramVariables)) {
			String paramName[] = StringUtils.split(paramNames, ",");
			String paramVariable[] = StringUtils.split(paramVariables, ",");
			for (int i = 0; i < paramName.length; i++) {
				try {
					boolean flag = false;
					String variable = new String(Base64.getDecoder().decode(paramVariable[i]));
					if(event.containsKey(variable)){
						String value = event.get(variable);
						nvParams.put(new String(Base64.getDecoder().decode(paramName[i])), value);
					}else if(channel != null){
						String value = channel.getVariable(variable);
						if (value == null) {
							value = "";
						}
						nvParams.put(new String(Base64.getDecoder().decode(paramName[i])), value);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		// url中带过来的参数
		if (urlParams != null) {
			String[] params = urlParams.split("&");
			for (int i = 0; i < params.length; i++) {
				if (params[i].indexOf('=') != -1) {
					nvParams.put(params[i].split("=")[0], params[i].split("=")[1]);
				} else {
					nvParams.put(params[i], "");
				}
			}
		}

		// 获取curl级别
		int level = 0;
		EnterpriseSetting setting = ContextUtil.getBean(RedisService.class).get(Const.REDIS_DB_CONF_INDEX
				, String.format(CacheKey.ENTERPRISE_SETTING_ENTERPRISE_ID_NAME,
				enterpriseId, EnterpriseSettingConst.ENTERPRISE_SETTING_NAME_CURL_LEVEL), EnterpriseSetting.class);
		if (setting != null && setting.getId() != null) {
			level = Integer.parseInt(setting.getValue());
		}
		curlData.setParams(nvParams);
		curlData.setLevel(level);
		curlData.setType(curlType);
		CurlPushClient.addPushQueue(curlData);
	}
}
