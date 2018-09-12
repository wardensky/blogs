# 用jmap分析java程序


之前的随笔提到用jstack分析java线程情况，也是在这个项目中，当线程的问题解决之后，发现程序的内存一直增长，于是用jmap工具分析了一下java程序占用内存的情况。

命令很简单，直接

```
jmap -histo 22955 > jmap.info
```
其中22955是java的pid，重定向到jmap.info文件中，其内容为：
```
 num     #instances         #bytes  class name
----------------------------------------------
   1:        585569      359014728  [C
   2:         95905       14389200  <constMethodKlass>
   3:        579358       13904592  java.lang.String
   4:         95905       12287600  <methodKlass>
   5:          8542       10324824  <constantPoolKlass>
   6:         21015        7564272  [B
   7:          8542        6355376  <instanceKlassKlass>
   8:          7126        5664512  <constantPoolCacheKlass>
   9:         53877        5603208  com.wisdombud.unicom.monitor.po.MessageBean
  10:        117112        2810688  java.util.Date
  11:         46743        2534632  [Ljava.lang.Object;
  12:          3669        2142176  <methodDataKlass>
  13:         25046        1850544  [Ljava.util.Hashtable$Entry;
  14:         51662        1653184  java.util.Hashtable$Entry
  15:         50881        1628192  java.util.concurrent.ConcurrentHashMap$HashEntry
  16:         43410        1389120  java.util.HashMap$Entry
  17:         55516        1332384  java.util.ArrayList
  18:         24915        1195920  java.util.Hashtable
  19:          8988        1162184  java.lang.Class
  20:         14523        1161840  java.lang.reflect.Method
  21:         21461        1030128  com.sun.org.apache.xerces.internal.dom.AttrNSImpl
  22:         12866         956456  [S
  23:         14959         750072  [[I
  24:          5752         694480  [I
  25:          9667         618688  com.sun.org.apache.xerces.internal.dom.ElementNSImpl
  26:         23097         554328  com.sun.org.apache.xerces.internal.dom.ParentNode$UserDataRecord
  27:         13430         537200  com.sun.org.apache.xerces.internal.dom.TextImpl
  28:          5279         526192  [Ljava.util.concurrent.ConcurrentHashMap$HashEntry;
  29:         15795         505440  com.sun.org.apache.xerces.internal.xni.QName
  30:          3120         476496  [Ljava.util.HashMap$Entry;
  31:         13949         446368  com.wisdombud.unicom.collect.linux.bean.ConfLinuxFileSys
  32:         23464         375424  java.lang.Object
  33:          3774         366032  [Ljava.lang.String;
  34:           429         233376  <objArrayKlassKlass>
  35:          9685         232440  com.sun.org.apache.xerces.internal.dom.AttributeMap
  36:          6981         223392  com.wisdombud.unicom.collect.linux.bean.PerfLinuxFileSys
  37:          4652         223296  java.util.HashMap
  38:          5279         211160  java.util.concurrent.ConcurrentHashMap$Segment
  39:          5512         176384  java.util.concurrent.locks.ReentrantLock$NonfairSync
  40:           243         173016  [Lcom.sun.org.apache.xerces.internal.util.SymbolTable$Entry;
  41:          3159         126360  java.lang.ref.SoftReference
  42:          2516         120768  com.wisdombud.unicom.collect.linux.bean.ConfLinux
  43:          2761         110440  java.util.LinkedHashMap$Entry
  44:          1143         100584  org.snmp4j.Snmp$PendingRequest
  45:          1364          98208  org.snmp4j.mp.StateReference
  46:          2352          94080  com.wisdombud.unicom.collect.linux.bean.PerfLinux

Total       2225122      467913224

```
我去掉了很多占用比较小类。

最重要的是后两列，第三列是占用的字节数，第四列是类，关于类，解释如下：
```
[C is a char[]
[S is a short[]
[I is a int[]
[B is a byte[]
[[I is a int[][]

```

参考：http://stackoverflow.com/questions/7913759/what-are-these-objects-in-the-jmap-histogram



可以看到，char比较多，但也能定位到自己代码中MessageBean占用比较多，迅速定位到操作此类的方法，在通过分析代码，找到了问题的原因。

jmap可以经常用，比如每10分钟运行一次，通过对比，观察哪些类的占用内存增长比较多。



另外，jmap还有dump的参数，其方法如下：

jmap -dump:format=b,file=mem.dat 22955

在这个项目中，dump出来的文件非常大，因为网络的原因没有传过来，所以没有分析。

关于dump的用法，可以参考：http://www.cnblogs.com/ggjucheng/archive/2013/04/16/3024986.html
