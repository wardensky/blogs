# BlockingQueue学习


### BlockingQueue

![](../images/blockqueue.png)

如果BlockQueue是空的,从BlockingQueue取东西的操作将会被阻断进入等待状态,直到BlockingQueue进了东西才会被唤醒.同样,如果BlockingQueue是满的,任何试图往里存东西的操作也会被阻断进入等待状态,直到BlockingQueue里有空间才会被唤醒继续操作.


### BlockingQueue接口中的方法

|-	|Throws Exception|	Special Value|	Blocks|	Times Out|
|-|-|-|-|-|
|Insert|	add(o)|	offer(o)|	put(o)|	offer(o, timeout, timeunit)|
|Remove	|remove(o)|	poll()|	take()|	poll(timeout, timeunit)|
|Examine|	element()|	peek()		|


1. ThrowsException：如果操作不能马上进行，则抛出异常
2. SpecialValue：如果操作不能马上进行，将会返回一个特殊的值，一般是true或者false
3. Blocks:如果操作不能马上进行，操作会被阻塞
4. TimesOut:如果操作不能马上进行，操作会被阻塞指定的时间，如果指定时间没执行，则返回一个特殊值，一般是true或者false

**不能向BlockingQueue中插入null，否则会报NullPointerException。**


#### add(anObject)

把anObject加到BlockingQueue里,即如果BlockingQueue可以容纳,则返回true,否则抛出异常。

#### offer(anObject)

表示如果可能的话,将anObject加到BlockingQueue里,即如果BlockingQueue可以容纳,则返回true,否则返回false.

#### put(anObject)

把anObject加到BlockingQueue里,如果BlockQueue没有空间,则调用此方法的线程被阻断直到BlockingQueue里面有空间再继续.

#### poll(time)

取走BlockingQueue里排在首位的对象,若不能立即取出,则可以等time参数规定的时间,取不到时返回null

#### take()

取走BlockingQueue里排在首位的对象,若BlockingQueue为空,阻断进入等待状态直到Blocking有新的对象被加入为止




## ArrayBlockingQueue

ArrayBlockingQueue是一个有边界的阻塞队列，它的内部实现是一个数组。有边界的意思是它的容量是有限的，我们必须在其初始化的时候指定它的容量大小，容量大小一旦指定就不可改变。

ArrayBlockingQueue是以先进先出的方式存储数据，最新插入的对象是尾部，最新移出的对象是头部。下面是一个初始化和使用ArrayBlockingQueue的例子：

```
BlockingQueue queue = new ArrayBlockingQueue(1024);
queue.put("1");
Object object = queue.take();
```

## LinkedBlockingQueue

LinkedBlockingQueue阻塞队列大小的配置是可选的，如果我们初始化时指定一个大小，它就是有边界的，如果不指定，它就是无边界的。说是无边界，其实是采用了默认大小为Integer.MAX_VALUE的容量 。它的内部实现是一个链表。

和ArrayBlockingQueue一样，LinkedBlockingQueue 也是以先进先出的方式存储数据，最新插入的对象是尾部，最新移出的对象是头部。下面是一个初始化和使LinkedBlockingQueue的例子：

```
BlockingQueue<String> unbounded = new LinkedBlockingQueue<String>();
BlockingQueue<String> bounded   = new LinkedBlockingQueue<String>(1024);
bounded.put("Value");
String value = bounded.take();
```


## PriorityBlockingQueue

PriorityBlockingQueue是一个没有边界的队列，它的排序规则和 java.util.PriorityQueue一样。需要注意，PriorityBlockingQueue中允许插入null对象。

所有插入PriorityBlockingQueue的对象必须实现 java.lang.Comparable接口，队列优先级的排序规则就是按照我们对这个接口的实现来定义的。

另外，我们可以从PriorityBlockingQueue获得一个迭代器Iterator，但这个迭代器并不保证按照优先级顺序进行迭代。

下面我们举个例子来说明一下，首先我们定义一个对象类型，这个对象需要实现Comparable接口：


## DelayQueue
一个使用优先级队列实现的无界阻塞队列。

## SynchronousQueue
一个不存储元素的阻塞队列。

SynchronousQueue队列内部仅允许容纳一个元素。当一个线程插入一个元素后会被阻塞，除非这个元素被另一个线程消费。

SynchronousQueue是一个神奇的队列，他是一个不存储元素的阻塞队列，也就是说他的每一个put操作都需要等待一个take操作，否则就不能继续添加元素了，有点儿像Exchanger，类似于生产者和消费者进行交换。

队列本身不存储任何元素，所以非常适用于传递性场景，两者直接进行对接。其吞吐量会高于ArrayBlockingQueue和LinkedBlockingQueue。

SynchronousQueue支持公平和非公平的访问策略，在默认情况下采用非公平性，也可以通过构造函数来设置为公平性。

SynchronousQueue的实现核心为Transferer接口，该接口有TransferQueue和TransferStack两个实现类，分别对应着公平策略和非公平策略。接口Transferer有一个tranfer()方法，该方法定义了转移数据，如果e != null，相当于将一个数据交给消费者，如果e == null，则相当于从一个生产者接收一个消费者交出的数据。

## LinkedTransferQueue
一个由链表结构组成的无界阻塞队列。

## LinkedBlockingDeque
一个由链表结构组成的双向阻塞队列。

## 参考

- [【Java并发之】BlockingQueue](https://blog.csdn.net/suifeng3051/article/details/48807423)
- [Java多线程-工具篇-BlockingQueue](http://www.cnblogs.com/jackyuj/archive/2010/11/24/1886553.html)
- [【死磕Java并发】—–J.U.C之阻塞队列：BlockingQueue总结](http://cmsblogs.com/?p=2440)
- [源代码在这里](https://github.com/wardensky/blogs/tree/master/00.source-code/blogs/java/src/main/java/com/zch/blogs/java/multithreads/queue)
- [线程----BlockingQueue (转)](http://www.cnblogs.com/likwo/archive/2010/07/01/1769199.html)
