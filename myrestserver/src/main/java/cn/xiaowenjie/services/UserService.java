package cn.xiaowenjie.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cn.xiaowenjie.beans.User;

@Service
public class UserService implements UserDetailsService {

	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("UrlUserService.loadUserByUsername()=" + username);
		
		return new User(username);
	}

}
