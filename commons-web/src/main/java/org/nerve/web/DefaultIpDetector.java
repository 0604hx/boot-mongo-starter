package org.nerve.web;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * com.zeus.web
 * Created by zengxm on 2017/8/23.
 */
@Component
public class DefaultIpDetector implements IpDetector {

	@Override
	public String getIp() {
		HttpServletRequest request = request();

		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			if("0:0:0:0:0:0:0:1".equals(ip))
				ip = "127.0.0.1";
		}
		return ip;
	}

	protected HttpServletRequest request(){
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		return ((ServletRequestAttributes)requestAttributes).getRequest();
	}

	@Override
	public String getUserAgent() {
		return request().getHeader("User-Agent");
	}
}
