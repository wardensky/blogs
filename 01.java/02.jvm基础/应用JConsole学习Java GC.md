# 应用JConsole学习Java GC


关于Java GC的知识，好多地方都讲了很多，今天我用JConsole来学习一下Java GC的原理。

## GC原理
在我的[上一篇](http://www.cnblogs.com/wardensky/p/4162121.html)中介绍了Java运行时数据区，在了解这些的基础上，对Java GC的理解能更清晰一些。

简单来讲，Java的内存分为堆和栈，其中堆是程序员用的内存，栈是系统用的内存。（*这句话不一定正确，但可以这么理解*）Java的内存管理主要是管理对象的分配和释放，或者说内存的分配和回收。在C或C++语言里面，内存是要自己控制的，new之后要delete掉，否则很容易出现内存泄漏。（*还记得当时写C的痛苦，不过通过写C代码，很好的了解了内存的分配机制*）在Java里面，分配内存和回收内存的事情是Jvm来管的。

Jvm有自己的机制来管理内存，具体的细节算法这里不讲，主要讲大概的处理方式。Jvm的GC主要处理堆内存，JVM内存模型中的堆可以细分为Young Generation和Old Generation（又称为Tenure Space），其中Young Generation又分为Eden Space（*Eden是伊甸园的意思，老鹰乐队有首歌叫Long Road out of Eden*）和Survivor spaces。如下图所示：
![](http://images.cnitblog.com/blog/239608/201412/152353513443951.jpg)


具体GC的过程如下：
> 1. 在Young Generation中，有一个叫Eden Space的空间，主要是用来存放新生的对象，还有两个Survivor Spaces（from、to），它们的大小总是一样，它们用来存放每次垃圾回收后存活下来的对象。
> 1. 在Old Generation中，主要存放应用程序中生命周期长的内存对象。
> 1. 在Young Generation块中，垃圾回收一般用Copying的算法，速度快。每次GC的时候，存活下来的对象首先由Eden拷贝到某个SurvivorSpace，当Survivor Space空间满了后，剩下的live对象就被直接拷贝到OldGeneration中去。因此，每次GC后，Eden内存块会被清空。
> 1. 在Old Generation块中，垃圾回收一般用mark-compact的算法，速度慢些，但减少内存要求。
> 1. 垃圾回收分多级，0级为全部(Full)的垃圾回收，会回收OLD段中的垃圾；1级或以上为部分垃圾回收，只会回收Young中的垃圾，内存溢出通常发生于OLD段或Perm段垃圾回收后，仍然无内存空间容纳新的Java对象的情况。

翻译成为更简单的语言：

> 1. 内存首先在Eden中分配；
> 1. Eden中满了之后，挪到Survivor Space，清空Eden；
> 1. Survivor Space满了之后，挪到Old Generation；
4. 如果这三个区域都满了，内存溢出OutOfMemory。

那上面这个过程如何得知呢？今天我就用JConsole给大家演示一下如何看GC的过程。

## JConsole介绍

先介绍一下JConsole。

### JConsole是什么
JConsole 是一个内置 Java 性能分析器，可以从命令行或在 GUI shell 中运行。您可以轻松地使用 JConsole（或者，它更高端的 “近亲” VisualVM ）来监控 Java 应用程序性能和跟踪 Java 中的代码。

### 如何启动JConsole
JConsole是一个程序，在windows里面找到JConsole.exe，双击启动即可。启动之后的界面如下图所示：

![](http://images.cnitblog.com/blog/239608/201412/160000082814373.png)

可以看到，JConsole即可以连接本地进程，也可以连接远程进程。

## 应用JConsole学习GC

那么，如何用JConsole学习GC的过程呢？我们首先要设计一个程序，这个程序一直保持内存增长，直到发生内存泄漏。在这个过程中，我们应该JConsole观察GC的过程。

### 写一个内存泄漏的程序
写一个内存泄漏的程序比较简单，读一个很大的文件到内存中，直至内存溢出，代码如下：

```

	package com.chzhao.test;

	import java.io.BufferedReader;
	import java.io.FileReader;
	import java.io.IOException;
	import java.util.ArrayList;
	import java.util.List;

	public class StringTest {
		public static void main(String[] args) throws InterruptedException,
				IOException {
			List<String> list = new ArrayList<String>();
			String file = "d:/wisdombud-unicom.log.2014-12-05";
			BufferedReader in = null;
			in = new BufferedReader(new FileReader(file));
			while (true) {
				String lineMsg = in.readLine();
				if (lineMsg == null || lineMsg.equals("")) {
					break;
				}
				list.add(lineMsg);
				Thread.sleep(10);
				System.out.println(list.size());
			}
			in.close();
		}
	}

```
这个文件是我程序的一个日志文件，足够大，300M+

### 设置启动参数
除了写程序之外，为了快点出现内存泄漏，我们把启动内存调小。在Eclispse里面调VM arguments就可以，内容为：

```
-Xms4M -Xmx4M
```

我设置了4M。
### 通过JConsole观察

首先把程序运行起来，在通过JConsole连接到程序上。
因为我们只关心内存，切换到内存标签页。有一个下拉图表，可以观察不同的内存情况。右下角有个柱状图，显示的是堆内存和栈内存的占用情况。其中堆内存包括Eden Space、Survivor Space和Tenured Gen。如下图所示：
![](http://images.cnitblog.com/blog/239608/201412/160022339841560.png)

可以看到，随着程序的运行，Eden Space会逐渐变满，到100%之后，Eden Space会变成0%，Survivor Space会变大；Survivor Space变100%之后，会挪到Tenured Gen中，Survivor Space变0%。这个过程和上面讲到的GC过程是一样的，很直观。

也可以观察上面的曲线图，Eden Space的图是类似波形图，每次到波谷都是进行了一次GC。Tenured Gen则是类似梯田，一直向上涨，直到内存溢出。如下两张图所示。

![Eden Space](http://images.cnitblog.com/blog/239608/201412/161051390311218.png)

*Eden Space的波形图*

![Tenured Gen](http://images.cnitblog.com/blog/239608/201412/161051460628666.png)

*Tenured Gen的波形图*

在VM摘要标签页，能看到更多JVM的信息，可以看到分配的内存已经等于堆的最大值了，所以内存溢出。（*为什么堆的最大值是5942Kb我没搞明白，不应该小于4M吗？如果哪位懂请指点一下。*）
![](http://images.cnitblog.com/blog/239608/201412/161051521879970.png)


## 结论
通过JConsole工具学习GC的过程，生动形象，很容易明白GC是如何进行的。然而这些知识都属于GC的基础知识，如果要了解更多GC的知识，还要多学习。

参考：

- 深入理解Java虚拟机
- [Java 内存模型及GC原理](http://blog.csdn.net/ithomer/article/details/6252552)
- [如何利用 JConsole观察分析Java程序的运行，进行排错调优](http://jiajun.iteye.com/blog/810150)
- [JVM系列三:JVM参数设置、分析](http://www.cnblogs.com/redcreen/archive/2011/05/04/2037057.html)
