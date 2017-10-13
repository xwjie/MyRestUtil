package cn.xiaowenjie.retrofitdemo.services;

import cn.xiaowenjie.myrestutil.http.GET;
import cn.xiaowenjie.myrestutil.http.Param;
import cn.xiaowenjie.myrestutil.http.Rest;
import cn.xiaowenjie.retrofitdemo.beans.ResultBean;

/**
 * 一些业务代码
 * 
 * 里面调用了一些http接口。
 * 
 * @author 肖文杰 https://github.com/xwjie/MyRestUtil
 *
 */
@Rest(value = "http://localhost:8081/test")
public abstract class SomeService {

	/**
	 * 在类里面增加一个抽象的http接口调用方法
	 * @return
	 */
	@GET("/get2")
	public abstract ResultBean get2(@Param("key") String key);

	/**
	 * 业务代码类中直接调用http接口
	 * 
	 * @return
	 */
	public String doSomething() {
		// 这里是一些业务代码，中间调用了其他系统的http接口。
		return "调用接口返回结果：" + get2("支持直接在类里面注入使用").getData();
	}
}
