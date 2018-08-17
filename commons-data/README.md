# Commons-data
> 基于`spring-data-jpa` 的通用数据操作层 及 常用组件

## cache
> 默认使用 [Caffeine](https://github.com/ben-manes/caffeine) 缓存实现

更多关于Spring Boot缓存说明详见：https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-caching.html


可用的缓存配置：
```yaml
spring:
	cache:
		caffeine:
			spec: maximumSize=2000,expireAfterWrite=5s

zeus:
	cache:
		enable: true	# 开启自定义缓存配置，否则使用 Spring Auto configuration
		caches:
			- name: dashboard
			  spec: maximumSize=100,expireAfterWrite=10s
			- name: controller
			  spec: maximumSize=100,expireAfterAccess=2s
```

配置 `spring.cache.caffeine.spec` 可以配置通用的缓存参数
（关于 Caffeine 的配置详见：[Caffeine](http://static.javadoc.io/com.github.ben-manes.caffeine/caffeine/2.2.2/com/github/benmanes/caffeine/cache/CaffeineSpec.html))


配置 `zeus.cache.enable=true` 即可开启自定义的缓存组件，具有以下功能：

1. 可以灵活配置不同 cache 的参数（如 缓存A 容量 100，每 10 秒失效； 缓存B 容量 10，超过 20 秒没命中即失效）
2. 定制 `CacheResolver` ，@Cacheable 若不指定缓存名，则根据 类名、方法名 规则自动生成


具体示例见： com.zeus.module.CacheTest