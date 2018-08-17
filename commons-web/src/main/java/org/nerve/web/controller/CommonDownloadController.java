package org.nerve.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * com.zeus.web.controller
 * Created by zengxm on 2017/8/23.
 */
public abstract class CommonDownloadController extends BaseController {
	/**
	 * 下载文件到浏览器
	 * @param file
	 * @param fileName
	 * @param handler
	 */
	public void downloadFile(HttpServletRequest request, HttpServletResponse response, File file, String fileName, FinishHandler handler)
			throws Exception {
		try(
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
				BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())
				){
			//设置 response 的头信息
			response.setContentType("text/html;charset=utf-8");
			request.setCharacterEncoding("UTF-8");
			response.setContentType("application/x-msdownload;");

			String fName = fileName == null?file.getName() : fileName;
			response.setHeader(
					"Content-disposition",
					"attachment; filename="+new String(fName.getBytes("utf-8"), "ISO-8859-1"));

			byte[] buff = new byte[1024];
			int bytesRead = bis.read(buff,0, buff.length);
			while (bytesRead != -1){
				bos.write(buff,0, bytesRead);
				bytesRead = bis.read(buff,0, bytesRead);
			}
			bos.flush();
		}catch(Exception e){
			throw e;
		}finally {
			handler.onEnd();
		}
	}

	/**
	 *
	 */
	public interface FinishHandler{
		void onEnd();
	}
}
