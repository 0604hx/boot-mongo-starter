package org.nerve.core.module.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * PROJECT		fire-eye-manage
 * PACKAGE		com.zeus.core.module.cache
 * FILE			ZeusCaffeineCacheManager.java
 * Created by 	zengxm on 2017/12/25.
 *
 * 扩展 cache 初始化能力
 */
public class ZeusCaffeineCacheManager extends CaffeineCacheManager {

	private final ConcurrentMap<String, Cache> initCacheMap = new ConcurrentHashMap<>(16);

	public void setCaches(Collection<? extends Cache> caches) {
		if(caches!=null)
			caches.forEach(cache->addCache(cache));
	}

	public void addCache(Cache cache){
		initCacheMap.put(cache.getName(), cache);
	}

	/**
	 * 重写 CaffeineCacheManager 的 getCache 方法
	 * 优先从 initCacheMap 中获取 缓存
	 *
	 * 所以，一旦设置了初始化 cache 则优先级为最高
	 *
	 * @param name
	 * @return
	 */
	@Override
	public Cache getCache(String name) {
		if(initCacheMap.containsKey(name))
			return initCacheMap.get(name);
		return super.getCache(name);
	}

	@Override
	public Collection<String> getCacheNames() {
		Set<String> names = new HashSet<>();
		this.initCacheMap.keySet().forEach(n->names.add(n));
		super.getCacheNames().forEach(n->names.add(n));
		return Collections.unmodifiableSet(names);
	}
}
