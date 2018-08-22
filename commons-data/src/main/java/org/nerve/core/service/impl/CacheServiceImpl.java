package org.nerve.core.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import org.nerve.core.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;

/**
 * PROJECT		fire-eye-manage
 * PACKAGE		com.zeus.core.service.impl
 * FILE			CacheServiceImpl.java
 * Created by 	zengxm on 2018/1/18.
 */
@Service
public class CacheServiceImpl implements CacheService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	CacheManager cacheManager;

	@Override
	public List<Map> list() {
		Collection<String> names = cacheManager.getCacheNames();
		List<Map> list = new ArrayList<>();

		names.forEach(name->{
			Cache cache = (Cache) cacheManager.getCache(name).getNativeCache();
			CacheStats stats = cache.stats();

			Map<String, Object> map = new HashMap<>();
			map.put("size", cache.estimatedSize());
			map.put("name", name);
			map.put("hitRate", stats.hitRate());
			map.put("request", stats.requestCount());       //总请求数
			map.put("average", stats.averageLoadPenalty()); //平均加载时间，单位纳秒
			map.put("failRate", stats.loadFailureRate());

			list.add(map);
		});
		return list;
	}

	@Override
	public void clean(String name) {
		CaffeineCache caffeineCache = (CaffeineCache) cacheManager.getCache(name);

		Assert.notNull(caffeineCache, "no cache with name = "+name);

		caffeineCache.clear();
		logger.info("cleanup cache(name={})...", name);
	}

	@Override
	public void cleanAll() {
		cacheManager.getCacheNames().forEach(n->cacheManager.getCache(n).clear());
	}
}
