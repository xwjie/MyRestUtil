package cn.xiaowenjie.myrestutil;

import cn.xiaowenjie.myrestutil.interfaces.ProxyCreater;
import org.springframework.cglib.proxy.CallbackHelper;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.InvocationHandler;

/**
 * 创建CGLib动态代理
 * @author 李佳明
 * @date 2017.10.18
 */
public class CGLibProxyCreater implements ProxyCreater {
	private Class<?> superClass;
	private Class<?> proxyClass;
	private String proxyClassName;
	private InvocationHandler invocationHandler;
	private Enhancer enhancer;
	private CallbackHelper callbackHelper;

	public CGLibProxyCreater(Class<?> superClass, String proxyClassName,
			CallbackHelper callbackHelper, InvocationHandler invocationHandler) {
		this.superClass = superClass;
		this.proxyClassName = proxyClassName;
		this.invocationHandler = invocationHandler;
		this.callbackHelper = callbackHelper;
		init(superClass, callbackHelper);
	}

	private void init(Class<?> superClass, CallbackHelper callbackHelper) {
		this.enhancer = new Enhancer();
		this.enhancer.setSuperclass(superClass);
		enhancer.setCallbackFilter(callbackHelper);
		enhancer.setCallbacks(callbackHelper.getCallbacks());
		this.proxyClass = this.enhancer.create().getClass();
	}

	@Override
	public Object newProxyInstance() throws Exception {
		return this.enhancer.create();
	}

	@Override
	public Class<?> getProxyClass() {
		return this.proxyClass;
	}
}