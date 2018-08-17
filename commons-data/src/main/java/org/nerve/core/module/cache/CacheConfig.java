package org.nerve.core.module.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * PROJECT		fire-eye-manage
 * PACKAGE		com.zeus.core.module.cache
 * FILE			CacheConfig.java
 * Created by 	zengxm on 2017/12/25.
 *
 * 只针对 Caffeine，详细的 cache 配置请见：http://static.javadoc.io/com.github.ben-manes.caffeine/caffeine/2.2.2/com/github/benmanes/caffeine/cache/CaffeineSpec.html
 */
@Configuration
@ConfigurationProperties(prefix = "zeus.cache")
public class CacheConfig {
	boolean enable=false;
	List<CacheBean> caches = new ArrayList<>();

	public boolean isEnable() {
		return enable;
	}

	public CacheConfig setEnable(boolean enable) {
		this.enable = enable;
		return this;
	}

	public List<CacheBean> getCaches() {
		return caches;
	}

	public CacheConfig setCaches(List<CacheBean> caches) {
		this.caches = caches;
		return this;
	}
}

