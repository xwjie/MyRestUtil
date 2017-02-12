package cn.xiaowenjie.myrestutil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@ComponentScan("cn.xiaowenjie")
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
