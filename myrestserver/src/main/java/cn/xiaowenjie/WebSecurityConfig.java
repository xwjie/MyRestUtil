package cn.xiaowenjie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import cn.xiaowenjie.services.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService userService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().antMatchers("/test/**").permitAll().antMatchers("/login").permitAll()
				.antMatchers("/logout").permitAll().antMatchers("/images/**").permitAll().antMatchers("/js/**")
				.permitAll().antMatchers("/css/**").permitAll().antMatchers("/fonts/**").permitAll().antMatchers("/")
				.permitAll().anyRequest().authenticated().and().sessionManagement().and().logout().and().httpBasic();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
		authManagerBuilder.userDetailsService(userService).passwordEncoder(new PasswordEncoder() {

			@Override
			public String encode(CharSequence rawPassword) {
				System.out.println("\n\nencode pass=" + rawPassword);
				return String.valueOf(rawPassword);
			}

			@Override
			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				System.out.println("==matches, " + rawPassword + " , " + encodedPassword);
				// FIXME 测试数据：密码只要是admin就可以
				return "admin".equals(rawPassword);
			}
		});
	}
}