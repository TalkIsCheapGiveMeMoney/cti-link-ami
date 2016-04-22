package org.asteriskjava.manager.userevent;

import org.asteriskjava.manager.event.UserEvent;

@SuppressWarnings("serial")
public class MixSoundEvent extends UserEvent {
	private String mixSound;

	public MixSoundEvent(Object source) {
		super(source);
	}

	public String getMixSound() {
		return mixSound;
	}

	public void setMixSound(String mixSound) {
		this.mixSound = mixSound;
	}

}
