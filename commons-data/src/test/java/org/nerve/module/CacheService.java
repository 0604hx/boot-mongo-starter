package org.nerve.module;

import org.nerve.auth.Account;
import org.nerve.utils.DateUtils;
import org.nerve.utils.encode.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * PROJECT		fire-eye-manage
 * PACKAGE		com.zeus.module
 * FILE			CacheService.java
 * Created by 	zengxm on 2017/12/25.
 */
@Service
public class CacheService {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Cacheable
	public Account getAccount(String id){
		logger.info("获取 Account， id={}", id);

		return new Account(id).setPassword(MD5Util.getStringMD5(System.nanoTime()+""));
	}

	/**
	 * 默认 5 秒后失效的 cache
	 * @param id
	 * @return
	 */
	@Cacheable("controller")
	public Account getAccountOnControllerCache(String id){
		return new Account(id).setPassword(DateUtils.getDateTime());
	}
}
