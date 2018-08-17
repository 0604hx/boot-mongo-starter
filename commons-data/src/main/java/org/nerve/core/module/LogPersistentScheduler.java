package org.nerve.core.module;

import org.nerve.core.domain.Log;
import org.nerve.core.repo.LogRepo;
import org.nerve.sys.L;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * PROJECT		fire-eye-manage
 * PACKAGE		com.zeus.core.module
 * FILE			LogPersistentScheduler.java
 * Created by 	zengxm on 2017/12/25.
 *
 *
 * 设置 zeus.log.enable = false 即可关闭定时保存 Log 到 DB 功能
 * 设置 zeus.log.interval = {数值} 设置定时器间隔，单位 ms
 */
@Component
@ConditionalOnProperty(name ="zeus.log.enable",matchIfMissing = true,havingValue = "true")
public class LogPersistentScheduler {

	@Autowired
	LogRepo logRepo;

	private long numberPreRun=100;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@PostConstruct
	protected void init(){
		logger.info("[Scheduler] 初始化 LogPersistentScheduler : 每次处理 {} 条数据 " +
				"(设置 zeus.log.enable = false 即可关闭定时保存 Log 到 DB 功能)", numberPreRun);
	}


	/**
	 * 默认20秒保存一次
	 */
	@Scheduled(fixedDelayString = "${zeus.log.interval:20000}")
	public void run(){
		long size = L.size();
		if(size<=0)
			return;

		int count = 0;
		for(long i=0; i<(size>numberPreRun?numberPreRun:size);i++){
			Log op = L.poll();
			if(op!=null){
				logRepo.save(op);
				count++;
			}
		}

		logger.debug("deal done! total {} saved into DB...", count);
	}
}
