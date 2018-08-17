package org.nerve.service;

import com.alibaba.fastjson.JSON;
import org.nerve.TestOnSpring;
import org.nerve.core.domain.Export;
import org.nerve.core.module.excel.ExcelHeaderProvider;
import org.nerve.core.module.excel.util.Header;
import org.nerve.core.service.ExportService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * com.zeus.service
 * Created by zengxm on 2017/8/24.
 */
@EnableAutoConfiguration
public class ExportServiceTest extends TestOnSpring {

	@Autowired
	ExportService service;

	@Autowired
	ExcelHeaderProvider headerProvider;

	@Test
	public void toJSON() throws IOException {
//		Export export = service.exportToJSON((root, query, cb) -> cb.and(cb.equal(root.get("name"), "TEST_0")), Account.class);
//
//		checkExport(export);
	}

	@Test
	public void toExcel() throws IOException {
//		Export export = service.exportToExcel(
//				(root, query, cb) -> cb.and(cb.equal(root.get("name"), "TEST_0")),
//				Account.class,
//				null,
//				headerProvider.get(Account.class.getSimpleName())
//		);
//
//		checkExport(export);
	}

	@Test
	public void beanToExcel() throws IOException {
		List<Header> headers = Arrays.asList(
				new Header("列标题","text",2000),
				new Header("字段名","field"),
				new Header("宽度","width")
		);

		Export export = service.exportToExcel(headers, null, headers);
	}

	public void checkExport(Export export) throws IOException {
		System.out.println(JSON.toJSONString(export, true));

		Path path = Paths.get(export.getPath());
		Assert.assertTrue("文件不存在："+export.getPath(), Files.exists(path));
		Assert.assertEquals("文件大小不一致", Files.size(Paths.get(export.getPath())), export.getFileSize());
	}
}
