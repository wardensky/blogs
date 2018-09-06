# 从Java源码分析HashMap


## 前言
Java里面的复杂数据结构，其底层都是数据啊，链表什么的。只不过人家（Doug Lea）写的好而已。所以，在看HashMap的源码之前，要在自己脑子里面想一想，如果是自己，应该怎么实现HashMap？

当前，在看HashMap之前，应该对HashMap的使用比较熟悉。

这里用的是Java8的版本，在数据量比较大的情况下，Java8用了红黑树来表示节点，提升效率。




## 重要的基础知识

HashMap不是简单的数据类型，里面应用了一些稍微高端的知识。

### 哈希或者散列(hash)

hash在中文中通常翻译为哈希或者散列，hash的准确定义如下：

```
A hash function is any function that can be used to map data of arbitrary size to data of fixed size. The values returned by a hash function are called hash values, hash codes, hash sums, or simply hashes. One use is a data structure called a hash table, widely used in computer software for rapid data lookup.
```

简单翻译如下：

```
哈希函数的指任何一个能把任意长度的数据变成固定长度数据的函数。一个哈希函数的返回值乘坐哈希值、哈希码、哈希和或者就是哈希。哈希的一个应用就是哈希表，在计算机软件中广泛用于快速查询。
```

### 哈希表

数组的特点是：寻址容易，插入和删除困难；而链表的特点是：寻址困难，插入和删除容易。那么我们能不能综合两者的特性，做出一种寻址容易，插入删除也容易的数据结构？答案是肯定的，这就是我们要提起的哈希表，哈希表有多种不同的实现方法，最常用的一种方法——拉链法，可以理解为“链表的数组”，如图：

 ![Alt text](./ds_hashtable1.png)

 左边很明显是个数组，数组的每个成员包括一个指针，指向一个链表的头，当然这个链表可能为空，也可能元素很多。我们根据元素的一些特征把元素分配到不同的链表中去，也是根据这些特征，找到正确的链表，再从链表中找出这个元素。

上面这个图就是HashMap的基本结构。

## HashMap里面的数据结构

HashMap或者Map里面定义了一些接口或者内部类，如下：

### Map.Entry
这是一个接口，定义在Map接口之下

```
interface Entry<K,V> {       
        K getKey();

        V getValue();

        V setValue(V value);

        boolean equals(Object o);

        int hashCode();
    }
```

### HashMap.Node
这个是上面接口的实现，实际上是一个带key和valued链表节点
```
    /**
     * Basic hash bin node, used for most entries.  (See below for
     * TreeNode subclass, and in LinkedHashMap for its Entry subclass.)
     */
    static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;
        final K key;
        V value;
        Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey()        { return key; }
        public final V getValue()      { return value; }
        public final String toString() { return key + "=" + value; }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public final boolean equals(Object o) {
            if (o == this)
                return true;
            if (o instanceof Map.Entry) {
                Map.Entry<?,?> e = (Map.Entry<?,?>)o;
                if (Objects.equals(key, e.getKey()) &&
                    Objects.equals(value, e.getValue()))
                    return true;
            }
            return false;
        }
    }
```

### HashMap.TreeNode

TreeNode实现了红黑树，代码特别多，这是其中一部分。
```

    /**
     * Entry for Tree bins. Extends LinkedHashMap.Entry (which in turn
     * extends Node) so can be used as extension of either regular or
     * linked node.
     */
    static final class TreeNode<K,V> extends LinkedHashMap.Entry<K,V> {
        TreeNode<K,V> parent;  // red-black tree links
        TreeNode<K,V> left;
        TreeNode<K,V> right;
        TreeNode<K,V> prev;    // needed to unlink next upon deletion
        boolean red;
        TreeNode(int hash, K key, V val, Node<K,V> next) {
            super(hash, key, val, next);
        }

        /**
         * Returns root of tree containing this node.
         */
        final TreeNode<K,V> root() {
            for (TreeNode<K,V> r = this, p;;) {
                if ((p = r.parent) == null)
                    return r;
                r = p;
            }
        }

后面还有很多……
```

### HashMap.KeySet

保存的是Map的key set集合，因为是key，不能重复，所以用Set。

```

    final class KeySet extends AbstractSet<K> {
        public final int size()                 { return size; }
        public final void clear()               { HashMap.this.clear(); }
        public final Iterator<K> iterator()     { return new KeyIterator(); }
        public final boolean contains(Object o) { return containsKey(o); }
        public final boolean remove(Object key) {
            return removeNode(hash(key), key, null, false, true) != null;
        }
        public final Spliterator<K> spliterator() {
            return new KeySpliterator<>(HashMap.this, 0, -1, 0, 0);
        }
        public final void forEach(Consumer<? super K> action) {
            Node<K,V>[] tab;
            if (action == null)
                throw new NullPointerException();
            if (size > 0 && (tab = table) != null) {
                int mc = modCount;
                for (int i = 0; i < tab.length; ++i) {
                    for (Node<K,V> e = tab[i]; e != null; e = e.next)
                        action.accept(e.key);
                }
                if (modCount != mc)
                    throw new ConcurrentModificationException();
            }
        }
    }

```


### HashMap.Values

保存的是Map的值集合，因为值可以重复，所以是collection。

```
    final class Values extends AbstractCollection<V> {
        public final int size()                 { return size; }
        public final void clear()               { HashMap.this.clear(); }
        public final Iterator<V> iterator()     { return new ValueIterator(); }
        public final boolean contains(Object o) { return containsValue(o); }
        public final Spliterator<V> spliterator() {
            return new ValueSpliterator<>(HashMap.this, 0, -1, 0, 0);
        }
        public final void forEach(Consumer<? super V> action) {
            Node<K,V>[] tab;
            if (action == null)
                throw new NullPointerException();
            if (size > 0 && (tab = table) != null) {
                int mc = modCount;
                for (int i = 0; i < tab.length; ++i) {
                    for (Node<K,V> e = tab[i]; e != null; e = e.next)
                        action.accept(e.value);
                }
                if (modCount != mc)
                    throw new ConcurrentModificationException();
            }
        }
    }

```

### HashMap.EntrySet

这是上面数据结构的一个Set集合。
```
    final class EntrySet extends AbstractSet<Map.Entry<K,V>> {
        public final int size()                 { return size; }
        public final void clear()               { HashMap.this.clear(); }
        public final Iterator<Map.Entry<K,V>> iterator() {
            return new EntryIterator();
        }
        public final boolean contains(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry<?,?> e = (Map.Entry<?,?>) o;
            Object key = e.getKey();
            Node<K,V> candidate = getNode(hash(key), key);
            return candidate != null && candidate.equals(e);
        }
        public final boolean remove(Object o) {
            if (o instanceof Map.Entry) {
                Map.Entry<?,?> e = (Map.Entry<?,?>) o;
                Object key = e.getKey();
                Object value = e.getValue();
                return removeNode(hash(key), key, value, true, true) != null;
            }
            return false;
        }
        public final Spliterator<Map.Entry<K,V>> spliterator() {
            return new EntrySpliterator<>(HashMap.this, 0, -1, 0, 0);
        }
        public final void forEach(Consumer<? super Map.Entry<K,V>> action) {
            Node<K,V>[] tab;
            if (action == null)
                throw new NullPointerException();
            if (size > 0 && (tab = table) != null) {
                int mc = modCount;
                for (int i = 0; i < tab.length; ++i) {
                    for (Node<K,V> e = tab[i]; e != null; e = e.next)
                        action.accept(e);
                }
                if (modCount != mc)
                    throw new ConcurrentModificationException();
            }
        }
    }
```

### 抽象类HashIterator

主要实现了Hash遍历的方法
```

    abstract class HashIterator {
        Node<K,V> next;        // next entry to return
        Node<K,V> current;     // current entry
        int expectedModCount;  // for fast-fail
        int index;             // current slot

        HashIterator() {
            expectedModCount = modCount;
            Node<K,V>[] t = table;
            current = next = null;
            index = 0;
            if (t != null && size > 0) { // advance to first entry
                do {} while (index < t.length && (next = t[index++]) == null);
            }
        }

        public final boolean hasNext() {
            return next != null;
        }

        final Node<K,V> nextNode() {
            Node<K,V>[] t;
            Node<K,V> e = next;
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            if (e == null)
                throw new NoSuchElementException();
            if ((next = (current = e).next) == null && (t = table) != null) {
                do {} while (index < t.length && (next = t[index++]) == null);
            }
            return e;
        }

        public final void remove() {
            Node<K,V> p = current;
            if (p == null)
                throw new IllegalStateException();
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            current = null;
            K key = p.key;
            removeNode(hash(key), key, null, false, false);
            expectedModCount = modCount;
        }
    }

```
它有三个实现类，分别对应key，value和NextNode
```

    final class KeyIterator extends HashIterator
        implements Iterator<K> {
        public final K next() { return nextNode().key; }
    }

    final class ValueIterator extends HashIterator
        implements Iterator<V> {
        public final V next() { return nextNode().value; }
    }

    final class EntryIterator extends HashIterator
        implements Iterator<Map.Entry<K,V>> {
        public final Map.Entry<K,V> next() { return nextNode(); }
    }

```

上面不是HashMap里面定义的全部的数据结构，但包含其主要的数据结构，HashMap就是构建在这些数据结构上的。

## 重要变量

###  DEFAULT_INITIAL_CAPACITY
```
    /**
     * The default initial capacity - MUST be a power of two.
     */
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16
```
这个里面应用了位移操作。注意，注释里面说了一句，容量必须是2的指数。
当数组长度为2的n次幂的时候，不同的key算得得index相同的几率较小，那么数据在数组上分布就比较均匀，也就是说碰撞的几率小，相对的，查询的时候就不用遍历某个位置上的链表，这样查询效率也就较高了。


###  MAXIMUM_CAPACITY

定义了HashMap里面做多的数量
```
    /**
     * The maximum capacity, used if a higher value is implicitly specified
     * by either of the constructors with arguments.
     * MUST be a power of two <= 1<<30.
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;
```

### DEFAULT_LOAD_FACTOR

这个负载因子在扩容的时候会用到，这个值是可以指定的。

```
    /**
     * The load factor used when none specified in constructor.
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
```

### TREEIFY_THRESHOLD

判断是否把链表变成红黑树的变量，数组某一个位置上，如果链表长度超过这个值，则变成红黑树。

```
   /**
     * The bin count threshold for using a tree rather than list for a
     * bin.  Bins are converted to trees when adding an element to a
     * bin with at least this many nodes. The value must be greater
     * than 2 and should be at least 8 to mesh with assumptions in
     * tree removal about conversion back to plain bins upon
     * shrinkage.
     */
    static final int TREEIFY_THRESHOLD = 8;
```

### UNTREEIFY_THRESHOLD

```
    /**
     * The bin count threshold for untreeifying a (split) bin during a
     * resize operation. Should be less than TREEIFY_THRESHOLD, and at
     * most 6 to mesh with shrinkage detection under removal.
     */
    static final int UNTREEIFY_THRESHOLD = 6;
```

### MIN_TREEIFY_CAPACITY

判断是否把链表变成红黑树的变量，这里指的是数组的长度。

```
    /**
     * The smallest table capacity for which bins may be treeified.
     * (Otherwise the table is resized if too many nodes in a bin.)
     * Should be at least 4 * TREEIFY_THRESHOLD to avoid conflicts
     * between resizing and treeification thresholds.
     */
    static final int MIN_TREEIFY_CAPACITY = 64;
```
## 一些重要的静态方法(Static utilities)
在正式看代码之前，看一下HashMap里面的一些“小方法”，说这些是小方法，因为这些方法看起来都很简单，但这些方法却很重要。

### hash(Object key)

先看代码：
```
    /**
     * Computes key.hashCode() and spreads (XORs) higher bits of hash
     * to lower.  Because the table uses power-of-two masking, sets of
     * hashes that vary only in bits above the current mask will
     * always collide. (Among known examples are sets of Float keys
     * holding consecutive whole numbers in small tables.)  So we
     * apply a transform that spreads the impact of higher bits
     * downward. There is a tradeoff between speed, utility, and
     * quality of bit-spreading. Because many common sets of hashes
     * are already reasonably distributed (so don't benefit from
     * spreading), and because we use trees to handle large sets of
     * collisions in bins, we just XOR some shifted bits in the
     * cheapest possible way to reduce systematic lossage, as well as
     * to incorporate impact of the highest bits that would otherwise
     * never be used in index calculations because of table bounds.
     */
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
```
一大段英文注释，典型的注释比代码长，我尝试翻译一下：
> 计算key的hashcode，然后高位和低位异或一下。因为哈希表应用2的幂次作为掩码，所以经常会发生哈希碰撞。（已知的例子是浮点类型作为key，按照连续的顺序放到一个小的table数字里面。）所以我们应用一种转换，把高位转换到低位。这是速度、功能和比特转换质量的一种权衡方案。因为很多hash都是适度分散的（因此扩散的收益不大），又因为我们应用树来处理比较大的冲突，所以我们应用异或和位移，以尽可能低成本的方法去减少系统级别的损失，同时也把原本不在数据范围内的高位包含进来了。

看代码可以知道，这段代码是把hashcode向右位移16位，然后与hashcode做与操作。下面这张图能说明这个操作：
![Alt text](./20160408155045341.png)

 就是上面这个图的操作。那为什么要这么做呢？

前面说过，HashMap的最下层是一个数组，所有的数据都要对应数字里面的某一个下标，这个下标的计算就是应用了HashCode
。但又因为HashCode是32位的，但计算数组的下标的时候，往往用不到高位，为了解决这个问题，加了一个与高位与的操作。计算数组下标的方法在put方法会讲到。

### tableSizeFor(int cap)

这个函数是用来判断数组增长的。因为数组的长度是2的幂次，所以给任意一个数字，必须返回正好大于这个数的幂次。先看代码。

```
    /**
     * Returns a power of two size for the given target capacity.
     */
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

```

可以简单写一个测试代码测一下：
```
package com.wardensky.demo.base;

public class HashMapSrcDemo {
	static final int MAXIMUM_CAPACITY = 1 << 30;

	public static void main(String[] args) {
		System.out.println(tableSizeFor(1000));
	}

	static final int tableSizeFor(int cap) {
		int n = cap - 1;
		n |= n >>> 1;
		n |= n >>> 2;
		n |= n >>> 4;
		n |= n >>> 8;
		n |= n >>> 16;
		return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
	}
}

```
输入100，返回128；输入1000，返回1024。他的原理是通过位移和与操作，把最高1位后面的0都变成1，然后再加1，就是2的幂次了。

这个算法还是比较巧妙的，如果是我写，估计会写一堆if else判断。


## 构造函数

HashMap有三个重载的构造函数。

```
   /**
     * Constructs an empty <tt>HashMap</tt> with the specified initial
     * capacity and load factor.
     *
     * @param  initialCapacity the initial capacity
     * @param  loadFactor      the load factor
     * @throws IllegalArgumentException if the initial capacity is negative
     *         or the load factor is nonpositive
     */
    public HashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                                               initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " +
                                               loadFactor);
        this.loadFactor = loadFactor;
        this.threshold = tableSizeFor(initialCapacity);
    }

    /**
     * Constructs an empty <tt>HashMap</tt> with the specified initial
     * capacity and the default load factor (0.75).
     *
     * @param  initialCapacity the initial capacity.
     * @throws IllegalArgumentException if the initial capacity is negative.
     */
    public HashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructs an empty <tt>HashMap</tt> with the default initial capacity
     * (16) and the default load factor (0.75).
     */
    public HashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted
    }
```
主要是给初始大小和负载因子复制，如果给了一个初始值，会调用上面的tableSizeFor方法。



## 增加元素

put方法是最常用的方法，与之相关的有几个方法。


### put
put方法调用了putVal方法。
```
  /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the old
     * value is replaced.
     *
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with <tt>key</tt>, or
     *         <tt>null</tt> if there was no mapping for <tt>key</tt>.
     *         (A <tt>null</tt> return can also indicate that the map
     *         previously associated <tt>null</tt> with <tt>key</tt>.)
     */
    public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }
```

### putVal

向Map里面放数据。基本逻辑就是：

- 如果没有初始化，初始化。
- 根据hash值找数组下标，如果找到之后此位置上没有值，则直接把这个Node放上面。
- 如果找打了值，则放到链表的最后面。
- 如果数据比较多，则变成TreeNode。
- 如果变大了（超过负载因子*容量），则resize

```

    /**
     * Implements Map.put and related methods
     *
     * @param hash hash for key
     * @param key the key
     * @param value the value to put
     * @param onlyIfAbsent if true, don't change existing value
     * @param evict if false, the table is in creation mode.
     * @return previous value, or null if none
     */
    final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
    	//tab是局部变量的数组，n是数组的长度，i
        Node<K,V>[] tab; Node<K,V> p; int n, i;
        //HashMap的初始化是在putVal方法里面开始的。
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;

        if ((p = tab[i = (n - 1) & hash]) == null)
        	//数组的下标上面没有元素，直接new一个node就好。
            tab[i] = newNode(hash, key, value, null);
        else {
        	//数组下标有元素的情况
            Node<K,V> e; K k;
            if (p.hash == hash &&
                ((k = p.key) == key || (key != null && key.equals(k))))
                e = p;
            else if (p instanceof TreeNode)
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
            else {
                for (int binCount = 0; ; ++binCount) {
                    if ((e = p.next) == null) {
                    	//插到最后一个下面就可以了。
                        p.next = newNode(hash, key, value, null);
                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                            treeifyBin(tab, hash);
                        break;
                    }
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                afterNodeAccess(e);
                return oldValue;
            }
        }
        ++modCount;
        if (++size > threshold)
            resize();
        afterNodeInsertion(evict);
        return null;
    }

```

### treeifyBin

```
final void treeifyBin(Node<K,V>[] tab, int hash) {
        int n, index; Node<K,V> e;
        //当tab.length<MIN_TREEIFY_CAPACITY(64)是还是进行resize
        if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY)
            resize();
        else if ((e = tab[index = (n - 1) & hash]) != null) {
            TreeNode<K,V> hd = null, tl = null;
            do {
                TreeNode<K,V> p = replacementTreeNode(e, null);
                if (tl == null)
                    hd = p;
                else {
                    p.prev = tl;
                    tl.next = p;
                }
                tl = p;
            } while ((e = e.next) != null);
            if ((tab[index] = hd) != null)
                //存储在红黑树
                hd.treeify(tab);
        }
    }

```

### resize

初始化和扩大HashMap的方法，基本逻辑如下：

- 如果各种为空，则初始化
- 否则直接乘以2
- 对于只有一个元素的情况，直接放到新的下标上面。
- 对于有多个元素的情况，一部分不动，一部分放到高位上去。

```

    /**
     * Initializes or doubles table size.  If null, allocates in
     * accord with initial capacity target held in field threshold.
     * Otherwise, because we are using power-of-two expansion, the
     * elements from each bin must either stay at same index, or move
     * with a power of two offset in the new table.
     *
     * @return the table
     */
    final Node<K,V>[] resize() {
        Node<K,V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        if (oldCap > 0) {
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            }
            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                     oldCap >= DEFAULT_INITIAL_CAPACITY)
            	//直接乘以2
                newThr = oldThr << 1; // double threshold
        }
        else if (oldThr > 0) // initial capacity was placed in threshold
            newCap = oldThr;
        else {               // zero initial threshold signifies using defaults
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        if (newThr == 0) {
            float ft = (float)newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                      (int)ft : Integer.MAX_VALUE);
        }
        //在这个代码之上基本上都是初始化用的。
        threshold = newThr;
        @SuppressWarnings({"rawtypes","unchecked"})
            Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
        table = newTab;
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                Node<K,V> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    if (e.next == null)
                    	//元素上面只有一个节点的情况，注意，要重新计算数组下标。
                        newTab[e.hash & (newCap - 1)] = e;
                    else if (e instanceof TreeNode)
                    	//树的情况
                        ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                    else { // preserve order
                    	//多个节点的情况，  分别记录头和尾的节点，因为是乘以2，所以分为高位和低位的情况，把高位和地位分别存储。
                        Node<K,V> loHead = null, loTail = null;
                        Node<K,V> hiHead = null, hiTail = null;
                        Node<K,V> next;
                        do {
                            next = e.next;
                            if ((e.hash & oldCap) == 0) {
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            }
                            else {
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);//直到没有节点
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
    }

```



## 查找元素


### get

计算出下标查找。
```
   /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * <p>More formally, if this map contains a mapping from a key
     * {@code k} to a value {@code v} such that {@code (key==null ? k==null :
     * key.equals(k))}, then this method returns {@code v}; otherwise
     * it returns {@code null}.  (There can be at most one such mapping.)
     *
     * <p>A return value of {@code null} does not <i>necessarily</i>
     * indicate that the map contains no mapping for the key; it's also
     * possible that the map explicitly maps the key to {@code null}.
     * The {@link #containsKey containsKey} operation may be used to
     * distinguish these two cases.
     *
     * @see #put(Object, Object)
     */
    public V get(Object key) {
        Node<K,V> e;
        return (e = getNode(hash(key), key)) == null ? null : e.value;
    }
```

### getNode

就是链表查找

```
  /**
     * Implements Map.get and related methods
     *
     * @param hash hash for key
     * @param key the key
     * @return the node, or null if none
     */
    final Node<K,V> getNode(int hash, Object key) {
        Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
        if ((tab = table) != null && (n = tab.length) > 0 &&
            (first = tab[(n - 1) & hash]) != null) {
            if (first.hash == hash && // always check first node
                ((k = first.key) == key || (key != null && key.equals(k))))
                return first;
            if ((e = first.next) != null) {
                if (first instanceof TreeNode)
                    return ((TreeNode<K,V>)first).getTreeNode(hash, key);
                do {
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        return e;
                } while ((e = e.next) != null);
            }
        }
        return null;
    }

```

## 其它


### 多线程下的HashMap死锁


### HashMap的table为什么是transient的


### HashMap和Hashtable的区别


有序数组存储数据，对数据的检索效率会很高，但是，插入和删除会有瓶颈产生。
链表存储数据，通常只能采用逐个比较的方法来检索数据（查找数据），但是，插入和删除的效率很高。

于是，将两者结合，取长补短，优势互补一下，就产生哈希散列这种存储方式。

具体是怎么样的呢？
我们可以理解成，在链表存储的基础上，对链表结构进行的一项改进。
我们将一个大的链表，拆散成几个或者几十个小的链表。
每个链表的表头，存放到一个数组里面。

这样，在从大链表拆分成小链表的时候就有讲究了。
我们按照什么规则来将一个大链表中的数据，分散存放到不同的链表中呢？
在计算机当中，肯定是要将规则数量化的，也就是说，这个规则，一定要是个数字，这样才比较好操作。
比如，按照存放时间，每5分钟一个时间段，将相同时间段存放的数据，放到同一个链表里面；
或者，将数据排序，每5个数据形成一个链表；
等等，等等，还有好多可以想象得到的方法。
但是，这些方法都会存在一些不足之处。
我们就在想了，如果存放的数据，都是整数就好了。
这样，我可以创建一个固定大小的数组，比如50个大小，然后，让数据（整数）对50进行取余运算，
然后，这些数据，自然就会被分成50个链表了，每个链表可以是无序的，反正链表要逐个比较进行查询。
如果，我一个有200个数据，分组后，平均每组也就4个数据，那么，链表比较，平均也就比较4次就好了。
但是，实际上，我们存放的数据，通常都不是整数。
所以，我们需要将数据对象映射成整数的一个算法。
HashCode方法，应运而生了。
每个数据对象，都会对应一个HashCode值，通过HashCode我们可以将对象分组存放到不同的队列里。
这样，在检索的时候，就可以减少比较次数。

在实际使用当中，HashCode方法、数组的大小 以及 数据对象的数量，这三者对检索的性能有着巨大的影响。
1.如果数组大小为1，那和链表存储没有什么区别了，
而且，还多了一步计算HashCode的时间，所以，数组不能太小，太小查询费时间。
2.如果我只存放1个数据对象，数组又非常大，那么，数组所占的内存空间，就比数据对象占的空间还大，
这样，少量数据对象，巨大的数组，虽然能够使检索速度，但是，浪费了很多内存空间。
3.如果所有对象的HashCode值都是相同的数，
那么，无论数组有多大，这些数据都会保存到同一个链表里面，
一个好的HashCode算法，可以使存放的数据，有较好的分散性，
在实际的实现当中，HashSet和HashMap都对数据对象产生的HashCode进行了二次散列处理，
使得数据对象具有更好的分散性。
