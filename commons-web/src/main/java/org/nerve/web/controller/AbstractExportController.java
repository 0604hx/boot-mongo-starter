package org.nerve.web.controller;

import org.nerve.auth.Account;
import org.nerve.common.Result;
import org.nerve.core.domain.Export;
import org.nerve.core.module.excel.ExcelHeaderProvider;
import org.nerve.core.repo.ExportRepo;
import org.nerve.core.service.ExportService;
import org.nerve.exception.ServiceException;
import org.nerve.repo.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用的数据导入 Controller ，继承此类然后扩展 entityMaps 即可
 * com.zeus.web.controller
 * Created by zengxm on 2017/8/24.
 */
public abstract class AbstractExportController extends AbstractController<Export, ExportRepo, ExportService> {

	@Autowired
	ExcelHeaderProvider headerProvider;

	protected final List<String> FORMAT_LIST = Arrays.asList("json","xlsx");
	protected final Map<String,Class> entityMaps = new HashMap<>();

	@PostConstruct
	protected void initEntityMap(){
		entityMaps.put("account", Account.class);
		entityMaps.put("export", Export.class);
	}


	/**
	 * 数据导出
	 * @param request
	 * @param pagination
	 * @param entity
	 * @param format
	 * @param pretty
	 * @return
	 */
	@ResponseBody
	@RequestMapping("{entity}/{format}")
	public Result exportDo(
			HttpServletRequest request,
			Pagination pagination,
			String filename,
			@PathVariable("entity") String entity,
			@PathVariable("format") String format,
			boolean pretty){
		return  result(re->{
			try{
				if(!entityMaps.containsKey(entity))
					throw new ClassNotFoundException();

				//判断格式
				if (!FORMAT_LIST.contains(format))
					throw new ServiceException(String.format("%s is not support, we just support : ", format, FORMAT_LIST));

				String exportFilename = (StringUtils.hasText(filename) ? filename : System.currentTimeMillis()) +"."+format;

				Export export = "json".equalsIgnoreCase(format) ?

						service.exportToJSON(buildMongoQueryFromRequest(request, DEFAULT_SEARCH_PREFIX), entityMaps.get(entity), exportFilename, pretty)
						:
						service.exportToExcel(buildMongoQueryFromRequest(request, DEFAULT_SEARCH_PREFIX), entityMaps.get(entity), exportFilename, headerProvider.get(entity))
						;

				re.setData(export);
			}
			catch (ClassNotFoundException e){
				throw new ServiceException("ClassNotFoundException: 请检查数据实体 "+entity+" 是否定义或授权");
			}
			catch (Exception e){
				logger.error(String.format("error on export ,entity = %s format=%s", entity, format), e);
				re.error(e);
			}
		});
	}

	/**
	 * 下载文件
	 * @param id
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/download/{id}")
	public void download(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Export export = service.download(id);

		downloadFile(request, response, new File(export.getPath()), export.getFilename(), () -> service.downloadDone(export));
	}
}
