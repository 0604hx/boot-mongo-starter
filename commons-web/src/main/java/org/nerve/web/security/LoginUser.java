package org.nerve.web.security;


import org.nerve.auth.Account;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collections;
import java.util.Set;

/**
 * com.zeus.ircp.examples.manager.component.security
 * Created by zengxm on 11/24/2016.
 */
public class LoginUser extends org.springframework.security.core.userdetails.User {

	private Account account;

	public LoginUser(Account account){
		super(
				account.getName(),
				account.getPassword(),
				Collections.emptyList()
		);
		this.account=account;
	}
	
	public LoginUser(Account account, Set<String> roles){
		super(
				account.getName(),
				account.getPassword(),
				roles==null?
						Collections.emptyList()
						:
						AuthorityUtils.createAuthorityList(roles.stream().toArray(String[]::new))
		);

		this.account=account;
	}

	public Account getAccount() {
		return account;
	}

	public LoginUser setAccount(Account account) {
		this.account = account;
		return this;
	}

	public String getId(){
		return account.getId();
	}
}
