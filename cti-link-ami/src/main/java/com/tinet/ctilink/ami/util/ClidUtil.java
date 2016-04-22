package com.tinet.ctilink.ami.util;

import com.tinet.ctilink.cache.CacheKey;
import com.tinet.ctilink.cache.RedisService;
import com.tinet.ctilink.inc.Const;
import com.tinet.ctilink.model.EnterpriseClid;
import com.tinet.ctilink.model.EnterpriseHotline;
import com.tinet.ctilink.model.Trunk;
import com.tinet.ctilink.util.ContextUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author fengwei //
 * @date 16/4/22 10:56
 */
public class ClidUtil {

    public static String getClid (int enterpriseId, int routerClidCallType, String customerNumber, String numberTrunk) {
        RedisService redisService = ContextUtil.getContext().getBean(RedisService.class);
        EnterpriseClid enterpriseClid = redisService.get(String.format(CacheKey.ENTERPRISE_CLID_ENTERPRISE_ID, enterpriseId)
                , EnterpriseClid.class);

        int clidType = 0;
        String clidNumber = "";
        if(enterpriseClid != null){
            switch(routerClidCallType){
                case Const.ROUTER_CLID_CALL_TYPE_IB_RIGHT://1呼入
                    clidType = enterpriseClid.getIbClidRightType();
                    clidNumber = enterpriseClid.getIbClidRightNumber();
                    break;
                case Const.ROUTER_CLID_CALL_TYPE_OB_LEFT://2外呼客户侧*
                    clidType = enterpriseClid.getObClidLeftType();
                    clidNumber = enterpriseClid.getObClidLeftNumber();
                    break;
                case Const.ROUTER_CLID_CALL_TYPE_OB_RIGHT://3外呼座席侧
                    clidType = enterpriseClid.getObClidRightType();
                    clidNumber = enterpriseClid.getObClidRightNumber();
                    break;
            }
            if(clidType != 0){
                String clid = "";
                if(clidType == 1){//外显中继号码，选取主热线号码对应的中继号码
                    List<EnterpriseHotline> enterpriseHotlines = redisService.getList(String.format(CacheKey.ENTERPRISE_HOTLINE_ENTERPRISE_ID, enterpriseId)
                            , EnterpriseHotline.class);
                    if(enterpriseHotlines.size() > 0){
                        clid = enterpriseHotlines.get(0).getNumberTrunk();
                    }

                }else if(clidType == 2){//外显客户号码
                    if(customerNumber.equals(Const.UNKNOWN_NUMBER)){
                        List<EnterpriseHotline> enterpriseHotlines = redisService.getList(String.format(CacheKey.ENTERPRISE_HOTLINE_ENTERPRISE_ID, enterpriseId)
                                , EnterpriseHotline.class);
                        if(enterpriseHotlines.size() > 0){
                            clid = enterpriseHotlines.get(0).getNumberTrunk();
                        }
                    }else{
                        clid = customerNumber;
                    }
                }else if(clidType == 3){//外显固定号码
                    if(StringUtils.isNotEmpty(clidNumber)){
                        String[] clidList = clidNumber.split(",");
                        if(clidList.length > 1){
                            Random r=new Random(); //实例化一个Random类
                            //随机产生一个整数
                            clid = clidList[r.nextInt(clidList.length)];
                        }else{
                            clid = clidNumber;
                        }
                    }
                } else if (clidType == 4) { //外显热线号码
                    if (StringUtils.isNotEmpty(numberTrunk) && StringUtils.isNumeric(numberTrunk)) {
                        EnterpriseHotline enterpriseHotline = redisService.get(String.format(CacheKey.ENTERPRISE_HOTLINE_ENTERPRISE_ID_NUMBER_TRUNK, enterpriseId, numberTrunk)
                                , EnterpriseHotline.class);
                        if (enterpriseHotline != null) {
                            clid = enterpriseHotline.getHotline();
                        }
                    } else {
                        List<EnterpriseHotline> enterpriseHotlines = redisService.getList(String.format(CacheKey.ENTERPRISE_HOTLINE_ENTERPRISE_ID, enterpriseId)
                                , EnterpriseHotline.class);
                        if(enterpriseHotlines.size() > 0){
                            clid = enterpriseHotlines.get(0).getHotline();
                        }
                    }

                }
                //加区号
                Trunk trunk = redisService.get(String.format(CacheKey.TRUNK_NUMBER_TRUNK, clid), Trunk.class);
                if(trunk != null) {
                    clid = trunk.getAreaCode() + clid;
                }
                return clid;
            }
        }
        return null;
    }
}
