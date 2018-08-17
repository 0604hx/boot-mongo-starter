package org.nerve.core.module.excel;

import org.nerve.core.module.excel.util.Header;

import java.util.List;

/**
 * com.zeus.core.module.excel
 * Created by zengxm on 2017/8/24.
 */
public interface ExcelHeaderProvider {

	/**
	 * 获取特定实体对象的导出时标题的配置信息
	 * @param entityName
	 * @return
	 */
	List<Header> get(String entityName);
}
