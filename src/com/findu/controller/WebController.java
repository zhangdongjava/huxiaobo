package com.findu.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * web网页接口，通过AuthInterceptor做消息鉴权。
 */
@Controller
@RequestMapping(AuthInterceptor.BASE_PATH)
public class WebController {

	private static Logger logger = Logger.getLogger(WebController.class);
	
	
}
