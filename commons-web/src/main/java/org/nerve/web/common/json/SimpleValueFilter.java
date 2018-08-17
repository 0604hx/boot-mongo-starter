package org.nerve.web.common.json;

import com.alibaba.fastjson.serializer.ValueFilter;
import org.nerve.auth.Account;
import org.apache.commons.lang3.StringUtils;

/**
 * com.zeus.web.common.json
 * Created by zengxm on 2017/8/23.
 */
public class SimpleValueFilter implements ValueFilter {
	@Override
	public Object process(Object obj, String name, Object value) {
		if(value==null) return value;

		//去除密码显示
		if(obj instanceof Account && StringUtils.equals("password",name))   return null;

		return value;
	}
}
