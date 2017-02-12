package cn.xiaowenjie.myrestutil.http;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RUNTIME)
public @interface GET {

	String value() default "";
}