package org.nerve.example;

import org.nerve.utils.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * com.zeus.example
 * Created by zengxm on 2017/8/23.
 */
@Controller
public class IndexController  {
	private final String LOGIN = "login";

	/**
	 * 自定义登录界面
	 * @return
	 */
	@RequestMapping(value = "/login",method = RequestMethod.GET)
	public String login(String error,ModelMap model){
		model.addAttribute("error",error);
		return LOGIN;
	}

	@ResponseBody
	@RequestMapping("date")
	public String datetime(){
		return DateUtils.getDateTime();
	}
}
