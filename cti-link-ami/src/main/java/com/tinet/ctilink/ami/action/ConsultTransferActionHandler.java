package com.tinet.ctilink.ami.action;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.tinet.ctilink.ami.AmiAction;
import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.manager.action.HangupAction;
import org.springframework.stereotype.Component;


/**
 * 咨询转接
 * 
 * @author tianzp
 */
@Component
public class ConsultTransferActionHandler extends AbstractActionHandler {

	private ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();

	@Override
	public String getAction() {
		return AmiAction.CONSULT_TRANSFER;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.info("handle {} action : {}", this.getAction(), params);

	/*	CtiAgent ctiAgent = getCtiAgent(params);
		if (ctiAgent == null) {
			return ERROR_BAD_PARAM;
		}

		if (StringUtils.isEmpty(ctiAgent.getChannel())) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NO_CHANNEL, "no channel");
		}

		HangupAction hangupAction = new HangupAction(ctiAgent.getChannel());
		hangupAction.setCause(new Integer(3));

		if (sendAction(hangupAction) == null) {
			return ERROR_EXCEPTION;
		} else {
			String limitTimeSecond = params.get(AmiAction.VARIABLE_LIMIT_TIME_SECOND);
			String limitTimeAlertSecond = params.get(AmiAction.VARIABLE_LIMIT_TIME_ALERT_SECOND);
			String limitTimeFile = params.get(AmiAction.VARIABLE_LIMIT_TIME_FILE);

			if (StringUtils.isNotEmpty(limitTimeSecond)) {
				if (StringUtils.isEmpty(limitTimeAlertSecond)) {
					limitTimeAlertSecond = "60";
				}
				Integer alertSecond = Integer.parseInt(limitTimeAlertSecond);
				Integer limitSecond = Integer.parseInt(limitTimeSecond);
				if (alertSecond >= limitSecond) {
					alertSecond = 0;
				}
				if (StringUtils.isEmpty(limitTimeFile)) {
					limitTimeFile = "1_minute_left";
				}

				AmiLimitTimeTask task = new AmiLimitTimeTask();
				task.setChannel(ctiAgent.getConsultChannel());
				task.setFile(limitTimeFile);
				task.setAlertSecond(alertSecond);

				scheduledExecutor.schedule(task, (limitSecond - alertSecond), TimeUnit.SECONDS);
			}
		}
*/
		return SUCCESS;
	}
}
