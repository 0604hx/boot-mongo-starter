package org.nerve.core.service;

import java.util.List;
import java.util.Map;

/**
 * PROJECT		fire-eye-manage
 * PACKAGE		com.zeus.core.service
 * FILE			CacheService.java
 * Created by 	zengxm on 2018/1/18.
 */
public interface CacheService {

	/**
	 * 获取当前缓存列表
	 * @return
	 */
	List<Map> list();

	/**
	 * 清空特定名称的缓存
	 * @param name
	 */
	void clean(String name);

	/**
	 * 清空全部缓存
	 */
	void cleanAll();
}
