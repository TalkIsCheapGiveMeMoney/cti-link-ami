package com.tinet.ctilink.ami.action.callback;

import java.util.Map;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.LiveException;
import org.asteriskjava.live.OriginateCallback;
import org.asteriskjava.live.internal.OriginateCallbackData;
import org.springframework.stereotype.Component;

@Component
public class OriginateActionCallback implements OriginateCallback{
	
	private Map<String, String> originateDataArray;
	private String originateClientData;

//	@Override
//	public void onSuccess(OriginateCallbackData callbackData) {
//		// TODO Auto-generated method stub
//		System.out.println("onSuccess"+originateDataArray.toString());
//	}
//
//	@Override
//	public void onFailure(OriginateCallbackData callbackData,LiveException cause) {
//		// TODO Auto-generated method stub
//		System.out.println("onFailure"+originateDataArray.toString());
//		
//	}

	public String getOriginateClientData() {
		return originateClientData;
	}

	public void setOriginateClientData(String originateClientData) {
		this.originateClientData = originateClientData;
	}

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
		
		System.out.println("onSuccess "+originateClientData);
		
	}

	@Override
	public void onNoAnswer(AsteriskChannel channel) {
		// TODO Auto-generated method stub
		System.out.println("onNoAnswer "+originateClientData);
	}

	@Override
	public void onBusy(AsteriskChannel channel) {
		// TODO Auto-generated method stub
		System.out.println("onBusy "+originateClientData);
	}

	@Override
	public void onFailure(LiveException cause) {
		// TODO Auto-generated method stub
		System.out.println("onFailure "+originateClientData+" cause:"+cause);
	}
	
//	public Class<?> getEventClass() {
//		return NewChannelEvent.class;
//	}
	
	
	
}
