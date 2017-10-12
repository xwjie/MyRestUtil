package cn.xiaowenjie.myrestutil;

import java.lang.reflect.Field;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.xiaowenjie.retrofitdemo.MyRestUtilApplication;
import cn.xiaowenjie.retrofitdemo.beans.ResultBean;
import cn.xiaowenjie.retrofitdemo.interfaces.IRequestDemo;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyRestUtilApplication.class)
public class MyRestUtilApplicationTests {

	@Autowired
	IRequestDemo demo;

	public static void saveGeneratedJdkProxyFiles() throws Exception {
		System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
	}

	@Test
	public void test1() throws Exception {
		saveGeneratedJdkProxyFiles();
		
		ResultBean get1 = demo.get1();
		System.out.println(get1);

		// 测试cache
		// 第二次调用，不会再发送http请求。
		get1 = demo.get1();
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
