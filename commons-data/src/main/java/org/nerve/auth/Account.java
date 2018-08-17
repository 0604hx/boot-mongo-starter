package org.nerve.auth;

import com.alibaba.fastjson.JSON;
import org.nerve.domain.DateEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

/**
 * com.zeus.auth
 * Created by zengxm on 2017/8/22.
 */
@Document
public class Account extends DateEntity {

	private String name;
	private String password;
	private String summary;

	/**
	 * 用户角色
	 */
	private String roles;

	private String loginIp;
	private Date loginDate;

	/**
	 * 最近一次密码修改时间
	 */
	private Date pwdResetDate;

	public Account(){}

	public Account(String name){
		this.name = name;
	}

	public Date getPwdResetDate() {
		return pwdResetDate;
	}

	public Account setPwdResetDate(Date pwdResetDate) {
		this.pwdResetDate = pwdResetDate;
		return this;
	}

	public String getName() {
		return name;
	}

	public Account setName(String name) {
		this.name = name;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public Account setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getSummary() {
		return summary;
	}

	public Account setSummary(String summary) {
		this.summary = summary;
		return this;
	}

	public Set<String> getRoleList(){
		if(StringUtils.isNotBlank(roles))
			return JSON.parseObject(roles.startsWith("[")?roles:String.format("['%s']", roles), Set.class);
		return Collections.EMPTY_SET;
	}
	public Account setRoleList(Collection<?> roleList){
		return setRoles(JSON.toJSONString(roleList));
	}

	public String getRoles() {
		return roles;
	}

	public Account setRoles(String roles) {
		this.roles = roles;
		return this;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public Account setLoginIp(String loginIp) {
		this.loginIp = loginIp;
		return this;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public Account setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
		return this;
	}
}
