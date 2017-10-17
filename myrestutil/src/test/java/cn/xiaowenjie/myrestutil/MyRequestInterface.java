package cn.xiaowenjie.myrestutil;

import org.springframework.cache.annotation.Cacheable;

interface MyRequestInterface {
	@Cacheable(key = "$abc")
	String get(String abc);
}