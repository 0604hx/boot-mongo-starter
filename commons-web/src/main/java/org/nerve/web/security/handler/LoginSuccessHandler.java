package org.nerve.web.security.handler;

import com.alibaba.fastjson.JSON;
import org.nerve.auth.Account;
import org.nerve.common.Result;
import org.nerve.web.security.LoginUser;
import org.nerve.web.security.jwt.TokenHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * com.zeus.web.security.handler
 * Created by zengxm on 2017/8/23.
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	TokenHelper tokenHelper;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		Account account = ((LoginUser)authentication.getPrincipal()).getAccount();

		/*
		生成 token
		保存的是 Account 对象，在 Filter 中需要反序列化到 POJO
		 */
		String token = tokenHelper.generateToken(account);

		logger.debug("--------------- LOGIN SUCCESS ---------------");
		logger.debug("login name: {}", account.getName());
		logger.debug("token: {}", token);
		logger.debug("--------------- LOGIN SUCCESS ---------------");

		response.getWriter().print(JSON.toJSONString(Result.ok(token)));
	}

}

