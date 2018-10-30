package org.nerve.repo;


import com.mongodb.CommandResult;
import com.mongodb.WriteResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * com.zeus.repo
 * Created by zengxm on 2017/8/22.
 */
public class CommonRepoImpl<T,ID extends java.io.Serializable> extends SimpleMongoRepository<T,ID> implements CommonRepo<T,ID>{

    protected final MongoOperations mongoTemplate;

    protected final MongoEntityInformation<T, ID> entityInformation;

    public CommonRepoImpl(MongoEntityInformation<T, ID> metadata, MongoOperations mongoOperations) {
        super(metadata, mongoOperations);

        this.mongoTemplate=mongoOperations;
        this.entityInformation = metadata;
    }

    protected Class<T> getEntityClass(){
        return entityInformation.getJavaType();
    }

    @Override
    public Page<T> find(Query query, Pageable p) {
        long total=mongoTemplate.count(query, getEntityClass());
        List<T> list=mongoTemplate.find(query.with(p), getEntityClass());

        return new PageImpl<T>(list, p, total);
    }

    @Override
    public Page<T> find(Criteria criteria, Pageable p) {
        return find(new Query(criteria), p);
    }

    @Override
    public T findOne(Criteria criteria) {
        return findOne(new Query(criteria));
    }

    @Override
    public T findOne(Query query) {
        if(query.getSortObject()==null)
            query.with(new Sort(Sort.Direction.DESC, "_id"));
        return mongoTemplate.findOne(query,entityInformation.getJavaType());
    }


    @Override
    public List<T> find(Query query, Pagination p) {
        long total = mongoTemplate.count(query, entityInformation.getJavaType());
        p.setTotal(total);
        query.skip((p.getPage() - 1) * p.getPageSize()).limit(p.getPageSize());
        return mongoTemplate.find(query, entityInformation.getJavaType());
    }

    @Override
    public List<T> find(Query query) {
        return mongoTemplate.find(query, entityInformation.getJavaType());
    }

    @Override
    public List<T> find(Criteria criteria, Pagination p) {
        return find(new Query(criteria),p);
    }

    @Override
    public Long delete(Criteria criteria) {
        WriteResult result = mongoTemplate.remove(new Query(criteria), entityInformation.getJavaType());
        return Long.parseLong(String.valueOf(result.getN()));
    }

    @Override
    public Long count(Criteria criteria) {
        return count(new Query(criteria));
    }

    @Override
    public Long count(Query query) {
        return mongoTemplate.count(query, entityInformation.getJavaType());
    }

    @Override
    public WriteResult updateFirst(Query query, Update update) {
        return mongoTemplate.updateFirst(query, update, entityInformation.getJavaType());
    }

    @Override
    public WriteResult update(Query query, Update update) {
        return mongoTemplate.updateMulti(query, update, entityInformation.getJavaType());
    }

    @Override
    public List<?> groupBy(String groupField, Criteria criteria) {
        return groupBy(new GroupModel(groupField, GroupModel.MAX), criteria, null);
    }

    @Override
    public List<?> groupBy(String groupField) {
        return groupBy(groupField, null);
    }

    @Override
    public List<?> groupBy(GroupModel model, Criteria criteria, Map<String, String> fields) {
        Assert.notNull(model, "GroupModel required!");
        Assert.hasText(model.groupBy, "groupField required! e.g.: name, age");
        MatchOperation matchF = Aggregation.match(Objects.isNull(criteria)? new Criteria() : criteria);

        GroupOperation group = Aggregation.group(model.groupBy).count().as(COUNT);

        if(!Objects.isNull(fields)) {
            for (String k : fields.keySet())
                group = group.last(k).as(fields.get(k));
        }

        List<AggregationOperation> options = new ArrayList<>();
        options.add(matchF);
        options.add(group);
        options.add(
                Aggregation.sort(
                        Objects.isNull(model.sort)? Sort.Direction.ASC : model.sort,
                        StringUtils.hasText(model.sortBy)? model.sortBy : COUNT
                )
        );
        if(model.limit>=0)
            options.add(Aggregation.limit(model.limit));
        return aggregate(Aggregation.newAggregation(options), Map.class);
    }

    @Override
    public <R> List<R> aggregate(Aggregation aggregation,Class<R> clazz) {
        AggregationResults<R> results = mongoTemplate.aggregate(aggregation, entityInformation.getJavaType(), clazz);
        return results.getMappedResults();
    }

    @Override
    public String getCollectionName() {
        return entityInformation.getCollectionName();
    }

    @Override
    public CommandResult execCommand(String json) {
        return mongoTemplate.executeCommand(json);
    }

    @Override
    public CommandResult getCollectionStats() {
        return execCommand("{collstats:'"+getCollectionName()+"'}");
    }
}
