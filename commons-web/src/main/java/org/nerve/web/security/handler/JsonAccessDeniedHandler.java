package org.nerve.web.security.handler;

import com.alibaba.fastjson.JSON;
import org.nerve.common.Result;
import org.nerve.exception.Exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * com.zeus.web.security.handler
 * Created by zengxm on 2017/8/23.
 */
public class JsonAccessDeniedHandler implements AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response, AccessDeniedException e)
			throws IOException, ServletException {
		Result result = new Result();
		result.setSuccess(false).setMessage(Exceptions.ACCESS_DENIED);

		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setCharacterEncoding("utf-8");
		response.getWriter().print(JSON.toJSONString(result));
	}
}
