package org.nerve.domain;

import org.apache.commons.lang3.StringUtils;

/**
 * PROJECT		fire-eye-manage
 * PACKAGE		com.zeus.domain
 * FILE			ID
 * Created by 	zengxm on 2018/1/13.
 */
public interface ID {
	String id();

	default boolean using(){
		return StringUtils.isNotBlank(id());
	}
}
