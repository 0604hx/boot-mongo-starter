package org.nerve.core.module.excel.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

/**
 * com.zeus.core.module.excel.util
 * Created by zengxm on 2017/8/24.
 */
public abstract class AbstractExcelExporter {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected List<Header> headers;

	protected int rowIndex;

	public interface Worker{
		void run(XSSFWorkbook workbook) throws IOException;
	}

	/**
	 * 导出到文件
	 * @param file
	 */
	public void toFile(File file) throws IOException {
		exportDo(workbook -> {
			try (FileOutputStream fos = new FileOutputStream(file)){
				workbook.write(fos);
				fos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * 导出到 流
	 * @param stream
	 * @throws IOException
	 */
	public void toStream(OutputStream stream)throws IOException{
		exportDo(workbook -> {
			workbook.write(stream);
			stream.flush();
		});
	}

	/**
	 *
	 * @param worker
	 */
	protected void exportDo(Worker worker) throws IOException {
		XSSFWorkbook book = new XSSFWorkbook();
		XSSFSheet sheet = book.createSheet();

		int len=createHeader(book,sheet);

		writeData(book,sheet,len);

		worker.run(book);
	}

	/**
	 * 子类需要实现的方法
	 * @param workbook
	 * @param sheet
	 * @param len
	 */
	protected abstract void writeData(XSSFWorkbook workbook, XSSFSheet sheet, int len);

	protected int createHeader(XSSFWorkbook workbook, XSSFSheet sheet){
		if(CollectionUtils.isEmpty(headers)){
			logger.error("invalid headers!");
			return 0;
		}

		XSSFFont font = workbook.createFont();
		font.setBold(true);
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFont(font);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		XSSFRow row = sheet.createRow(rowIndex);
		for (int i = 0; i < headers.size(); i++) {
			sheet.setColumnWidth(i, headers.get(i).width);
			XSSFCell cell = row.createCell(i);
			cell.setCellStyle(cellStyle);
			cell.setCellType(CellType.STRING);
			cell.setCellValue(headers.get(i).text);
		}

		rowIndex += 1;
		return headers.size();
	}
}
