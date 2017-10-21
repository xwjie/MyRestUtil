package cn.xiaowenjie.myrestutil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.Set;

import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.cglib.proxy.CallbackHelper;
import org.springframework.cglib.proxy.NoOp;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.xiaowenjie.myrestutil.beans.RequestInfo;
import cn.xiaowenjie.myrestutil.beans.RestInfo;
import cn.xiaowenjie.myrestutil.http.GET;
import cn.xiaowenjie.myrestutil.http.Param;
import cn.xiaowenjie.myrestutil.http.Rest;
import cn.xiaowenjie.myrestutil.interfaces.IRequestHandle;
import lombok.extern.slf4j.Slf4j;

/**
 * 扫描所有符合条件接口类，生成代理类处理请求，并把代理类注册到spring容器中。
 *
 * @author 肖文杰
 * @author 李佳明
 * @date 2017.4.30
 * @date 2017.10.11 将普通bean修改为BeanFactoryPostProcessor，保证IRequestHandle优先于其他任何bean注册到容器中
 * @date 2017.10.12 添加对Class的代理
 */
@Component
@Slf4j
public class RestUtilInit implements BeanFactoryPostProcessor {

	private DefaultListableBeanFactory defaultListableBeanFactory;

	@Override
	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory configurableListableBeanFactory)
			throws BeansException {
		this.defaultListableBeanFactory = (DefaultListableBeanFactory) configurableListableBeanFactory;
		try {
			this.init();
		}
		catch (NoSuchMethodException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public void init() throws NoSuchMethodException, ClassNotFoundException {
		Set<Class<?>> requests = new Reflections(getBaseScanPackage())
				.getTypesAnnotatedWith(Rest.class);

		for (Class<?> cls : requests) {
			createProxyClass(cls);
		}
	}

	/**
	 * 自动获取扫描路径
	 *
	 * @return
	 * @throws NoSuchMethodException
	 * @throws ClassNotFoundException
	 */
	private String getBaseScanPackage()
			throws NoSuchMethodException, ClassNotFoundException {
		String baseScanPackage = "cn.xiaowenjie";

		// 如果不是JUnit启动容器的，可以使用自动获取路径。JUnit启动的，只能使用硬编码或者配置文件
		if (!StackTraceHelper
				.isRunByJunit(StackTraceHelper.getMainThreadStackTraceElements())) {
			StackTraceHelper.getBasePackageByMain(2);
		}

		return baseScanPackage;
	}

	/**
	 * 根据场景创建JDK动态代理或者CGlib动态代理
	 */
	private void createProxyClass(Class<?> cls) throws NoSuchMethodException {
		log.info("\tcreate proxy for class:{}", cls);
		final RestInfo restInfo = extractRestInfo(cls);
		MyInvocationHandler handler = getMyInvocationHandler(restInfo);
		// 创建动态代理类定义
		BeanDefinition beanDefinition = getProxyBeanDefinition(cls, handler, restInfo);
		registerBeanDefinition(cls, beanDefinition);
	}

	private MyInvocationHandler getMyInvocationHandler(RestInfo restInfo) {
		return new MyInvocationHandler(restInfo);
	}

	private void registerBeanDefinition(Class<?> cls, BeanDefinition beanDefinition) {
		this.defaultListableBeanFactory.registerBeanDefinition(cls.getSimpleName(),
				beanDefinition);
	}

	private BeanDefinition getProxyBeanDefinition(Class<?> cls,
			MyInvocationHandler handler, RestInfo restInfo) throws NoSuchMethodException {
		boolean proxyClass = isProxyClass(cls);
		// 当注解了@Rest的类型是Class或者Rest的proxyClass的属性为true时，使用CGLib进行代理
		if (proxyClass || !cls.isInterface()) {
			Class<?> clazz = getCGLibProxyClass(cls, handler, restInfo);
			return getCGLibBeanDefinition(clazz);
		}
		else {
			Class<?> clazz = getJDKDynamicProxyClass(cls, handler);
			return getJDKBeanDefinition(clazz, handler);
		}
	}

	private Class<?> getCGLibProxyClass(Class<?> cls, MyInvocationHandler handler,
			RestInfo restInfo) {
		String newClassName = cls.getCanonicalName() + "Proxy";
		CallbackHelper callbackHelper = getCallbackFilter(cls, restInfo);
		CGLibProxyCreater cgLibProxyCreater = new CGLibProxyCreater(cls, newClassName,
				callbackHelper, handler);
		return cgLibProxyCreater.getProxyClass();
	}

	private Class<?> getJDKDynamicProxyClass(Class<?> cls, MyInvocationHandler handler)
			throws NoSuchMethodException {
		String newClassName = cls.getCanonicalName() + "Proxy";
		JDKProxyCreater jdkProxyCreater = new JDKProxyCreater(newClassName,
				new Class<?>[] { cls }, handler);
		return jdkProxyCreater.getProxyClass();
	}

	private BeanDefinition getJDKBeanDefinition(Class<?> proxyClass,
			MyInvocationHandler handler) {
		BeanDefinition beanDefinition = BeanDefinitionBuilder
				.genericBeanDefinition(proxyClass).addConstructorArgValue(handler)
				.getRawBeanDefinition();
		beanDefinition.setAutowireCandidate(true);
		return beanDefinition;
	}

	private BeanDefinition getCGLibBeanDefinition(Class<?> proxyClass) {
		BeanDefinition beanDefinition = BeanDefinitionBuilder
				.genericBeanDefinition(proxyClass).getRawBeanDefinition();
		beanDefinition.setAutowireCandidate(true);
		return beanDefinition;
	}

	/**
	 * 只对添加了@GET注解的方法进行Rest代理
	 */
	private CallbackHelper getCallbackFilter(Class<?> cls, final RestInfo restInfo) {
		CallbackHelper callbackHelper = new CallbackHelper(cls, new Class[] {}) {
			@Override
			protected Object getCallback(Method method) {
				if (method.getAnnotation(GET.class) == null) {
					return NoOp.INSTANCE;
				}
				else {
					return new MyInvocationHandler(restInfo);
				}

			}
		};
		return callbackHelper;
	}

	/**
	 * 判断是不是需要代理类（相对于代理接口，是个是代理类则使用cglib）
	 * <p>
	 * 不是interface，或者 proxyClass=true 返回true
	 *
	 * @param cls
	 * @return
	 */
	private boolean isProxyClass(Class<?> cls) {
		return !cls.isInterface() || cls.getAnnotation(Rest.class).proxyClass();
	}

	private RestInfo extractRestInfo(Class<?> cls) {
		RestInfo restinfo = new RestInfo();
		Rest annotation = cls.getAnnotation(Rest.class);
		String host = annotation.value();
		restinfo.setHost(host);
		return restinfo;
	}

	/**
	 * Extract request info request info.
	 *
	 * @param method the method
	 * @param args the args
	 * @return the request info
	 */
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
			// added by 李佳明：编译时必须加上 -g 参数才会生成方法参数名
			// TODO parameters[i].getName() 居然得到的结果是arg0
			Param param = parameters[i].getAnnotation(Param.class);

			if (param != null) {
				params.put(param.value(), String.valueOf(args[i]));
			}
		}

		return params;
	}

	private class MyInvocationHandler implements InvocationHandler,
			org.springframework.cglib.proxy.InvocationHandler {

		private RestInfo restInfo;

		public MyInvocationHandler(RestInfo restInfo) {
			this.restInfo = restInfo;
		}

		/**
		 * 请求处理类（只获取一次）
		 */
		private IRequestHandle requestHandle;

		private IRequestHandle getRequestHandler() {
			if (this.requestHandle == null) {
				this.requestHandle = defaultListableBeanFactory
						.getBean(IRequestHandle.class);
			}

			return this.requestHandle;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			RequestInfo request = extractRequestInfo(method, args);
			return getRequestHandler().handle(restInfo, request);
		}
	}
}