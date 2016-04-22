package com.tinet.ctilink.ami.action.callback;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.LiveException;
import org.asteriskjava.live.OriginateCallback;
import org.asteriskjava.manager.response.ManagerResponse;

import com.tinet.ctilink.ami.action.PreviewOutcallActionHandler;

public class PreviewOutcallCallback implements OriginateCallback {
	private PreviewOutcallActionHandler previewOutcallActionHandler;
	private String cid;
	private long callStartTime;
	private String orderCallBackId;
	private String loginStatus;
	private String pauseDescription;

	public PreviewOutcallCallback(PreviewOutcallActionHandler previewOutcallActionHandler, String cid, long callStartTime, String orderCallBackId,String loginStatus,String pauseDescription) {
		this.previewOutcallActionHandler = previewOutcallActionHandler;
		this.cid = cid;
		this.callStartTime = callStartTime;
		this.orderCallBackId = orderCallBackId;
		this.loginStatus = loginStatus; 
		this.pauseDescription = pauseDescription;
	}

	@Override
	public void onDialing(AsteriskChannel channel) {

	}

	@Override
	public void onSuccess(AsteriskChannel channel) {
		ManagerResponse response = new ManagerResponse();
		response.setUniqueId(channel.getId());
		response.setResponse("Success");
		previewOutcallActionHandler.handleResponse(response, cid, callStartTime, orderCallBackId,loginStatus,pauseDescription);
	}

	@Override
	public void onNoAnswer(AsteriskChannel channel) {
		ManagerResponse response = new ManagerResponse();
		response.setUniqueId(channel.getId());
		response.setResponse("Noanswer");
		response.setMessage(response.getUniqueId());
		previewOutcallActionHandler.handleResponse(response, cid, callStartTime, orderCallBackId,loginStatus,pauseDescription);
	}

	@Override
	public void onBusy(AsteriskChannel channel) {
		ManagerResponse response = new ManagerResponse();
		response.setUniqueId(channel.getId());
		response.setResponse("Busy");
		response.setMessage(response.getUniqueId());
		previewOutcallActionHandler.handleResponse(response, cid, callStartTime, orderCallBackId,loginStatus,pauseDescription);
	}

	@Override
	public void onFailure(LiveException cause) {
		ManagerResponse response = new ManagerResponse();
		response.setUniqueId("");
		response.setResponse("Failure");
		response.setMessage(response.getUniqueId());
		previewOutcallActionHandler.handleResponse(response, cid, callStartTime, orderCallBackId,loginStatus,pauseDescription);
	}

}
