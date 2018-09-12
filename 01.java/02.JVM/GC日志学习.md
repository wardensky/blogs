# GC日志学习

每种收集器的日志形式都是由它们自身的实现所决定的，换言之，每种收集器的日志格式都可以不一样。


```
package com.zch.blogs.java.jvm;

import java.util.ArrayList;
import java.util.List;

public class HeapOverflowTest {
	public static void main(String[] args) {
		List<HeapOverflowTest> list = new ArrayList<HeapOverflowTest>();
		while (true) {
			list.add(new HeapOverflowTest());
		}
	}
}

```

JVM参数

```
-Xms40M -Xmx80M -XX:+PrintGCDetails -XX:+UseSerialGC
```

GC日志
```
[GC (Allocation Failure) [DefNew: 10944K->1344K(12288K), 0.0265965 secs] 10944K->7337K(39616K), 0.0266298 secs] [Times: user=0.01 sys=0.01, real=0.03 secs]
[GC (Allocation Failure) [DefNew: 12288K->1344K(12288K), 0.0342038 secs] 18281K->16170K(39616K), 0.0342457 secs] [Times: user=0.02 sys=0.00, real=0.04 secs]
[GC (Allocation Failure) [DefNew: 12288K->1344K(12288K), 0.0359997 secs] 27114K->27113K(39616K), 0.0360283 secs] [Times: user=0.03 sys=0.01, real=0.04 secs]
[GC (Allocation Failure) [DefNew: 12288K->1344K(12288K), 0.0273624 secs][Tenured: 36713K->30143K(36756K), 0.0715526 secs] 38057K->30143K(49044K), [Metaspace: 2635K->2635K(1056768K)], 0.0993197 secs] [Times: user=0.08 sys=0.00, real=0.10 secs]
[GC (Allocation Failure) [DefNew: 20160K->2496K(22656K), 0.0347189 secs] 50303K->50101K(72896K), 0.0347470 secs] [Times: user=0.03 sys=0.00, real=0.03 secs]
[GC (Allocation Failure) [DefNew: 14896K->14896K(22656K), 0.0000302 secs][Tenured: 47605K->50239K(50240K), 0.1275756 secs] 62501K->55124K(72896K), [Metaspace: 2636K->2636K(1056768K)], 0.1277177 secs] [Times: user=0.12 sys=0.00, real=0.13 secs]
[Full GC (Allocation Failure) [Tenured: 54655K->48857K(54656K), 0.1440266 secs] 79231K->68548K(79232K), [Metaspace: 2636K->2636K(1056768K)], 0.1440664 secs] [Times: user=0.14 sys=0.00, real=0.14 secs]
[Full GC (Allocation Failure) [Tenured: 54656K->53246K(54656K), 0.1736905 secs] 79232K->77822K(79232K), [Metaspace: 2636K->2636K(1056768K)], 0.1737239 secs] [Times: user=0.17 sys=0.00, real=0.17 secs]
[Full GC (Allocation Failure) [Tenured: 54655K->54655K(54656K), 0.1689977 secs] 79231K->79231K(79232K), [Metaspace: 2636K->2636K(1056768K)], 0.1690337 secs] [Times: user=0.15 sys=0.00, real=0.17 secs]
[Full GC (Allocation Failure) [Tenured: 54655K->54644K(54656K), 0.1751553 secs] 79231K->79220K(79232K), [Metaspace: 2636K->2636K(1056768K)], 0.1752517 secs] [Times: user=0.16 sys=0.00, real=0.17 secs]
[Full GC (Allocation Failure) [Tenured: 54656K->54656K(54656K), 0.1490259 secs] 79232K->79232K(79232K), [Metaspace: 2636K->2636K(1056768K)], 0.1491018 secs] [Times: user=0.15 sys=0.00, real=0.15 secs]
[Full GC (Allocation Failure) [Tenured: 54656K->54656K(54656K), 0.1853180 secs] 79232K->79232K(79232K), [Metaspace: 2636K->2636K(1056768K)], 0.1853478 secs] [Times: user=0.15 sys=0.01, real=0.19 secs]
[Full GC (Allocation Failure) [Tenured: 54656K->287K(54656K), 0.0269163 secs] 79232K->287K(79232K), [Metaspace: 2636K->2636K(1056768K)], 0.0269525 secs] [Times: user=0.02 sys=0.00, real=0.02 secs]
Exception in thread "main" Heap
 def new generation   total 24576K, used 794K [0x00000007bb000000, 0x00000007bcaa0000, 0x00000007bcaa0000)
  eden space 21888K,   3% used [0x00000007bb000000, 0x00000007bb0c69e0, 0x00000007bc560000)
  from space 2688K,   0% used [0x00000007bc560000, 0x00000007bc560000, 0x00000007bc800000)
  to   space 2688K,   0% used [0x00000007bc800000, 0x00000007bc800000, 0x00000007bcaa0000)
 tenured generation   total 54656K, used 287K [0x00000007bcaa0000, 0x00000007c0000000, 0x00000007c0000000)
   the space 54656K,   0% used [0x00000007bcaa0000, 0x00000007bcae7f58, 0x00000007bcae8000, 0x00000007c0000000)
 Metaspace       used 2667K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 288K, capacity 386K, committed 512K, reserved 1048576K
java.lang.OutOfMemoryError: Java heap space
	at com.zch.blogs.java.jvm.HeapOverflowTest.main(HeapOverflowTest.java:10)


```

## 参考

- [Java虚拟机5：Java垃圾回收（GC）机制详解](https://www.cnblogs.com/xrq730/p/4836700.html)
