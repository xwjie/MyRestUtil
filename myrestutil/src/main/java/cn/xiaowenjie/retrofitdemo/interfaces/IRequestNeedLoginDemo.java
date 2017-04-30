package cn.xiaowenjie.retrofitdemo.interfaces;

import cn.xiaowenjie.myrestutil.http.GET;
import cn.xiaowenjie.myrestutil.http.Param;
import cn.xiaowenjie.myrestutil.http.Rest;
import cn.xiaowenjie.retrofitdemo.beans.ResultBean;

@Rest("http://localhost:8081/testneedlogin")
public interface IRequestNeedLoginDemo {

	@GET
	ResultBean get1();

	@GET("/get2")
	ResultBean getWithKey(@Param("key") String key);
}
