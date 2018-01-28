package cn.xiaowenjie.myrestutil;

import org.springframework.cglib.proxy.CallbackHelper;
import org.springframework.cglib.proxy.Enhancer;

import cn.xiaowenjie.myrestutil.interfaces.ProxyCreater;

/**
 * 创建CGLib动态代理
 * 
 * @author 李佳明
 * @date 2017.10.18
 */
public class CGLibProxyCreater implements ProxyCreater {
	private Class<?> proxyClass;
	private Enhancer enhancer;

	public CGLibProxyCreater(Class<?> superClass, CallbackHelper callbackHelper) {
		init(superClass, callbackHelper);
	}

	private void init(Class<?> superClass, CallbackHelper callbackHelper) {
		this.enhancer = new Enhancer();

		this.enhancer.setSuperclass(superClass);
		this.enhancer.setCallbackFilter(callbackHelper);
		this.enhancer.setCallbacks(callbackHelper.getCallbacks());

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