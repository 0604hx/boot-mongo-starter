package org.nerve.core.service.impl;

import org.nerve.core.domain.Setting;
import org.nerve.core.repo.SettingRepo;
import org.nerve.core.service.SettingService;
import org.nerve.enums.Fields;
import org.nerve.service.CommonServiceImpl;
import org.nerve.utils.StringUtils;
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

	@Override
	public Setting load(Enum e) {
		return load(e.name());
	}

	@Cacheable("settings")
	@Override
	public String value(String uuid) {
		Setting setting = load(uuid);
		return Objects.isNull(setting)? null : setting.getContent();
	}

	@Cacheable("settings")
	@Override
	public String value(Enum e) {
		return value(e.name());
	}

	@Cacheable("settings")
	@Override
	public int intValue(Enum uuid, int defaultValue) {
		String value = value(uuid);
		return StringUtils.isEmpty(value)? defaultValue : Integer.valueOf(value);
	}

	@Cacheable("settings")
	@Override
	public float floatValue(Enum uuid, float defaultValue) {
		String value = value(uuid);
		return StringUtils.isEmpty(value)? defaultValue : Float.valueOf(value);
	}

	@Cacheable("settings")
	@Override
	public boolean booleanValue(Enum uuid, boolean defaultValue) {
		String value = value(uuid);
		return StringUtils.isEmpty(value)? defaultValue : Boolean.valueOf(value);
	}
}
