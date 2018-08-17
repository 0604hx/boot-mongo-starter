package org.nerve.core.module.excel.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.nerve.utils.DateUtils;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

/**
 * com.zeus.core.module.excel.util
 * Created by zengxm on 2017/8/24.
 */
public class PojoExcelExporter<T extends Object> extends AbstractExcelExporter {

	protected Collection<T> dataList;
	protected Class clazz;

	public PojoExcelExporter(List<Header> headers){
		super();
		this.headers = headers;
	}

	public Collection<T> getDataList() {
		return dataList;
	}

	public PojoExcelExporter setDataList(Collection<T> dataList) {
		this.dataList = dataList;
		return this;
	}

	protected ValueFilter simpleDateFilter = (object, name, value) -> {
		if(value != null && value instanceof Date)
			return DateUtils.formatDate((Date)value, DateUtils.DATE_TIME);

		return value;
	};

	/**
	 *
	 * @param workbook
	 * @param sheet
	 * @param len
	 */
	@Override
	protected void writeData(XSSFWorkbook workbook, XSSFSheet sheet, int len) {
		if(CollectionUtils.isEmpty(dataList))
			return;

		dataList.forEach(obj->{
			XSSFRow row = sheet.createRow(rowIndex++);

			logger.debug("writing data from {} {}",obj,JSON.toJSONString(obj, simpleDateFilter));
			JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(obj, simpleDateFilter));

			IntStream.range(0, len).forEach(i->{
				XSSFCell cell = row.createCell(i);
				Object value = getValue(jsonObject, headers.get(i).field);
				if(value != null)
					cell.setCellValue(value.toString());
			});
		});
	}

	/**
	 * 获取指定的数据值
	 * @param obj
	 * @param field
	 * @return
	 */
	protected Object getValue(JSONObject obj, String field){
		if(!field.contains("."))
			return obj.get(field);

		JSONObject tmp = obj;
		String[] fs = field.split(".");
		for (int i = 0; i < fs.length; i++) {
			if(i == fs.length - 1)
				return tmp.get(fs[i]);

			tmp = tmp.getJSONObject(fs[i]);
			if(tmp == null)
				return StringUtils.EMPTY;
		}

		return StringUtils.EMPTY;
	}
}
