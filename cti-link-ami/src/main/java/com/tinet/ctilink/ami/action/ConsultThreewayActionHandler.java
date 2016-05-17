package com.tinet.ctilink.ami.action;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.inc.AmiParamConst;

/**
 * 咨询三方
 * 
 * @author tianzp
 */
@Component
public class ConsultThreewayActionHandler extends AbstractActionHandler {

	private ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();

	@Override
	public String getAction() {
		return AmiParamConst.CONSULT_THREEWAY;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.info("handle {} action : {}", this.getAction(), params);

		/*CtiAgent ctiAgent = getCtiAgent(params);
		if (ctiAgent == null) {
			return ERROR_BAD_PARAM;
		}

		if (StringUtils.isEmpty(ctiAgent.getChannel())) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NO_CHANNEL, "no channel");
		}

		if (StringUtils.isEmpty(ctiAgent.getConsultChannel())) {
			return AmiActionResponse.createFailResponse(AmiAction.ERRORCODE_NO_CHANNEL, "no consult channel");
		}

		SetVarAction setVarAction = new SetVarAction(ctiAgent.getConsultChannel(), Const.CONSULT_THREEWAY_CHAN,
				ctiAgent.getChannel());
		RedirectAction redirectAction = new RedirectAction();
		redirectAction.setChannel(ctiAgent.getConsultChannel());
		redirectAction.setContext(Const.DIALPLAN_CONTEXT_CONSULT_THREEWAY);
		redirectAction.setExten(ctiAgent.getCid());
		redirectAction.setPriority(1);

		if (sendAction(setVarAction) == null || sendAction(redirectAction) == null) {
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

	@Override
	public AmiActionResponse handle(Map<String, String> fixParams, Map<String, String> optionalParams) {
		// TODO Auto-generated method stub
		return null;
	}

}
