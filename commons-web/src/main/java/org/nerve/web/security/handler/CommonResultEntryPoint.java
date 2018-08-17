package org.nerve.web.security.handler;

import com.alibaba.fastjson.JSON;
import org.nerve.common.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * com.zeus.web.security.handler
 * Created by zengxm on 2017/8/23.
 */
abstract public class CommonResultEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e)
			throws IOException, ServletException {
		Result result = new Result();
		onResult(result, httpServletRequest,httpServletResponse, e);
		httpServletResponse.getWriter().write(JSON.toJSONString(result));
	}

	protected abstract void onResult(Result result, HttpServletRequest req, HttpServletResponse resp, AuthenticationException e);
}
