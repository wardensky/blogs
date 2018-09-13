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


**试验源代码**

```
public static void testPretenureSizeThreshold() {
  byte[] allocation;
  allocation = new byte[4 * _1MB];
}
```
**JVM参数**

```
-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
-XX:PretenureSizeThreshold=3145728  -XX:+UseSerialGC


```

因为我是Java8，所以要指定 **-XX:+UseSerialGC**

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

既然虚拟机采用了分代收集的思想来管理内存，那么内存回收时就必须能识别哪些对象应放在新生代，哪些对象应放在老年代中。为了做到这点，虚拟机给每个对象定义了一个对象年龄（Age）计数器。如果对象在Eden出生并经过第一次MinorGC后仍然存活，并且能被Survivor容纳的话，将被移动到Survivor空间中，并且对象年龄设为1。对象在Survivor区中每“熬过”一次MinorGC，年龄就增加1岁，当它的年龄增加到一定程度（默认为15岁），就将会被晋升到老年代中。

对象晋升老年代的年龄阈值，可以通过参数```-XX:MaxTenuringThreshold```设置。可以试试分别以```-XX:MaxTenuringThreshold=1```和```-XX:MaxTenuringThreshold=15```两种设置来执行下列代码中的testTenuringThreshold()方法，此方法中的allocation1对象需要256KB内存，Survivor空间可以容纳。当MaxTenuringThreshold=1时，allocation1对象在第二次GC发生时进入老年代，新生代已使用的内存GC后非常干净地变成0KB。而MaxTenuringThreshold=15时，第二次GC发生后，allocation1对象则还留在新生代Survivor空间，这时新生代仍然有404KB被占用。



**试验源代码**

```
/**
 * 长期存活的对象将进入老年代
 */
@SuppressWarnings("unused")
public static void testTenuringThreshold() {
  byte[] allocation1, allocation2, allocation3;
  allocation1 = new byte[_1MB / 4];
  allocation2 = new byte[_1MB * 4];
  allocation3 = new byte[_1MB * 4];
  allocation3 = null;
  allocation3 = new byte[_1MB * 4];
}

```



**JVM参数**

```
-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
-XX:MaxTenuringThreshold=1 -XX:+PrintTenuringDistribution  -XX:+UseSerialGC
```

**输出**

```
[GC (Allocation Failure) [DefNew
Desired survivor size 524288 bytes, new threshold 1 (max 1)
- age   1:     575952 bytes,     575952 total
: 5351K->562K(9216K), 0.0050145 secs] 5351K->4658K(19456K), 0.0080827 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
[GC (Allocation Failure) [DefNew
Desired survivor size 524288 bytes, new threshold 1 (max 1)
: 4658K->0K(9216K), 0.0009326 secs] 8754K->4655K(19456K), 0.0009554 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
Heap
 def new generation   total 9216K, used 4178K [0x00000007bec00000, 0x00000007bf600000, 0x00000007bf600000)
  eden space 8192K,  51% used [0x00000007bec00000, 0x00000007bf014930, 0x00000007bf400000)
  from space 1024K,   0% used [0x00000007bf400000, 0x00000007bf400000, 0x00000007bf500000)
  to   space 1024K,   0% used [0x00000007bf500000, 0x00000007bf500000, 0x00000007bf600000)
 tenured generation   total 10240K, used 4655K [0x00000007bf600000, 0x00000007c0000000, 0x00000007c0000000)
   the space 10240K,  45% used [0x00000007bf600000, 0x00000007bfa8bf28, 0x00000007bfa8c000, 0x00000007c0000000)
 Metaspace       used 2639K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 285K, capacity 386K, committed 512K, reserved 1048576K


```


**JVM参数**

```
-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
-XX:MaxTenuringThreshold=15 -XX:+PrintTenuringDistribution  -XX:+UseSerialGC
```

**输出**


```
[GC (Allocation Failure) [DefNew
Desired survivor size 524288 bytes, new threshold 1 (max 15)
- age   1:     575952 bytes,     575952 total
: 5351K->562K(9216K), 0.0047180 secs] 5351K->4658K(19456K), 0.0047570 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[GC (Allocation Failure) [DefNew
Desired survivor size 524288 bytes, new threshold 15 (max 15)
: 4658K->0K(9216K), 0.0010252 secs] 8754K->4655K(19456K), 0.0010550 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
Heap
 def new generation   total 9216K, used 4178K [0x00000007bec00000, 0x00000007bf600000, 0x00000007bf600000)
  eden space 8192K,  51% used [0x00000007bec00000, 0x00000007bf014930, 0x00000007bf400000)
  from space 1024K,   0% used [0x00000007bf400000, 0x00000007bf400000, 0x00000007bf500000)
  to   space 1024K,   0% used [0x00000007bf500000, 0x00000007bf500000, 0x00000007bf600000)
 tenured generation   total 10240K, used 4655K [0x00000007bf600000, 0x00000007c0000000, 0x00000007c0000000)
   the space 10240K,  45% used [0x00000007bf600000, 0x00000007bfa8bf28, 0x00000007bfa8c000, 0x00000007c0000000)
 Metaspace       used 2638K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 285K, capacity 386K, committed 512K, reserved 1048576K

```



## 动态对象年龄判定

为了能更好地适应不同程序的内存状况，虚拟机并不是永远地要求对象的年龄必须达到了MaxTenuringThreshold才能晋升老年代，如果在Survivor空间中相同年龄所有对象大小的总和大于Survivor空间的一半，年龄大于或等于该年龄的对象就可以直接进入老年代，无须等到MaxTenuringThreshold中要求的年龄。

执行下列代码的testTenuringThreshold2()方法，并设置-XX:MaxTenuringThreshold=15，会发现运行结果中Survivor的空间占用仍然为0%，而老年代比预期增加了6%，也就是说，allocation1、allocation2对象都直接进入了老年代，而没有等到15岁的临界年龄。因为这两个对象加起来已经到达了512KB，并且它们是同年的，满足同年对象达到Survivor空间的一半规则。我们只要注释掉其中一个对象new操作，就会发现另外一个就不会晋升到老年代中去了。

**试验源代码**

```
@SuppressWarnings("unused")
public static void testTenuringThreshold2() {
  byte[] allocation1, allocation2, allocation3, allocation4;
  allocation1 = new byte[_1MB / 4];
  // allocation1+ allocation2 大于 survivo 空间 一半 allocation2= new byte[ _1MB/ 4];
  allocation3 = new byte[4 * _1MB];
  allocation4 = new byte[4 * _1MB];
  allocation4 = null;
  allocation4 = new byte[4 * _1MB];
}
```
**JVM参数**

```
-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=15
-XX:+PrintTenuringDistribution  -XX:+UseSerialGC
```

因为我是Java8，所以要指定**-XX:+UseSerialGC**

**输出**

```
[GC (Allocation Failure) [DefNew
Desired survivor size 524288 bytes, new threshold 1 (max 15)
- age   1:     576152 bytes,     576152 total
: 5351K->562K(9216K), 0.0041785 secs] 5351K->4658K(19456K), 0.0042160 secs] [Times: user=0.01 sys=0.00, real=0.01 secs]
[GC (Allocation Failure) [DefNew
Desired survivor size 524288 bytes, new threshold 15 (max 15)
: 4658K->0K(9216K), 0.0015029 secs] 8754K->4655K(19456K), 0.0015398 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
Heap
 def new generation   total 9216K, used 4178K [0x00000007bec00000, 0x00000007bf600000, 0x00000007bf600000)
  eden space 8192K,  51% used [0x00000007bec00000, 0x00000007bf014930, 0x00000007bf400000)
  from space 1024K,   0% used [0x00000007bf400000, 0x00000007bf400000, 0x00000007bf500000)
  to   space 1024K,   0% used [0x00000007bf500000, 0x00000007bf500000, 0x00000007bf600000)
 tenured generation   total 10240K, used 4655K [0x00000007bf600000, 0x00000007c0000000, 0x00000007c0000000)
   the space 10240K,  45% used [0x00000007bf600000, 0x00000007bfa8bff0, 0x00000007bfa8c000, 0x00000007c0000000)
 Metaspace       used 2638K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 285K, capacity 386K, committed 512K, reserved 1048576K


```



## 空间分配保障

在发生MinorGC之前，虚拟机会先检查老年代最大可用的连续空间是否大于新生代所有对象总空间，如果这个条件成立，那么MinorGC可以确保是安全的。如果不成立，则虚拟机会查看HandlePromotionFailure设置值是否允许担保失败。如果允许，那么会继续检查老年代最大可用的连续空间是否大于历次晋升到老年代对象的平均大小，如果大于，将尝试着进行一次MinorGC，尽管这次MinorGC是有风险的；如果小于，或者HandlePromotionFailure设置不允许冒险，那这时也要改为进行一次FullGC。

下面解释一下“冒险”是冒了什么风险，前面提到过，新生代使用复制收集算法，但为了内存利用率，只使用其中一个Survivor空间来作为轮换备份，因此当出现大量对象在MinorGC后仍然存活的情况（最极端的情况就是内存回收后新生代中所有对象都存活），就需要老年代进行分配担保，把Survivor无法容纳的对象直接进入老年代。与生活中的贷款担保类似，老年代要进行这样的担保，前提是老年代本身还有容纳这些对象的剩余空间，一共有多少对象会活下来在实际完成内存回收之前是无法明确知道的，所以只好取之前每一次回收晋升到老年代对象容量的平均大小值作为经验值，与老年代的剩余空间进行比较，决定是否进行FullGC来让老年代腾出更多空间。

取平均值进行比较其实仍然是一种动态概率的手段，也就是说，如果某次MinorGC存活后的对象突增，远远高于平均值的话，依然会导致担保失败（HandlePromotionFailure）。如果出现了HandlePromotionFailure失败，那就只好在失败后重新发起一次FullGC。虽然担保失败时绕的圈子是最大的，但大部分情况下都还是会将HandlePromotionFailure开关打开，避免FullGC过于频繁，这个参数在JDK6Update24之前的版本中有效。

## 参考

- 周志明. 深入理解Java虚拟机：JVM高级特性与最佳实践（第2版）
- [Java虚拟机7：内存分配原则](https://www.cnblogs.com/xrq730/p/4841177.html)
