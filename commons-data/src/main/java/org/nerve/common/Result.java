package org.nerve.common;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * Result.java
 * @author seven
 * last edit by:udk
 * 2010-12-5
 */
public class Result {
	/** 总记录数 */
	private long total;
	/** 查询结果 */
	private boolean success = true;
	/** 额外的信息 */
	private Object data;
	private String message;
	public Result(){}

	public Result(Consumer<Result> consumer){
		try{
			consumer.accept(this);
		}catch (Exception e){
			this.error(e);
			e.printStackTrace();
		}
	}

	public Result(boolean success, String message){
		this.success = success;
		this.message = message;
	}
	public Result(boolean success, String message, Object data){
		this.success = success;
		this.message = message;
		this.data =data;
	}
	public Result(long total, Collection<?> data){
		this.total = total;
		this.data = data;
	}

    /**
     * 操作成功
     * @param data
     * @return
     */
	public static Result ok(Object data){
	    Result re=new Result();
	    re.setSuccess(true).setData(data);
	    return re;
    }

    public static Result fail(Exception throwable){
		Result re = new Result();
		re.error(throwable);
		return re;
    }

    /**
     *
     * @param data
     * @param message
     * @return
     */
    public static Result ok(Object data, String message){
        Result re=new Result();
        re.setSuccess(true).setData(data);
        re.setMessage(message);
        return re;
    }

	public void error(Exception e){
		this.success = false;
		if(e.getCause() == null)
			message = e.getMessage();
		else
			message = e.getCause().getMessage();
	}
	
	public String getMessage() {
		return message;
	}

	public Result setMessage(String message) {
		this.message = message;
		return this;
	}

	public long getTotal() {
		return total;
	}
	public Result setTotal(long total) {
		this.total = total;
		return this;
	}
	public Result setSuccess(boolean success) {
		this.success = success;
		return this;
	}
	public boolean isSuccess() {
		return success;
	}
	public Object getData() {
		return data;
	}
	public Result setData(Object data) {
		this.data = data;
		return this;
	}

	public Result set(Object data, String msg){
		return setData(data).setMessage(msg);
	}
}
