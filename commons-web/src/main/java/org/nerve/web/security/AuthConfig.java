package org.nerve.web.security;

import org.nerve.web.security.captcha.CaptchaConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * com.zeus.web.security
 * Created by zengxm on 2017/8/23.
 */
@Configuration
@ConfigurationProperties(prefix = "nerve.security")
public class AuthConfig {
	final static String SPLIT = ",";
	/**
	 * rememberMe 的有效时间，单位为秒
	 */
	int expire = 15768000;

	Map<String,String> map;

	/**
	 * 静态资源列表
	 */
	String statics = "/,/index.html,/error,/static/**,/public/**,/token,/app.html,/mobile.html,/captcha-code,/favicon.ico";

	/**
	 * 额外的大众授权的路径
	 */
	String popular = "";

	/**
	 * 是否开启 Remember ME
	 */
	boolean rememberMe = false;

	/**
	 * 默认的登录页面
	 */
	String loginPage = "/come-on-boy";
	String loginPostPage = "/come-on-boy";

	/**
	 * remember-me 的 cookie 名称
	 */
	String cookie = "nerve-boot-starter";

	CaptchaConfig captcha = new CaptchaConfig();

	public int getExpire() {
		return expire;
	}

	public AuthConfig setExpire(int expire) {
		this.expire = expire;
		return this;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public AuthConfig setMap(Map<String, String> map) {
		this.map = map;
		return this;
	}

	public String getStatics() {
		return statics;
	}

	public AuthConfig setStatics(String statics) {
		this.statics = statics;
		return this;
	}

	public String getPopular() {
		return popular;
	}

	public AuthConfig setPopular(String popular) {
		this.popular = popular;
		return this;
	}

	public boolean isRememberMe() {
		return rememberMe;
	}

	public AuthConfig setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
		return this;
	}

	public String getLoginPage() {
		return loginPage;
	}

	public AuthConfig setLoginPage(String loginPage) {
		this.loginPage = loginPage;
		return this;
	}

	public String getLoginPostPage() {
		return loginPostPage;
	}

	public AuthConfig setLoginPostPage(String loginPostPage) {
		this.loginPostPage = loginPostPage;
		return this;
	}

	public CaptchaConfig getCaptcha() {
		return captcha;
	}

	public AuthConfig setCaptcha(CaptchaConfig captcha) {
		this.captcha = captcha;
		return this;
	}

	public String getCookie() {
		return cookie;
	}

	public AuthConfig setCookie(String cookie) {
		this.cookie = cookie;
		return this;
	}
}
