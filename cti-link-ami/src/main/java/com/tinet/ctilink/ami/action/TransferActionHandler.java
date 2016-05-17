package com.tinet.ctilink.ami.action;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.tinet.ctilink.ami.inc.AmiParamConst;


/**
 * 转移
 * 
 * @author tianzp
 */
@Component
public class TransferActionHandler extends AbstractActionHandler {

	@Override
	public String getAction() {
		return AmiParamConst.TRANSFER;
	}

	@Override
	public AmiActionResponse handle(Map<String, String> params) {
		logger.info("handle {} action : {}", this.getAction(), params);

/*		CtiAgent ctiAgent = getCtiAgent(params);
		if (ctiAgent == null) {
			return ERROR_BAD_PARAM;
		}

		String transferObject = params.get(AmiAction.VARIABLE_TRANSFER_OBJECT);// 转移的对象
		String objectType = params.get(AmiAction.VARIABLE_OBJECT_TYPE); // 0：普通电话1：座席号
																		// 2：IVR
																		// id+IVR
																		// 节点
		String extension = objectType + transferObject + "#"; // AA1 shuold be

		if (StringUtils.isEmpty(ctiAgent.getChannel())) {
			AmiActionResponse.createFailResponse(9,"坐席不在通话中");
		}

		FeatureAction transferAction = new FeatureAction();
		transferAction.setChannel(ctiAgent.getChannel());
		transferAction.setContext(Const.DIALPLAN_CONTEXT_TRANSFER);
		transferAction.setExtension(extension);
		transferAction.setFeature("blindxfer");

		if (sendAction(transferAction) == null) {
			return ERROR_EXCEPTION;
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
