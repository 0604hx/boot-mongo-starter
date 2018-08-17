package org.nerve.web.security.handler;

import org.nerve.common.Result;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * com.zeus.web.security.handler
 * Created by zengxm on 2017/8/23.
 */
public class LoginRequiredEntryPoint extends CommonResultEntryPoint {
	@Override
	protected void onResult(Result result, HttpServletRequest req, HttpServletResponse resp, AuthenticationException e) {
		result.setSuccess(false).setMessage("Login Required");

		resp.setStatus(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value());
	}
}
