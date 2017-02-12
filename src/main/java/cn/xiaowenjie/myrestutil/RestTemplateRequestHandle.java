package cn.xiaowenjie.myrestutil;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import cn.xiaowenjie.myrestutil.beans.RequestInfo;
import cn.xiaowenjie.myrestutil.interfaces.IRequestHandle;
import cn.xiaowenjie.retrofitdemo.beans.Result;

@Component
public class RestTemplateRequestHandle implements IRequestHandle {

	private static final String HOST = "http://127.0.0.1:8081/demo/";

	@Autowired
	RestTemplate rest;

	@PostConstruct
	public void init() {
		System.out.println("\n\n\n-----RestTemplateRequestHandle.init()---------\n\n\n");
	}

	@Override
	public Object handle(RequestInfo request) {
		System.out.println(" handle request, ulr=" + request.getUrl());

		Result result = rest.getForObject(HOST + request.getUrl(), Result.class);

		return result;
	}

}
