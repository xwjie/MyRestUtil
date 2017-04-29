package cn.xiaowenjie.myrestutil;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;

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

	@PostConstruct
	public void init() {
		System.out.println("\n\n\n-----RestTemplateRequestHandle.init()---------\n\n\n");
	}

	@Override
	public Object handle(RestInfo restInfo, RequestInfo request) {
		System.out.println("\n\n handle request,  restInfo=" + restInfo);
		System.out.println(" handle request,  request=" + request);

		String url = extractUrl(restInfo, request);
		System.out.println(" handle url:" + url);

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
