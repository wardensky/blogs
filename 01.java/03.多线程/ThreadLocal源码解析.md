# ThreadLocal源码解析

## 简介

ThreadLocal虽然解决了这个多线程变量的复杂问题，但是它的源码实现却是比较简单的。ThreadLocalMap是实现ThreadLocal的关键，我们先从它入手。

ThreadLocalMap

ThreadLocalMap其内部利用Entry来实现key-value的存储，如下：

```
       static class Entry extends WeakReference<ThreadLocal<?>> {
            /** The value associated with this ThreadLocal. */
            Object value;

            Entry(ThreadLocal<?> k, Object v) {
                super(k);
                value = v;
            }
        }
```

从上面代码中可以看出Entry的key就是ThreadLocal，而value就是值。同时，Entry也继承WeakReference，所以说Entry所对应key（ThreadLocal实例）的引用为一个弱引用（关于弱引用这里就不多说了，感兴趣的可以关注这篇博客：Java 理论与实践: 用弱引用堵住内存泄漏）

ThreadLocalMap的源码稍微多了点，我们就看两个最核心的方法getEntry()、set(ThreadLocal> key, Object value)方法。

**set(ThreadLocal> key, Object value)**

```
    private void set(ThreadLocal<?> key, Object value) {

        ThreadLocal.ThreadLocalMap.Entry[] tab = table;
        int len = tab.length;

        // 根据 ThreadLocal 的散列值，查找对应元素在数组中的位置
        int i = key.threadLocalHashCode & (len-1);

        // 采用“线性探测法”，寻找合适位置
        for (ThreadLocal.ThreadLocalMap.Entry e = tab[i];
            e != null;
            e = tab[i = nextIndex(i, len)]) {

            ThreadLocal<?> k = e.get();

            // key 存在，直接覆盖
            if (k == key) {
                e.value = value;
                return;
            }

            // key == null，但是存在值（因为此处的e != null），说明之前的ThreadLocal对象已经被回收了
            if (k == null) {
                // 用新元素替换陈旧的元素
                replaceStaleEntry(key, value, i);
                return;
            }
        }

        // ThreadLocal对应的key实例不存在也没有陈旧元素，new 一个
        tab[i] = new ThreadLocal.ThreadLocalMap.Entry(key, value);

        int sz = ++size;

        // cleanSomeSlots 清楚陈旧的Entry（key == null）
        // 如果没有清理陈旧的 Entry 并且数组中的元素大于了阈值，则进行 rehash
        if (!cleanSomeSlots(i, sz) && sz >= threshold)
            rehash();
    }
```

这个set()操作和我们在集合了解的put()方式有点儿不一样，虽然他们都是key-value结构，不同在于他们解决散列冲突的方式不同。集合Map的put()采用的是拉链法，而ThreadLocalMap的set()则是采用开放定址法（具体请参考散列冲突处理系列博客）。掌握了开放地址法该方法就一目了然了。

set()操作除了存储元素外，还有一个很重要的作用，就是replaceStaleEntry()和cleanSomeSlots()，这两个方法可以清除掉key == null 的实例，防止内存泄漏。在set()方法中还有一个变量很重要：threadLocalHashCode，定义如下：

private final int threadLocalHashCode = nextHashCode();
从名字上面我们可以看出threadLocalHashCode应该是ThreadLocal的散列值，定义为final，表示ThreadLocal一旦创建其散列值就已经确定了，生成过程则是调用nextHashCode()：

    private static AtomicInteger nextHashCode = new AtomicInteger();

    private static final int HASH_INCREMENT = 0x61c88647;

    private static int nextHashCode() {
        return nextHashCode.getAndAdd(HASH_INCREMENT);
    }
nextHashCode表示分配下一个ThreadLocal实例的threadLocalHashCode的值，HASH_INCREMENT则表示分配两个ThradLocal实例的threadLocalHashCode的增量，从nextHashCode就可以看出他们的定义。


```

getEntry()

        private Entry getEntry(ThreadLocal<?> key) {
            int i = key.threadLocalHashCode & (table.length - 1);
            Entry e = table[i];
            if (e != null && e.get() == key)
                return e;
            else
                return getEntryAfterMiss(key, i, e);
        }
```

由于采用了开放定址法，所以当前key的散列值和元素在数组的索引并不是完全对应的，首先取一个探测数（key的散列值），如果所对应的key就是我们所要找的元素，则返回，否则调用getEntryAfterMiss()，如下：

```
        private Entry getEntryAfterMiss(ThreadLocal<?> key, int i, Entry e) {
            Entry[] tab = table;
            int len = tab.length;

            while (e != null) {
                ThreadLocal<?> k = e.get();
                if (k == key)
                    return e;
                if (k == null)
                    expungeStaleEntry(i);
                else
                    i = nextIndex(i, len);
                e = tab[i];
            }
            return null;
        }

```        
这里有一个重要的地方，当key == null时，调用了expungeStaleEntry()方法，该方法用于处理key == null，有利于GC回收，能够有效地避免内存泄漏。

get()

返回当前线程所对应的线程变量
    public T get() {
        // 获取当前线程
        Thread t = Thread.currentThread();

        // 获取当前线程的成员变量 threadLocal
        ThreadLocalMap map = getMap(t);
        if (map != null) {
            // 从当前线程的ThreadLocalMap获取相对应的Entry
            ThreadLocalMap.Entry e = map.getEntry(this);
            if (e != null) {
                @SuppressWarnings("unchecked")

                // 获取目标值        
                T result = (T)e.value;
                return result;
            }
        }
        return setInitialValue();
    }
首先通过当前线程获取所对应的成员变量ThreadLocalMap，然后通过ThreadLocalMap获取当前ThreadLocal的Entry，最后通过所获取的Entry获取目标值result。

getMap()方法可以获取当前线程所对应的ThreadLocalMap，如下：

    ThreadLocalMap getMap(Thread t) {
        return t.threadLocals;
    }
set(T value)

设置当前线程的线程局部变量的值。
    public void set(T value) {
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null)
            map.set(this, value);
        else
            createMap(t, value);
    }
获取当前线程所对应的ThreadLocalMap，如果不为空，则调用ThreadLocalMap的set()方法，key就是当前ThreadLocal，如果不存在，则调用createMap()方法新建一个，如下：

    void createMap(Thread t, T firstValue) {
        t.threadLocals = new ThreadLocalMap(this, firstValue);
    }
initialValue()

返回该线程局部变量的初始值。
    protected T initialValue() {
        return null;
    }
该方法定义为protected级别且返回为null，很明显是要子类实现它的，所以我们在使用ThreadLocal的时候一般都应该覆盖该方法。该方法不能显示调用，只有在第一次调用get()或者set()方法时才会被执行，并且仅执行1次。

remove()

将当前线程局部变量的值删除。
    public void remove() {
        ThreadLocalMap m = getMap(Thread.currentThread());
        if (m != null)
            m.remove(this);
    }
该方法的目的是减少内存的占用。当然，我们不需要显示调用该方法，因为一个线程结束后，它所对应的局部变量就会被垃圾回收。

ThreadLocal为什么会内存泄漏

前面提到每个Thread都有一个ThreadLocal.ThreadLocalMap的map，该map的key为ThreadLocal实例，它为一个弱引用，我们知道弱引用有利于GC回收。当ThreadLocal的key == null时，GC就会回收这部分空间，但是value却不一定能够被回收，因为他还与Current Thread存在一个强引用关系，如下（图片来自http://www.jianshu.com/p/ee8c9dccc953）：



由于存在这个强引用关系，会导致value无法回收。如果这个线程对象不会销毁那么这个强引用关系则会一直存在，就会出现内存泄漏情况。所以说只要这个线程对象能够及时被GC回收，就不会出现内存泄漏。如果碰到线程池，那就更坑了。

那么要怎么避免这个问题呢？

在前面提过，在ThreadLocalMap中的setEntry()、getEntry()，如果遇到key == null的情况，会对value设置为null。当然我们也可以显示调用ThreadLocal的remove()方法进行处理。

下面再对ThreadLocal进行简单的总结：

ThreadLocal 不是用于解决共享变量的问题的，也不是为了协调线程同步而存在，而是为了方便每个线程处理自己的状态而引入的一个机制。这点至关重要。
每个Thread内部都有一个ThreadLocal.ThreadLocalMap类型的成员变量，该成员变量用来存储实际的ThreadLocal变量副本。
ThreadLocal并不是为线程保存对象的副本，它仅仅只起到一个索引的作用。它的主要木得视为每一个线程隔离一个类的实例，这个实例的作用范围仅限于线程内部。


## 参考

- [【死磕Java并发】—–深入分析ThreadLocal](http://cmsblogs.com/?p=2442)
- [Java多线程10：ThreadLocal的作用及使用](https://www.cnblogs.com/xrq730/p/4854820.html)
