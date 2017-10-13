# MyRestUtil
给部门写的基于springboot的rest调用框架。

# 说明

最近部门在搞服务器，系统和系统直接rest调用越来越多。为了简化调用和基于公司的灵活配置编写了本框架。本框架使用jdk的动态代理技术，给出了spring下最方便简洁的使用方法。所有代码都是自主研发，代码简洁明了，一共不到200行，可基于公司特色随意修改。

使用的时候，只需要定义一个接口类，即可直接注入使用。

框架只演示了get请求和支持http basic认证，需要支持post请求和其他认证方式的（如sso），自己寻找TODO标签补全代码即可。

# 更新记录
## 2017.10.11 将 `RestUtilInit` 由普通bean修改为 `BeanFactoryPostProcessor` ，保证IRequestHandle优先与其他任何bean注册到容器中

解决bean的依赖问题，不需要在接口类中增加 `@Lazy` 注解。 感谢 [李佳明](https://github.com/pkpk1234) 修改完善。

## 2017.10.13 增加功能：支持直接在类里面增加方法调用（使用cglib代理实现）

这个特性效果不错，也很符合实际使用场景。之前的场景是调用http接口的时候，需要先定义一个接口，然后注入调用，现在不需要了，直接在普通类中增加方法即可。

如下：业务代码中增加一个调用http接口的get2方法，然后直接调用即可。

```Java
@Rest(value = "http://localhost:8081/test")
public abstract class SomeService {

  /**
   * 在类里面增加一个抽象的http接口调用方法
   * @return
   */
  @GET("/get2")
  public abstract ResultBean get2(@Param("key") String key);

  /**
   * 业务代码类中直接调用http接口
   * 
   * @return
   */
  public String doSomething() {
    // 这里是一些业务代码，中间调用了其他系统的http接口。
    return "调用接口返回结果：" + get2("支持直接在类里面注入使用").getData();
  }
}
```



# 使用说明

## 指定RestTemplate

使用RestTemplate处理请求。需要配置RestTemplate。springboot下配置如下：

```Java
@Autowired(required = false)
List<ClientHttpRequestInterceptor> interceptors;

@Bean
public RestTemplate restTemplate() {
  System.out.println("-------restTemplate-------");

  RestTemplate restTemplate = new RestTemplate();

  // 设置拦截器，用于http basic的认证等
  restTemplate.setInterceptors(interceptors);

  return restTemplate;
}
```

如果需要登录，以http basic认证为例，增加以下配置bean即可

```Java
@Component
public class HttpBasicRequestInterceptor implements ClientHttpRequestInterceptor {

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
      throws IOException {

    // TODO 需要得到当前用户
    System.out.println("---------需要得到当前用户，然后设置账号密码-----------");

    String plainCreds = "xiaowenjie:admin";
    byte[] plainCredsBytes = plainCreds.getBytes();
    byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);

    String headerValue = new String(base64CredsBytes);

    HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);
    requestWrapper.getHeaders().add("Authorization", "Basic " + headerValue);

    return execution.execute(requestWrapper, body);
  }
}
```

## 编写接口类

接口上指定 `@Rest` 配置服务器信息，方法上指定 `@GET` 配置请求信息。

```Java
import cn.xiaowenjie.myrestutil.http.GET;
import cn.xiaowenjie.myrestutil.http.Param;
import cn.xiaowenjie.myrestutil.http.Rest;
import cn.xiaowenjie.retrofitdemo.beans.ResultBean;

@Rest("http://localhost:8081/test")
public interface IRequestDemo {

  @GET
  ResultBean get1();

  @GET("/get2")
  ResultBean getWithKey(@Param("key") String key);

  @GET("/get3")
  ResultBean getWithMultKey(@Param("key1") String key, @Param("key2") String key2);
}
```


## 使用

直接在spring框架上注入接口即可使用。

```Java
@Autowired
IRequestDemo demo;

public String test() {
  String msg = "<h1>invoke remote rest result</h1>";

  ResultBean get1 = demo.get1();

  msg += "<br/>get1 result=" + get1;

  ResultBean get2 = demo.getWithKey("key-------");

  msg += "<br/>get2 result=" + get2;

  ResultBean get3 = demo.getWithMultKey("key11111", "key22222");

  msg += "<br/>get3 result=" + get3;

  return msg;
}
```

# 使用本工程例子

导入2个springboot工程 myrestserver 和 myrestutil。myrestserver端口为8081，用于测试rest服务。分别启动2个应用，然后访问 http://127.0.0.1:8080/ 

![](/pictures/1.png) 

点击链接，会在8080的后台调用8081的rest请求，并把请求接口返回到页面。

![](/pictures/2.png) 

# 工作原理

框架代码在单独的 `MyRestUtil\myrestutil\restutil` 目录中，主要逻辑都在 `RestUtilInit` 上，代码非常精简，一看就明白。

* 使用 `org.reflections.Reflections` 得到所有配置了 `@Rest` 的接口列表
* 根据 `@Rest` 得到服务器配置信息 `RestInfo`
* 使用 `Proxy.newProxyInstance` 生成接口的代理类，`invoke` 方法中根据 `@GET` 得到该方法请求信息 `RequestInfo`，调用 `IRequestHandle` 接口处理请求，。
* 把生成的代理类注入到spring容器中。

# 如果不使用RestTemplate需要怎么修改？

实现 `IRequestHandle` , 注释掉默认的 `RestTemplateRequestHandle` 类即可。
