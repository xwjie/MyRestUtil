package cn.xiaowenjie.myrestutil;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.xiaowenjie.retrofitdemo.beans.Result;
import cn.xiaowenjie.retrofitdemo.interfaces.IRequestDemo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyRestUtilApplicationTests {

	@Autowired
	IRequestDemo demo;

	@Test
	public void test() {
		assertNotNull(demo);

		Result get1 = demo.get1();

		System.out.println(get1);
	}

	@Test
	public void test2() {
		Result get1 = demo.get3("2332323");
		System.out.println(get1);
	}

}
