package com.zch.blogs.java.jvm;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class MetaspaceOOMTest {
	/**
     * JVM参数:-XX:MetaspaceSize=8m -XX:MaxMetaspaceSize=16m -XX:+PrintFlagsInitial
     */
    public static void main(String[] args) {
        int i = 0;
        
        try {
            for (;;) {
                i++;
                
                Enhancer enhancer = new Enhancer();
                enhancer.setSuperclass(OOMObject.class);
                enhancer.setUseCache(false);
                enhancer.setCallback(new MethodInterceptor() {
                    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                        return proxy.invokeSuper(obj, args);
                    }
                });
                enhancer.create();
                System.out.println("i = " + i);
            }
        } catch (Exception e) {
            System.out.println("第" + i + "次时发生异常");
            e.printStackTrace();
        }
    }
    
    static class OOMObject {
        
    }
}
