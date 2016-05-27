package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.asteriskjava.manager.action.SorceryMemoryCacheExpireAction;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.inc.AmiActionTypeConst;
import com.tinet.ctilink.ami.inc.AmiParamConst;


/**
 * 设置通道变量
 * 
 * @author hongzk
 */
@Component
public class SorceryExpireActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiActionTypeConst.SORCERY_EXPIRE;
	}

	@Override
	public AmiActionResponse handle(Map<String, Object> params) {
		logger.info("handle {} action : {}", this.getAction(), params);
		
		
		String sorceryCache = (String)params.get(AmiParamConst.SORCERY_CACHE);			
				
		SorceryMemoryCacheExpireAction sorceryMemoryCacheExpireAction = new SorceryMemoryCacheExpireAction();
		sorceryMemoryCacheExpireAction.setCache(sorceryCache);
		
		
		if (sendAction(sorceryMemoryCacheExpireAction) == null)
		{
			return ERROR_EXCEPTION;
		}

		return SUCCESS;
	}



}
