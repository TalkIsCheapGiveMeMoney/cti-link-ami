package com.tinet.ctilink.ami.webcall;

import java.util.*;

import com.tinet.ctilink.AmiConst;
import com.tinet.ctilink.ami.AmiManager;
import com.tinet.ctilink.ami.util.*;
import com.tinet.ctilink.cache.CacheKey;
import com.tinet.ctilink.cache.RedisService;
import com.tinet.ctilink.conf.model.*;
import com.tinet.ctilink.curl.CurlData;
import com.tinet.ctilink.curl.CurlPushClient;
import com.tinet.ctilink.ami.entity.Caller;
import com.tinet.ctilink.ami.entity.LogWeb400Call;
import com.tinet.ctilink.inc.Const;
import com.tinet.ctilink.inc.Macro;
import com.tinet.ctilink.ami.util.AreaCodeUtil;
import com.tinet.ctilink.ami.util.ClidUtil;
import com.tinet.ctilink.util.LocalIpUtil;
import com.tinet.ctilink.ami.util.RouterUtil;
import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.response.ManagerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 网上400呼叫和短信留言业务逻辑实现类
 */

@Component
public class WebCallExecutor {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AmiManager amiManager;

    @Autowired
    private RedisService redisService;

    @Autowired
    private WebCallLogEngine webCallLogEngine;

    private String numberTrunk;
    private String hotline;
    private String gwIp;
    private Caller caller;
    private int web400IvrId;
    private String destInterface;
    private String clid;
    private int amiResponseTimeout;
    private String fullTel;

	/*=====================================================================================================
    Function:			call
	Precondition:
	Input:				String ip, String cookieName, String tel, String ext, String enterpriseId, Long timeout
								ip						ip address
								cookieName		        cookie name
								tel						the telephone number to call
								ext						the extension  number
								enterpriseId			the id of the enterprise
								timeout					unit: millisecond
	Output:						type											int
					Const.WEB400_CALL_FAILED							the called peer declines the call or timeout
					Const.WEB400_CALL_SUCCESS							the called peer answers the call in time
					Const.WEB400_CALL_UNAVAILABLE_AMI_SERVER			there is no available ami server
					Const.WEB400_CALL_UNAVAILABLE_SYSTEM_SETTING		the setting of system is not correct
					Const.WEB400_CALL_UNAVAILABLE_ENTERPRISE_SETTING	the setting of enterprise is not correct
					Const.WEB400_CALL_UNAVAILABLE_ROUTER				the setting of table router is not intact
					Const.WEB400_CALL_UNAVAILABLE_ENTERPRISE_STATUS		the enterprise business status is not normal
					Const.WEB400_CALL_UNAVAILABLE_TEL					tel conflicts with enterprise trunk numbers 
					Const.WEB400_CALL_UNAVAILABLE_TIME					web400call is not available now
					Const.WEB400_CALL_UNAVAILABLE						the enterprise does not have the business of web400call
					Const.WEB400_CALL_BLACK_IP							ip in black list
					Const.WEB400_CALL_BLACK_COOKIENAME					cookie name in black list
					Const.WEB400_CALL_BLACK_TEL							tel in black list
					Const.WEB400_CALL_ENTERPRISE_BLACK_TEL				tel in black list in table black_tel 
					Const.WEB400_CALL_NO_IVR							ivr is not set in table web400_config
					Const.WEB400_CALL_UNKNOWN_ERROR						almost impossible return value,just  to cover all forks
	Purpose:		to originate a call, then execute some sql if needed and finally return the result of this process
	Reference:
	=====================================================================================================*/

    public WebCallResponse execute(WebCall webCall) {
        // TTS合成
        if (sendTts(webCall)) {
            // 设置通道变量
            if (StringUtils.isEmpty(webCall.getParams().get(Const.CDR_USER_FIELD))) {
                if (StringUtils.isNotEmpty(webCall.getUniqueId())) {
                    // 将uniqueId打入通道变量CDR(userfield)
                    webCall.getParams().put(Const.CDR_USER_FIELD, webCall.getUniqueId());
                }
            }
            // 把webcall请求时间戳打入通道，webcall_request_time
            webCall.getParams().put(Const.WEBCALL_REQUEST_TIME,
                    String.valueOf(webCall.getRequestTime().getTime() / 1000));
            webCall.setResult(-1);
            String[] telList;
            if (webCall.getTel().contains(",")) { // 多号码呼叫
                telList = webCall.getTel().split(",");
                if (telList.length > 0) {
                    for (int i = 0; i < telList.length; ++i) {
                        webCall.setTel(telList[i]);
                        String[] subTelList = webCall.getTel().split("-");
                        // 增加customer号码支持01087120766-5960这种格式
                        if (subTelList.length == 2) {
                            webCall.setTel(subTelList[0]);
                            webCall.setSubtel(subTelList[1]);
                        }
                        try {
                            webCall.setCallTime(new Date());
                            boolean web400Push;
                            if (webCall.getMultiTelPush() == 1) {// 推最后一个
                                if (i == telList.length - 1) {// 最后一个
                                    web400Push = (i == telList.length - 1);
                                } else {
                                    web400Push = false;
                                }
                            } else {
                                web400Push = true;
                            }
                            webCall.getParams().put(Const.WEBCALL_INDEX, String.valueOf(i));
                            StringBuilder sb = new StringBuilder();
                            webCall.setResult(executeCall(webCall.getTel(), webCall.getSubtel(), webCall.getEnterpriseId(),
                                    webCall.getUniqueId(), webCall.getParams(), webCall.getIvrId(),
                                    webCall.getTimeout(), webCall.getClidNumber(), webCall.getAmd(),
                                    web400Push, sb));
                            webCall.setCdrUniqueId(sb.toString());
                            if (webCall.getResult() == 1) { // 呼叫成功了返回answerTime
                                webCall.setAnswerTime(new Date());
                                webCall.setCallStatus(1);
                                break;
                            }
                            webCall.setCallStatus(0);
                            Thread.sleep(webCall.getMultiTelDelay() * 1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                try {
                    webCall.setCallTime(new Date());
                    webCall.getParams().put(Const.WEBCALL_INDEX, "0");
                    StringBuilder sb = new StringBuilder();
                    webCall.setResult(executeCall(webCall.getTel(),
                            webCall.getSubtel(), webCall.getEnterpriseId(), webCall.getUniqueId(),
                            webCall.getParams(), webCall.getIvrId(), webCall.getTimeout(),
                            webCall.getClidNumber(), webCall.getAmd(), true, sb));
                    webCall.setCdrUniqueId(sb.toString());
                    if (webCall.getResult() == 1) { // 呼叫成功了返回answerTime
                        webCall.setAnswerTime(new Date());
                        webCall.setCallStatus(1);
                    } else { // 网上400未接受
                        webCall.setCallStatus(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            webCall.setResult(15);
        }

        // 记录呼叫结果
        // 参数描述：ip--地址，cookie--名称，tel--外呼的电话号码，subtel--分机号码(空为"")，enterpriseId--企业ID，
        // 超时时间(ms),uniqueid--访问企业网站唯一标识
        // 返回：0失败，1成功，2AMIServer不可用，3系统配置不可用，4企业设置不可用，
        // 5路由设置不可用，6企业状态无效，7企业电话冲突，8当前时间不可用，9企业未开启网上外呼服务，
        // 10骚扰ip，11骚扰cookie_name，12骚扰电话，13企业黑名单电话，14未配置IVR，15未知错误...

        webCall.setEndTime(new Date());

        String webCallResultMessage;

        switch (webCall.getResult()) {
            case 0:
                webCallResultMessage = "呼叫失败";
                break;
            case 1:
                webCallResultMessage = "呼叫成功";
                break;
            case 2:
                webCallResultMessage = "AMIServer不可用";
                break;
            case 3:
                webCallResultMessage = "系统配置不可用";
                break;
            case 4:
                webCallResultMessage = "企业设置不可用";
                break;
            case 5:
                webCallResultMessage = "路由设置不可用";
                break;
            case 6:
                webCallResultMessage = "企业状态无效";
                break;
            case 7:
                webCallResultMessage = "企业电话冲突";
                break;
            case 8:
                webCallResultMessage = "当前时间不可用";
                break;
            case 9:
                webCallResultMessage = "企业未开启网上外呼服务";
                break;
            case 10:
                webCallResultMessage = "骚扰ip";
                break;
            case 11:
                webCallResultMessage = "骚扰cookie_name";
                break;
            case 12:
                webCallResultMessage = "骚扰电话";
                break;
            case 13:
                webCallResultMessage = "企业黑名单电话";
                break;
            case 14:
                webCallResultMessage = "未配置IVR";
                break;
            case 15:
                webCallResultMessage = "TTS合成失败";
                break;
            case 16:
                webCallResultMessage = "企业ID不正确";
                break;
            case 17:
                webCallResultMessage = "IVR ID不正确";
                break;
            case 18:
                webCallResultMessage = "外显号码不正确";
                break;
            default:
                webCallResultMessage = "未知错误";
                break;
        }

        // 如果异步进行结果推送
        if (StringUtils.isNotEmpty(webCall.getCallbackUrl())) { // 回调地址不是空
            // 获取curl级别
            int level = 0;
            EnterpriseSetting setting = redisService.get(Const.REDIS_DB_CONF_INDEX, String.format(CacheKey.ENTERPRISE_SETTING_ENTERPRISE_ID_NAME,
                    webCall.getEnterpriseId(), Const.ENTERPRISE_SETTING_NAME_CURL_LEVEL), EnterpriseSetting.class);
            if (setting != null && StringUtils.isNumeric(setting.getValue())) {
                level = Integer.parseInt(setting.getValue());
            }

            Map<String, String> nvParams = new HashMap<>();
            // 添加自定义回调参数
            for (String key : webCall.getCallbackParams().keySet()) {// 遍历key
                nvParams.put(key, webCall.getCallbackParams().get(key));
            }
            CurlData curlData = new CurlData();
            curlData.setUniqueId(webCall.getUniqueId());
            curlData.setEnterpriseId(webCall.getEnterpriseId());
            curlData.setUrl(webCall.getCallbackUrl());
            curlData.setRetry(2);// 重试2次，一共三次
            curlData.setTimeout(10);

            nvParams.put("result", String.valueOf(webCall.getResult()));
            nvParams.put("description", webCallResultMessage);
            nvParams.put("enterpriseId", webCall.getEnterpriseId().toString());
            nvParams.put("customerNumber", webCall.getTel());
            nvParams.put("subtel", webCall.getSubtel());
            nvParams.put("uniqueId", webCall.getCdrUniqueId());
            nvParams.put("timeout", webCall.getTimeout().toString());
            nvParams.put("clidNumber", webCall.getClidNumber());
            nvParams.put("userField", webCall.getUniqueId());
            nvParams.put("startTime", Long.toString(webCall.getStartTime().getTime() / 1000));

            if (webCall.getResult() == 1) { // 呼叫成功了返回answerTime
                nvParams.put("answerTime", Long.toString(webCall.getAnswerTime().getTime() / 1000));
            } else { // 其他返回结束时间
                nvParams.put("endTime", Long.toString(webCall.getEndTime().getTime() / 1000));
            }

            curlData.setParams(nvParams);
            curlData.setRequestTime(new Date());
            curlData.setLevel(level);
            curlData.setType(Const.CURL_TYPE_WEBCALL);
            CurlPushClient.addPushQueue(curlData);
        } else {
            logger.debug("网上400异步调用，tel=" + webCall.getTel() + "，回调地址是空，不进行结果推送！");
        }

        // 日志入库
        webCallLogEngine.insertWebCallLog(webCall);

        WebCallResponse response = new WebCallResponse();
        response.setUniqueId(webCall.getCdrUniqueId());
        response.setResult(webCall.getResult());
        response.setDescription(webCallResultMessage);

        return response;
    }

    /**
     * 网上400外呼。
     *
     * @param tel          呼叫号码。
     * @param subtel       分机号。
     * @param enterpriseId 企业号。
     * @param timeout      超时时间。
     * @param uniqueid     唯一标识
     * @param ivrId        指定ivrId
     * @return 返回Const.java中定义的呼叫结果。
     */

    public int executeCall(String tel, String subtel, int enterpriseId, String uniqueid, Map<String, String> params, String ivrId, int timeout, String clidNumber, Integer amd, boolean web400Push, StringBuilder cdrUniqueId) {
        try {
            int res = check(tel, subtel, enterpriseId, ivrId, clidNumber);
            if (res == Const.WEB400_CALL_CHECK_OK) {
                Map<String, String> variables = new HashMap<>();
                variables.put("__" + Const.CDR_CUSTOMER_NUMBER, caller.getCallerNumber()); // 客户号码
                variables.put("__" + Const.CDR_CUSTOMER_NUMBER_TYPE, String.valueOf(caller.getTelType())); // 电话类型
                variables.put("__" + Const.CDR_CUSTOMER_AREA_CODE, caller.getAreaCode()); // 区号
                variables.put("__" + Const.ENTERPRISE_ID, String.valueOf(enterpriseId));
                variables.put("__" + Const.CDR_ENTERPRISE_ID, String.valueOf(enterpriseId));
                variables.put("__" + Const.CDR_CALL_TYPE, String.valueOf(Const.CDR_CALL_TYPE_OB_WEBCALL));
                variables.put("__" + Const.CDR_IVR_ID, String.valueOf(web400IvrId));
                // 判断是否打开号码状态语音识别
                EnterpriseSetting setting = redisService.get(Const.REDIS_DB_CONF_INDEX, String.format(CacheKey.ENTERPRISE_SETTING_ENTERPRISE_ID_NAME,
                        enterpriseId, Const.ENTERPRISE_SETTING_NAME_TEL_STATUS_IDENTIFICATION), EnterpriseSetting.class);

                if (setting != null && "1".equals(setting.getValue())) {
                    variables.put("__" + Const.IS_TSI, "1");
                }
                variables.put(Const.CDR_START_TIME, String.valueOf(new Date().getTime() / 1000));
                variables.put(Const.CDR_STATUS, String.valueOf(Const.CDR_STATUS_IB_NO_ANSWER_WEB400_UNACCEPT));
                variables.put(Const.CDR_GW_IP, gwIp);
                variables.put(Const.CDR_NUMBER_TRUNK, numberTrunk);
                variables.put(Const.WEBCALL_TEL, tel);
                variables.put(Const.WEBCALL_IVR_ID, String.valueOf(web400IvrId));
                variables.put(Const.IS_AMD_ON, String.valueOf(amd));
                // 提前增加设置group，让webcall并发限制生效
                variables.put("GROUP(enterprise)", String.valueOf(enterpriseId));
                // 网上400ivr设置暂时不启用
                // variables.put("__" + Const.IVR_ID,
                // String.valueOf(web400IvrId));

                if (subtel != null && !subtel.equals("")) {
                    variables.put(Const.SUBTEL, subtel); // set
                    // channel
                    // variable
                    // subtel:
                    // extension
                    // number
                }
                if (params.size() > 0) {
                    variables.putAll(params);
                }
                int routerClidType = Const.ROUTER_CLID_CALL_TYPE_OB_LEFT;
                SipHeaderUtil.setAlternateRouter(variables, LocalIpUtil.getRealIp(), 0, "CCIC2", enterpriseId,
                        String.valueOf(Const.CDR_CALL_TYPE_OB_WEBCALL), String.valueOf(routerClidType), 1);

                OriginateAction originateAction;
                ManagerResponse originateResponse;
                originateAction = new OriginateAction();
                originateAction.setChannel(destInterface); // set channel

                EnterpriseSetting enterpriseSetting = redisService.get(Const.REDIS_DB_CONF_INDEX, String.format(CacheKey.ENTERPRISE_SETTING_ENTERPRISE_ID_NAME,
                        enterpriseId, Const.ENTERPRISE_SETTING_NAME_CRM_CURL_WEB400_STATUS), EnterpriseSetting.class);

                String web400 = enterpriseSetting != null ? enterpriseSetting.getValue() : null;
                if (web400 != null && web400.equals("1")) {
                    if (web400Push) {
                        originateAction.setContext(Const.DIALPLAN_CONTEXT_WEB400_PUSH); // set
                    } else {
                        originateAction.setContext(Const.DIALPLAN_CONTEXT_WEB400_NOPUSH); // set
                    }
                } else {
                    originateAction.setContext(Const.DIALPLAN_CONTEXT_WEB400_NOPUSH); // set
                }
                originateAction.setExten(numberTrunk); // set exten
                originateAction.setTimeout((long) timeout * 1000); // set
                // timeout
                originateAction.setPriority(1); // set priority 1
                originateAction.setCallerId(clid);
                originateAction.setVariables(variables);

                try {
                    originateResponse = amiManager.getManager().sendAction(originateAction,
                            amiResponseTimeout);
                    if (originateResponse.getResponse().equals("Success")) // call
                    // success
                    {
                        cdrUniqueId.append(originateResponse.getMessage());
                        res = Const.WEB_CALL_SUCCESS;
                    } else // call failed
                    {
                        res = Const.WEB_CALL_FAILED;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // amiListener.stop();
                    res = Const.WEB_CALL_FAILED;
                }
            }

            // 向log_web400_call表中插入数据
            LogWeb400Call logWeb400Call = new LogWeb400Call();
            logWeb400Call.setEnterpriseId(enterpriseId);
            logWeb400Call.setHotline(hotline);
            logWeb400Call.setUniqueid(uniqueid);
            logWeb400Call.setTel(fullTel);
            logWeb400Call.setResult(res);
            //TODO 日志  可能不需要存了
            //logWeb400CallService.saveOrUpdate(logWeb400Call);

            return res;
        } catch (BeansException e) {
            e.printStackTrace();
            return Const.WEB400_CALL_FAILED;
        }
    }

    /**
     * 网上400外呼流程执行的所有检查验证操作。
     */
    @SuppressWarnings("unchecked")
    private int check(String tel, String ext, int enterpriseId, String ivrId, String clidNumber) {
        if (StringUtils.isNotEmpty(ext)) {
            fullTel = tel + "-" + ext;
        } else {
            fullTel = tel;
        }
        // check enterpriseId
        Entity entity = redisService.get(Const.REDIS_DB_CONF_INDEX, String.format(CacheKey.ENTITY_ENTERPRISE_ID, enterpriseId), Entity.class);
        if (entity == null) {
            return Const.WEB400_CALL_ILLEGAL_ENTERPRISE;
        }
        // get a trunk number from table trunk
        List<EnterpriseHotline> enterpriseHotlines = redisService.getList(Const.REDIS_DB_CONF_INDEX, String.format(CacheKey.ENTERPRISE_HOTLINE_ENTERPRISE_ID,
                enterpriseId), EnterpriseHotline.class);
        if (enterpriseHotlines.size() == 0 || enterpriseHotlines.get(0).getIsMaster() != 1) {
            return Const.WEB400_CALL_UNAVAILABLE_ENTERPRISE_SETTING;
        }
        numberTrunk = enterpriseHotlines.get(0).getNumberTrunk();
        hotline = enterpriseHotlines.get(0).getHotline();

        if (entity.getStatus() != Const.ENTITY_STATUS_OK) {
            return Const.WEB400_CALL_UNAVAILABLE_ENTERPRISE_STATUS;
        }

        caller = AreaCodeUtil.updateGetAreaCode(tel, "");

        Gateway gateway = RouterUtil.getRouterGateway(enterpriseId, Const.ROUTER_CLID_CALL_TYPE_OB_LEFT,
                caller);
        if (gateway == null) {
            return Const.WEB400_CALL_UNAVAILABLE_ROUTER;
        }
        gwIp = gateway.getIpAddr();
        destInterface = "SIP/" + gateway.getPrefix() + caller.getCallerNumber() + "@"
                + gateway.getIpAddr();


        clid = ClidUtil.getClid(enterpriseId, Const.ROUTER_CLID_CALL_TYPE_OB_LEFT,
                caller.getCallerNumber(), "");

        // get amiResponseTimeout from table system_setting
        amiResponseTimeout = Macro.AMI_RESPONSE_TIMEOUT;
        // timeout unit: millisecond
        switch (Macro.AMI_RESPONSE_UNIT) {
            case Const.SYSTEM_SETTING_PROPERTY_SECOND:
                amiResponseTimeout *= 1000;
                break;
            case Const.SYSTEM_SETTING_PROPERTY_MINUTE:
                amiResponseTimeout *= 1000 * 60;
                break;
            case Const.SYSTEM_SETTING_PROPERTY_HOUR:
                amiResponseTimeout *= 1000 * 60 * 60;
                break;
            default:
                return Const.WEB_CALL_UNAVAILABLE_SYSTEM_SETTING; // return
        }

        // 首先看启用了黑名单还是白名单，或者什么都没有启用
        EnterpriseSetting enterpriseSetting = redisService.get(Const.REDIS_DB_CONF_INDEX, String.format(CacheKey.ENTERPRISE_SETTING_ENTERPRISE_ID_NAME,
                enterpriseId, Const.ENTERPRISE_SETTING_NAME_RESTRICT_TEL_TYPE), EnterpriseSetting.class);

        int isRestrictTel = 0;
        if (null != enterpriseSetting) {
            String value = enterpriseSetting.getValue();
            if (!"".equals(value) && null != value) {
                Integer restrictType = 0;
                if (value.equals("1")) {
                    restrictType = 1;
                } else if (value.equals("2")) {
                    restrictType = 2;
                }
                RestrictTel restrictTel = redisService.get(Const.REDIS_DB_CONF_INDEX, String.format(CacheKey.RESTRICT_TEL_ENTERPRISE_ID_TYPE_RESTRICT_TYPE_TEL,
                        enterpriseId, Const.RESTRICT_TEL_TYPE_IB, restrictType, caller.getCallerNumber()), RestrictTel.class);
                if (restrictTel != null) {
                    isRestrictTel = 1;
                }
            }
        }

        if (isRestrictTel == 1) {
            return Const.WEB400_CALL_ENTERPRISE_BLACK_TEL;
        }

        // check whether tel conflicts with enterprise trunk numbers
        List<EnterpriseHotline> enterpriseHotlineList = redisService.getList(Const.REDIS_DB_CONF_INDEX, String.format(CacheKey.ENTERPRISE_HOTLINE_ENTERPRISE_ID, enterpriseId),
                EnterpriseHotline.class);
        for (EnterpriseHotline enterpriseHotline : enterpriseHotlineList) {
            if (enterpriseHotline.getNumberTrunk().equals(caller.getCallerNumber()) ||
                    enterpriseHotline.getHotline().equals(caller.getCallerNumber())) {
                return Const.WEB400_CALL_UNAVAILABLE_TEL;
            }
        }

        if (StringUtils.isNotEmpty(ivrId) && !"0".equals(ivrId)) {
            web400IvrId = Integer.valueOf(ivrId);
        } else {
            return Const.WEB400_CALL_UNAVAILABLE_IVR_ID;
        }

        if (StringUtils.isNotEmpty(clidNumber)) {
            EnterpriseHotline enterpriseHotline = redisService.get(Const.REDIS_DB_CONF_INDEX, String.format(CacheKey.ENTERPRISE_HOTLINE_ENTERPRISE_ID_NUMBER_TRUNK
                            , enterpriseId, clidNumber), EnterpriseHotline.class);
            // check clid
            if (enterpriseHotline != null) {
                clid = clidNumber;
            } else {
                EnterpriseClid enterpriseClid = redisService.get(Const.REDIS_DB_CONF_INDEX, String.format(CacheKey.ENTERPRISE_CLID_ENTERPRISE_ID
                        , enterpriseId), EnterpriseClid.class);
                String clidLeftNumber = enterpriseClid.getObClidLeftNumber();
                if (StringUtils.isNotEmpty(clidLeftNumber)) {
                    String[] clidNumberArr = clidLeftNumber.split(",");
                    for (String c : clidNumberArr) {
                        if (c.equals(clidNumber)) {
                            clid = clidNumber;
                            break;
                        }
                    }
                    if (StringUtils.isEmpty(clid)) {
                        return Const.WEB400_CALL_UNAVAILABLE_CLID_NUMBER;
                    }
                } else {
                    return Const.WEB400_CALL_UNAVAILABLE_CLID_NUMBER;
                }
            }
        } // end of clid


        // check ivr_id from table web400_config when templateType is user
        // defined template.
        IvrProfile ivrProfile = redisService.get(Const.REDIS_DB_CONF_INDEX, String.format(CacheKey.IVR_PROFILE_ENTERPRISE_ID_ID, enterpriseId, web400IvrId)
                , IvrProfile.class);
        if (ivrProfile == null) {
            return Const.WEB400_CALL_NO_IVR;
        }
        // check ivr_id is in entity
        if (!String.valueOf(enterpriseId).equals(String.valueOf(ivrProfile.getEnterpriseId()))) {
            return Const.WEB400_CALL_UNAVAILABLE_IVR_ID;
        }

        return Const.WEB400_CALL_CHECK_OK;
    }


    public boolean sendTts(WebCall webCall) {
        String text = "";
        boolean success = true;
        for (String key : webCall.getParams().keySet()) {// 遍历key
            if (webCall.getParams().get(key).equals("2")) {// 需要tts转换
                System.out.println(webCall.getParams().get(key));
                text += " " + webCall.getParams().get(key);
            }
        }
        if (StringUtils.isNotEmpty(text)) {
            Date ttsStart = new Date();
            String res = AgiShellWrapperClient.execute(AmiConst.ASTERISK_AMI_HOST, Macro.AGI_SHELL_PORT, "ttsc_break_send.sh",
                    "'" + webCall.getUniqueId() + "'" + text);
            Date ttsEnd = new Date();
            webCall.setTtsDuration((int) (ttsEnd.getTime() - ttsStart.getTime()) / 1000);
            if (!res.equals("0")) {
                success = false;// tts合成超时
                webCall.setResult(15);
            }
        }
        return success;
    }

}