package cn.xiaowenjie.myrestutil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.xiaowenjie.myrestutil.beans.RequestInfo;
import cn.xiaowenjie.myrestutil.beans.RestInfo;
import cn.xiaowenjie.myrestutil.http.GET;
import cn.xiaowenjie.myrestutil.http.Param;
import cn.xiaowenjie.myrestutil.http.Rest;
import cn.xiaowenjie.myrestutil.interfaces.IRequestHandle;

/**
 * 扫描所有符合条件接口类，生成代理类处理请求，并把代理类注册到spring容器中。
 * 
 * @author 肖文杰
 * @date 2017.4.30
 */
@Component
public class RestUtilInit {

	@Autowired
	IRequestHandle requestHandle;

	@Autowired
	ApplicationContext ctx;

	@PostConstruct
	public void init() {

		Set<Class<?>> requests = new Reflections("cn.xiaowenjie").getTypesAnnotatedWith(Rest.class);

		for (Class<?> cls : requests) {
			System.out.println("create proxy for class:" + cls);

			// rest服务器相关信息
			final RestInfo restInfo = extractRestInfo(cls);

			InvocationHandler handler = new InvocationHandler() {
				@Override
				public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
					RequestInfo request = extractRequestInfo(method, args);
					return requestHandle.handle(restInfo, request);
				}
			};

			// 创建动态代理类
			Object proxy = Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class<?>[] { cls }, handler);

			registerBean(cls.getName(), proxy);
		}

	}

	private RestInfo extractRestInfo(Class<?> cls) {
		RestInfo restinfo = new RestInfo();

		Rest annotation = cls.getAnnotation(Rest.class);

		String host = annotation.value();
		restinfo.setHost(host);

		return restinfo;
	}

	protected RequestInfo extractRequestInfo(Method method, Object[] args) {
		RequestInfo info = new RequestInfo();

		// TODO 目前只写了get请求，需要支持post等在这里增加
		GET annotation = method.getAnnotation(GET.class);

		// url
		String url = annotation.value();

		// 没有配置路径取函数名称
		if (StringUtils.isEmpty(url)) {
			url = "/" + method.getName();
		}

		info.setUrl(url);

		// 返回类型
		info.setReturnType(method.getReturnType());

		// 参数
		LinkedHashMap<String, String> params = extractParams(method, args);
		info.setParams(params);

		return info;
	}

	private LinkedHashMap<String, String> extractParams(Method method, Object[] args) {
		Parameter[] parameters = method.getParameters();

		if (parameters.length == 0) {
			return null;
		}

		LinkedHashMap<String, String> params = new LinkedHashMap<>();
		for (int i = 0; i < parameters.length; i++) {
			// FIXME 需要考虑变量名映射功能
			// TODO parameters[i].getName() 居然得到的结果是arg0
			Param param = parameters[i].getAnnotation(Param.class);

			if (param != null) {
				params.put(param.value(), String.valueOf(args[i]));
			}
		}

		return params;
	}

	public void registerBean(String name, Object obj) {

		// 获取BeanFactory
		DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) ctx
				.getAutowireCapableBeanFactory();

		// 动态注册bean.
		defaultListableBeanFactory.registerSingleton(name, obj);
	}

}
