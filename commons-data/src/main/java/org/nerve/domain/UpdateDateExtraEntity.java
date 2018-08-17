package org.nerve.domain;

import java.util.Date;

/**
 * PROJECT		fire-eye-manage
 * PACKAGE		com.zeus.domain
 * FILE			UpdateDateExtraEntity.java
 * Created by 	zengxm on 2017/12/20.
 */
public class UpdateDateExtraEntity extends DateExtraEntity {
	protected Date updateDate;

	public Date getUpdateDate() {
		return updateDate;
	}

	public UpdateDateExtraEntity setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
		return this;
	}
}
