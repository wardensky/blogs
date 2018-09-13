# JVM内存分配和回收策略

## TLAB

首先讲讲什么是TLAB。内存分配的动作，可以按照线程划分在不同的空间之中进行，即每个线程在Java堆中预先分配一小块内存，称为本地线程分配缓冲（Thread Local Allocation Buffer，TLAB）。哪个线程需要分配内存，就在哪个线程的TLAB上分配。虚拟机是否使用TLAB，可以通过-XX:+/-UseTLAB参数来设定。这么做的目的之一，也是为了并发创建一个对象时，保证创建对象的线程安全性。TLAB比较小，直接在TLAB上分配内存的方式称为快速分配方式，而TLAB大小不够，导致内存被分配在Eden区的内存分配方式称为慢速分配方式。

## 对象优先在Eden分配


大多数情况下，对象在新生代Eden区中分配。当Eden区没有足够空间进行分配时，虚拟机将发起一次MinorGC。虚拟机提供了```-XX:+PrintGCDetails```这个收集器日志参数，告诉虚拟机在发生垃圾收集行为时打印内存回收日志，并且在进程退出的时候输出当前的内存各区域分配情况。在实际应用中，内存回收日志一般是打印到文件后通过日志工具进行分析，不过本实验的日志并不多，直接阅读就能看得很清楚。

如下代码的testAllocation()方法中，尝试分配3个2MB大小和1个4MB大小的对象，在运行时通过```-Xms20M、-Xmx20M、-Xmn10M```这3个参数限制了Java堆大小为20MB，不可扩展，其中10MB分配给新生代，剩下的10MB分配给老年代。```-XX:SurvivorRatio=8```决定了新生代中Eden区与一个Survivor区的空间比例是8:1，从输出的结果也可以清晰地看到```edenspace8192K、fromspace1024K、tospace1024K```的信息，新生代总可用空间为9216KB（Eden区+1个Survivor区的总容量）。

执行testAllocation()中分配allocation4对象的语句时会发生一次MinorGC，这次GC的结果是新生代6651KB变为148KB，而总内存占用量则几乎没有减少（因为allocation1、allocation2、allocation3三个对象都是存活的，虚拟机几乎没有找到可回收的对象）。这次GC发生的原因是给allocation4分配内存的时候，发现Eden已经被占用了6MB，剩余空间已不足以分配allocation4所需的4MB内存，因此发生MinorGC。GC期间虚拟机又发现已有的3个2MB大小的对象全部无法放入Survivor空间（Survivor空间只有1MB大小），所以只好通过分配担保机制提前转移到老年代去。这次GC结束后，4MB的allocation4对象顺利分配在Eden中，因此程序执行完的结果是Eden占用4MB（被allocation4占用），Survivor空闲，老年代被占用6MB（被allocation1、allocation2、allocation3占用）。通过GC日志可以证实这一点。

MinorGC和FullGC有什么不一样吗？

- 新生代GC（MinorGC）：指发生在新生代的垃圾收集动作，因为Java对象大多都具备朝生夕灭的特性，所以MinorGC非常频繁，一般回收速度也比较快。
- 老年代GC（MajorGC/FullGC）：指发生在老年代的GC，出现了MajorGC，经常会伴随至少一次的MinorGC（但非绝对的，在ParallelScavenge收集器的收集策略里就有直接进行MajorGC的策略选择过程）。MajorGC的速度一般会比MinorGC慢10倍以上。



```
package com.zch.blogs.java.jvm;

public class MinorGcTest {
	private static final int _1MB = 1024 * 1024;

	// VM 参数：-verbose:gc-Xms20M-Xmx20M-Xmn10M-XX:+PrintGCDetails -XX: SurvivorRatio=
	// 8
	public static void testAllocation() {
		byte[] allocation1, allocation2, allocation3, allocation4;

		allocation1 = new byte[2 * _1MB];
		allocation2 = new byte[2 * _1MB];
		allocation3 = new byte[2 * _1MB];
		allocation4 = new byte[4 * _1MB];
	}

	public static void main(String[] args) {
		testAllocation();
	}
}

```


输出：

```
Heap
 PSYoungGen      total 9216K, used 7307K [0x00000007bf600000, 0x00000007c0000000, 0x00000007c0000000)
  eden space 8192K, 89% used [0x00000007bf600000,0x00000007bfd22f18,0x00000007bfe00000)
  from space 1024K, 0% used [0x00000007bff00000,0x00000007bff00000,0x00000007c0000000)
  to   space 1024K, 0% used [0x00000007bfe00000,0x00000007bfe00000,0x00000007bff00000)
 ParOldGen       total 10240K, used 4096K [0x00000007bec00000, 0x00000007bf600000, 0x00000007bf600000)
  object space 10240K, 40% used [0x00000007bec00000,0x00000007bf000010,0x00000007bf600000)
 Metaspace       used 2637K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 285K, capacity 386K, committed 512K, reserved 1048576K
```

另外，[文章](https://www.cnblogs.com/xrq730/p/4841177.html)中提到了
>在Client模式下，最后分配的4M在新生代中，先分配的6M在老年代中；在Server模式下，最后分配的4M在老年代中，先分配的6M在新生代中。说明不同的垃圾收集器组合对于对象的分配是有影响的。讲下两者差别的原因：
>1、Client模式下，新生代分配了6M，虚拟机在GC前有6487K，比6M也就是6144K多，多主要是因为TLAB和EdenAllocationTest这个对象占的空间，TLAB可以通过“-XX:+PrintTLAB”这个虚拟机参数来查看大小。OK，6M多了，然后来了一个4M的，Eden+一个Survivor总共就9M不够分配了，这时候就会触发一次Minor GC。但是触发Minor GC也没用，因为allocation1、allocation2、allocation3三个引用还存在，另一块1M的Survivor也不够放下这6M，那么这次Minor GC的效果其实是通过分配担保机制将这6M的内容转入老年代中。然后再来一个4M的，由于此时Minor GC之后新生代只剩下了194K了，够分配了，所以4M顺利进入新生代。
>2、Server模式下，前面都一样，但是在GC的时候有一点区别。在GC前还会进行一次判断，如果要分配的内存>=Eden区大小的一半，那么会直接把要分配的内存放入老年代中。要分配4M，Eden区8M，刚好一半，而且老年代10M，够分配，所以4M就直接进入老年代去了。为了验证一下结论，我们把3个2M之后分配的4M改为3M看一下。

我没有自己写代码测试。

## 大对象直接进入老年代

所谓的大对象是指，需要大量连续内存空间的Java对象，最典型的大对象就是那种很长的字符串以及数组（笔者列出的例子中的byte[]数组就是典型的大对象）。大对象对虚拟机的内存分配来说就是一个坏消息（替Java虚拟机抱怨一句，比遇到一个大对象更加坏的消息就是遇到一群“朝生夕灭”的“短命大对象”，写程序的时候应当避免），经常出现大对象容易导致内存还有不少空间时就提前触发垃圾收集以获取足够的连续空间来“安置”它们。

虚拟机提供了一个-XX:PretenureSizeThreshold参数，令大于这个设置值的对象直接在老年代分配。这样做的目的是避免在Eden区及两个Survivor区之间发生大量的内存复制（复习一下：新生代采用复制算法收集内存）。

执行下列代码中的testPretenureSizeThreshold()方法后，我们看到Eden空间几乎没有被使用，而老年代的10MB空间被使用了40%，也就是4MB的allocation对象直接就分配在老年代中，这是因为PretenureSizeThreshold被设置为3MB（就是3145728，这个参数不能像-Xmx之类的参数一样直接写3MB），因此超过3MB的对象都会直接在老年代进行分配。注意PretenureSizeThreshold参数只对Serial和ParNew两款收集器有效，ParallelScavenge收集器不认识这个参数，ParallelScavenge收集器一般并不需要设置。如果遇到必须使用此参数的场合，可以考虑ParNew加CMS的收集器组合。


**试验代码**

```
public static void testPretenureSizeThreshold() {
  byte[] allocation;
  allocation = new byte[4 * _1MB];
}
```
**JVM参数**

```
-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:PretenureSizeThreshold=3145728  -XX:+UseSerialGC


```

因为我是Java8，所以要指定**-XX:+UseSerialGC**

**输出**

```

Heap
 def new generation   total 9216K, used 1164K [0x00000007bec00000, 0x00000007bf600000, 0x00000007bf600000)
  eden space 8192K,  14% used [0x00000007bec00000, 0x00000007bed23018, 0x00000007bf400000)
  from space 1024K,   0% used [0x00000007bf400000, 0x00000007bf400000, 0x00000007bf500000)
  to   space 1024K,   0% used [0x00000007bf500000, 0x00000007bf500000, 0x00000007bf600000)
 tenured generation   total 10240K, used 4096K [0x00000007bf600000, 0x00000007c0000000, 0x00000007c0000000)
   the space 10240K,  40% used [0x00000007bf600000, 0x00000007bfa00010, 0x00000007bfa00200, 0x00000007c0000000)
 Metaspace       used 2637K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 285K, capacity 386K, committed 512K, reserved 1048576K


```

## 长期存活的对象将进入老年代

## 动态对象年龄判定

## 空间分配保障


## 参考

- 周志明. 深入理解Java虚拟机：JVM高级特性与最佳实践（第2版）
- [Java虚拟机7：内存分配原则](https://www.cnblogs.com/xrq730/p/4841177.html)
