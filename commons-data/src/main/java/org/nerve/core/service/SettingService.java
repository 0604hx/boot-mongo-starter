package org.nerve.core.service;


import org.nerve.core.domain.Setting;
import org.nerve.core.repo.SettingRepo;
import org.nerve.service.CommonService;

/**
 * PROJECT		jpkg-manage
 * PACKAGE		com.zeus.core.service
 * FILE			SettingService.java
 * Created by 	zengxm on 2018/7/11.
 */
public interface SettingService extends CommonService<Setting, SettingRepo> {

	Setting load(String uuid);

	String value(String uuid);

	int intValue(String uuid);

	float floatValue(String uuid);

	boolean booleanValue(String uuid);
}
