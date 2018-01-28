package cn.xiaowenjie.myrestutil.invocationhandlers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.util.StringUtils;

import cn.xiaowenjie.myrestutil.beans.RequestInfo;
import cn.xiaowenjie.myrestutil.beans.RestInfo;
import cn.xiaowenjie.myrestutil.http.GET;
import cn.xiaowenjie.myrestutil.http.Param;
import cn.xiaowenjie.myrestutil.interfaces.IRequestHandle;

/**
 * jdk和cglib都会调用的代理处理类。所以实现了jdk和cglib的2个接口
 * 
 * @author 肖文杰 https://github.com/xwjie/MyRestUtil
 * @author 李佳明
 */
public class MyInvocationHandler implements InvocationHandler, org.springframework.cglib.proxy.InvocationHandler {

	/**
	 * 根据【类】上面提取出来的的服务器信息
	 */
	private final RestInfo restInfo;

	/**
	 * spring的beanFactory。
	 * 
	 * 为了在运行时得到实际的网络请求处理类的bean
	 */
	private final BeanFactory beanFactory;

	public MyInvocationHandler(RestInfo restInfo, BeanFactory beanFactory) {
		this.restInfo = restInfo;
		this.beanFactory = beanFactory;
	}

	/**
	 * 请求处理类（只获取一次）
	 */
	private IRequestHandle requestHandle;

	private IRequestHandle getRequestHandler() {
		// 得到处理类
		if (this.requestHandle == null) {
			this.requestHandle = this.beanFactory.getBean(IRequestHandle.class);

			if (this.requestHandle == null) {
				throw new NullPointerException("没有在spring的容器里面得到IRequestHandle的实现类");
			}
		}

		return this.requestHandle;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		RequestInfo request = extractRequestInfo(method, args);
		return getRequestHandler().handle(restInfo, request);
	}

	/**
	 * Extract request info request info.
	 *
	 * @param method
	 *            the method
	 * @param args
	 *            the args
	 * @return the request info
	 */
	private RequestInfo extractRequestInfo(Method method, Object[] args) {
		// TODO 目前只写了get请求，需要支持post等在这里增加
		GET annotation = method.getAnnotation(GET.class);

		if (annotation == null) {
			throw new NullPointerException("当前被代理的方法" + method.getName() + "没有得到定义的注解信息！");
		}

		RequestInfo info = new RequestInfo();

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
}