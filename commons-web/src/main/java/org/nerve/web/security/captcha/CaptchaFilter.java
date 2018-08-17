package org.nerve.web.security.captcha;

import org.nerve.exception.ServiceException;
import org.nerve.web.security.AuthConfig;
import org.nerve.web.security.handler.LoginFailHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * PROJECT		jiepai-manage
 * PACKAGE		com.nerve.component.security.verification
 * FILE			VerificationFilter.java
 * Created by 	zengxm on 2018/5/10.
 */
public class CaptchaFilter extends OncePerRequestFilter {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	final AuthConfig config;

	OrRequestMatcher matcher;

	private AuthenticationFailureHandler handler = new LoginFailHandler();

	public CaptchaFilter(AuthConfig config) {
		this.config = config;
		init();
	}

	public void init() {
		logger.debug("init matcher....{}", config);
		matcher = new OrRequestMatcher(new AntPathRequestMatcher(config.getLoginPostPage()));
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain chain) throws ServletException, IOException {

		if(matcher.matches(request)) {
			CaptchaConfig vConfig = config.getCaptcha();
			logger.debug("check verification code...");
			Code code = (Code) WebUtils.getSessionAttribute(request, vConfig.parameter);
			String codeInRequest = request.getParameter(vConfig.parameter);

			try{
				if(Objects.isNull(code))
					throw new VerificationException(vConfig.msgBad);

				if(StringUtils.isBlank(codeInRequest))
					throw new VerificationException(vConfig.msgRequire);

				//判断是否过期
				if(code.isExpired())
					throw new VerificationException(vConfig.msgExpire);

				if((vConfig.sensitive && StringUtils.equals(codeInRequest, code.value))
						|| (!vConfig.sensitive && StringUtils.equalsIgnoreCase(codeInRequest, code.value))){

					logger.debug("verification passed!");
				}else{
					logger.debug("bad verification code : (sensitive = {}) expected {} but got {}", vConfig.sensitive, code.value, codeInRequest);
					throw new VerificationException(vConfig.msgBad);
				}
			}catch (VerificationException e){
				handler.onAuthenticationFailure(request, response, e);
				return;
			}
		}

		chain.doFilter(request, response);
	}

	public CaptchaFilter setHandler(AuthenticationFailureHandler handler) {
		if(Objects.isNull(handler))
			throw new ServiceException(getClass()+" : AuthenticationFailureHandler must not be null!");
		logger.debug("use fail-handler: ", handler);
		this.handler = handler;
		return this;
	}

	class VerificationException extends AuthenticationException {
		VerificationException(String msg) {
			super(msg);
		}
	}
}
