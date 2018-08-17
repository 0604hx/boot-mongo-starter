package org.nerve.service;

import com.mongodb.WriteResult;
import org.nerve.domain.ID;
import org.nerve.enums.Fields;
import org.nerve.repo.Pagination;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by zengxm on 2017/8/22.
 */
public interface CommonService<T extends ID, R extends CrudRepository<T,String>> {
	Sort SORT_ID_ASC = new Sort(Sort.Direction.ASC, Fields.ID.value());
	Sort SORT_ID_DESC = new Sort(Sort.Direction.DESC, Fields.ID.value());
	Sort SORT_ADD_DATE_ASC = new Sort(Sort.Direction.ASC, Fields.ADD_DATE.value());
	Sort SORT_ADD_DATE_DESC = new Sort(Sort.Direction.DESC, Fields.ADD_DATE.value());

	String DATE_SIMPLE = "yyyyMMddHHmmss";

	/**
	 * 获取指定id的实体对象
	 * @param id
	 * @return
	 */
	T get(String id);

	/**
	 * 保存实体
	 * @param t
	 */
	void save(T t);

	/**
	 * 保存前进行的操作
	 * @param t
	 * @return 返回false时，保存操作将终止
	 */
	boolean onBeforeSave(T t);

	void delete(T t);

	void delete(String id);

	/**
	 * 删除对象前的操作
	 * @param t
	 * @return 返回false终止删除
	 */
	boolean onBeforeDelete(T t);

	List<T> list(Criteria c, Pagination p);

	List<T> list(Query q, Pagination p);

	/**
	 * 更新指定的属性
	 * @param id
	 * @param field
	 * @param value
	 */
	void modifyField(String id, String field, String value);

	long count();

	long count(Criteria criteria);

	/**
	 * 更新操作（只更新满足条件的第一个Document）
	 * @param id
	 * @param update
	 */
	WriteResult runUpdate(String id, Update update);

	/**
	 *
	 * @param criteria
	 * @param update
	 */
	WriteResult runUpdate(Criteria criteria, Update update);

	/**
	 * 遍历数据表
	 * @param query
	 * @param func
	 * @param preSize
	 * @return
	 */
//	Result doSomethingWith(
//			Query query,
//			func:(T,Long,Long)=>Boolean,preSize:Int=1000
//			);
}
