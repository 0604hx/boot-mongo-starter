package org.nerve.sys;

import org.nerve.auth.Account;
import org.nerve.core.domain.Log;
import org.nerve.domain.ID;
import org.nerve.enums.LogType;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Consumer;

/**
 * org.nerve.webmagic.pool
 * Created by zengxm on 2016/4/21 0021.
 */
public final class L {
	public static final int MAX = 100;
	private static BlockingDeque<Log> innerDeque=new LinkedBlockingDeque<>(MAX);
//	private static AtomicInteger atomicCount=new AtomicInteger(0);

	public static void log(LogType logType, String message){
		log(logType.value(), message);
	}

	public static void log(LogType logType, String message, ID entity, Account account){
		log(logType.value(), message, entity, account);
	}

	public static void log(int op, String message){
		log(op,message,null,null);
	}
	public static void log(int op, String message, Account account){
		push(new Log(null,op,account,message));
	}
	public static void log(int op, String message, ID entity){
		push(new Log(entity, op, "", message));
	}
	public static void log(int op, String message, ID entity, Account account){
		push(new Log(entity, op,account,message));
	}
	public static void log(Log log){
		push(log);
	}
	public static void log(int op, String message, Throwable e, ID entity, Account account){
		Log l = new Log(entity,op, account, message);
		l.setE(ExceptionUtils.getStackTrace(e));

		push(l);
	}

	public static void push(Log data){
		if(!innerDeque.offerLast(data)){
			innerDeque.poll();
			innerDeque.offerLast(data);
		}
	}

	public static Log take() throws InterruptedException {
		return innerDeque.take();
	}

	public static Log poll(){
		return innerDeque.poll();
	}

	public static long size(){
		return innerDeque.size();
	}

	public static void forEachRemaining(Consumer<Log> consumer){
		innerDeque.iterator().forEachRemaining(consumer);
	}
}
