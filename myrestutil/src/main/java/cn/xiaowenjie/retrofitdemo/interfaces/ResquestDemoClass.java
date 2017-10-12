package cn.xiaowenjie.retrofitdemo.interfaces;

import cn.xiaowenjie.myrestutil.http.GET;
import cn.xiaowenjie.myrestutil.http.Param;
import cn.xiaowenjie.myrestutil.http.Rest;
import cn.xiaowenjie.retrofitdemo.beans.ResultBean;

@Rest(value = "http://localhost:8081/test",proxyClass = true)
public abstract class ResquestDemoClass {

	@GET
	public ResultBean get1(){return null;};

	@GET("/get2")
	public ResultBean getWithKey(@Param("key") String key) {return null;};

	@GET("/get3")
	public abstract ResultBean getWithMultKey(@Param("key1") String key, @Param("key2") String key2);

	public String doSomething() {
		return "test";
	}
}
