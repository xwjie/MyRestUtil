package cn.xiaowenjie.myrestutil;

import cn.xiaowenjie.myrestutil.interfaces.ProxyCreater;
import lombok.Getter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

/**
 * 创建JDK动态代理
 * @author 李佳明
 * @date 2017.10.18
 */
@Getter
public class JDKProxyCreater implements ProxyCreater {

    private Class<?>[] interfaces;
    private Class<?> proxyClass;
    private String proxyClassName;
    private Constructor<?> proxyConstructor;
    private InvocationHandler invocationHandler;

    public JDKProxyCreater(String proxyClassName, Class<?>[] interfaces, InvocationHandler invocationHandler) throws NoSuchMethodException {
        this.proxyClassName = proxyClassName;
        this.interfaces = interfaces;
        this.invocationHandler = invocationHandler;
        this.proxyClass = Proxy.getProxyClass(JDKProxyCreater.class.getClassLoader(), this.interfaces);
        this.proxyConstructor = this.proxyClass.getConstructor(InvocationHandler.class);
    }

    @Override
    public Object newProxyInstance() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Object result = this.proxyConstructor.newInstance(this.invocationHandler);
        return result;
    }

    @Override
    public Class<?> getProxyClass() {
        return this.proxyClass;
    }
}