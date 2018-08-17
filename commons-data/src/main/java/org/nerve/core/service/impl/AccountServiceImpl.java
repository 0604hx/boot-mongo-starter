package org.nerve.core.service.impl;

import org.nerve.auth.Account;
import org.nerve.auth.AccountRepo;
import org.nerve.core.service.AccountService;
import org.nerve.enums.Fields;
import org.nerve.exception.Exceptions;
import org.nerve.exception.ServiceException;
import org.nerve.service.CommonServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * com.zeus.core.service.impl
 * Created by zengxm on 2017/8/23.
 */
@Service
public class AccountServiceImpl extends CommonServiceImpl<Account,AccountRepo> implements AccountService{
	BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();

	/**
	 * 最短密码限制
	 */
	@Value("${zeus.account.pwdLength:6}")
	int pwdMinLength = 6;

	@Override
	public boolean onBeforeSave(Account account) {
		Assert.hasText(account.getName(), "用户名不能为空");
		if(!account.using()){
			//判断是否名称重复了
			Account oldAccount = repo.findOne(Criteria.where(Fields.NAME.value()).is(account.getName()));
			if(oldAccount!=null){
				throw new ServiceException(String.format("%s 已经存在，不能重复增加！", account.getName()));
			}

			checkPwd(account.getPassword());

			//加密密码
			account.setPassword(encoder.encode(account.getPassword()));
		}
		return true;
	}

	@Override
	protected List<String> ignorePropertiesOnSave() {
		return Arrays.asList("password");
	}

	/**
	 * 验证密码
	 *
	 * @param pwd
	 */
	private void checkPwd(String pwd){
		Assert.hasText(pwd, Exceptions.USER_PWD_NOT_EMPTY);
		Assert.isTrue(pwd.length()>=pwdMinLength, Exceptions.USER_PWD_MIN_LENGTH+ pwdMinLength);
	}

	@Override
	public void changePwd(String id, String pwd) {
		//检测密码
		checkPwd(pwd);

		Account account = get(id);
		account.setPassword(encoder.encode(pwd));
		account.setPwdResetDate(new Date());

		repo.save(account);
		log.info("update [ID={}] password success!", id);
	}
}
