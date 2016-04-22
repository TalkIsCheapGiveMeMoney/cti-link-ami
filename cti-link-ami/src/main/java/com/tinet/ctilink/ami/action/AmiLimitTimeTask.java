package com.tinet.ctilink.ami.action;

import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.AsteriskServer;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.manager.action.HangupAction;

import com.tinet.ctilink.ami.AmiManager;
import com.tinet.ctilink.util.ContextUtil;

/**
 * @author Jiangsl
 *
 */
public class AmiLimitTimeTask implements Runnable {
	private String channel;
	private String file;
	private Integer alertSecond;

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public Integer getAlertSecond() {
		return alertSecond;
	}

	public void setAlertSecond(Integer alertSecond) {
		this.alertSecond = alertSecond;
	}

	@Override
	public void run() {
		AsteriskServer asteriskServer = ContextUtil.getBean(AmiManager.class).getManager()
				.getAsteriskServer();
		AsteriskChannel consultChannel = asteriskServer.getChannelByName(channel);
		if (consultChannel != null) {
			if (!consultChannel.getState().equals(ChannelState.HUNGUP)) {
				if (alertSecond > 0) {
					Map<String, String> variables = new HashMap<String, String>();
					variables.put("context", "mix_sound");
					variables.put("extension", "s");
					variables.put("targetContext", "global_channel_spy");
					variables.put("targetExtension", "s");
					variables.put("mix_sound", file);
					variables.put("spied_channel", channel);
					variables.put("spied_unique_id", consultChannel.getId());
					ContextUtil.getBean(CallLocalActionHandler.class).handle(variables);
					try {
						Thread.sleep(alertSecond * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				consultChannel = asteriskServer.getChannelByName(channel);
				if (consultChannel != null && !consultChannel.getState().equals(ChannelState.HUNGUP)) {
					HangupAction hangupAction = new HangupAction(channel);
					hangupAction.setCause(new Integer(16));
					try {
						asteriskServer.sendAction(hangupAction);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

	}
}
