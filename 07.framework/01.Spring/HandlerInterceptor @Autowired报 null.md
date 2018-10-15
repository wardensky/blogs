# HandlerInterceptor @Autowired报 null


在写spring boot里面拦截器HandlerInterceptor 的时候
里面肯定要查数据库，或者缓存之类的，肯定要```@Autowired```


然而一直报null，不管是dao还是service都一样报null。需要换个写法。


```
public class MyInterceptor implements HandlerInterceptor {

  @Bean
  MyConfiguration myConfiguration() {
    return new MyConfiguration();
  }

  public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
    myConfiguration().say();

    return true;
  }

}

```
