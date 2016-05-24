package com.tinet.ctilink.ami.action.callback;

import java.util.Map;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.LiveException;
import org.asteriskjava.live.OriginateCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.event.AmiEventPublisher;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.json.JSONObject;

@Component
public class OriginateActionCallback implements OriginateCallback{
	
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSuccess(AsteriskChannel channel) {
		// TODO Auto-generated method stub
//		Map<String,String> callbackData = channel.getVariables();
		if(originateDataArray == null)
			return;
		originateDataArray.put("result", AmiParamConst.ORIGINATE_RESPONSE_RESULT_SUCCESS);		
		JSONObject json = new JSONObject();
	    json.putAll( originateDataArray );	    
	    System.out.println("onSuccess: "+json);
	    amiEventPublisher.publish(json);		
	}

	@Override
	public void onNoAnswer(AsteriskChannel channel) {
		// TODO Auto-generated method stub
		if(originateDataArray == null)
			return;
		originateDataArray.put("result", AmiParamConst.ORIGINATE_RESPONSE_RESULT_ERROR);		
		JSONObject json = new JSONObject();
	    json.putAll( originateDataArray );	    
	    System.out.println("onNoAnswer: "+json);
	    amiEventPublisher.publish(json);
	}

	@Override
	public void onBusy(AsteriskChannel channel) {
		// TODO Auto-generated method stub
//		System.out.println("onBusy "+originateClientData);
		if(originateDataArray == null)
			return;
		originateDataArray.put("result", AmiParamConst.ORIGINATE_RESPONSE_RESULT_ERROR);		
		JSONObject json = new JSONObject();
	    json.putAll( originateDataArray );	    
	    System.out.println("onBusy: "+json);
	    amiEventPublisher.publish(json);
	}

	@Override
	public void onFailure(LiveException cause) {
		// TODO Auto-generated method stub
//		System.out.println("onFailure "+originateClientData+" cause:"+cause);
		
		if(originateDataArray == null)
			return;
		originateDataArray.put("result", AmiParamConst.ORIGINATE_RESPONSE_RESULT_ERROR);		
		JSONObject json = new JSONObject();
	    json.putAll( originateDataArray );	    
	    System.out.println("onFailure: "+json);
	    amiEventPublisher.publish(json);
	}
	
}
