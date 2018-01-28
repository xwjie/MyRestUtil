package cn.xiaowenjie.myrestutil;

import cn.xiaowenjie.retrofitdemo.MyRestUtilApplication;
import cn.xiaowenjie.retrofitdemo.beans.ResultBean;
import cn.xiaowenjie.retrofitdemo.interfaces.IRequestDemo;
import cn.xiaowenjie.retrofitdemo.services.SomeService;
import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyRestUtilApplication.class)
@Slf4j
public class MyRestUtilApplicationTests {

	@Autowired
	IRequestDemo demo;

	public static void saveGeneratedJdkProxyFiles() throws Exception {
		System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
	}

	@Autowired
	SomeService service;

	@Test
	public void test1() throws Exception {
		saveGeneratedJdkProxyFiles();

		ResultBean get1 = demo.get1();
		System.out.println(get1);

		assertTrue(get1.getCode() == 0);

		// 测试cache
		// 第二次调用，不会再发送http请求。
		System.out.println("第二次获取，有cache的情况下不会再次发送请求");
		ResultBean get2 = demo.get1();
		System.out.println(get2);

		// 如果有cache。那么2次获取的结果应该是同一个对象
		assertTrue(get1 == get2);
	}

	@Test
	public void test2() {
		ResultBean get2 = demo.getWithKey("2332323");
		System.out.println(get2);
		assertTrue(get2.getCode() == 0);
	}

	@Test
	public void test3() {
		ResultBean get3 = demo.getWithMultKey("param111", "param222");
		System.out.println(get3);
		assertTrue(get3.getCode() == 0);
	}

	@Test
	public void testCglibProxy() {
		System.out.println(service.getClass());
		String result = service.doSomething();
		System.out.println(result);
		assertNotNull(result);
	}
}
