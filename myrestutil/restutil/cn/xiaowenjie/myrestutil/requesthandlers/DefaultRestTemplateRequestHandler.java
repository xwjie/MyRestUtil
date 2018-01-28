package cn.xiaowenjie.myrestutil.requesthandlers;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import cn.xiaowenjie.myrestutil.beans.RequestInfo;
import cn.xiaowenjie.myrestutil.beans.RestInfo;
import cn.xiaowenjie.myrestutil.interfaces.IRequestHandle;

/**
 * 实现了IRequestHandle，基于resttemplate处理rest请求。
 * 需要在spring容器中注册RestTemplate。
 * 
 * @author 肖文杰
 * @date 2017.4.30
 */
@Component
public class DefaultRestTemplateRequestHandler implements IRequestHandle {

	@Autowired
	RestTemplate rest;

	@Override
	public Object handle(RestInfo restInfo, RequestInfo request) {
		System.err.println("\n\n\thandle request,  restInfo=" + restInfo);
		System.err.println("\thandle request,  request=" + request);

		String url = extractUrl(restInfo, request);
		System.err.println("\thandle url:" + url);

		//TODO 目前只写了get请求，需要支持post等在这里增加
		//TODO 需要在这里增加异常处理，如登录失败，链接不上

		Object result = rest.getForObject(url, request.getReturnType());

		return result;
	}

	/**
	 * 生成真实的url
	 * 
	 * @param restInfo
	 * @param request
	 * @return
	 */
	private String extractUrl(RestInfo restInfo, RequestInfo request) {
		String url = restInfo.getHost() + request.getUrl();

		if (request.getParams() == null) {
			return url;
		}

		Set<Entry<String, String>> entrySet = request.getParams().entrySet();

		String params = "";

		for (Iterator<Entry<String, String>> iterator = entrySet.iterator(); iterator.hasNext();) {
			Entry<String, String> entry = iterator.next();
			params += entry.getKey() + '=' + entry.getValue() + '&';
		}

		return url + '?' + params.substring(0, params.length() - 1);
	}

}
