package org.nerve.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * com.zeus.tonglian.domain
 * Created by zengxm on 2017/8/22.
 */
public class IdEntityTest {

	DateEntity dateEntity = new DateEntity();

	@Test
	public void eqTest(){
		assertEquals(false, dateEntity.equals(null));
		assertEquals(false, dateEntity.equals("Hello"));
		assertEquals(false, dateEntity.equals(new DateEntity()));
		assertEquals(true, dateEntity.equals(dateEntity));
	}
}
