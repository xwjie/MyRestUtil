package cn.xiaowenjie.myrestutil;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import cn.xiaowenjie.retrofitdemo.MyRestUtilApplication;
import cn.xiaowenjie.retrofitdemo.beans.ResultBean;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyRestUtilApplication.class)
public class RestTemplateTest {

	private static final String HOST = "http://127.0.0.1:8081/test";

	@Autowired
	private RestTemplate t;

	@Before
	public void init() {
		assertNotNull(t);
	}

	@Test
	public void test() {
		ResultBean result = t.getForObject(HOST + "/get1", ResultBean.class);

		assertNotNull(result);

		System.out.println(result);

	}

}
