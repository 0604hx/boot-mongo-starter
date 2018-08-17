package org.nerve.domain;

import java.util.Date;

/**
 * PROJECT		fire-eye-manage
 * PACKAGE		com.zeus.domain
 * FILE			DateExtraEntity.java
 * Created by 	zengxm on 2017/12/20.
 */
public class DateExtraEntity extends ExtraEntity {
	protected Date addDate;

	public Date getAddDate() {
		return addDate;
	}

	public DateExtraEntity setAddDate(Date addDate) {
		this.addDate = addDate;
		return this;
	}
}
