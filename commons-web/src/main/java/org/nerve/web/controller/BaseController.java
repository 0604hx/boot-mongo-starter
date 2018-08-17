package org.nerve.web.controller;

import com.alibaba.fastjson.JSON;
import org.nerve.common.Result;
import org.apache.commons.lang3.time.DateUtils;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.function.Consumer;

/**
 * 控制器基类
 */
public abstract class BaseController {

	protected final String INDEX = "index";
	protected final String SUCCESS = "success";
	protected final String LIST = "list";
	protected final String EDIT = "edit";
	protected final String DELETE = "delete";
	protected final String SAVE = "save";
	protected final String VIEW = "view";
	/**分页key*/
	protected final String PAGINATION = "page";

	protected final String MESSAGE = "message";
	protected final String ADD = "add";

	protected final String ENTITY = "entity";

	/**
	 * 默认的搜索参数前缀
	 */
	protected final String DEFAULT_SEARCH_PREFIX = "s_";

	/**
	 * 控制器对应的模板目录
	 * @return
	 */
	protected String getTemplatePath(){
		return "";
	}

	/**
	 * @param viewName
	 * @return
	 */
	protected String view(String viewName) {
		return getTemplatePath()+viewName;
	}
	protected String redirect(String viewName) {
		return "redirect:"+viewName;
	}

	/**
	 * 向Response发送文本（默认使用UTF-8编码）
	 * @param res           Response
	 * @param msg           内容
	 * @throws IOException  IO异常
	 */
	protected void write(HttpServletResponse res, String msg)throws IOException{
		initResponse(res);
		res.getWriter().print(msg);
	}

	/**
	 * 设置response，默认的编码为utf-8
	 * @return       Http Response
	 */
	protected HttpServletResponse initResponse(HttpServletResponse resp){
		resp.setHeader("content-type", "text/html;charset=utf-8");
		return resp;
	}

	/**
	 * 判断是否为GET形式的请求
	 * @param req       Http 请求
	 * @return          true = GET 请求
	 */
	protected boolean isGET(HttpServletRequest req){
		return req==null?false:req.getMethod().equalsIgnoreCase("get");
	}

	private static String[] parsePatterns = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
			"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm" };

	protected Query buildMongoQueryFromRequest(HttpServletRequest request, String prefix){
		//获取search_关键字开头的参数
		Map<String, Object> map = WebUtils.getParametersStartingWith(request, prefix);

		return buildMongoQueryFromMap(map);
	}

	protected Criteria buildMongoCriteriaFromRequest(HttpServletRequest request, String prefix){
		Map<String, Object> map = WebUtils.getParametersStartingWith(request, prefix);

		return buildQueryItemFromMap(map).criteria;
	}

	protected Criteria buildMongoCriteriaFromRequest(HttpServletRequest request){
		return buildMongoCriteriaFromRequest(request, DEFAULT_SEARCH_PREFIX);
	}

	public Query buildMongoQueryFromMap(Map<String, Object> map){
		QueryItem item = buildQueryItemFromMap(map);

		Query query=new Query(item.criteria);
		if(item.sorts.size()==0)
			item.addSort(new Sort(Sort.Direction.DESC,"_id"));
		for(Sort s:item.sorts)
			query.with(s);
		return query;
	}

	/**
	 *
	 * @param map
	 * @return
	 */
	public QueryItem buildQueryItemFromMap(Map<String,Object> map){
		QueryItem queryItem = new QueryItem();

		if(map.size()>0){
			List<Criteria> criterias = new ArrayList<>();
			map.forEach((k,v)->{
				if(StringUtils.isEmpty(v))
					return;
				String t[] = k.split("_");
				boolean isSpecial=k.endsWith("_");      //是否需要特殊处理
				boolean isArray = v instanceof Object[];
				if(t.length<2)
					return ;

				//如果不是数组，则尝试进行日期转换
				if(!isArray){
					try{
						v = DateUtils.parseDate((String) v, parsePatterns);
						//如果最后的参数空的，那么就转换成long的时间戳，如search_GT_addDate_
						if(isSpecial){
							v=Long.valueOf(((Date)v).getTime());
						}

					} catch (ParseException e) {
					}
				}

				//注意：一些特殊情况，属性名中可能包含了_，这个是用来split的，所以这个时候，先处理_
				switch (t[0].toUpperCase()){
					case "LIKE":
						if(t.length==2)
							criterias.add(Criteria.where(t[1].replaceAll("@","_")).regex((String)v));
						else {
							Criteria[] likeCs = new Criteria[t.length-1];
							Criteria c = new Criteria();
							for(int i=1;i<t.length;i++){
								if(!StringUtils.isEmpty(v))
									likeCs[i-1]=(Criteria.where(t[i].replaceAll("@","_")).regex((String)v));
							}
							criterias.add(c.orOperator(likeCs));
						}
						break;
					case "EQ":{
						//如果需要特殊处理，则转换成ObjectId或者Long
						boolean isObjectId= ObjectId.isValid((String)v);
						//判断是否为true、false
						if(!isObjectId
								&& ("false".equalsIgnoreCase((String)v) || "true".equalsIgnoreCase((String)v))){
							criterias.add(Criteria.where(t[1]).is(Boolean.valueOf((String)v)));
						}else{
							criterias.add(Criteria.where(t[1]).is(
									isSpecial?
											(isObjectId?new ObjectId((String)v):Long.valueOf((String)v))
											:v));
						}

						break;
					}
					case "IN":  {
						//通常来说，数组就是用于 IN 查询，这里装换成 List
						List<Object> list = new ArrayList<>();
						Object[] vTemp = ((Object[]) v);
						for (Object o : vTemp) {
							String oString = (String)o;
							if(isSpecial){
								boolean isObjectId=ObjectId.isValid(oString);
								list.add(isObjectId?new ObjectId(oString):Long.valueOf(oString));
							}else
								list.add(oString);
						}

						System.out.println(String.format(" 执行 IN 查询：%s in %s", t[1], list));
						criterias.add(Criteria.where(t[1]).in(list));break;
					}
					//范围匹配时，如果需要特殊处理，则装换成long
					case "LT"   :criterias.add(Criteria.where(t[1]).lt(isSpecial?Long.valueOf((String)v):v));break;
					case "LTE"  :criterias.add(Criteria.where(t[1]).lte(isSpecial?Long.valueOf((String)v):v));break;
					case "GT"   :criterias.add(Criteria.where(t[1]).gt(isSpecial?Long.valueOf((String)v):v));break;
					case "GTE"  :criterias.add(Criteria.where(t[1]).gte(isSpecial?Long.valueOf((String)v):v));break;
					case "NE"   :criterias.add(Criteria.where(t[1]).ne(parseValue(v, isSpecial)));break;
					case "NOT"  :criterias.add(Criteria.where(t[1]).nin(v));break;
					case "SORT" :{
						Sort.Direction d= Sort.Direction.ASC;
						if(v.toString().equals("1")||v.toString().equalsIgnoreCase("DESC"))
							d= Sort.Direction.DESC;
						queryItem.addSort(new Sort(d, t[1]));
						break;
					}
				}
			});

			queryItem.addCriterias(criterias);
		}

		return queryItem;
	}

	private class QueryItem{
		final Criteria criteria = new Criteria();
		List<Sort> sorts = new ArrayList<>();

		public QueryItem addCriterias(Collection<? extends Criteria> criterias){
			if(!CollectionUtils.isEmpty(criterias)){
				Criteria[] cs = new Criteria[criterias.size()];
				criteria.andOperator(criterias.toArray(cs));
			}
			return this;
		}

		public QueryItem addSort(Sort sort){
			this.sorts.add(sort);
			return this;
		}
	}


	private static final Map<String,Object> SEARCH_VALUES = new HashMap<>();
	static {
		SEARCH_VALUES.put("@@","");
		SEARCH_VALUES.put("@NULL@", null);
		System.out.println("初始化 SEARCH_VALUES ："+SEARCH_VALUES);
	}

	/**
	 * 在处理 EQ、NE 的操作，如果希望 value 是空字符串、null 时略显乏力
	 * 目前方案是使用特殊的字符进行替换，详见 SEARCH_VALUES
	 *
	 * @param v
	 * @param isSpecial
	 * @return
	 */
	private Object parseValue(Object v, boolean isSpecial){
		if(isSpecial)
			return Long.valueOf((String) v);
		if(SEARCH_VALUES.containsKey(v))
			return SEARCH_VALUES.get(v);
		return v;
	}

	protected String toJson(Object obj){
		return JSON.toJSONString(obj);
	}

	/**
	 * 通用的返回result
	 * @param consumer  逻辑处理
	 * @return          Result
	 */
	protected Result result(Consumer<Result> consumer){
		Result result=new Result();
		try{
			consumer.accept(result);
		}catch (Exception e){
			result.error(e);
			e.printStackTrace();
		}
		return result;
	}
}
