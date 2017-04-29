package cn.xiaowenjie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class MyrestserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyrestserverApplication.class, args);
	}

}
