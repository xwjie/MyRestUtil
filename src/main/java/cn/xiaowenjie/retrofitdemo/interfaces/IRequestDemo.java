package cn.xiaowenjie.retrofitdemo.interfaces;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import cn.xiaowenjie.myrestutil.http.GET;
import cn.xiaowenjie.retrofitdemo.beans.Result;

@GET("/get")
public interface IRequestDemo {

	@GET("get1")
	Result get1();

	@Cacheable
	@GET("get2/{param1}")
	Result get2(String str);

	@GET("get3")
	Result get3(String param2);

}
