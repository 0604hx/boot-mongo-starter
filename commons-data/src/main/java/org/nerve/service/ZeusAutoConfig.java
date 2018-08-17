package org.nerve.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.Environment;
import reactor.bus.EventBus;

/**
 * com.zeus.service
 * Created by zengxm on 2017/9/21.
 */
@Configuration
public class ZeusAutoConfig {

	Logger logger = LoggerFactory.getLogger(ZeusAutoConfig.class);

	/**
	 * 配置默认的 EventBus
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(EventBus.class)
	public EventBus eventBus(){
		Environment environment = Environment.initializeIfEmpty().assignErrorJournal();
		logger.info("检测到未定义 EventBus，使用默认配置 , environment={} ...", environment);

		return EventBus.create(environment, Environment.THREAD_POOL);
	}

}
