package org.nerve.core.module.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * PROJECT		fire-eye-manage
 * PACKAGE		com.zeus.core.module.cache
 * FILE			ZeusCacheResolver.java
 * Created by 	zengxm on 2017/12/25.
 */
public class ZeusCacheResolver implements CacheResolver {
	private CacheManager cacheManager;

	public ZeusCacheResolver(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	@Override
	public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
		List<Cache> caches = new ArrayList<>();

		Set<String> names = context.getOperation().getCacheNames();
		if(CollectionUtils.isEmpty(names)){
			Method method = context.getMethod();
			names.add(String.format("%s#%s", method.getDeclaringClass().getName() ,method.getName()));
		}

		names.forEach(n->caches.add(cacheManager.getCache(n)));

		return caches;
	}
}
