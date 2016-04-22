package com.tinet.ctilink.ami.webcall;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * WebCall同步调用服务的实现
 * 
 * @author Jiangsl
 *
 */
@Service
public class WebCallServiceImp implements WebCallService {

	@Autowired
	private WebCallEngine webCallEngine;
	
	@Autowired
	private WebCallExecutor webCallExecutor;

	@Override
	public WebCallResponse callSync(WebCall webCall) {
		return webCallExecutor.execute(webCall);
	}

	@Override
	public WebCallResponse callAysnc(WebCall webCall) {
		webCallEngine.pushEvent(webCall);

		WebCallResponse response = new WebCallResponse();
		response.setResult(1);
		response.setDescription("提交成功");
		return response;
	}

}
