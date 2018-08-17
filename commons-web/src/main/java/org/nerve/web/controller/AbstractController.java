package org.nerve.web.controller;

import org.nerve.common.Result;
import org.nerve.domain.IdEntity;
import org.nerve.repo.CommonRepo;
import org.nerve.repo.Pagination;
import org.nerve.service.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * com.zeus.web.controller
 * Created by zengxm on 2017/8/23.
 */
public class AbstractController<T extends IdEntity, R extends CommonRepo<T,String>, S extends CommonService<T,R>> extends CommonDownloadController {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected String getTemplatePath() {
		return "";
	}

	@Autowired
	protected S service;

	@RequestMapping("index")
	public String index(){
		return view(INDEX);
	}

	/**
	 * 获取某个资源
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public T get(@PathVariable("id") String id){
		return service.get(id);
	}

	@ResponseBody
	@RequestMapping("list")
	public Result list(HttpServletRequest req, Pagination pagination){
		List<T> list = service.list(buildMongoQueryFromRequest(req, DEFAULT_SEARCH_PREFIX),pagination);
		return new Result(pagination.getTotal(), list);
	}

	@ResponseBody
	@RequestMapping(value={"add","edit"}, method = RequestMethod.POST)
	public Result add(T t){
		return result(re->{
			service.save(t);
			if(t instanceof IdEntity)
				re.setData(t.getId());
		});
	}

	@ResponseBody
	@RequestMapping("delete")
	public Result delete(@RequestParam("ids")String[] ids){
		return result(re->{
			for (String id : ids) {
				service.delete(id);
			}
		});
	}

	@ResponseBody
	@RequestMapping("modify")
	public Result modifyField(String id,String field,String value){
		return result(re->service.modifyField(id,field,value));
	}
}
