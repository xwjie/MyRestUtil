package cn.xiaowenjie.myrestutil;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Test;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class AnnotationUtilTest {
	@Test
	public void reflectionAnnotation() throws NoSuchMethodException,
			InvocationTargetException, IllegalAccessException {

		Class<?> clazz = Cacheable.class;
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			log.info("Get method : {}", method.getName());
			log.info("\tGet Default Value:{}", method.getDefaultValue());
			log.info("\tGet Return Type:{}", method.getReturnType());
		}

		Class<?> myClazz = MyRequestInterface.class;
		Method getMethod = myClazz.getMethod("get", String.class);
		Annotation cacheable = getMethod.getAnnotation(Cacheable.class);
		Class<? extends Annotation> annotationType = cacheable.annotationType();
		log.info("annotation type : {}", annotationType.getSimpleName());
		log.info("value method get : {}",
				MethodUtils.invokeExactMethod(cacheable, "key"));

	}

	private static interface MyRequestInterface {
		@RequestMapping(path = "/abc", method = RequestMethod.DELETE)
		@Cacheable(key = "$abc")
		public String get(String abc);
	}
}
