package com.stackroute.keepnote.aspectj;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/* Annotate this class with @Aspect and @Component */
@Component
@Aspect
public class LoggingAspect {
	private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

	/*
	 * Write loggers for each of the methods of Reminder controller, any particular method
	 * will have all the four aspectJ annotation
	 * (@Before, @After, @AfterReturning, @AfterThrowing).
	 */
	@Before("execution(* com.stackroute.keepnote.controller.ReminderController.*(..))")
	public void before(){
		logger.info("@Before:");
	}
	
	@After("execution(* com.stackroute.keepnote.controller.ReminderController.*(..))")
	public void after(){
		logger.info("After");
	}
	
	@AfterThrowing(pointcut = "execution(* com.stackroute.keepnote.controller.ReminderController.*(..))",
			throwing="exception")
	public void afterThrowing(Exception exception){
		logger.info("AfterReturning");
		logger.info("Exception caught:"+ exception.getMessage());
	}
	
	@AfterReturning(pointcut = "execution(* com.stackroute.keepnote.controller.ReminderController.*(..))",
			returning="val")
	public void afterReturning(Object val){
		logger.info("Return"+ val);
		logger.info("AfterReturning");
	}
}
