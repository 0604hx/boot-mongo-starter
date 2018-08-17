package org.nerve.repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * 启动自动配置，不然跨模块的组件无法正确装载
 * com.zeus.repo
 * Created by zengxm on 2017/8/23.
 */
@Configuration
@AutoConfigurationPackage
@EnableMongoRepositories(repositoryBaseClass = CommonRepoImpl.class, basePackages = ("org.nerve"))
public class RepositoryConfig {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MongoDbFactory dbFactory;
	@Autowired
	private MongoMappingContext context;
	@Autowired
	private CustomConversions conversions;

	/**
	 * 去除文档中的 _class 字段（默认开启）
	 * 如果想要保存此字段，则通过设置： zeus.repository.keepClass=true
	 *
	 * ------------------------------ information --------------------------
	 * 默认情况下，spring mongo 会为每个文档增加_class字段，保存的是该文档映射的实体类
	 * 这样做主要是为了映射文档到POJO时能找到该类
	 * 一般情况下时不需要这个功能的，而且增加此字段后，在数据量大的情况下造成资源浪费
	 *
	 * @return
	 */
	@Bean
	@ConditionalOnProperty(name = "nerve.repository.keepClass", matchIfMissing = true,havingValue = "false")
	public MappingMongoConverter mappingMongoConverter(){
		logger.info("[Customing MappingMongoConverter] remove '_class' field. If you want to keep it ,just set nerve.repository.keepClass = true");
		DefaultDbRefResolver dbRefResolver = new DefaultDbRefResolver(dbFactory);
		MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
		mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null,context));

		mappingConverter.setCustomConversions(conversions);
		return mappingConverter;
	}
}
