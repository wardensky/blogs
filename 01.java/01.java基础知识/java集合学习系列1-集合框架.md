# java集合学习系列1-集合框架




## 引言

我自己总结，学习编程知识的几个步骤。
第一步：知道这个东西是什么，从整体上有一个概念，最好一句话能说明白。或者找一个类似的东西做类比，能把新知识套到已有的知识上。
第二步：知道这个东西怎么用，能跑起来一个简单的demo。
第三步：参考别人的用法，找到这个东西的最佳实践。
第四步：弄明白这个东西是怎么搞出来的，什么原理。这一步搞明白了，也能验证第三步的正确性。
第五步：参考他的原理，自己搞一个类似的东西。

学习最重要的就是为自己所用，要把知识消化，而不是简单的记下来。所以要去看原理，看明白原理，必能转化为自己的知识。最常见的方法就是说出来，给别人讲出来，或者写出来。所谓“知易行难”，很多东西觉得自己明白了，其实距离真正的理解还是有差距。

本系列文章准备采用这个方法论，学习java集合知识，争取搞明白第四步。
本系列包含的文章有：
- java集合学习系列1-集合框架
- java集合学习系列1-集合框架
- java集合学习系列1-集合框架
- java集合学习系列1-集合框架
- java集合学习系列1-集合框架
- java集合学习系列1-集合框架


## 从类和接口说起

先看一张图
![类关系图](http://7.happykeke.top/tech_Java%20collection%20cheat%20sheet.PNG)

 这张图可以清晰的看出来，java集合大部分实现了Map和Collection两个接口。在此之下，又有我们常见的List、Queue、
 Set等接口。我们先把这些接口说明白。
## Collection
javadoc里面的内容
>The root interface in the collection hierarchy. A collection represents a group of objects, known as its elements. Some collections allow duplicate elements and others do not. Some are ordered and others unordered.

Collection是集合的根，包含了一组数据。他提供的方法包括：
```
size()
isEmpty()
contains(Object)
iterator()
toArray()
toArray(T[])
add(E)
remove(Object)
containsAll(Collection<?>)
addAll(Collection<? extends E>)
removeAll(Collection<?>)
removeIf(Predicate<? super E>)
retainAll(Collection<?>)
clear()
equals(Object)
hashCode()
spliterator()
stream()
parallelStream()
```
大部分都是常见的，没什么可说的。



## Map
>An object that maps keys to values. A map cannot contain duplicate keys; each key can map to at most one value.

Map就是key value。
其方法如下：
```
size()
isEmpty()
containsKey(Object)
containsValue(Object)
get(Object)
put(K, V)
remove(Object)
putAll(Map<? extends K, ? extends V>)
clear()
keySet()
values()
entrySet()
equals(Object)
hashCode()
getOrDefault(Object, V)
forEach(BiConsumer<? super K, ? super V>)
replaceAll(BiFunction<? super K, ? super V, ? extends V>)
putIfAbsent(K, V)
remove(Object, Object)
replace(K, V, V)
replace(K, V)
computeIfAbsent(K, Function<? super K, ? extends V>)
computeIfPresent(K, BiFunction<? super K, ? super V, ? extends V>)
compute(K, BiFunction<? super K, ? super V, ? extends V>)
merge(K, V, BiFunction<? super V, ? super V, ? extends V>)
```
map提供的接口就比collection多了，值得关注的有：

## List

## Queue

## Set

## Qeuque

##参考
[Java集合（1）一 集合框架](http://www.cnblogs.com/konck/p/7739471.html)
[Java Collections Framework Cheat Sheet](http://pierrchen.blogspot.com/2014/03/java-collections-framework-cheat-sheet.html)
