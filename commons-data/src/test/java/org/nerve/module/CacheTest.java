package org.nerve.module;

import org.nerve.TestOnSpring;
import org.nerve.auth.Account;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.ActiveProfiles;

/**
 * PROJECT		fire-eye-manage
 * PACKAGE		com.zeus.module
 * FILE			CacheTest.java
 * Created by 	zengxm on 2017/12/25.
 */
@EnableAutoConfiguration
@EnableCaching
@ActiveProfiles("cache")
public class CacheTest extends TestOnSpring {
	@Autowired
	CacheManager cacheManager;

	@Autowired
	CacheService cacheService;

	@Test
	public void manager(){
		System.out.println(cacheManager);

		Cache cache = cacheManager.getCache("controller");
		System.out.println(cache);
	}

	/**
	 * 对通用缓存进行测试
	 * 默认只存活 5秒，见 application-cache.yml
	 * @throws InterruptedException
	 */
	@Test
	public void accountService() throws InterruptedException {
		printCaches();
		String id = getClass().getName();
		Account account = print(cacheService.getAccount(id));

		System.out.println("等待 3 秒（缓存并未失效）...");
		Thread.sleep(3000);
		// 继续获取
		Account account1 = print(cacheService.getAccount(id));
		Assert.assertEquals(account.getPassword(), account1.getPassword());

		System.out.println("等待 3 秒（此时缓存已经失效，因为 通用 的缓存有效期为 5 s）...");
		Thread.sleep(3000);

		Account account2 = print(cacheService.getAccount(id));
		Assert.assertNotEquals(account.getPassword(), account2.getPassword());

		printCaches();
	}

	/**
	 * 对 name=controller 的缓存进行测试
	 * 2 秒内没有访问后即失效
	 */
	@Test
	public void accountServiceWithCacheController() throws InterruptedException {
		String id = getClass().getName();
		Account account = print(cacheService.getAccountOnControllerCache(id));

		System.out.println("等待 1 秒（缓存并未失效）...");
		Thread.sleep(1000);
		// 继续获取
		Account account1 = print(cacheService.getAccountOnControllerCache(id));
		Assert.assertEquals(account.getPassword(), account1.getPassword());

		System.out.println("等待 2.5 秒（此时缓存已经失效，因为 通用 的缓存有效期为 5 s）...");
		Thread.sleep(2500);

		Account account2 = print(cacheService.getAccountOnControllerCache(id));
		Assert.assertNotEquals(account.getPassword(), account2.getPassword());
	}

	private void  printCaches(){
		System.out.println("-------------------START CACHE-------------------");
		cacheManager.getCacheNames().forEach(name->{
			System.out.println(name+" = "+cacheManager.getCache(name));
		});
		System.out.println("-------------------END CACHE-------------------");
	}

	private Account print(Account account){
		System.out.println(account+" id="+account.getId()+", pwd="+account.getPassword());
		return account;
	}
}
