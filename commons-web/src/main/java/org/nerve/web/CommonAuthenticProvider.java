package org.nerve.web;

import org.nerve.auth.Account;
import org.nerve.auth.AuthenticProvider;
import org.nerve.web.security.LoginUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * com.zeus.web
 * Created by zengxm on 2017/8/23.
 */
@Service
@ConditionalOnWebApplication
public class CommonAuthenticProvider implements AuthenticProvider {

	@Value("${zeus.msg.noLogin:尚未登录}")
	String noLoginMsg;

	@Override
	public Account get() {
		try{
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(principal instanceof LoginUser)
				return ((LoginUser) principal).getAccount();

			throw new RuntimeException(noLoginMsg);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getId() {
		return get().getId();
	}
}
