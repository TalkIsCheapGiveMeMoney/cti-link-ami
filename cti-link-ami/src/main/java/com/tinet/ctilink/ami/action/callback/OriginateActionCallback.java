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

@Component
public class OriginateActionCallback implements OriginateCallback{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	protected AmiEventPublisher amiEventPublisher;
	
	private Map<String, String> originateDataArray;
	

	public Map<String, String> getOriginateDataArray() {
		return originateDataArray;
	}

	public void setOriginateDataArray(Map<String, String> originateDataArray) {
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
		JSONObject json = new JSONObject();
	    json.putAll( originateDataArray );	    
	    
	    logger.debug("onSuccess json:" + json.toString());
	    amiEventPublisher.publish(json);		
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
		JSONObject json = new JSONObject();
	    json.putAll( originateDataArray );	    
	    
	    logger.debug("onFailure json:" + json.toString());
	    amiEventPublisher.publish(json);
	}
	
}
