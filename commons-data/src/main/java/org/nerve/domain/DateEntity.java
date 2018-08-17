package org.nerve.domain;

import java.util.Date;

/**
 * com.zeus.domain
 * Created by zengxm on 2017/8/22.
 */
public class DateEntity extends IdEntity {
	protected Date addDate;

	public Date getAddDate() {
		return addDate;
	}

	public DateEntity setAddDate(Date addDate) {
		this.addDate = addDate;
		return this;
	}
}
