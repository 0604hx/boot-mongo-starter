package org.nerve.core.module.cache;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * PROJECT		fire-eye-manage
 * PACKAGE		com.zeus.core.module.cache
 * FILE			CacheConfiguration.java
 * Created by 	zengxm on 2017/12/25.
 *
 */
@Configuration
@ConditionalOnProperty(name ="nerve.cache.enable", matchIfMissing = true, havingValue = "true")
@EnableConfigurationProperties(CacheProperties.class)
public class CacheConfiguration extends CachingConfigurerSupport {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	CacheConfig cacheConfig;
	@Autowired
	CacheProperties cacheProperties;
	@Autowired(required = false)
	CacheLoader<Object, Object> cacheLoader=null;

	@PostConstruct
	protected void init(){
		logger.info("[Cache] 初始化 CacheConfiguration ( set nerve.cache.enable=false if you want to disable zeus cache!)");
		logger.info("[Cache] {}", cacheConfig.caches);
	}

	@Bean
	@Override
	public CacheResolver cacheResolver() {
		return new ZeusCacheResolver(cacheManager());
	}

	@Bean
	@Override
	public CacheManager cacheManager() {
		ZeusCaffeineCacheManager cacheManager = createCacheManager();
		if (!CollectionUtils.isEmpty(cacheProperties.getCacheNames())) {
			cacheManager.setCacheNames(cacheProperties.getCacheNames());
		}

		cacheConfig.caches.forEach(bean -> {
			boolean hasSpec = StringUtils.hasText(bean.spec);
			if(!hasSpec)
				logger.warn("[Cache] 检测到 name={} 的cache没有定义 spec 属性...", bean.name);

			CaffeineCache cache = new CaffeineCache(bean.name, Caffeine.from(hasSpec?bean.spec:"").build());
			logger.info("[Cache] add cache name={} {}", bean.name, cache);
			cacheManager.addCache(cache);
		});

		return cacheManager;
	}

	private ZeusCaffeineCacheManager createCacheManager(){
		ZeusCaffeineCacheManager cacheManager = new ZeusCaffeineCacheManager();
		setCacheBuilder(cacheManager);
		if (cacheLoader != null) {
			cacheManager.setCacheLoader(this.cacheLoader);
		}
		return cacheManager;
	}

	private void setCacheBuilder(CaffeineCacheManager cacheManager) {
		String specification = cacheProperties.getCaffeine().getSpec();
		if (StringUtils.hasText(specification)) {
			cacheManager.setCacheSpecification(specification);
		}
	}
}
