package com.tinet.ctilink.ami.util;

import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.cache.CacheKey;
import com.tinet.ctilink.cache.RedisService;
import com.tinet.ctilink.curl.CurlData;
import com.tinet.ctilink.curl.CurlPushClient;
import com.tinet.ctilink.inc.Const;
import com.tinet.ctilink.conf.model.EnterprisePushAction;
import com.tinet.ctilink.conf.model.EnterpriseSetting;
import com.tinet.ctilink.inc.EnterpriseSettingConst;
import com.tinet.ctilink.util.ContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.live.AsteriskChannel;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * AMI工具类
 * 
 * @author tianzp
 */
public class AmiUtil {

	public static void pushCurl(AsteriskChannel channel, Map<String, String> event, int enterpriseId, int pushType, int curlType) {
		List<EnterprisePushAction> pushActionList = ContextUtil.getBean(RedisService.class).getList(Const.REDIS_DB_CONF_INDEX
				, String.format(CacheKey.ENTERPRISE_HANGUP_ACTION_ENTERPRISE_ID_TYPE, enterpriseId, pushType), EnterprisePushAction.class);

		if (pushActionList != null) {
			for (EnterprisePushAction pushAction : pushActionList) {
				CurlData curlData = new CurlData();
				// curlData.setUniqueId(channelUniqueId);
				curlData.setEnterpriseId(enterpriseId);
				String url = pushAction.getUrl();
				if (pushAction.getMethod() != null && pushAction.getMethod() == 1) {
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
				curlData.setTimeout(pushAction.getTimeout());
				curlData.setRetry(pushAction.getRetry());

				Map<String, String> nvParams = new HashMap<String, String>();
				String paramVariables = pushAction.getParamVariable();
				String paramNames = pushAction.getParamName();
				if (StringUtils.isNotEmpty(paramNames) && StringUtils.isNotEmpty(paramVariables)) {
					String paramName[] = StringUtils.split(paramNames, ",");
					String paramVariable[] = StringUtils.split(paramVariables, ",");
					for (int i = 0; i < paramName.length; i++) {
						try {
							boolean flag = false;
							String variable = new String(Base64.getDecoder().decode(paramVariable[i]));
							for (String key : event.keySet()) {
								if (key.equals(AmiParamConst.EVENT)) {
									continue;
								}

								if (key.equals(variable)) {
									String value = event.get(key);
									if (value == null) {
										value = "";
									}
									nvParams.put(new String(Base64.getDecoder().decode(paramName[i])), value);
									flag = true;
									break;
								}
							}
							if (!flag) {
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
	}

}
