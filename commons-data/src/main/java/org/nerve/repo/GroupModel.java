package org.nerve.repo;

import org.springframework.data.domain.Sort;

import static org.springframework.util.ObjectUtils.nullSafeHashCode;

/**
 * PROJECT		fire-eye-manage
 * PACKAGE		com.zeus.repo
 * FILE			GroupModel.java
 * Created by 	zengxm on 2018/2/11.
 */
public class GroupModel {
	public static final int MAX = -1;

	public String groupBy;    //分组字段
	public int limit = 20;  //分组结果大小
	public Sort.Direction sort; //排序方式
	public String sortBy;   //排序的字段（分组结果中的其中一个字段）

	public GroupModel(){
	}

	public GroupModel(String groupBy){
		this.groupBy = groupBy;
	}
	public GroupModel(String groupBy, int limit){
		this.groupBy = groupBy;
		this.limit = limit;
	}

	public GroupModel(String groupBy, int limit, Sort.Direction sort, String sortBy){
		this(groupBy, limit);
		this.sort = sort;
		this.sortBy = sortBy;
	}

	@Override
	public int hashCode() {
		return nullSafeHashCode(groupBy) + limit + nullSafeHashCode(sort) + nullSafeHashCode(sortBy);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj instanceof GroupModel)
			return hashCode() == obj.hashCode();
		return false;
	}

	public String getGroupBy() {
		return groupBy;
	}

	public GroupModel setGroupBy(String groupBy) {
		this.groupBy = groupBy;
		return this;
	}

	public int getLimit() {
		return limit;
	}

	public GroupModel setLimit(int limit) {
		this.limit = limit;
		return this;
	}

	public Sort.Direction getSort() {
		return sort;
	}

	public GroupModel setSort(Sort.Direction sort) {
		this.sort = sort;
		return this;
	}

	public String getSortBy() {
		return sortBy;
	}

	public GroupModel setSortBy(String sortBy) {
		this.sortBy = sortBy;
		return this;
	}
}
