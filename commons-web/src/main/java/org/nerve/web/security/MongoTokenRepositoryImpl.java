package org.nerve.web.security;

import org.nerve.enums.Fields;
import org.nerve.web.security.jwt.Token;
import org.nerve.web.security.jwt.TokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * PROJECT		boot-mongo-starter
 * PACKAGE		org.nerve.web.security
 * FILE			MongoTokenRepositoryImpl.java
 * Created by 	zengxm on 2018/8/16.
 */
@Component
public class MongoTokenRepositoryImpl implements PersistentTokenRepository {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	TokenRepository repo;

	@Override
	public void createNewToken(PersistentRememberMeToken token) {
		Token t = new Token();
		t.setId(token.getSeries());
		t.setUsername(token.getUsername())
				.setValue(token.getTokenValue())
				.setAddDate(token.getDate());
		repo.save(t);

		logger.debug("create new token for {}: {}", t.getUsername(), t.getValue());
	}

	@Override
	public void updateToken(String series, String tokenValue, Date lastUsed) {
		repo.updateFirst(
				Query.query(Criteria.where(Fields.ID.value()).is(series)),
				Update.update("value", tokenValue).set("addDate", lastUsed)
		);
		logger.debug("update token for {}: {}", series, tokenValue);
	}

	@Override
	public PersistentRememberMeToken getTokenForSeries(String seriesId) {
		try {
			Token token = repo.findOne(seriesId);
			return new PersistentRememberMeToken(token.getUsername(), token.id(), token.getValue(), token.getAddDate());
		}catch (Exception e){
			logger.debug("fail on load token : {}", e.getMessage());
			return null;
		}
	}

	@Override
	public void removeUserTokens(String username) {
		repo.delete(Criteria.where("username").is(username));
		logger.debug("delete token for {}", username);
	}
}
