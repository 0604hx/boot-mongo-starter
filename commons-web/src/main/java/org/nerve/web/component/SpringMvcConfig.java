package org.nerve.web.component;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.nerve.web.common.json.SimpleValueFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.AppCacheManifestTransformer;
import org.springframework.web.servlet.resource.GzipResourceResolver;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by fdt on 17/2/2.
 */
@Configuration
public class SpringMvcConfig extends WebMvcConfigurerAdapter {
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    protected static Logger logger = LoggerFactory.getLogger(SpringMvcConfig.class);

    @Bean
    public ShallowEtagHeaderFilter shallowETagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
    }

    /**
     * 默认使用FastJson
     */
    @Value("${zeus.fastJson:true}")
    private boolean fastJson=true;

    @Autowired(required = false)
    private ValueFilter valueFilter;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(new ResourceHttpMessageConverter());
        converters.add(new ByteArrayHttpMessageConverter());

        converters.add(fastJson?
                fastJsonConverter()
                :
                jackson2Converter());
    }

    @Bean
    public StringHttpMessageConverter stringHttpMessageConverter() {
        return new StringHttpMessageConverter(UTF_8);
    }

    protected AbstractHttpMessageConverter fastJsonConverter(){
        logger.debug("init FastJSON Converter , to unable fastJson, just set 'zeus.fastJson=false'");
        //使用FastJSON
        final FastJsonHttpMessageConverter fastJsonConverter = new FastJsonHttpMessageConverter();
        final FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(SerializerFeature.IgnoreErrorGetter);
        config.setCharset(UTF_8);
        if(valueFilter == null){
            logger.debug("there is no bean for ValueFilter, use  com.zeus.web.component.json.SimpleValueFilter() ...");
            valueFilter = new SimpleValueFilter();
        }
        config.setSerializeFilters(valueFilter);

        fastJsonConverter.setFastJsonConfig(config);
        return fastJsonConverter;
    }

    protected AbstractGenericHttpMessageConverter jackson2Converter(){
        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        converter.setObjectMapper(objectMapper);

        return converter;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /*
        增加缓存
        由于前端代码打包后 是放在 public/static 目录，故对 static/** 到 public/static 的映射
         */
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:public/static/")
                .setCachePeriod(360000)
                .resourceChain(true)
                .addResolver(new GzipResourceResolver())
                .addTransformer(new AppCacheManifestTransformer())
        ;
    }
}
