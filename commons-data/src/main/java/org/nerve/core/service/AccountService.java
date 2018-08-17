package org.nerve.core.service;

import org.nerve.auth.Account;
import org.nerve.auth.AccountRepo;
import org.nerve.service.CommonService;

/**
 * Created by zengxm on 2017/8/23.
 */
public interface AccountService extends CommonService<Account,AccountRepo> {
	/**
	 * 修改密码
	 * @param id
	 * @param pwd
	 */
	void changePwd(String id, String pwd);
}
