package org.nerve.example;

import com.alibaba.fastjson.JSON;
import org.nerve.auth.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.Consumer;

import static reactor.bus.selector.Selectors.$;

/**
 * com.zeus.example
 * Created by zengxm on 2017/8/23.
 */
@ComponentScan("org.nerve")      //扫描com.zeus包不然无法加载Spring bean
@SpringBootApplication
public class ExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExampleApplication.class, args);
	}

	@Autowired
	EventBus eventBus;

	@Bean
	public Consumer<Event<Account>> accountEventConsumer(){
		Consumer<Event<Account>> consumer = accountEvent -> {
			Account account = accountEvent.getData();

			System.out.println("监听到 ACCOUNT.INDEX 事件："+ JSON.toJSONString(account));
		};

		eventBus.on($("ACCOUNT.INDEX"), consumer);
		return consumer;
	}

}
