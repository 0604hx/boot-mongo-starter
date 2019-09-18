package org.nerve.repo;

import com.mongodb.CommandResult;
import com.mongodb.WriteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Map;

/**
 * com.zeus.repo
 * Created by zengxm on 2017/8/22.
 */
@NoRepositoryBean
public interface CommonRepo<T,ID extends java.io.Serializable> extends MongoRepository<T,ID>{
    String COUNT = "count";

    Page<T> find(Query query, Pageable p);
    Page<T> find(Criteria criteria, Pageable p);

    /**
     * 获取第一个，默认按照ID desc排序
     * @param criteria
     * @return
     */
    T findOne(Criteria criteria);
    T findOne(Query query);
    T findOne(ID id);

    /**
     * 根据Criteria来进行分页查询
     * @param query
     * @param p
     * @return
     */
    List<T> find(Query query, Pagination p);

    /**
     * @param query
     * @return
     */
    List<T> find(Query query);

    /**
     * 根据Query来进行分页查询
     * @param criteria
     * @param p
     * @return
     */
    List<T> find(Criteria criteria,Pagination  p);

    /**
     * 根据Criteria来删除执行的记录
     * @param criteria
     */
    Long delete(Criteria criteria);

    /**
     * 根据Criteria来统计
     * @param criteria
     */
    Long count(Criteria criteria);

    Long count(Query query);

    UpdateResult updateFirst(Query query, Update update);

    UpdateResult update(Query query, Update update);

    /**
     * 对某个字段进行分组统计
     * @param model             需要groupby的字段
     * @param criteria          遍历条件
     * @param fields            需要抽取出来的字段（key=原字段，value=抽取后的字段名，不能包含.）
     * @return
     */
    List<?> groupBy(GroupModel model, Criteria  criteria, Map<String, String> fields);

    List<?> groupBy(String groupField, Criteria  criteria);

    List<?> groupBy(String groupField);

    /**
     * 聚合操作
     * @param aggregation
     */
    <R> List<R> aggregate(Aggregation aggregation,Class<R> clazz);

    /**
     * 获取表名
     * @return
     */
    String getCollectionName();

    Document execCommand(String json);

    /**
     * 获取表的状态信息：execCommand("{collstats:'colName'})
     *
     * 返回结果样例：
     * {
     "ns" : "mongos.mongo_document",
     "count" : NumberInt(22821684),
     "size" : NumberLong(9328350918),
     "avgObjSize" : NumberInt(408),
     "storageSize" : NumberLong(2417422336),
     }
     */
    Document getCollectionStats();
}
