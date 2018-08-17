package org.nerve.core.service;

import org.nerve.core.domain.Export;
import org.nerve.core.module.excel.util.Header;
import org.nerve.core.repo.ExportRepo;
import org.nerve.domain.IdEntity;
import org.nerve.service.CommonService;
import org.nerve.utils.DateUtils;
import org.springframework.data.mongodb.core.query.Query;

import java.io.IOException;
import java.util.List;

/**
 * Created by zengxm on 2017/8/24.
 */
public interface ExportService extends CommonService<Export,ExportRepo> {

	/**
	 * 文件下载完成后的钩子
	 * @param export
	 */
	void downloadDone(Export export);

	/**
	 * 下载某个导出文件
	 * @param id
	 * @return
	 */
	Export download(String id);

	/**
	 *
	 * @param query
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	default Export exportToJSON(Query query, Class<?> clazz)throws IOException{
		return exportToJSON(
				query,
				clazz,
				String.format("%s_%s.json", clazz.getSimpleName(), DateUtils.getDate("yyyyMMddHHmmss")),
				true
				);
	}




	/**
	 * 导出特定查询结果为 JSON 数据
	 * 默认不分页，注意数据量多大的情况
	 *
	 * @param query     查询条件
	 * @param clazz             实体
	 * @param filename          文件名，默认为 clazz.simpleName + {yyyyMMddHHmmss}.json
	 * @param format            是否格式化 JSON 输出，默认为 true
	 * @return
	 */
	Export exportToJSON(Query query, Class<?> clazz, String filename, boolean format) throws IOException;

	/**
	 *
	 * @param query
	 * @param clazz
	 * @param filename
	 * @param headerList
	 * @return
	 * @throws IOException
	 */
	<T extends IdEntity> Export exportToExcel(Query query, Class<T> clazz, String filename, List<Header> headerList) throws IOException;

	/**
	 * 导出普通的 数据对象 到Excel
	 * @param dataList
	 * @param filename
	 * @param headerList
	 * @return
	 */
	Export exportToExcel(List<?> dataList, String filename, List<Header> headerList) throws IOException;
}
