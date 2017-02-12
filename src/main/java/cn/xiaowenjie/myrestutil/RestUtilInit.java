package cn.xiaowenjie.myrestutil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import cn.xiaowenjie.myrestutil.beans.RequestInfo;
import cn.xiaowenjie.myrestutil.http.GET;
import cn.xiaowenjie.myrestutil.interfaces.IRequestHandle;

@Component
public class RestUtilInit {

	@Autowired
	IRequestHandle requestHandle;

	@Autowired
	ApplicationContext ctx;

	@PostConstruct
	public void init() {

		Set<Class<?>> requests = new Reflections("cn.xiaowenjie").getTypesAnnotatedWith(GET.class);

		System.out.println(requests);
		System.out.println("==============n\n\n\n");

		InvocationHandler handler = new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

				RequestInfo request = extractRequestInfo(method, args);

				return requestHandle.handle(request);
			}
		};

		registerBean("handler", handler.getClass());

		for (Class<?> cls : requests) {
			Object demo2 = Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class<?>[] { cls }, handler);

			registerBean(cls.getName(), demo2.getClass());
		}

	}

	protected RequestInfo extractRequestInfo(Method method, Object[] args) {
		RequestInfo info = new RequestInfo();

		GET annotation = method.getAnnotation(GET.class);

		String url = annotation.value();
		
		info.setUrl(url);
		
		
		return info;
	}

	public void registerBean(String name, Class<?> beanClass) {

		// 获取BeanFactory
		DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) ctx
				.getAutowireCapableBeanFactory();

		// 创建bean信息.
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(beanClass);

		// beanDefinitionBuilder.addPropertyValue("name","张三");

		// 动态注册bean.
		defaultListableBeanFactory.registerBeanDefinition(name, beanDefinitionBuilder.getBeanDefinition());
	}

}
