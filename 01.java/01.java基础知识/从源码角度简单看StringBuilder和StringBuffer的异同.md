# 从源码角度简单看StringBuilder和StringBuffer的异同


## 概述

StringBuilder和StringBuffer是两个容易混淆的概念，本文从源码入手，简单看二者的异同。
容易知道的是，这两者有一个是线程安全的，而且线程安全的那个效率低。

## java doc里面的说明

java doc是写源码的人写的注释，先看java doc。

### StringBuilder
A mutable sequence of characters. This class provides an API compatible with StringBuffer, but with no guarantee of synchronization. This class is designed for use as a drop-in replacement for StringBuffer in places where the string buffer was being used by a single thread (as is generally the case). Where possible, it is recommended that this class be used in preference to StringBuffer as it will be faster under most implementations.

The principal operations on a StringBuilder are the append and insert methods, which are overloaded so as to accept data of any type. Each effectively converts a given datum to a string and then appends or inserts the characters of that string to the string builder. The append method always adds these characters at the end of the builder; the insert method adds the characters at a specified point.

For example, if z refers to a string builder object whose current contents are "start", then the method call z.append("le") would cause the string builder to contain "startle", whereas z.insert(4, "le") would alter the string builder to contain "starlet".

In general, if sb refers to an instance of a StringBuilder, then sb.append(x) has the same effect as sb.insert(sb.length(), x).

Every string builder has a capacity. As long as the length of the character sequence contained in the string builder does not exceed the capacity, it is not necessary to allocate a new internal buffer. If the internal buffer overflows, it is automatically made larger.

Instances of StringBuilder are not safe for use by multiple threads. If such synchronization is required then it is recommended that java.lang.StringBuffer be used.

Unless otherwise noted, passing a null argument to a constructor or method in this class will cause a NullPointerException to be thrown.

Since:
1.5
Author:
Michael McCloskey
See Also:
java.lang.StringBuffer
java.lang.String

### StringBuffer
A thread-safe, mutable sequence of characters. A string buffer is like a String, but can be modified. At any point in time it contains some particular sequence of characters, but the length and content of the sequence can be changed through certain method calls.

String buffers are safe for use by multiple threads. The methods are synchronized where necessary so that all the operations on any particular instance behave as if they occur in some serial order that is consistent with the order of the method calls made by each of the individual threads involved.

The principal operations on a StringBuffer are the append and insert methods, which are overloaded so as to accept data of any type. Each effectively converts a given datum to a string and then appends or inserts the characters of that string to the string buffer. The append method always adds these characters at the end of the buffer; the insert method adds the characters at a specified point.

For example, if z refers to a string buffer object whose current contents are "start", then the method call z.append("le") would cause the string buffer to contain "startle", whereas z.insert(4, "le") would alter the string buffer to contain "starlet".

In general, if sb refers to an instance of a StringBuffer, then sb.append(x) has the same effect as sb.insert(sb.length(),

Whenever an operation occurs involving a source sequence (such as appending or inserting from a source sequence), this class synchronizes only on the string buffer performing the operation, not on the source. Note that while StringBuffer is designed to be safe to use concurrently from multiple threads, if the constructor or the append or insert operation is passed a source sequence that is shared across threads, the calling code must ensure that the operation has a consistent and unchanging view of the source sequence for the duration of the operation. This could be satisfied by the caller holding a lock during the operation's call, by using an immutable source sequence, or by not sharing the source sequence across threads.

Every string buffer has a capacity. As long as the length of the character sequence contained in the string buffer does not exceed the capacity, it is not necessary to allocate a new internal buffer array. If the internal buffer overflows, it is automatically made larger.

Unless otherwise noted, passing a null argument to a constructor or method in this class will cause a NullPointerException to be thrown.

As of release JDK 5, this class has been supplemented with an equivalent class designed for use by a single thread, StringBuilder. The StringBuilder class should generally be used in preference to this one, as it supports all of the same operations but it is faster, as it performs no synchronization.

Since:
JDK1.0
Author:
Arthur van Hoff
See Also:
java.lang.StringBuilder
java.lang.String

## javadoc小结
从上面可以看出：
- StringBuffer和StringBuilder都可以认为是可变的String。
- StringBuffer是线程安全的，先出现，在JDK1.0的时候就有了。
- StringBuilder是非线程安全的，后出现，在JDK1.5才有。
- 二者的接口完全一样，StringBuilder更快。

其实，正常的用，知道这几点就好了，不过还是想看看源码里面怎么实现的。


## 源码

### 如何实现线程安全

看源码可以知道，这二者都继承了一个抽象类AbstractStringBuilder
```
 public final class StringBuffer
    extends AbstractStringBuilder
    implements java.io.Serializable, CharSequence

public final class StringBuilder
    extends AbstractStringBuilder
    implements java.io.Serializable, CharSequence
```
代码量也不是很大，StringBuilder440行代码，StringBuffer718行代码，最多的是AbstractStringBuilder，一共1440行代码。

看代码从几个角度看，一是一些关键的内部结构是怎么样的，二是我们常用的函数是怎么实现的。因为String是不可变的，是放在常量池里面的。猜测StringBuilder和StringBuffer应该是用char数组实现的。

```
    /**
     * The value is used for character storage.
     */
    char[] value;

    /**
     * The count is the number of characters used.
     */
    int count;
```
可以看出，用value存数据，用一个count表示长度。

看几个常见方法的不同实现。
StringBuffer
```
    @Override
    public synchronized StringBuffer append(String str) {
        toStringCache = null;
        super.append(str);
        return this;
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public synchronized StringBuffer insert(int offset, String str) {
        toStringCache = null;
        super.insert(offset, str);
        return this;
    }

    @Override
    public synchronized String toString() {
        if (toStringCache == null) {
            toStringCache = Arrays.copyOfRange(value, 0, count);
        }
        return new String(toStringCache, true);
    }

```

StringBuilder
```
    @Override
    public StringBuilder append(String str) {
        super.append(str);
        return this;
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public StringBuilder insert(int offset, String str) {
        super.insert(offset, str);
        return this;
    }

    @Override
    public String toString() {
        // Create a copy, don't share the array
        return new String(value, 0, count);
    }

```

由代码可知，大部分情况想，StrinbBuffer只是增加了一个synchronized关键字来保证线程安全。但toString方法不同，这个后面再说。


### 初始化大小和如何增长

既然实际上是一个数组，那么数组开始的大小和增长的方法就很重要，我们通过代码看一下。

```
    /**
     * Constructs a string buffer with no characters in it and an
     * initial capacity of 16 characters.
     */
    public StringBuffer() {
        super(16);
    }

    /**
     * Constructs a string builder with no characters in it and an
     * initial capacity of 16 characters.
     */
    public StringBuilder() {
        super(16);
    }


```
可以看到，这两个默认构造函数都说明默认的数组大小是16。
为什么是16呢？我没想明白。

接下来关心如何增长的？我们看看append的实现

```
    public AbstractStringBuilder append(String str) {
        if (str == null)
            return appendNull();
        int len = str.length();
        ensureCapacityInternal(count + len);
        str.getChars(0, len, value, count);
        count += len;
        return this;
    }


    /**
     * This method has the same contract as ensureCapacity, but is
     * never synchronized.
     */
    private void ensureCapacityInternal(int minimumCapacity) {
        // overflow-conscious code
        if (minimumCapacity - value.length > 0)
            expandCapacity(minimumCapacity);
    }

    /**
     * This implements the expansion semantics of ensureCapacity with no
     * size check or synchronization.
     */
    void expandCapacity(int minimumCapacity) {
        int newCapacity = value.length * 2 + 2;
        if (newCapacity - minimumCapacity < 0)
            newCapacity = minimumCapacity;
        if (newCapacity < 0) {
            if (minimumCapacity < 0) // overflow
                throw new OutOfMemoryError();
            newCapacity = Integer.MAX_VALUE;
        }
        value = Arrays.copyOf(value, newCapacity);
    }
```
上面三个方法说明了如何扩容。
- 把当前的容量*2+2
- 如果新增加的长度大于这个值，则设为新增加的值
- 如果溢出，则抛出OutOfMemoryError


### StringBuffer中toString的实现

```
    /**
     * A cache of the last value returned by toString. Cleared
     * whenever the StringBuffer is modified.
     */
    private transient char[] toStringCache;

    @Override
    public synchronized StringBuffer append(String str) {
        toStringCache = null;
        super.append(str);
        return this;
    }
    @Override
    public synchronized String toString() {
        if (toStringCache == null) {
            toStringCache = Arrays.copyOfRange(value, 0, count);
        }
        return new String(toStringCache, true);
    }
```
可以看到，定义了一个数组toStringCache，每次数据有变化，都把这个设置为null。在toString的时候，再重新从当前数据里面拿。
transient关键字是为了避免这个数组被序列化。


## 小结

其实java本身的源码写的还是比较简单的，学习知识如果能从源码入手，能够更深入的理解很多原理。本文只是简单的列举了一部分源码，简单说明StringBuffer和StringBuilder的异同。有兴趣的朋友可以自己看一下。
