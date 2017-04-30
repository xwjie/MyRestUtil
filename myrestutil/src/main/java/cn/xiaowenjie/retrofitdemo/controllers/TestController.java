package cn.xiaowenjie.retrofitdemo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.xiaowenjie.retrofitdemo.beans.ResultBean;
import cn.xiaowenjie.retrofitdemo.interfaces.IRequestDemo;
import cn.xiaowenjie.retrofitdemo.interfaces.IRequestNeedLoginDemo;

@RestController
public class TestController {

	@Autowired
	IRequestDemo demo;

	@Autowired
	IRequestNeedLoginDemo needlogindemo;

	@GetMapping("/testrest")
	public String test() {
		String msg = "<h1>invoke remote rest result</h1>";

		ResultBean get1 = demo.get1();

		msg += "<br/>get1 result=" + get1;

		ResultBean get2 = demo.getWithKey("key-------");

		msg += "<br/>get2 result=" + get2;

		ResultBean get3 = demo.getWithMultKey("key11111", "key22222");

		msg += "<br/>get3 result=" + get3;

		return msg;
	}

	@GetMapping("/testrestneedlogin")
	public String test2() {
		String msg = "<h1>invoke remote rest result(need http basic auth)</h1>";

		ResultBean get1 = needlogindemo.get1();

		msg += "<br/>get1 result=" + get1;

		ResultBean get2 = needlogindemo.getWithKey("key-------");

		msg += "<br/>get2 result=" + get2;

		return msg;
	}
}
