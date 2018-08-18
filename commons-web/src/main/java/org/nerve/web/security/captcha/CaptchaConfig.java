package org.nerve.web.security.captcha;

/**
 * PROJECT		jiepai-manage
 * PACKAGE		com.nerve.component.security.verification
 * FILE			VerificationConfig.java
 * Created by 	zengxm on 2018/5/10.
 */
public class CaptchaConfig {

	public static final int NUMBER = 0;     //纯数字
	public static final int LETTER = 1;     //纯字母
	public static final int MIXED = 2;      //数字、字母混合


	//默认不开启
	boolean enable = false;
	/*
	验证码长度
	 */
	int length = 6;
	int mode = NUMBER;
	/*
	验证码有效期，单位 秒
	 */
	int expire = 5 * 60;
	/*
	是否大小写敏感
	 */
	boolean sensitive = false;
	/*
	图片默认宽度
	 */
	int width = 15 * length;
	/*
	图片默认高度
	 */
	int height = 24;
	/*
	请求中的参数名称
	 */
	String parameter = "captcha";

	String msgBad = "验证码不正确";
	String msgExpire = "验证码已过期";
	String msgRequire = "验证码不能为空";

	public String getMsgBad() {
		return msgBad;
	}

	public CaptchaConfig setMsgBad(String msgBad) {
		this.msgBad = msgBad;
		return this;
	}

	public String getMsgExpire() {
		return msgExpire;
	}

	public CaptchaConfig setMsgExpire(String msgExpire) {
		this.msgExpire = msgExpire;
		return this;
	}

	public String getMsgRequire() {
		return msgRequire;
	}

	public CaptchaConfig setMsgRequire(String msgRequire) {
		this.msgRequire = msgRequire;
		return this;
	}

	public String getParameter() {
		return parameter;
	}

	public CaptchaConfig setParameter(String parameter) {
		this.parameter = parameter;
		return this;
	}

	public boolean isEnable() {
		return enable;
	}

	public CaptchaConfig setEnable(boolean enable) {
		this.enable = enable;
		return this;
	}

	public int getLength() {
		return length;
	}

	public CaptchaConfig setLength(int length) {
		this.length = length;
		return this;
	}

	public int getMode() {
		return mode;
	}

	public CaptchaConfig setMode(int mode) {
		this.mode = mode;
		return this;
	}

	public boolean isSensitive() {
		return sensitive;
	}

	public CaptchaConfig setSensitive(boolean sensitive) {
		this.sensitive = sensitive;
		return this;
	}

	public int getWidth() {
		return width;
	}

	public CaptchaConfig setWidth(int width) {
		this.width = width;
		return this;
	}

	public int getHeight() {
		return height;
	}

	public CaptchaConfig setHeight(int height) {
		this.height = height;
		return this;
	}
}
