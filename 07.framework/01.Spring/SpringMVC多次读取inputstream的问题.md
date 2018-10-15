# SpringMVC多次读取InputStream的问题

## 问题描述

在Filter中读取Request中的流后, 然后在Controller中@RequestBody的参数无法注入而导致 400 错误。

## 代码

### CachingRequestBodyFilter

```
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class CachingRequestBodyFilter extends GenericFilterBean {
  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
          throws IOException, ServletException {
    HttpServletRequest currentRequest = (HttpServletRequest) servletRequest;
    AuthenticationRequestWrapper wrappedRequest = new AuthenticationRequestWrapper(currentRequest);
    chain.doFilter(wrappedRequest, servletResponse);
  }
}
```

### AuthenticationRequestWrapper
```

import com.google.common.primitives.Bytes;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class AuthenticationRequestWrapper extends HttpServletRequestWrapper {

  private byte[] requestBody = new byte[0];
  private boolean bufferFilled = false;

  /**
   * - Constructs a request object wrapping the given request.
   * <p>
   * - @param request The request to wrap
   * - @throws IllegalArgumentException if the request is null
   */
  public AuthenticationRequestWrapper(HttpServletRequest request) {
    super(request);
  }

  public byte[] getRequestBody() throws IOException {
    if (bufferFilled) {
      return Arrays.copyOf(requestBody, requestBody.length);
    }
    InputStream inputStream = super.getInputStream();
    byte[] buffer = new byte[102400]; // 100kb buffer
    int bytesRead;
    while ((bytesRead = inputStream.read(buffer)) != -1) {
      requestBody = Bytes.concat(this.requestBody, Arrays.copyOfRange(buffer, 0, bytesRead));
    }
    bufferFilled = true;
    return requestBody;
  }

  @Override
  public ServletInputStream getInputStream() throws IOException {
    return new CustomServletInputStream(getRequestBody()); // <1>
  }

  private static class CustomServletInputStream extends ServletInputStream {

    private ByteArrayInputStream buffer;

    public CustomServletInputStream(byte[] contents) {
      this.buffer = new ByteArrayInputStream(contents);
    }

    @Override
    public int read() throws IOException {
      return buffer.read();
    }

    @Override
    public boolean isFinished() {
      return buffer.available() == 0;
    }

    @Override
    public boolean isReady() {
      return true;
    }

    @Override
    public void setReadListener(ReadListener listener) {
      throw new RuntimeException("Not implemented");
    }
  }
}
```

### 用法
```
AuthenticationRequestWrapper wrappedRequest = new AuthenticationRequestWrapper(request);
InputStream is = wrappedRequest.getInputStream();
String in = IOUtils.toString(is);
```

## 参考
- [解决在Filter中读取Request中的流后, 然后在Controller中@RequestBody的参数无法注入而导致 400 错误](https://blog.csdn.net/java_gchsh/article/details/79207460)
- [Java过滤器与SpringMVC拦截器之间的关系与区别](https://www.cnblogs.com/csniper/p/5570386.html)
- [springMVC拦截器从Request中获取Json格式并解决request的请求流只能读取一次的问题](https://blog.csdn.net/sdut406/article/details/81369983)
- [How can I read request body multiple times in Spring 'HandlerMethodArgumentResolver'?](https://stackoverflow.com/questions/34804205/how-can-i-read-request-body-multiple-times-in-spring-handlermethodargumentresol)
- [这个是解决思路](https://stackoverflow.com/questions/10210645/http-servlet-request-lose-params-from-post-body-after-read-it-once/17129256#17129256)
- [这个是3.0版本的解决方法](https://stackoverflow.com/questions/29208456/httpservletrequestwrapper-example-implementation-for-setreadlistener-isfinish?lq=1)
- [Java过滤器，SpringMVC拦截器之间的一顺序点关系](https://www.cnblogs.com/dreamroute/p/4198087.html)
- [SpringMVC的拦截器（Interceptor）和过滤器（Filter）的区别与联系](https://blog.csdn.net/xiaoyaotan_111/article/details/53817918)
