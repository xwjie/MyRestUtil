package cn.xiaowenjie.retrofitdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@ComponentScan("cn.xiaowenjie")
@EnableWebMvc
@SpringBootApplication
public class MyRestUtilApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyRestUtilApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		System.out.println("-------restTemplate-------");

		return new RestTemplate();
	}

}
