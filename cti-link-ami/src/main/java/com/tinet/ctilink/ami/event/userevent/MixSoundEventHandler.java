package com.tinet.ctilink.ami.event.userevent;

import java.util.HashMap;
import java.util.Map;

import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.UserEvent;
import org.asteriskjava.manager.userevent.MixSoundEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.action.CallLocalActionHandler;
import com.tinet.ctilink.ami.event.AbstractAmiEventHandler;
import com.tinet.ctilink.ami.event.AmiUserEventHandler;

/**
 *
 * @author tianzp
 */
@Component
public class MixSoundEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {

	@Autowired
	private CallLocalActionHandler callLocalActionHandler;

	@Override
	public Class<?> getEventClass() {
		return MixSoundEvent.class;
	}

	@Override
	public void handle(ManagerEvent event) {
		logger.info("handle {} : {}.", this.getEventClass().getSimpleName(), event);

		Map<String, String> variables = new HashMap<String, String>();
		variables.put("context", "mix_sound");
		variables.put("extension", "s");
		variables.put("targetContext", "global_channel_spy");
		variables.put("targetExtension", "s");
		variables.put("mix_sound", ((MixSoundEvent) event).getMixSound());
		variables.put("spied_channel", ((MixSoundEvent) event).getChannel());
		variables.put("spied_unique_id", ((MixSoundEvent) event).getUniqueId());

		callLocalActionHandler.handle(variables);

	}

}
