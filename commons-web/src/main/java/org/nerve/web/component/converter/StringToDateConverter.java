package org.nerve.web.component.converter;

import org.apache.commons.lang3.StringUtils;
import org.nerve.utils.DateUtils;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

/**
 * 自定义日期转换器
 * 针对 时间戳 到日期的转换
 * com.zeus.web.component.converter
 * Created by zengxm on 4/7/2017.
 */
public class StringToDateConverter implements Converter<String,Date> {

	@Override
	public Date convert(String s) {
		if(StringUtils.isBlank(s))
			return null;
		if(s.length()==13)
			return new Date(Long.valueOf(s));
		return DateUtils.parseDate(s);
	}
}
