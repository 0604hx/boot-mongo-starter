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

	Setting load(Enum e);

	String value(String uuid);

	String value(Enum e);

	int intValue(Enum uuid, int defaultValue);

	float floatValue(Enum uuid, float defaultValue);

	boolean booleanValue(Enum uuid, boolean defaultValue);
}
