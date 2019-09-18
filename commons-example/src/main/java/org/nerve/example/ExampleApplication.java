package org.nerve.example;

import org.nerve.example.event.AccountVisitEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * com.zeus.example
 * Created by zengxm on 2017/8/23.
 */
@ComponentScan("org.nerve")      //扫描com.zeus包不然无法加载Spring bean
@SpringBootApplication
@EnableAsync
public class ExampleApplication {

	final Logger logger = LoggerFactory.getLogger(getClass());

	public static void main(String[] args) {
		SpringApplication.run(ExampleApplication.class, args);
	}


	@Async
	@EventListener
	public void accountVisitListener(AccountVisitEvent event){
		logger.info("监听到 AccountVisitEvent : name={}", event.getName());
	}
}
