package com.razz.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggerService {

	private final static Map<Class<?>, Logger> loggerMap;

	static {
		loggerMap = Collections.synchronizedMap( new HashMap<Class<?>, Logger>() );
	}
	
	public void trace(Class<?> clazz, String format, Object...args) {
		getLogger(clazz).trace( String.format(format, args) );
	}
	
	public void debug(Class<?> clazz, String format, Object...args) {
		getLogger(clazz).debug( String.format(format, args) );
	}
	
	public void info(Class<?> clazz, String format, Object...args) {
		getLogger(clazz).info( String.format(format, args) );
	}
	
	public void warn(Class<?> clazz, String format, Object...args) {
		getLogger(clazz).warn( String.format(format, args) );
	}
	
	public void error(Class<?> clazz, String format, Object...args) {
		getLogger(clazz).error( String.format(format, args) );
	}
	
	public void error(Class<?> clazz, Exception e, String format, Object...args) {
		getLogger(clazz).error(String.format(format, args), e);
	}
	
	public synchronized Logger getLogger(Class<?> clazz) {
		if( !loggerMap.containsKey(clazz) ) {
			final Logger logger = LoggerFactory.getLogger(clazz);
			loggerMap.put(clazz, logger);
		}
		return loggerMap.get(clazz);
	}
	
}
