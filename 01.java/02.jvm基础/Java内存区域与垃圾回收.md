# Java内存区域与垃圾回收


##  Java内存区域

### 运行时数据区

![运行时数据区](../images/JVM运行时数据区.png)

### 程序计数器
　程序计数器是一块较小的内存空间，它的作用可以看作是当前线程所执行的字节码的行号指示器。在虚拟机的概念模型里，字节码解释器工作时就是通过改变这个计数器的值来选取下一条需要执行的字节码指令，分支、循环、跳转、异常处理、线程恢复等基础功能都需要依赖这个计数器完成。每个线程都有自己的计数器。

　　如果线程正在执行的是一个Java方法，这个计数器记录的是正在执行的虚拟机字节码指令的地址；如果正在执行的是Native方法，这个计数器的值为空（Undefined）。

　　此内存区域是唯一一个Java虚拟机规范中没有规定任何OutOfMemoryError情况的区域。
### Java虚拟机栈
　Java虚拟机栈描述的是Java方法执行的内存模型：每个方法被执行的时候都会同时创建一个栈帧（Stack Frame）用于存储局部变量表、操作数栈、动态链接、方法出口等信息。每一个方法被调用直至执行完成的过程，就对应着一个栈帧在虚拟机栈中从入栈到出栈的过程。

　　经常有人把Java内存分为堆内存和栈内存，这种分法比较粗糙，Java内存区域的划分实际上远比这复杂。其中所说的栈就是虚拟机栈，或者说是虚拟机栈中的局部变量部分。

　　局部变量表存放了编译期可知的各种基本数据类型（boolean、byte、char、short、int、float、long、double）、对象引用和returnAddress类型（指向了一条字节码指令的地址）。

　　其中64位长度的long和double类型的数据会占用2个局部变量空间（Slot），其余的数据类型只占用1个。局部变量所属的内存空间在编译期间完成分配，当进入一个方法时，这个方法需要的帧中分配多大的局部变量空间是完全确定的，在方法运行期间不会改变局部变量表的大小。

　　Java虚拟机规范中对这个区域规定了两个异常状况：如果线程请求的栈深度大于虚拟机说允许的深度，抛出StackOverflowError异常；如果虚拟机栈可以动态扩展，当扩展时无法申请到足够内存时抛出OutOfMemoryError异常。
　　与程序计数器一样，Java虚拟机栈也是线程私有的，它的生命周期与线程相同。虚拟机栈描述的是Java方法执行的内存模型：每个方法被执行的时候都会同时创建一个栈帧（Stack Frame）用于存储局部变量表、操作栈、动态链接、方法出口等信息。每一个方法被调用直至执行完成的过程，就对应着一个栈帧在虚拟机栈中从入栈到出栈的过程。
　　经常有人把Java内存区分为堆内存（Heap）和栈内存（Stack），这种分法比较粗糙，Java内存区域的划分实际上远比这复杂。所指的“栈”就是现在讲的虚拟机栈，或者说是虚拟机栈中的局部变量表部分。局部变量表存放了编译期可知的各种基本数据类型（boolean、byte、char、short、int、float、long、double）、对象引用（reference类型），它不等同于对象本身，根据不同的虚拟机实现，它可能是一个指向对象起始地址的引用指针，也可能指向一个代表对象的句柄或者其他与此对象相关的位置）和returnAddress类型（指向了一条字节码指令的地址）。
### 本地方法栈
本地方法栈与虚拟机栈所发挥的作用是非常相似的，其区别不过是虚拟机栈为虚拟机执行Java方法（字节码）服务，而本地方法则是虚拟机使用到的Native方法服务。虚拟机规范中对本地方法栈中的方法使用的语言、使用方式和数据结构并没有强制规定，因此具体的虚拟机可以自由实现它。甚至与虚拟机栈合二为一。本地方法栈区域也会抛出StackOverflowError和OutOfmemoryError异常。
本地方法栈（Native Method Stacks）与虚拟机栈所发挥的作用是非常相似的，其区别不过是虚拟机栈为虚拟机执行Java方法服务，而本地方法栈则是为虚拟机使用到的Native方法服务。虚拟机规范中对本地方法栈中的方法使用的语言、使用方式与数据结构并没有强制规定，因此具体的虚拟机可以自由实现它。甚至有的虚拟机（譬如Sun HotSpot虚拟机）直接就把本地方法栈和虚拟机栈合二为一。与虚拟机栈一样，本地方法栈区域也会抛出StackOverflowError和OutOfMemoryError异常。在Java虚拟机规范中，对这个区域规定了两种异常状况：如果线程请求的栈深度大于虚拟机所允许的深度，将抛出StackOverflowError异常；如果虚拟机栈可以动态扩展（当前大部分的Java虚拟机都可动态扩展，只不过Java虚拟机规范中也允许固定长度的虚拟机栈），当扩展时无法申请到足够的内存时会抛出OutOfMemoryError异常。

### Java堆

   Java堆是被所有线程共享的一块内存区域，在虚拟机启动时创建。此内存区域的唯一目的就是存放对象实例，几乎所有的对象实例都在这里分配内存。

　　Java堆是垃圾收集器管理的主要区域，因此很多时候也被称为“GC堆”。如果从内存回收的角度看，由于现在收集器基本上都是采用的分代收集算法，所以Java堆还可以细分为：新生代和老年代；再细致一点的有Eden空间、From Survivor空间、To Survivor空间等。如果从内存分配角度看，线程共享的Java堆中可能划分出多个线程私有的分配缓冲区。进一步划分的目的是为了更好的回收内存，存储的都仍然是对象实例。
　　对于大多数应用来说，Java堆（Java Heap）是Java虚拟机所管理的内存中最大的一块。Java堆是被所有线程共享的一块内存区域，在虚拟机启动时创建。此内存区域的唯一目的就是存放对象实例，几乎所有的对象实例都在这里分配内存。根据Java虚拟机规范的规定，Java堆可以处于物理上不连续的内存空间中，只要逻辑上是连续的即可，就像我们的磁盘空间一样。在实现时，既可以实现成固定大小的，也可以是可扩展的，不过当前主流的虚拟机都是按照可扩展来实现的（通过-Xmx和-Xms控制）。如果在堆中没有内存完成实例分配，并且堆也无法再扩展时，将会抛出OutOfMemoryError异常。

### 方法区
　　方法区用于存放已被虚拟机加载的类信息、常量、静态变量、即时编译器编译后的代码等数据。虽然Java虚拟机规范把方法区描述为堆的一个逻辑部分，但是它却有一个别名叫做Non-Heap。
　　Java虚拟机规范对方法区的限制非常宽松，除了和Java堆一样不需要连续的内存和可以选择固定大小或者可扩展外，还可以选择不实现垃圾收集。这个区域的内存回收目标主要是针对常量池的回收和对类型的卸载，一般来说这个区域的回收效果比较难以令人满意。当方法区无法满足内存分配需求时，抛出OutOfMemoryError异常。
　　方法区（Method Area）与Java堆一样，是各个线程共享的内存区域，它用于存储已被虚拟机加载的类信息、常量、静态变量、即时编译器编译后的代码等数据。虽然Java虚拟机规范把方法区描述为堆的一个逻辑部分，但是它却有一个别名叫做Non-Heap（非堆），目的应该是与Java堆区分开来。
　　对于习惯在HotSpot虚拟机上开发和部署程序的开发者来说，很多人愿意把方法区称为“永久代”（Permanent Generation），本质上两者并不等价，仅仅是因为HotSpot虚拟机的设计团队选择把GC分代收集扩展至方法区，或者说使用永久代来实现方法区而已。对于其他虚拟机（如BEA JRockit、IBM J9等）来说是不存在永久代的概念的。
    从JDK7开始永久代的移除工作，贮存在永久代的一部分数据已经转移到了堆或者是Native Memory。但永久代仍然存在于JDK7，并没有完全的移除：符号引用(Symbols)转移到了Native Memory;字面量(interned strings)转移到了堆;类的静态变量(class statics)转移到了堆。
　　这次JDK8修改了JVM，去掉了PermGen内存，所以永久代的参数-XX:PermSize和-XX:MaxPermSize也被移除了，转而出现了一个Metaspace，其实这两个的作用都类似，都是用来装载一些类信息。但是PermGen是在JVM内存中的，而新的Metaspace是直接在navite memory中的，因此由于Permanent Generation默认很小而导致内存溢出的情况理论上是不会再出现了，因为Metaspace默认大小只要不超过机器内存，那么就是无限制的。不过也可以通过参数-XX:MaxMetaspaceSize=2M来限制它的大小，超过这个大小一样会报内存溢出。

### 运行时常量池
　　运行时常量池（Runtime Constant Pool）是方法区的一部分，Class文件中除了有类的版本、字段、方法、接口等描述信息外，还有一项信息是常量池，用于存放编译期生成的各种字面量和符号引用，这部分内容将在类加载后存放到方法区的运行时常量池中。

　　运行时常量池相对于Class文件常量池的另一个重要特征是具备动态性，Java语言并不要求常量一定只能在编译期产生，也就是并非预置入Class文件中常量池的内存才能进入方法区运行时常量池，运行期间也可能将新的常量放入池中，这种特性被被使用的较多的是String类的intern()法。

　　当常量池无法再申请到内存的时候会抛出OutOfMemoryError异常。
运行时常量池（Runtime Constant Pool）是方法区的一部分。Class文件中除了有类的版本、字段、方法、接口等描述等信息外，还有一项信息是常量池（Constant Pool Table），用于存放编译期生成的各种字面量和符号引用，这部分内容将在类加载后存放到方法区的运行时常量池中。运行时常量池相对于Class文件常量池的另外一个重要特征是具备动态性，Java语言并不要求常量一定只能在编译期产生，也就是并非预置入Class文件中常量池的内容才能进入方法区运行时常量池，运行期间也可能将新的常量放入池中，这种特性被开发人员利用得比较多的便是String类的intern()方法。 既然运行时常量池是方法区的一部分，自然会受到方法区内存的限制，当常量池无法再申请到内存时会抛出OutOfMemoryError异常。
    字面量在java6、java7/8中各有不同，如果想监控内存，可以使用jvisualvm这个工具。
    在java7以前，字面量被放在永久代（PermGen）中，由于PermGen默认很小，因此如果在运行期间使用intern方法的话，很容易PermGen内存溢出，所以java7以前不推荐使用这个方法。可以使用XX:MaxPermSize=128m这个参数来增加永久代的大小。
    从java7开始把常量池的字面量移动到堆中，意味着你不再被限制在固定的内存中啦，它为Java8 彻底移除PermGen作准备。所有的字符对象将和其他普通对象一样位于堆中.你可以通过-XX:StringTableSize=1009这个参数来调整字符串字面量大小，由于移到了Heap 中，如果String pool 中的一个String，没有被任何实例所引用，是会被GC回收的。
    除了字符串字面量，实际上还有整型字面量、浮点型字面量等等，但都大同小异，只不过数值类型的字面量不可以手动添加常量，程序启动时字面量中的字面量就已经确定了，比如整型字面量中的字面量范围：-128~127，只有这个范围的数字可以用到字面量。

## Java内存分配与内存回收
这里所说的内存分配，主要指的是在堆上的分配。Java内存分配和回收的机制概括的说，就是：分代分配，分代回收。对象将根据存活的时间被分为：年轻代（Young Generation）、年老代（Old Generation）、永久代（Permanent Generation，也就是方法区）。如下图：
![](http://www.importnew.com/wp-content/uploads/2012/12/Figure-1-GC-Area-Data-Flow.png)
　　年轻代（Young Generation）：对象被创建时，内存的分配首先发生在年轻代（大对象可以直接被创建在年老代），大部分的对象在创建后很快就不再使用，因此很快变得不可达，于是被年轻代的GC机制清理掉（IBM的研究表明，98%的对象都是很快消亡的），这个GC机制被称为Minor GC或叫Young GC。注意，Minor GC并不代表年轻代内存不足，它事实上只表示在Eden区上的GC。

　　年轻代上的内存分配是这样的，年轻代可以分为3个区域：Eden区和两个存活区（Survivor 0 、Survivor 1）。内存分配过程为：
　　
![](http://www.importnew.com/wp-content/uploads/2012/12/Figure-3-Before-After-a-GC.png)
　　　　
绝大多数刚创建的对象会被分配在Eden区，其中的大多数对象很快就会消亡。Eden区是连续的内存空间，因此在其上分配内存极快；
当Eden区满的时候，执行Minor GC，将消亡的对象清理掉，并将剩余的对象复制到一个存活区Survivor0（此时，Survivor1是空白的，两个Survivor总有一个是空白的）；
此后，每次Eden区满了，就执行一次Minor GC，并将剩余的对象都添加到Survivor0；
当Survivor0也满的时候，将其中仍然活着的对象直接复制到Survivor1，以后Eden区执行Minor GC后，就将剩余的对象添加Survivor1（此时，Survivor0是空白的）。
当两个存活区切换了几次（HotSpot虚拟机默认15次，用-XX:MaxTenuringThreshold控制，大于该值进入老年代，但这只是个最大值，并不代表一定是这个值）之后，仍然存活的对象（其实只有一小部分，比如，我们自己定义的对象），将被复制到老年代。
　　从上面的过程可以看出，Eden区是连续的空间，且Survivor总有一个为空。经过一次GC和复制，一个Survivor中保存着当前还活着的对象，而Eden区和另一个Survivor区的内容都不再需要了，可以直接清空，到下一次GC时，两个Survivor的角色再互换。因此，这种方式分配内存和清理内存的效率都极高，这种垃圾回收的方式就是著名的“停止-复制（Stop-and-copy）”清理法（将Eden区和一个Survivor中仍然存活的对象拷贝到另一个Survivor中），这不代表着停止复制清理法很高效，其实，它也只在这种情况下高效，如果在老年代采用停止复制，则挺悲剧的。

　　在Eden区，HotSpot虚拟机使用了两种技术来加快内存分配。分别是bump-the-pointer和TLAB（Thread-Local Allocation Buffers），这两种技术的做法分别是：由于Eden区是连续的，因此bump-the-pointer技术的核心就是跟踪最后创建的一个对象，在对象创建时，只需要检查最后一个对象后面是否有足够的内存即可，从而大大加快内存分配速度；而对于TLAB技术是对于多线程而言的，将Eden区分为若干段，每个线程使用独立的一段，避免相互影响。TLAB结合bump-the-pointer技术，将保证每个线程都使用Eden区的一段，并快速的分配内存。

　　年老代（Old Generation）：对象如果在年轻代存活了足够长的时间而没有被清理掉（即在几次Young GC后存活了下来），则会被复制到年老代，年老代的空间一般比年轻代大，能存放更多的对象，在年老代上发生的GC次数也比年轻代少。当年老代内存不足时，将执行Major GC，也叫 Full GC。　　

![java堆内存](http://images.cnitblog.com/blog/239608/201412/152353513443951.jpg)

## 应用jconsole学习垃圾回收
那么，如何用JConsole学习GC的过程呢？我们首先要设计一个程序，这个程序一直保持内存增长，直到发生内存泄漏。在这个过程中，我们应该JConsole观察GC的过程。
### 写一个内存泄漏的程序
写一个内存泄漏的程序比较简单，读一个很大的文件到内存中，直至内存溢出，代码如下：
```
package com.wisdombud.chzhao.demo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JConsoleDemo {

	public static void main(String[] args) throws InterruptedException, IOException {
		List<String> list = new ArrayList<String>();
		String file = "/Users/chunhuizhao/Software/cn_windows_7_ultimate_x64_dvd_x15-66043.iso";
		BufferedReader in = null;
		in = new BufferedReader(new FileReader(file));
		while (true) {
			char[] cbuf = new char[1024];
			int ret = in.read(cbuf);
			if (ret <= 0) {
				break;
			}
			list.add(cbuf.toString());
			Thread.sleep(10);
			System.out.println(ret + "\t" + list.size());
		}
		in.close();
	}

}
```
这个文件足够大，3G+

### 设置启动参数
除了写程序之外，为了快点出现内存泄漏，我们把启动内存调小。在Eclispse里面调VM arguments就可以，内容为：

```
-Xms4M -Xmx4M
```
我设置了4M。

>-Xms               Specifies  the  initial  size  of the memory allocation pool.
                           This value must be a multiple of  1024  greater  than  1  MB.
                           Append  the letter k or K to indicate kilobytes, the letter m
                           or M to indicate megabytes, the letter g  or  G  to  indicate
                           gigabytes,  or  the letter t or T to indicate terabytes.  The
                           default value is 2MB. Examples:
			             -Xms6291456
                           -Xms6144k
                           -Xms6m

>-Xmx               Specifies the maximum size, in bytes, of the  memory  alloca-
                           tion  pool.   This  value  must be a multiple of 1024 greater
                           than 2 MB.  Append the letter k or K to  indicate  kilobytes,
                           the letter m or M to indicate megabytes, the letter g or G to
                           indicate gigabytes, or the letter t or  T  to  indicate  ter-
                           abytes.  The default value is 64MB. Examples:
						   -Xmx83886080
                           -Xmx81920k
                           -Xmx80m

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


## 参考资料
[Java内存区域](http://www.cnblogs.com/warden/archive/2012/11/18/2775874.html)
[JAVA内存区域](http://blog.csdn.net/gjb724332682/article/details/47311289)
[成为JavaGC专家（1）—深入浅出Java垃圾回收机制](http://www.importnew.com/1993.html)
