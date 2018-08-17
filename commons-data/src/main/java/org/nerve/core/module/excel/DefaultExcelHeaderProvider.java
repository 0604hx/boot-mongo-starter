package org.nerve.core.module.excel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.nerve.core.module.excel.util.Header;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * com.zeus.core.module.excel
 * Created by zengxm on 2017/8/24.
 */
@Component
public class DefaultExcelHeaderProvider implements ExcelHeaderProvider{

	private Logger logger = LoggerFactory.getLogger(getClass());

	protected final String ENCODING = "utf-8";
	protected final String HEADER_FILE = "config/excel.headers.json";

	private Map<String,List<Header>> headerMap = new HashMap<>();

	@Override
	public List<Header> get(String entityName) {
		String name = entityName.toLowerCase();
		if(!headerMap.containsKey(name))
			logger.debug("could not find headers for {}! Please check {}", entityName, HEADER_FILE);

		return headerMap.get(name);
	}

	@PostConstruct
	protected void init(){
		logger.debug("loading excel header config from {}...", HEADER_FILE);
		try{
			URL url = ResourceUtils.getURL(ResourceUtils.CLASSPATH_URL_PREFIX + HEADER_FILE);
			String fileData = IOUtils.toString(url.openStream(), ENCODING);
			//解析 JSON
			JSONObject jsonObject = JSON.parseObject(fileData);
			jsonObject.keySet().forEach(k->{
				List<Header> headers = new ArrayList<>();

				JSONArray array = jsonObject.getJSONArray(k);
				if(!CollectionUtils.isEmpty(array)){
					for (int i = 0; i < array.size(); i++) {
						headers.add(array.getObject(i, Header.class));
					}
				}

				headerMap.put(k.toLowerCase(), headers);
				logger.debug("loaded headers for {}, size={}", k, headers.size());
			});
		} catch (Exception e) {
			logger.error("error on init Excel Header:", e);
		}
	}
}
