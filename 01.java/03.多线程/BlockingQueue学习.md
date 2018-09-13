# BlockingQueue学习


### BlockingQueue

如果BlockQueue是空的,从BlockingQueue取东西的操作将会被阻断进入等待状态,直到BlockingQueue进了东西才会被唤醒.同样,如果BlockingQueue是满的,任何试图往里存东西的操作也会被阻断进入等待状态,直到BlockingQueue里有空间才会被唤醒继续操作.


## ArrayBlockingQueue
一个由数组结构组成的有界阻塞队列。

## LinkedBlockingQueue
一个由链表结构组成的无界阻塞队列。

## PriorityBlockingQueue
一个支持优先级排序的无界阻塞队列。

## DelayQueue
一个使用优先级队列实现的无界阻塞队列。

## SynchronousQueue
一个不存储元素的阻塞队列。

## LinkedTransferQueue
一个由链表结构组成的无界阻塞队列。

## LinkedBlockingDeque
一个由链表结构组成的双向阻塞队列。

## 参考

- [Java多线程-工具篇-BlockingQueue](http://www.cnblogs.com/jackyuj/archive/2010/11/24/1886553.html)
- [【死磕Java并发】—–J.U.C之阻塞队列：BlockingQueue总结](http://cmsblogs.com/?p=2440)
- [源代码在这里](https://github.com/wardensky/blogs/tree/master/00.source-code/blogs/java/src/main/java/com/zch/blogs/java/multithreads/queue)
- [线程----BlockingQueue (转)](http://www.cnblogs.com/likwo/archive/2010/07/01/1769199.html)
