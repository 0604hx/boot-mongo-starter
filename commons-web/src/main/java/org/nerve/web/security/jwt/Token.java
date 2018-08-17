package org.nerve.web.security.jwt;

import org.nerve.domain.DateEntity;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * PROJECT		boot-mongo-starter
 * PACKAGE		org.nerve.web.security.jwt
 * FILE			Token.java
 * Created by 	zengxm on 2018/8/16.
 */
@Document(collection = "account_token")
public class Token extends DateEntity {
	String username;
	String value;

	public String getUsername() {
		return username;
	}

	public Token setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getValue() {
		return value;
	}

	public Token setValue(String value) {
		this.value = value;
		return this;
	}
}
