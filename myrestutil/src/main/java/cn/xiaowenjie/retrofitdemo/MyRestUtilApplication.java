package cn.xiaowenjie.retrofitdemo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

@ComponentScan("cn.xiaowenjie")
@SpringBootApplication
@EnableCaching
public class MyRestUtilApplication {

	public static void main(String[] args) throws Exception {
		
		SpringApplication.run(MyRestUtilApplication.class, args);
	}

	@Autowired(required = false)
	List<ClientHttpRequestInterceptor> interceptors;

	@Bean
	public RestTemplate restTemplate() {
		System.out.println("-------restTemplate-------");

		RestTemplate restTemplate = new RestTemplate();

		// 设置拦截器，用于http basic的认证等
		restTemplate.setInterceptors(interceptors);

		return restTemplate;
	}



}
