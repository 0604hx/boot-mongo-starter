package org.nerve;

import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * com.zeus
 * Created by zengxm on 2017/8/23.
 */
@ComponentScan("com.zeus")
@SpringBootApplication
@SpringBootTest
@RunWith(SpringRunner.class)
@EnableScheduling
public class TestOnSpring {
}
