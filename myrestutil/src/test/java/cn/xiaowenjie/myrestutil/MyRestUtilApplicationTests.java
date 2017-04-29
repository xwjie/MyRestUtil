package cn.xiaowenjie.myrestutil;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.xiaowenjie.retrofitdemo.beans.ResultBean;
import cn.xiaowenjie.retrofitdemo.interfaces.IRequestDemo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyRestUtilApplicationTests {

	@Autowired
	IRequestDemo demo;

	@Test
	public void test() {
		assertNotNull(demo);

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

}
