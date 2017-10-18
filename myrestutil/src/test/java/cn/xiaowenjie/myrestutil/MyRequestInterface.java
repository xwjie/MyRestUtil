package cn.xiaowenjie.myrestutil;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;

@Aspect
@Controller("myRequestInterfaceController")
interface MyRequestInterface {

    @Cacheable(key = "$abc")
    @NotNull
    @Min(10)
	String get(String abc);
}