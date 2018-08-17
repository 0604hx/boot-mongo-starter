package org.nerve.core.domain;

import org.nerve.auth.Account;
import org.nerve.domain.DateEntity;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 数据导出记录
 * com.zeus.core.domain
 * Created by zengxm on 2017/8/24.
 */
@Document(collection = "sys_export")
public class Export extends DateEntity {
	/**
	 * 关联的人
	 */
	Account account;

	String filename;
	long fileSize;
	String path;        //文件保存路径
	boolean deleteAfterDownload;     //下载完成后是否需要删除
	Date updateDate;    //更新日期，如果是导出则为最新的下载日期，如果是导入则为入库日期
	String query;       //查询条件
	long dataLen;        //导出的数据条数
	String entity;      //实体类

	public Export(){}

	/**
	 *
	 * @param clazz
	 */
	public Export(Class clazz){
		if(clazz!=null)
			this.entity = clazz.getName();
		this.addDate = new Date();
	}

	public Account getAccount() {
		return account;
	}

	public Export setAccount(Account account) {
		this.account = account;
		return this;
	}

	public String getFilename() {
		return filename;
	}

	public Export setFilename(String filename) {
		this.filename = filename;
		return this;
	}

	public long getFileSize() {
		return fileSize;
	}

	public Export setFileSize(long fileSize) {
		this.fileSize = fileSize;
		return this;
	}

	public String getPath() {
		return path;
	}

	public Export setPath(String path) {
		this.path = path;
		return this;
	}

	public boolean isDeleteAfterDownload() {
		return deleteAfterDownload;
	}

	public Export setDeleteAfterDownload(boolean deleteAfterDownload) {
		this.deleteAfterDownload = deleteAfterDownload;
		return this;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public Export setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
		return this;
	}

	public String getQuery() {
		return query;
	}

	public Export setQuery(String query) {
		this.query = query;
		return this;
	}

	public long getDataLen() {
		return dataLen;
	}

	public Export setDataLen(long dataLen) {
		this.dataLen = dataLen;
		return this;
	}

	public String getEntity() {
		return entity;
	}

	public Export setEntity(String entity) {
		this.entity = entity;
		return this;
	}
}