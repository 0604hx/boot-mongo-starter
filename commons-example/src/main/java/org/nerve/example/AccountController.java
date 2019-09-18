package org.nerve.example;

import org.nerve.auth.Account;
import org.nerve.auth.AccountRepo;
import org.nerve.core.service.AccountService;
import org.nerve.example.event.AccountVisitEvent;
import org.nerve.web.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * com.zeus.example
 * Created by zengxm on 2017/8/23.
 */
@RestController
@RequestMapping("account")
public class AccountController extends AbstractController<Account, AccountRepo, AccountService>{

	@Autowired
	ApplicationContext context;

	/**
	 * 发送 ACCOUNT.INDEX 事件
	 *
	 * 由相应的 Consumer 消费
	 * @param name
	 * @return
	 */
	@RequestMapping("visit/{name}")
	public String visit(@PathVariable String name){
		System.out.println("用户名："+name);

		context.publishEvent(new AccountVisitEvent(name));

		return name;
	}
}
