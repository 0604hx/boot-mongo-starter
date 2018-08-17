package org.nerve.web.security;

import org.nerve.auth.Account;
import org.nerve.auth.AccountRepo;
import org.nerve.web.IpDetector;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;

/**
 * 从数据库中读取用户信息
 * com.zeus.web.security
 * Created by zengxm on 2017/8/23.
 */
@Service
public class MongoUserDetailsService implements UserDetailsService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private AccountRepo accountRepo;
	@Autowired(required = false)
	private IpDetector ipDetector;

	@Autowired
	private BossConfig bossConfig;

	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		//根据名字查询用户
		Account account = accountRepo.findOne(Criteria.where("name").is(s));
		if(account!=null){
			onLogin(account);

			logger.info("{} try to login [IP={}][ROLES={}]", s, account.getLoginIp(), account.getRoles());

			return new LoginUser(account, account.getRoleList());
		}else{
			if(bossConfig.enable){
				logger.info("BOSS mode enable, try to login with BOSS Identity ...");
				if(StringUtils.equals(s, bossConfig.name))
					return new LoginUser(bossConfig.build(), Collections.emptySet());
			}
		}

		throw new IllegalArgumentException(String.format("username not exist:%s", s));
	}

	private void onLogin(Account account){
		accountRepo.save(
				account.setLoginIp(ipDetector!=null?ipDetector.getIp():null)
						.setLoginDate(new Date())
		);
	}
}
