package com.dongverine.component;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;
import ch.qos.logback.core.util.FileSize;

@Component
public class CustomLoggerFactory {

	@Value("${customlog.filepath}")
	String customLogPath;
	
	public Logger createLoggerGivenFileName(Class<?> klass, String file_log_name) {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

		RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<ILoggingEvent>();
		rollingFileAppender.setFile(customLogPath +"/"+ file_log_name + ".log");
		rollingFileAppender.setContext(lc);

		FixedWindowRollingPolicy rollingPolicy = new FixedWindowRollingPolicy();
		rollingPolicy.setContext(lc);
		rollingPolicy.setParent(rollingFileAppender);
		rollingPolicy
				.setFileNamePattern(customLogPath +"/"+ file_log_name + ".%i" + ".log");
		rollingPolicy.start();

		SizeBasedTriggeringPolicy<ILoggingEvent> triggeringPolicy = new SizeBasedTriggeringPolicy<ILoggingEvent>();
		triggeringPolicy.setMaxFileSize(FileSize.valueOf("10MB"));
		triggeringPolicy.start();

		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		encoder.setPattern("▶ %-5level %d{HH:mm:ss.SSS} [%thread] %logger[%method:%line] - %msg%n");
		encoder.setContext(lc);
		encoder.start();

		rollingFileAppender.setEncoder(encoder);
		rollingFileAppender.setRollingPolicy(rollingPolicy);
		rollingFileAppender.setTriggeringPolicy(triggeringPolicy);
		rollingFileAppender.start();

		Logger logger = (Logger) LoggerFactory.getLogger(klass);
		logger.addAppender(rollingFileAppender);
		logger.setLevel(Level.INFO);
		logger.setAdditive(false);
		return logger;
	}
}
