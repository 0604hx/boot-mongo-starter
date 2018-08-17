package org.nerve.web.security.captcha;

/**
 * PROJECT		jiepai-manage
 * PACKAGE		com.nerve.component.security.verification
 * FILE			Code.java
 * Created by 	zengxm on 2018/5/10.
 */
public class Code {
	String value;
	long expireOn;

	public Code(String value){
		//默认 5 分钟过期
		this(value, System.currentTimeMillis() + 5*60*1000);
	}

	public Code(String value, long expireOn){
		this.value = value;
		this.expireOn = expireOn;
	}

	public boolean isExpired(){
		return System.currentTimeMillis()>=expireOn;
	}
}
