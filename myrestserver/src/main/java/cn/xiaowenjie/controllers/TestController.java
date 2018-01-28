package cn.xiaowenjie.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.xiaowenjie.beans.ResultBean;

@RequestMapping("/test")
@RestController
public class TestController {

	@GetMapping("/get1")
	public ResultBean get1() {
		System.out.println("TestController.get1()");
		return new ResultBean("get1 success!");
	}

	@GetMapping("/get2")
	public ResultBean get2(String key) {
		System.out.println("TestController.get2() =" + key);
		return new ResultBean("get2 success, key=" + key);
	}

	@GetMapping("/get3")
	public ResultBean get3(String key1, String key2) {
		System.out.println("TestController.get3() =" + key1 + ", " + key2);
		return new ResultBean("get3 success, key=" + key1 + ", " + key2);
	}
}
