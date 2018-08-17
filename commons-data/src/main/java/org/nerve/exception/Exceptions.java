package org.nerve.exception;

/**
 * com.zeus.exception
 * Created by zengxm on 2017/8/23.
 */
public final class Exceptions {
	public static final String LOGIN_FAIL = "你输入的账号不存在或者密码错误";
	public static final String ACCESS_DENIED = "对不起，你没有执行该操作的权限！";

	public static final String NULL_ENTITY = "操作的实体对象不存在";
	public static final String NULL = "对象不存在";
	public static final String EMPTY = "内容不能为空"
;
	public static final String UNIQUE = "实体对象已重复";
	public static final String TRASH = "对象已经被删除或者失效，无法修改";
	public static final String ENTITY_AUTH = "你没有权限操作此对象（可能这条数据不属于你）";

	public static final String USER_NAME_NOT_EMPTY = "用户名不能为空";
	public static final String USER_PWD_NOT_EMPTY = "密码必须填写";
	public static final String USER_PWD_MIN_LENGTH = "密码长度不能少于";

	public static final String NO_LOGIN = "not-sign-in";

	public static final String INVALID_ENTITY = "无效的数据对象（请检查录入的数据项是否满足要求）";
	public static final String MULTI_ENTITY = "重复的数据对象（已存在与当前录入一致的数据，请检查输入）";
}
