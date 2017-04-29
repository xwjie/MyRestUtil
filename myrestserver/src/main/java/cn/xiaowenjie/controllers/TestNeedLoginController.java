package cn.xiaowenjie.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.xiaowenjie.beans.ResultBean;
import cn.xiaowenjie.beans.User;

@RequestMapping("/testneedlogin")
@RestController
public class TestNeedLoginController {

	@GetMapping("/get1")
	public ResultBean get1(@AuthenticationPrincipal User user) {
		System.out.println("TestNeedLoginController.get1() =" + user);
		return new ResultBean("get1 success!");
	}

	@GetMapping("/get2")
	public ResultBean get2(@AuthenticationPrincipal User user, String key) {
		System.out.println("TestNeedLoginController.get1() =" + user + ", key" + key);
		return new ResultBean("get2 success, key=" + key);
	}

}
