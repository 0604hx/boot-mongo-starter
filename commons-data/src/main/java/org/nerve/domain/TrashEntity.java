package org.nerve.domain;


/**
 * com.zeus.domain
 * Created by zengxm on 2017/8/22.
 */
public class TrashEntity extends IdEntity {
	protected boolean trash;

	public boolean isTrash() {
		return trash;
	}

	public TrashEntity setTrash(boolean trash) {
		this.trash = trash;
		return this;
	}
}
