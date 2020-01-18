package com.dongverine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dongverine.component.CustomLoggerFactory;

import ch.qos.logback.classic.Logger;

@Controller
public class RestController {
	@Autowired
	CustomLoggerFactory clogfactory;
	
	@RequestMapping( value="/log", method = RequestMethod.GET)
	@ResponseBody
	public String getLog(String param) {
		if(param!=null) {
			Logger log = clogfactory.createLoggerGivenFileName(RestController.class, param);
			log.info("aaaaa");
		}
		
		return "ok => param:"+param;
	}
}
