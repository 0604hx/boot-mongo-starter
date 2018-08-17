package org.nerve.core.module.excel;

import org.nerve.core.domain.Export;

import java.io.File;
import java.io.IOException;

/**
 * Created by zengxm on 2017/8/24.
 */
public interface ExcelWorker {

	/**
	 *
	 * @param export
	 * @param file
	 */
	void run(Export export, File file) throws IOException;
}
