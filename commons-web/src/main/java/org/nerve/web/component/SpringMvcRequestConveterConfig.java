package org.nerve.web.component;

import org.nerve.web.component.converter.StringToDateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;

/**
 * com.zeus.web.component
 * Created by zengxm on 4/7/2017.
 */
@Configuration
public class SpringMvcRequestConveterConfig {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RequestMappingHandlerAdapter handlerAdapter;

	/**
	 * 默认使用FastJson
	 */
	@Value("${zeus.fastJson:true}")
	private boolean fastJson=true;


	/**
	 * 增加字符串转日期的功能
	 */
	@PostConstruct
	public void initEditableValidation() {
		ConfigurableWebBindingInitializer initializer = (ConfigurableWebBindingInitializer) handlerAdapter.getWebBindingInitializer();
		if (fastJson && initializer.getConversionService() != null) {
			logger.debug("add StringToDateConverter(), to unable fastJson, just set 'zeus.fastJson=false'");
			GenericConversionService genericConversionService = (GenericConversionService) initializer
					.getConversionService();
			genericConversionService.addConverter(new StringToDateConverter());
		}
	}
}
