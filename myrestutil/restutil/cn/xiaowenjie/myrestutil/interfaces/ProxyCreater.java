package cn.xiaowenjie.myrestutil.interfaces;

/**
 * 用于创建代理对象的Class和Instance的接口
 *
 * @author 李佳明
 * @date 2017.10.18
 */
public interface ProxyCreater {
    Object newProxyInstance() throws Exception;
    Class<?> getProxyClass();
}