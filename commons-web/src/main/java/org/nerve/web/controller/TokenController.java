package org.nerve.web.controller;

import com.alibaba.fastjson.JSON;
import org.nerve.auth.Account;
import org.nerve.auth.AuthenticProvider;
import org.nerve.common.Result;
import org.nerve.exception.Exceptions;
import org.nerve.exception.ServiceException;
import org.nerve.web.security.LoginUser;
import org.nerve.web.security.jwt.TokenHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.isTrue;

/**
 * com.zeus.web.controller
 * Created by zengxm on 2017/8/23.
 */
@RestController
public class TokenController extends BaseController{
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private AuthenticProvider authProvider;

	@Autowired
	private TokenHelper tokenHelper;

	@Autowired
	private UserDetailsService userDetailsService;

	/**
	 * 获取当前登录的用户信息
	 * @param detail    是否获取完整的信息
	 * @return
	 */
	@RequestMapping("whoami")
	public Result aboutMe(boolean detail){
		Result result = new Result();
		Account account = authProvider.get();

		if(account != null){
			//如果是获取完整的用户信息
			if(detail){
				result.setData(account);
			}else {
				Account simpleAccount = new Account();
				simpleAccount.setId(account.getId());
				simpleAccount.setName(account.getName());

				result.setData(simpleAccount);
			}
		}else
			result.error(new ServiceException(Exceptions.NO_LOGIN));
		return result;
	}

	/**
	 * 刷新 JWT 的token
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/token/refresh")
	public Result refreshAuthenticationToken(HttpServletRequest request, HttpServletResponse response) {
		return result(re->{
			String authToken = tokenHelper.getToken( request );
			hasText(authToken, "existed token is required while get a new token!");

			String tokenData = tokenHelper.getSubjectFromToken(authToken);
			//转换成 Account 对象
			Account account = JSON.parseObject(tokenData, Account.class);
			//查询最新的 Account 对象
			UserDetails user = userDetailsService.loadUserByUsername(account.getName());
			Account lastAccount = ((LoginUser)user).getAccount();

			//核对 ID
			isTrue(lastAccount.equals(account), "Account information has been updated and you need to verify again!");

			//判断是否能够更新 token
			isTrue(tokenHelper.canTokenBeRefreshed(authToken,lastAccount.getPwdResetDate()),"This token is expired or invalid!");

			//创建新的 token
			String newToken = tokenHelper.generateToken(lastAccount);
			logger.debug("{} refresh token to : {}", lastAccount.getName(), newToken);

			re.setData(newToken);
		});
	}
}

