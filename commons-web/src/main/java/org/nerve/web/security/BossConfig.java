package org.nerve.web.security;

import org.nerve.auth.Account;
import org.nerve.utils.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;

/**
 * PROJECT		fire-eye-manage
 * PACKAGE		com.zeus.web.security
 * FILE			BossConfig.java
 * Created by 	zengxm on 2017/12/18.
 */
@Configuration
@ConfigurationProperties(prefix = "zeus.boss")
public class BossConfig {
	boolean enable=true;
	String name = "boss";
	String password = "";

	@PostConstruct
	private void init(){
		if(this.enable){
			System.out.println("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 开启 BOSS 模式 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
			String pwd = StringUtils.getRandomNumbersAndLetters(32);
			System.out.println("password: "+pwd);
			System.out.println("(Set zeus.boss.enable=false if you want disable BOSS mode)");
			this.password = new BCryptPasswordEncoder().encode(pwd);
			System.out.println("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 开启 BOSS 模式 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
		}
	}


	public Account build(){
		Account account = new Account(name);
		account.setId(System.currentTimeMillis()+"");
		account.setPassword(password);

		return account;
	}
}
