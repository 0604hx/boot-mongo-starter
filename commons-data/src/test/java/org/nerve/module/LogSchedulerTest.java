package org.nerve.module;

import org.nerve.TestOnSpring;
import org.nerve.auth.Account;
import org.nerve.enums.LogType;
import org.nerve.sys.L;
import org.junit.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

/**
 * PROJECT		fire-eye-manage
 * PACKAGE		com.zeus.module
 * FILE			LogSchedulerTest.java
 * Created by 	zengxm on 2017/12/25.
 */
@EnableAutoConfiguration
public class LogSchedulerTest extends TestOnSpring {

	@Test
	public void log() throws InterruptedException {
		L.log(LogType.CREATE, "新增测试用例："+getClass());

		Account account = new Account("集成显卡");
		L.log(LogType.DELETE, "删除测试用例数据："+getClass(), account, account);

		System.out.println("等待 LogScheduler 的执行...");
		Thread.sleep(6000);
		L.log(LogType.ERROR.value(), "删除测试用例数据："+getClass(), new RuntimeException("没有足够的权限进行此操作"), account, account);
		Thread.sleep(6000);
	}
}
