package cn.xiaowenjie.myrestutil;

import static org.junit.Assert.assertEquals;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Test;
import org.springframework.cache.annotation.Cacheable;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import sun.misc.ProxyGenerator;

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

	@Test
	public void addAnnotationToClassTest() throws NoSuchMethodException,
			ClassNotFoundException, InvocationTargetException, CannotCompileException,
			NotFoundException, IllegalAccessException {

		Class<?> myClazz = MyRequestInterface.class;
		Method getMethod = myClazz.getMethod("get", String.class);
		Annotation annotation = getMethod.getAnnotation(Cacheable.class);

		String proxyClassName = myClazz.getCanonicalName() + "Proxy";
		byte[] myRequestInterfaceProxy = ProxyGenerator.generateProxyClass(proxyClassName,
				new Class<?>[] { MyRequestInterface.class });
		Class<?> myClazz2 = AnnotationUtil.addAnnotationToClass(proxyClassName,
				myRequestInterfaceProxy, annotation);
		Cacheable cacheable = myClazz2.getAnnotation(Cacheable.class);
		if (cacheable != null) {
			String key = cacheable.key();
			log.info(key);
			assertEquals("$abc", key);
		}
	}

}
