package cn.xiaowenjie.myrestutil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.aspectj.lang.annotation.Aspect;
import org.junit.Test;
import org.springframework.cache.annotation.Cacheable;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import sun.misc.ProxyGenerator;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Slf4j
public class AnnotationUtilTest {

	@Test
	public void testDynamicProxyLostAnnotations() throws NoSuchMethodException {
		Class<?> myClazz = MyRequestInterface.class;
		Annotation[] classAnnotations = myClazz.getAnnotations();
		log.info("Class annotations of interface MyRequestInterface");
		for (Annotation annotation : classAnnotations) {
			log.info("\t\t{}", annotation);
		}

		Method get = myClazz.getMethod("get",String.class);
		Annotation[] methodAnnotations = get.getAnnotations();
		log.info("Method annotations of get method");
		for (Annotation annotation : methodAnnotations) {
			log.info("\t\t{}", annotation);
		}

		Object proxy = Proxy.newProxyInstance(MyRequestInterface.class.getClassLoader(),
				new Class<?>[] { MyRequestInterface.class }, new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args)
							throws Throwable {
						return null;
					}
				});

		Class<?> proxyClass = proxy.getClass();
		Annotation[] proxyClassAnnotations = proxyClass.getAnnotations();
		//长度为0
		assertEquals(0,proxyClassAnnotations.length);
		proxyClassAnnotations = proxyClass.getDeclaredAnnotations();
		assertEquals(0,proxyClassAnnotations.length);

		Method proxyGet = proxyClass.getMethod("get",String.class);
		Annotation[] proxyMethodAnnotations = proxyGet.getAnnotations();
		//长度为0
		assertEquals(0,proxyMethodAnnotations.length);
		proxyMethodAnnotations = proxyGet.getDeclaredAnnotations();
		assertEquals(0,proxyMethodAnnotations.length);


		Class<?>[] interfaces = proxyClass.getInterfaces();
		for(Class<?> superInterface : interfaces) {
			log.info("superInterface {}",superInterface.getCanonicalName());
		}

	}

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

	@Test
	public void addAnnotationToMethod()
			throws NoSuchMethodException, InvocationTargetException,
			CannotCompileException, NotFoundException, IllegalAccessException {
		Class<?> myClazz = MyRequestInterface.class;
		Method getMethod = myClazz.getMethod("get", String.class);
		Annotation annotation = getMethod.getAnnotation(Cacheable.class);

		String proxyClassName = myClazz.getCanonicalName() + "Proxy";
		byte[] myRequestInterfaceProxy = ProxyGenerator.generateProxyClass(proxyClassName,
				new Class<?>[] { MyRequestInterface.class });
		Class<?> myClazz2 = AnnotationUtil.addAnnotationToMethod(proxyClassName, "get",
				myRequestInterfaceProxy, annotation);
		getMethod = myClazz2.getMethod("get", String.class);
		Cacheable cacheable = getMethod.getAnnotation(Cacheable.class);
		if (cacheable != null) {
			String key = cacheable.key();
			log.info(key);
			assertEquals("$abc", key);
		}
	}

	@Test
	public void addAnnotations() throws NoSuchMethodException, InvocationTargetException,
			CannotCompileException, NotFoundException, IllegalAccessException {
		Class<?> myClazz = MyRequestInterface.class;

		List<Annotation> classAnnotations = new ArrayList<>();
		Aspect aspect = myClazz.getAnnotation(Aspect.class);
		classAnnotations.add(aspect);
		Controller controller = myClazz.getAnnotation(Controller.class);
		classAnnotations.add(controller);

		List<AnnotationMetaDataInfo.MethodAnnotation> methodAnnotations = new ArrayList<>();
		List<Annotation> annotations = new ArrayList<>();
		Method getMethod = myClazz.getMethod("get", String.class);
		Cacheable cacheable = getMethod.getAnnotation(Cacheable.class);
		annotations.add(cacheable);
		NotNull notNull = getMethod.getAnnotation(NotNull.class);
		annotations.add(notNull);
		Min min = getMethod.getAnnotation(Min.class);
		annotations.add(min);

		methodAnnotations
				.add(new AnnotationMetaDataInfo.MethodAnnotation("get", annotations));

		String newClassName = myClazz.getCanonicalName() + "Proxy";

		AnnotationMetaDataInfo annotationMetaDataInfo = AnnotationMetaDataInfo.builder()
				/* .classAnnotations(classAnnotations) */.newClassName(newClassName)
				.methodAnnotations(methodAnnotations).build();

		byte[] myRequestInterfaceProxy = ProxyGenerator.generateProxyClass(newClassName,
				new Class<?>[] { MyRequestInterface.class });

		Class<?> proxyClass = AnnotationUtil.addAnnotaions(myRequestInterfaceProxy,
				annotationMetaDataInfo);

		Aspect aspect2 = proxyClass.getAnnotation(Aspect.class);
		assertNotNull(aspect2);

		Controller controller2 = proxyClass.getAnnotation(Controller.class);
		assertEquals(controller.value(), controller2.value());

		getMethod = proxyClass.getMethod("get", String.class);

		Cacheable cacheable2 = getMethod.getAnnotation(Cacheable.class);
		assertNotNull(cacheable2);
		assertEquals(cacheable.key(), cacheable2.key());

		NotNull notNull2 = getMethod.getAnnotation(NotNull.class);
		assertNotNull(notNull2);

		Min min2 = getMethod.getAnnotation(Min.class);
		assertNotNull(min2);
		assertEquals(min.value(), min2.value());
	}

}
