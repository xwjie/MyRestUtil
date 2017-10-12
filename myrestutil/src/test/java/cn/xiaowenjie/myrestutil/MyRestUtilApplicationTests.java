package cn.xiaowenjie.myrestutil;

import cn.xiaowenjie.retrofitdemo.interfaces.ResquestDemoClass;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.xiaowenjie.retrofitdemo.MyRestUtilApplication;
import cn.xiaowenjie.retrofitdemo.beans.ResultBean;
import cn.xiaowenjie.retrofitdemo.interfaces.IRequestDemo;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyRestUtilApplication.class)
@Slf4j
public class MyRestUtilApplicationTests {

	@Autowired
	IRequestDemo demo;

	@Autowired
	ResquestDemoClass resquestDemoClass;

	@Test
	public void test() {
		ResultBean get1 = demo.get1();
		System.out.println(get1);
	}

	@Test
	public void test2() {
		ResultBean get2 = demo.getWithKey("2332323");
		System.out.println(get2);
	}

	@Test
	public void test3() {
		ResultBean get3 = demo.getWithMultKey("param111", "param222");
		System.out.println(get3);
	}

	@Test
	public void test4() {
		ResultBean get1 = resquestDemoClass.get1();
		log.info("get1 result: {}",get1);
	}

	@Test
	public void test5() {
		ResultBean get3 = resquestDemoClass.getWithMultKey("param111", "param222");
		log.info("get3 result: {}",get3);
	}

	@Test
	public void test6() {
		String result = resquestDemoClass.doSomething();
		assertEquals("test",result);
	}
}
