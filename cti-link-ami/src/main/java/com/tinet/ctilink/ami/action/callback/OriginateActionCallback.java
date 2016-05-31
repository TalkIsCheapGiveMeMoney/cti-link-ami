package com.tinet.ctilink.ami.action.callback;

import java.util.Map;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.LiveException;
import org.asteriskjava.live.OriginateCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.Application;
import com.tinet.ctilink.ami.event.AmiEventPublisher;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.json.JSONObject;
import com.tinet.ctilink.util.ContextUtil;


public class OriginateActionCallback implements OriginateCallback{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
		
	protected AmiEventPublisher amiEventPublisher;
	
	private JSONObject originateDataArray;
	
	public OriginateActionCallback() {
		amiEventPublisher = ContextUtil.getBean(AmiEventPublisher.class);
	}
	
	public JSONObject getOriginateDataArray() {
		return originateDataArray;
	}

	public void setOriginateDataArray(JSONObject originateDataArray) {
		this.originateDataArray = originateDataArray;
	}
	
	@Override
	public void onDialing(AsteriskChannel channel) {
		
	}

	@Override
	public void onSuccess(AsteriskChannel channel) {

		if(originateDataArray == null)
			return;
		originateDataArray.put("result", AmiParamConst.ORIGINATE_RESPONSE_RESULT_SUCCESS);	
	    logger.debug("onSuccess json:" + originateDataArray.toString());
	    amiEventPublisher.publish(originateDataArray);		
	}

	@Override
	public void onNoAnswer(AsteriskChannel channel) {
		if(originateDataArray == null)
			return;
		originateDataArray.put("result", AmiParamConst.ORIGINATE_RESPONSE_RESULT_ERROR);		
		JSONObject json = new JSONObject();
	    json.putAll( originateDataArray );
	    
	    logger.debug("onNoAnswer json:" + json.toString());
	    amiEventPublisher.publish(json);
	}

	@Override
	public void onBusy(AsteriskChannel channel) {
		if(originateDataArray == null)
			return;
		originateDataArray.put("result", AmiParamConst.ORIGINATE_RESPONSE_RESULT_ERROR);		
		JSONObject json = new JSONObject();
	    json.putAll( originateDataArray );	    
	    
	    logger.debug("onBusy json:" + json.toString());
	    amiEventPublisher.publish(json);
	}

	@Override
	public void onFailure(LiveException cause) {
		if(originateDataArray == null)
			return;
		originateDataArray.put("result", AmiParamConst.ORIGINATE_RESPONSE_RESULT_ERROR);		
		    
	    
	    logger.debug("onFailure json:" + originateDataArray.toString());
	    amiEventPublisher.publish(originateDataArray);
	}
	
}
