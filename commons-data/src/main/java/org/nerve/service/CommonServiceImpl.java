package org.nerve.service;

import com.mongodb.WriteResult;
import org.nerve.auth.AuthenticProvider;
import org.nerve.domain.DateEntity;
import org.nerve.domain.ID;
import org.nerve.domain.TrashEntity;
import org.nerve.domain.UpdateDateEntity;
import org.nerve.enums.Events;
import org.nerve.enums.Fields;
import org.nerve.enums.LogType;
import org.nerve.exception.Exceptions;
import org.nerve.exception.ServiceException;
import org.nerve.repo.CommonRepo;
import org.nerve.repo.Pagination;
import org.nerve.sys.L;
import org.nerve.util.GenericItem;
import org.nerve.utils.ConvertUtils;
import org.nerve.utils.reflection.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.Assert;
import reactor.bus.Event;
import reactor.bus.EventBus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * com.zeus.service
 * Created by zengxm on 2017/8/22.
 */
public class CommonServiceImpl<T extends ID, R extends CommonRepo<T,String>> extends GenericItem<T> implements CommonService<T,R> {
	protected Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	protected R repo;

	@Autowired(required = false)
	protected AuthenticProvider authProvider;

	@Autowired
	protected EventBus eventBus;

	@Override
	public T get(String id) {
		return repo.findOne(id);
	}

    /**
     * 保存前进行的操作
     * @param t
     * @return 返回false时，保存操作将终止
     */
    @Override
    public boolean onBeforeSave(T t) {
        return true;
    }

    /**
     * 保存后需要执行的操作，之类可以扩展此方法
     * @param t
     */
    protected void onAfterSave(T t,boolean isNew){
	    sendEvent(t,isNew? Events.CREATE: Events.UPDATE);
    }

    /**
     * 更新指定的属性
     * @param id
     * @param field
     * @param value
     */
    @Override
    public void modifyField(String id, String field, String value) {
        T entity = get(id);
        if(entity != null){
            Field f = ReflectionUtils.getAccessibleField(entity, field);
            ReflectionUtils.setFieldValue(entity, field, ConvertUtils.convertStringToObject(value, f.getType()));

            save(entity);
        }
    }

    @Override
    public void delete(T t) {
        if(onBeforeDelete(t)){
            //如果是TrashEntity对象则更新trash属性
            if(t instanceof TrashEntity){
                ((TrashEntity) t).setTrash(true);

                repo.save(t);
            }else
                repo.delete(t);

            L.log(LogType.DELETE,"删除 "+nameOfDomain()+" "+textForEntity(t), t, authProvider.get());
            log.info("删除对象 {} {}", nameOfDomain(), textForEntity(t));
            onAfterDelete(t);
        }
    }

    @Override
    public void delete(String id) {
        delete(get(id));
    }

    @Override
    public List<T> list(Criteria c, Pagination p){
        return list(new Query(c), p);
    }

    @Override
    public List<T> list(Query q, Pagination p){
        if(repo instanceof CommonRepo)
            return repo.find(q,p);
        log.warn("please Use CommonRepository, because We need to call find() method! Here will return emptyList.");
        return new ArrayList<T>();
    }

    /**
     * 删除对象前的操作
     * @param t    entity
     * @return 返回false终止删除
     */
    @Override
    public boolean onBeforeDelete(T t) {
        return true;
    }

    /**
     * 删除对象后的行为
     * @param t
     */
    protected void onAfterDelete(T t){
	    sendEvent(t, Events.DELETE);
    }

	/**
	 * 保存实体
	 * @param t
	 * @return
	 */
	@Override
	public void save(T t) {
		if(!checkAvailable(t))
			throw new ServiceException(Exceptions.INVALID_ENTITY);

		if(t.using()){
			T oldEntity = repo.findOne(t.id());
			try {
				//默认 id，addDate 不需要复制
				List<String> fields = new ArrayList<>();
				fields.add(Fields.ID.value());
				if(t instanceof DateEntity)
					fields.add(Fields.ADD_DATE.value());
				fields.addAll(ignorePropertiesOnSave());

				org.springframework.beans.BeanUtils.copyProperties(t, oldEntity, fields.stream().toArray(String[]::new));
			} catch (Exception e) {
				e.printStackTrace();
			}

			if(onBeforeSave(oldEntity)){
				if(oldEntity instanceof UpdateDateEntity)
					((UpdateDateEntity) oldEntity).setUpdateDate(new Date());

				repo.save(oldEntity);
                L.log(LogType.UPDATE,"更新对象 "+nameOfDomain()+" "+textForEntity(t), t, authProvider.get());
				log.info("更新对象 {} {}", nameOfDomain(), textForEntity(t));

				onAfterSave(oldEntity, false);
			}
		}else{
			//如果是 DateEntity 则自动设置录入日期
			if(t instanceof DateEntity)
				((DateEntity) t).setAddDate(new Date());

			if(onBeforeSave(t)){
				repo.save(t);
                L.log(LogType.CREATE,"保存对象 "+nameOfDomain()+" "+textForEntity(t), t, authProvider.get());
				log.info("保存对象 {} {}", nameOfDomain(), textForEntity(t));

				onAfterSave(t, true);
			}
		}
	}

	/**
	 * 更新式保存实体时，不需要copy的属性列表
	 * @return
	 */
	protected List<String> ignorePropertiesOnSave(){
		return Collections.EMPTY_LIST;
	}

    /**
     * 检测数据的有效性
     * @param entity
     * @return 返回 true 则可以保存，否则报错
     */
    protected boolean checkAvailable(T entity){
        return true;
    }

    protected Query idQuery(T t){
        return idQuery(t.id());
    }

    protected Query idQuery(String id){
        return new Query(Criteria.where("_id").is(id));
    }

    @Override
    public long count() {
        return repo.count();
    }

    @Override
    public long count(Criteria criteria) {
        if(repo instanceof CommonRepo)
            return repo.count(criteria);

        log.warn("please Use CommonRepository, because We need to call count(Criteria) method! Here will return -1.");
        return -1;
    }

    protected T getById(String id){
        T entity = get(id);
        Assert.notNull(entity,"ID="+entity.id()+"的对象不存在");
        return entity;
    }

    @Override
    public WriteResult runUpdate(String id, Update update) {
        return runUpdate(Criteria.where(Fields.ID.value()).is(id), update);
    }

    @Override
    public WriteResult runUpdate(Criteria criteria, Update update) {
        if(repo instanceof CommonRepo)
            return repo.updateFirst(new Query(criteria), update);

        log.warn("please Use CommonRepository, because We need to call updateFirst() method! Here will return null.");
        return null;
    }

	/**
	 * 获取泛型的类名
	 * @return
	 */
	protected String nameOfDomain(){
		return getGenericClass().getSimpleName();
	}

	/**
	 * 实体描述信息，之类可以重写此方法来自定义描述信息
	 *
	 * @param t
	 * @return
	 */
	protected String textForEntity(T t){
		return String.format("(id=%s)", t.id());
	}


	protected void sendEvent(T t, Events events){
		String eventKey = String.format("%s.%s", t.getClass().getSimpleName().toUpperCase(), events);
		eventBus.notify(eventKey, Event.wrap(t));

		log.info("publish event {} on {}#{}", eventKey, t.getClass().getName(), t.id());
	}
}
