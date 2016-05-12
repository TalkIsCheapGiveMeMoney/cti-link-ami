package com.tinet.ctilink.ami.event;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.userevent.ConsultTransferEvent;
import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.AmiAction;
import com.tinet.ctilink.ami.inc.AmiEventConst;


/**
 * 咨询转接事件
 * @author tianzp
 */
@Component
public class ConsultTransferEventHandler extends AbstractAmiEventHandler implements AmiUserEventHandler {
	
	@Override
	public Class<?> getEventClass() {
		return ConsultTransferEvent.class;
	}

	@Override
	public void handle(ManagerEvent event) {
		logger.info("handle {} : {}.", this.getEventClass().getSimpleName(), event);
		
		String enterpriseId = ((ConsultTransferEvent) event).getEnterpriseId();
		String cno = ((ConsultTransferEvent) event).getCno();
		String consulterCno = ((ConsultTransferEvent) event).getConsulterCno();
		
		if (StringUtils.isNotEmpty(consulterCno)) {
			Map<String, String> userEvent = new HashMap<String, String>();
			userEvent.put("type", AmiAction.VARIABLE_EVENT);
			userEvent.put(AmiAction.VARIABLE_NAME, AmiEventConst.CONSULT_TRANSFER);
			userEvent.put(AmiAction.VARIABLE_ENTERPRISE_ID, enterpriseId);
			userEvent.put(AmiAction.VARIABLE_CNO, cno);
			userEvent.put(AmiAction.VARIABLE_CONSULTER_CNO, consulterCno);
			publishEvent(userEvent);
		}
	}
}
