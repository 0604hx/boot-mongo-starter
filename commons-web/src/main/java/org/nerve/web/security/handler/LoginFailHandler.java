package org.nerve.web.security.handler;

import com.alibaba.fastjson.JSON;
import org.nerve.common.Result;
import org.nerve.exception.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * com.zeus.web.security.handler
 * Created by zengxm on 2017/8/23.
 */
public class LoginFailHandler implements AuthenticationFailureHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e)
			throws IOException {
		logger.info("LOGIN FAIL:"+e.getMessage());

//		Result result = new Result();
//		result.setSuccess(false).setMessage(Exceptions.LOGIN_FAIL);
//
		//解决中文乱码问题
		httpServletResponse.setContentType("application/json;charset=UTF-8");
		httpServletResponse.getWriter().print(JSON.toJSONString(Result.fail(e)));
	}
}
