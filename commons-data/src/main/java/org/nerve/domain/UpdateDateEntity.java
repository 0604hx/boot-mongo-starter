package org.nerve.domain;

import java.util.Date;

/**
 * com.zeus.domain
 * Created by zengxm on 2017/8/22.
 */
public class UpdateDateEntity extends DateEntity{
	protected Date updateDate;

	public Date getUpdateDate() {
		return updateDate;
	}

	public UpdateDateEntity setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
		return this;
	}
}
