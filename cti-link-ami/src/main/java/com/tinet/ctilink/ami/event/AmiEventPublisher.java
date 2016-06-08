package com.tinet.ctilink.ami.event;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tinet.ctilink.conf.model.EnterprisePushAction;
import com.tinet.ctilink.inc.EnterpriseSettingConst;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.inc.AmiEventTypeConst;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.cache.CacheKey;
import com.tinet.ctilink.cache.RedisService;
import com.tinet.ctilink.conf.model.EnterpriseSetting;
import com.tinet.ctilink.curl.CurlData;
import com.tinet.ctilink.curl.CurlPushClient;
import com.tinet.ctilink.inc.Const;
import com.tinet.ctilink.json.JSONObject;


/**
 * AMI事件推送器
 * 
 * @author Jiangsl
 *
 * @author Tianzp
 * 
 */
@Component
public class AmiEventPublisher {
	@Autowired
	private RedisService redisService;
	
	public void publish(JSONObject event) {
		redisService.lpush(Const.REDIS_DB_CTI_INDEX, AmiEventTypeConst.AMI_EVENT_LIST, event.toString());
	}

	public void publish(Map<String, String> event) {

		redisService.lpush(Const.REDIS_DB_CTI_INDEX, AmiEventTypeConst.AMI_EVENT_LIST, event.toString());
		// 根据企业设置推送AMI状态
//		pushevent(event);
	}
	
	
	

	private void pushevent(Map<String, String> event) {
		if (event.get(AmiParamConst.EVENT).equals(AmiEventTypeConst.STATUS)) {
			int enterpriseId = Integer.parseInt(event.get(AmiParamConst.ENTERPRISE_ID));
			List<EnterprisePushAction> pushActionList = redisService.getList(Const.REDIS_DB_CONF_INDEX, String.format(CacheKey.ENTERPRISE_HANGUP_ACTION_ENTERPRISE_ID_TYPE, enterpriseId,
					Const.ENTERPRISE_PUSH_TYPE_CLIENT_STATUS), EnterprisePushAction.class);
			if (pushActionList != null) {
				for (EnterprisePushAction pushAction : pushActionList) {

					String url = pushAction.getUrl();
					String urlParams = null;
					// url支持 ?abc=a&bbb=c的格式
					if (url != null && url.indexOf('?') != -1) {
						String[] temp = url.split("\\?");
						url = temp[0];
						if (temp.length > 1) {
							urlParams = temp[1];
						}
					}
					CurlData curlData = new CurlData();
					curlData.setType(Const.CURL_TYPE_CLIENT_STATUS);
					curlData.setEnterpriseId(enterpriseId);
					curlData.setUrl(url);
					curlData.setTimeout(pushAction.getTimeout());
					curlData.setRetry(pushAction.getRetry());
					if (pushAction.getMethod() != null && pushAction.getMethod() == 1) {
						curlData.setMethod("GET");
					}

					// 准备替换变量
					String paramName[] = StringUtils.split(pushAction.getParamName(), ",");
					String paramVariable[] = StringUtils.split(pushAction.getParamVariable(), ",");

					Map<String, String> nvParams = new HashMap<String, String>();
					for (String key : event.keySet()) {
						if (key.equals(AmiParamConst.EVENT)) {
							continue;
						}
						for (int i = 0; i < paramName.length; i++) {
							String variable = new String(Base64.getDecoder().decode(paramVariable[i]));
							if (variable.equals(key)) {
								String paramKey = new String(Base64.getDecoder().decode(paramName[i]));
								nvParams.put(paramKey, event.get(key));
								break;
							}
						}

						nvParams.put(key, event.get(key));
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
					EnterpriseSetting setting = redisService.get(Const.REDIS_DB_CONF_INDEX, String.format(CacheKey.ENTERPRISE_SETTING_ENTERPRISE_ID_NAME,
							enterpriseId, EnterpriseSettingConst.ENTERPRISE_SETTING_NAME_CURL_LEVEL), EnterpriseSetting.class);

					if (setting != null && StringUtils.isNumeric(setting.getValue())) {
						level = Integer.parseInt(setting.getValue());
					}
					curlData.setParams(nvParams);
					curlData.setRequestTime(new Date());
					curlData.setLevel(level);

					CurlPushClient.addPushQueue(curlData);
				}
			}
		}
	}

}
