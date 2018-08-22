package org.nerve.web.component;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.nerve.auth.Account;
import org.nerve.auth.AccountRepo;
import org.nerve.core.domain.Setting;
import org.nerve.core.repo.SettingRepo;
import org.nerve.enums.LogType;
import org.nerve.sys.L;
import org.nerve.util.FileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 初始化超级用户
 *
 * PROJECT		boot-mongo-starter
 * PACKAGE		org.nerve.web.component
 * FILE			AccountInitComponent.java
 * Created by 	zengxm on 2018/8/21.
 */
@Component
@ConditionalOnProperty(name ="nerve.security.init",matchIfMissing = true, havingValue = "true")
public class SystemInitComponent {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final AccountRepo accountRepo;
	private final SettingRepo settingRepo;

	@Value("${nerve.security.name:admin}")
	String adminName = "";
	@Value("${nerve.security.password:ironman@123456.}")
	String adminPwd = "";

	public SystemInitComponent(AccountRepo accountRepo, SettingRepo settingRepo) {
		this.accountRepo = accountRepo;
		this.settingRepo = settingRepo;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void init(){
		createAdmin();
		initSetting();
	}


	private void createAdmin(){
		if(accountRepo.count() == 0L){
			logger.info("检测到管理员账号为空，现在进行 {} 账号的初始化 (密码可以通过 nerve.security.password 进行配置)...", adminName);

			Account account = new Account(adminName);
			account.setPassword(new BCryptPasswordEncoder().encode(adminPwd))
					.setRoles("ADMIN")
					.setSummary("Admin Account created by SYSTEM_INIT !")
					.setAddDate(new Date())
			;

			accountRepo.save(account);
			logger.info("$adminName 账号初始化成功!");
			L.log(LogType.SYSTEM, adminName+" 账号初始化成功!  (密码可以通过 nerve.security.password 进行配置)");
		}
	}

	private void initSetting(){
		logger.info("init default Setting...");

		try{
			String jsonContent = FileLoader.loadAsString("init.setting.json");
			List<Setting> initSettings = JSON.parseArray(jsonContent, Setting.class);

			Date date = new Date();
			initSettings.stream()
					.filter(setting -> settingRepo.countByUuid(setting.getUuid()) == 0)
					.peek(setting -> {
						setting.setUpdateDate(date).setAddDate(date);
						if(StringUtils.isEmpty(setting.getContent()))
							setting.setContent(setting.getDefaultContent());
					})
					.forEach(setting -> {
						settingRepo.save(setting);
						logger.debug("检测到 {}({}) 的配置不存在，系统自动创建... ", setting.getUuid(), setting.getTitle());
						L.log(
								LogType.SYSTEM.value(),
								String.format("[初始化] 创建配置项 %s( %s ) : %s",
										setting.getTitle(), setting.getUuid(), setting.getSummary()),
								setting
						);
					})
			;
		}catch (IOException e){
			logger.error("无法初始化 Setting ：{}", ExceptionUtils.getMessage(e));
		}
	}
}
