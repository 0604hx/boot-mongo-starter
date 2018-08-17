package org.nerve.auth;

/**
 * Created by zengxm on 2017/8/22.
 */
public interface AuthenticProvider {

	/**
	 * 获取当前登录的用户
	 * @return
	 */
	Account get();

	/**
	 * 获取当前登录用户ID，如果不存在，则返回0
	 * @return
	 */
	String getId();
}
