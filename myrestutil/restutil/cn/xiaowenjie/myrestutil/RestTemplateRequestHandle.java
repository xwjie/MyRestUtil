package cn.xiaowenjie.myrestutil;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import cn.xiaowenjie.myrestutil.beans.RequestInfo;
import cn.xiaowenjie.myrestutil.beans.RestInfo;
import cn.xiaowenjie.myrestutil.interfaces.IRequestHandle;

@Component
public class RestTemplateRequestHandle implements IRequestHandle {

	@Autowired
	RestTemplate rest;

	@Override
	public Object handle(RestInfo restInfo, RequestInfo request) {
		System.out.println("\n\n handle request,  restInfo=" + restInfo);
		System.out.println(" handle request,  request=" + request);

		String url = extractUrl(restInfo, request);
		System.out.println(" handle url:" + url);

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
