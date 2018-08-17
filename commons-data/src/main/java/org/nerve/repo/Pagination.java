package org.nerve.repo;


/** 
 * 通用的分页工具
 */

public class Pagination {
	public final static String ORDER_DIRECTION_ASC = "ASC"; 
	public final static String ORDER_DIRECTION_DESC = "DESC";
	
	/**
	 * 默认每页记录数
	 */
	public static final int DEFAULT_PAGE_SIZE = 20;

	/**
	 * 当前页码
	 */
	protected int page=1;
	protected int pageSize = DEFAULT_PAGE_SIZE;
	protected long total = 0;    //最大值

	public int getPage() {
		return page;
	}

	public Pagination setPage(int page) {
		this.page = page;
		return this;
	}

	public int getPageSize() {
		return pageSize;
	}

	public Pagination setPageSize(int pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	public long getTotal() {
		return total;
	}

	public Pagination setTotal(long total) {
		this.total = total;
		return this;
	}
}
