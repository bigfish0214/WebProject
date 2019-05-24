package com.example.wenda.aspect;


import java.util.Date;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {
	private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);
	@Before("execution (* com.example.wenda.controller.*.*(..))")
	public void beforeMethod(JoinPoint joinPoint) {
		StringBuilder sb = new StringBuilder();
		for(Object arg : joinPoint.getArgs()) {
			if(arg != null) {
				sb.append("args:"+arg.toString() + "|");
			}
		}
		
		logger.info("before method " + sb.toString());
	}
	
	@After("execution (* com.example.wenda.controller.*.*(..))")
	public void afterMethod() {
		logger.info("atfter method" + new Date());
	}
}
