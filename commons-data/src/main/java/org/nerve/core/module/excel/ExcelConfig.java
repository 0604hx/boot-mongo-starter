package org.nerve.core.module.excel;

import org.nerve.core.module.excel.util.Header;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * com.zeus.core.module.excel
 * Created by zengxm on 2017/8/24.
 */
@Configuration
@ConfigurationProperties(prefix = "zeus.module.export")
public class ExcelConfig {
	String dir;

	Map<String,List<Header>> headers;
}
