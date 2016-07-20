package com.findu.listener;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.findu.utils.ErrorCodeUtil;
import com.findu.utils.SysParamUtils;

@Component
public class SysInitListener implements ApplicationContextAware {
	
	private ApplicationContext context;


	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		context = arg0;
		SysParamUtils.initSystemParams(context);
		ErrorCodeUtil.initSysErrorCode(context);
	}

}
