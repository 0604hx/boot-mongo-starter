package org.nerve.core.repo;


import org.nerve.core.domain.Setting;
import org.nerve.repo.CommonRepo;

/**
 * PROJECT		jpkg-manage
 * PACKAGE		com.zeus.core.repo
 * FILE			SettingRepo.java
 * Created by 	zengxm on 2018/7/11.
 */
public interface SettingRepo extends CommonRepo<Setting, String> {

	Setting findFirstByUuid(String uuid);

	int countByUuid(String uuid);
}
