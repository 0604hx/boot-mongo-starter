package org.nerve.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.ValueFilter;
import org.nerve.auth.Account;
import org.nerve.core.domain.Export;
import org.nerve.core.module.excel.ExcelWorker;
import org.nerve.core.module.excel.util.Header;
import org.nerve.core.module.excel.util.PojoExcelExporter;
import org.nerve.core.repo.ExportRepo;
import org.nerve.core.service.ExportService;
import org.nerve.domain.IdEntity;
import org.nerve.enums.LogType;
import org.nerve.exception.Exceptions;
import org.nerve.service.CommonServiceImpl;
import org.nerve.sys.L;
import org.nerve.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

/**
 * com.zeus.core.service.impl
 * Created by zengxm on 2017/8/24.
 */
@Service
public class ExportServiceImpl extends CommonServiceImpl<Export,ExportRepo> implements ExportService{
	@Autowired
	MongoTemplate mongoTemplate;


	/**
	 * 默认的保存路径
	 */
	@Value("${zeus.export.dir:_export}")
	String exportDir;

	protected final String ENCODING = "utf-8";

	@Override
	public void downloadDone(Export export) {
		if(export!= null){
			export.setUpdateDate(new Date());
			if(export.isDeleteAfterDownload()){
				try {
					Files.deleteIfExists(Paths.get(export.getPath()));
				} catch (IOException e) {
					log.error("error on delete export file {} : {}", export.getPath(), e.getMessage());
				}
			}

			repo.save(export);
		}
	}

	@Override
	public Export download(String id) {
		Account account = authProvider.get();
		Export export = get(id);

		Assert.notNull(export, Exceptions.NULL_ENTITY);
		//检查资源是否属于当前登录用户
		Assert.isTrue(export.getAccount() != null && export.getAccount().equals(account), "该资源[ID="+id+"]不属于你，故无法使用");
		//检查文件是否存在
		Assert.isTrue(Files.exists(Paths.get(export.getPath())), "资源的原文件已经被删除，无法操作");

		return export;
	}

	protected ValueFilter simpleDateFilter = (object, name, value) -> {
		if(value != null && value instanceof Date)
			return DateUtils.formatDate((Date)value, DateUtils.DATE_TIME);

		return value;
	};

	@Override
	public Export exportToJSON(Query query, Class<?> clazz, String filename, boolean format) throws IOException {
		Export export = new Export();

		File file = getOrBuildFile(clazz, filename);
		Account account = authProvider.get();

		//使用 BufferWriter
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		final long[] count = {0};
		try{
			mongoTemplate.stream(query, clazz).forEachRemaining((Consumer<Object>) o -> {
				try {
					writer.write(JSON.toJSONString(o, format));
					writer.newLine();
					count[0] +=1;
				} catch (IOException e) {
					e.printStackTrace();
				}
			});

			writer.flush();

			export.setAddDate(new Date());
			export.setEntity(clazz.getName());
			export.setFilename(filename);
			export.setPath(file.getAbsolutePath());
			export.setFileSize(file.length());

			export.setQuery(query.getQueryObject().toString());

			export.setDataLen(count[0]);

			if(account!=null){
				export.setAccount(account);
			}

			logWithSave(export);
			return export;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("导出数据出错",e);

			throw e;
		}finally {
			if(writer!=null){

				writer.close();
			}
		}
	}

	/**
	 *
	 * @param headerList
	 */
	protected void checkHeaders(List<Header> headerList){
		log.debug("export to excel with headers {}", headerList);
		if(CollectionUtils.isEmpty(headerList))
			throw new RuntimeException("headers is empty! Please custom excel headers for export");
	}

	@Override
	public <T extends IdEntity> Export exportToExcel(Query query, Class<T> clazz, String filename, List<Header> headerList) throws IOException {
		checkHeaders(headerList);

		return exportToExcelDo((export, file)->{
			//加载数据
			List<T> dataList = mongoTemplate.find(query, clazz);

			PojoExcelExporter excelExporter = new PojoExcelExporter<T>(headerList);
			excelExporter.setDataList(dataList);
			excelExporter.toFile(file);

			export.setDataLen(dataList.size());
			export.setQuery(query.getQueryObject().toString());

		}, clazz, filename, headerList);
	}


	@Override
	public Export exportToExcel(List<?> dataList, String filename, List<Header> headerList) throws IOException {
		checkHeaders(headerList);

		return exportToExcelDo((export, file)->{
			PojoExcelExporter excelExporter = new PojoExcelExporter(headerList);
			excelExporter.setDataList(dataList);
			excelExporter.toFile(file);

			export.setDataLen(dataList.size());
		},null, filename, headerList);
	}

	protected Export exportToExcelDo(ExcelWorker worker, Class<?> clazz, String filename, List<Header> headerList) throws IOException {
		Export export = new Export(clazz);
		File file = getOrBuildFile(
				clazz,
				StringUtils.hasText(filename)?
						filename
						:
						String.format("%s.xlsx", DateUtils.getDate("yyyyMMddHHmmss"))
		);

		worker.run(export, file);

		return saveExport(export, clazz, file, -1);
	}

	protected Export saveExport(Export export, Class clazz, File file, long count){
		Export ex = export==null?new Export(clazz) : export;
		if(authProvider!=null)
			ex.setAccount(authProvider.get());
		if(count>=0)
			ex.setDataLen(count);

		ex.setFileSize(file.length())
				.setPath(file.getAbsolutePath())
				.setFilename(file.getName());

		logWithSave(ex);
		return ex;
	}

	protected void logWithSave(Export export){
		repo.save(export);
		//保存操作日志
		String msg = String.format(
				"导出 %d 条 %s 数据到 %s(文件大小 %d b), 查询条件：%s",
				export.getDataLen(),
				export.getEntity(),
				export.getPath(),
				export.getFileSize(),
				export.getQuery()
		);
		log.info(msg);

		L.log(LogType.EXPORT, msg, export, authProvider!=null?authProvider.get():null);
	}

	/**
	 * 保存 Export 对象
	 * @param clazz
	 * @param file
	 * @param count
	 * @return
	 */
	protected Export saveExport(Class clazz, File file, long count){
		return saveExport(null, clazz, file, count);
	}

	/**
	 * 获取最终保存的文件，命名规则：{dir}/{entityClass}/{月份，如：2017-02}/{filename}
	 * @param clazz
	 * @param filename
	 * @return
	 */
	protected Path getFile(Class clazz, String filename){
		return Paths.get(exportDir, clazz==null?"":clazz.getSimpleName(), DateUtils.getDate("yyyy-MM"), filename);
	}

	/**
	 *
	 * @param clazz
	 * @param filename
	 * @return
	 */
	protected File getOrBuildFile(Class clazz, String filename){
		Path filePath = getFile(clazz, filename);

		File file = new File(filePath.toFile().getAbsolutePath());
		File parentFile = file.getParentFile();
		if(!parentFile.exists()){
			log.debug("create directory : {}", parentFile.getPath());
			parentFile.mkdirs();
		}
		return file;
	}

}
