package org.nerve.core.service.impl;

import org.nerve.core.domain.Setting;
import org.nerve.core.repo.SettingRepo;
import org.nerve.core.service.SettingService;
import org.nerve.enums.Fields;
import org.nerve.service.CommonServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * PROJECT		jpkg-manage
 * PACKAGE		com.zeus.core.service.impl
 * FILE			SettingServiceImpl.java
 * Created by 	zengxm on 2018/7/11.
 */
@Service
public class SettingServiceImpl extends CommonServiceImpl<Setting, SettingRepo> implements SettingService {

	@Override
	public Setting load(String uuid) {
		return repo.findOne(Criteria.where(Fields.UUID.value()).is(uuid));
	}

	@Cacheable("settings")
	@Override
	public String value(String uuid) {
		Setting setting = load(uuid);
		return Objects.isNull(setting)? null : setting.getContent();
	}

	@Cacheable("settings")
	@Override
	public int intValue(String uuid) {
		return 0;
	}

	@Cacheable("settings")
	@Override
	public float floatValue(String uuid) {
		return 0;
	}

	@Cacheable("settings")
	@Override
	public boolean booleanValue(String uuid) {
		return false;
	}
}
