# synchronized与ReentrantLock比较


网上随便一搜，到处都是synchronized与ReentrantLock比较。


- ReentrantLock是公平锁，可以设置公平性。
- ReentrantLock要手动设置加锁和解锁。
- ReenTrantLock提供了一个Condition（条件）类，用来实现分组唤醒需要唤醒的线程们，而不是像synchronized要么随机唤醒一个线程要么唤醒全部线程。
- ReenTrantLock提供了一种能够中断等待锁的线程的机制，通过lock.lockInterruptibly()来实现这个机制。
- Synchronized是依赖于JVM实现的，而ReenTrantLock是JDK实现的。



## 参考

- [ReenTrantLock可重入锁（和synchronized的区别）总结](https://www.cnblogs.com/baizhanshi/p/7211802.html)
