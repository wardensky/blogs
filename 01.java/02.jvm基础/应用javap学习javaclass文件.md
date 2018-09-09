# 应用javap学习java class文件




## class文件内容概述
JVM运行Java程序的时候，它会加载对应的class文件，并提取class文件中的信息存放在JVM开辟出来的方法区 内存中。那么这个class文件里面到底有些什么内容呢？
>class 文件是java代码编译之后的文件，但其实很多其它运行在jvm上的语言最后编译成的也是class文件，如jRuby等。所以可以说class文件是可以被jvm接受并运行的字节码文件。
>Class文件是一组以8位字节为基础单位的二进制流，各个数据项目严格按照顺序紧凑地排列在Class文件之中，中间没有添加任何分隔符，这使得整个Class文件中存储的内容几乎全部是程序运行的必要数据，没有空隙存在。根据Java虚拟机规范的规定，Class文件格式采用一种类似于C语言结构体的伪结构来存储数据。


class文件包含以下内容
>1. 魔数 magic number
>2. 版本号 minor_version major_verion
>3. 常量池 Constant pool
>4. 访问标志 access_flag
>5. 类索引、父类索引与接口索引  this_class super_class interfaces
>6. 字段表集合   fileds
>7. 方法表集合   methods
>7. 属性表集合

### 魔数 magic number
class文件的开始是magic number和版本号。
头4个字节是magic number，即
```
cafe babe
```
### 版本号 minor_version major_verion
第5和第6字节是次版本号，第7和第8字节是主版本号。
版本号的具体对应关系如下：

|jdk版本号|7,8字节|
|-|-|
|jdk8|52 0x34|
|jdk7|51 0x33|
|jdk6|50 0x32|
|jdk5|49 0x31|

### 常量池 Constant pool
>常量池可以理解为Class文件之中的资源仓库，它是Class文件结构中与其他项目关联最多的数据类型，也是占用Class文件空间最大的数据项目之一，同时它还是在Class文件中第一个出现的表类型数据项目。由于常量池中常量的数量是不固定的，所以在常量池的入口需要放置一项u2类型的数据，代表常量池容量计数值（constant_pool_count）。与Java中语言习惯不一样的是，这个容量计数是从1而不是0开始的。关于常量池的分析在下面的例子中会有体现。

常量池里面包含以下类型内容：

|类型|标志|描述|
|-|-|
|CONSTANT_Utf8_info|1|utf-8编码的字符串|
|CONSTANT_Integer_info|3|整型字面量|
|CONSTANT_Float_info|4|浮点型字面量|
|CONSTANT_Long_info|5|长整型字面量|
|CONSTANT_Double_info|6|双精度浮点型字面量|
|CONSTANT_Class_info|7|类或接口的符号引用|
|CONSTANT_String_info|8|字符串类型字面量|
|CONSTANT_Fieldref_info|9|字段的符号引用|
|CONSTANT_Methodref_info|10|类中方法的符号引用|
|CONSTANT_InterfaceMethodref_info|11|接口中方法的符号引用|
|CONSTANT_NameAndType_info|12|字段或方法的部分符号引用|
|CONSTANT_MethodHandle_info|15|表示方法句柄|
|CONSTANT_MethodType_info|16|标识方法类型|
|CONSTANT_InvokeDynamic_info|18|表示一个动态方法调用点|

这14种表都有一个共同的特点，就是表开始的第一位是一个u1类型的标志位（取值见上表中标志列），代表当前这个常量属于哪种常量类型。每种常量的结构也不同，见下表。
![Alt text](./常量池类型.jpg)




### 访问标志 access_flag
> 占用2个字节。用来表明该class文件中定义的是类还是接口，访问修饰符是public还是缺省。类或接口是否是抽象的。类是否是final的。
![Alt text](./访问标志.jpg)

### 类索引、父类索引与接口索引  this_class super_class interfaces
>类索引（this_class）和父类索引（super_class）都是一个u2类型的数据，而接口索引集合（inter-faces）是一组u2类型的数据的集合，Class文件中由这三项数据来确定这个类的继承关系。类索引用于确定这个类的全限定名，父类索引用于确定这个类的父类的全限定名。类索引、父类索引和接口索引集合都按顺序排列在访问标志之后，类索引和父类索引用两个u2类型的索引值表示，它们各自指向一个类型为CONSTANT_Class_info的类描述符常量，通过CONSTANT_Class_info类型的常量中的索引值可以找到定义在CONSTANT_Utf8_info类型的常量中的全限定名字符串。
>类索引、父类索引和接口索引集合都按顺序排列在访问标志之后，类索引和父类索引用两个u2类型的索引值表示，它们各自指向一个类型为CONSTANT_Class_info的类描述符常量，通过CONSTANT_Class_info类型的常量中的索引值可以找到定义在CONSTANT_Utf8_info类型的常量中的全限定名字符串。

### 字段表集合   field_info
>字段表（field_info）用于描述接口或者类中声明的变量。字段（field）包括类级变量以及实例级变量，但不包括在方法内部声明的局部变量。可以包括的信息有：字段的作用域（public、private、protected修饰符）、是实例变量还是类变量（static修饰符）、可变性（final）、并发可见性（volatile修饰符，是否强制从主内存读写）、可否被序列化（transient修饰符）、字段数据类型（基本类型、对象、数组）、字段名称。上述这些信息中，各个修饰符都是布尔值，要么有某个修饰符，要么没有，很适合使用标志位来表示。而字段叫什么名字、字段被定义为什么数据类型，这些都是无法固定的，只能引用常量池中的常量来描述。
字段表结构如下图所示：
![Alt text](./字段表结构.jpg)
字段访问标志如下图所示：
![Alt text](./字段访问标志.jpg)

### 方法表集合   method_info
>Class文件存储格式中对方法的描述与对字段的描述几乎采用了完全一致的方式，方法表的结构如同字段表一样，依次包括了访问标志（access_flags）、名称索引（name_index）、描述符索引（descriptor_index）、属性表集合（attributes）几项，见下表。

![Alt text](./方法表结构.jpg)
其访问标志与字段表类似，不赘述。

### 属性表集合
>在Class文件、字段表、方法表都可以携带自己的属性表集合，以用于描述某些场景专有的信息。在Class文件、字段表、方法表都可以携带自己的属性表集合，以用于描述某些场景专有的信息。
jvm里面定义的属性包括以下类型：
![Alt text](./属性表1.jpg)
![Alt text](./属性表2.jpg)



## javap命令
>javap是jdk下面的一个反编译class文件的程序( Java class file disassembler)，所以说用javap来学习class文件最为合适不过。

>help和man是学习的好方法，javap的help和man分别得到以下内容：
```
IvydeMacBook-Air% javap -help
用法: javap <options> <classes>
其中, 可能的选项包括:
  -help  --help  -?        输出此用法消息
  -version                 版本信息
  -v  -verbose             输出附加信息
  -l                       输出行号和本地变量表
  -public                  仅显示公共类和成员
  -protected               显示受保护的/公共类和成员
  -package                 显示程序包/受保护的/公共类
                           和成员 (默认)
  -p  -private             显示所有类和成员
  -c                       对代码进行反汇编
  -s                       输出内部类型签名
  -sysinfo                 显示正在处理的类的
                           系统信息 (路径, 大小, 日期, MD5 散列)
  -constants               显示最终常量
  -classpath <path>        指定查找用户类文件的位置
  -cp <path>               指定查找用户类文件的位置
  -bootclasspath <path>    覆盖引导类文件的位置

```

```
IvydeMacBook-Air% man javap

man的内容比较长，就不贴在这里来。
```
可以看出－verbose参数最全面，能查看所有信息。下面的实例分析中，我们就采用－verbose参数，查看class文件。

## 实例分析
在说完上面的枯燥知识后，我们来实践一下，来分析一个真实的class文件。我们采用两种方式分析class文件：直接查看二进制和javap分析。通过两种方式的对比，加深对class文件结构的认识。
### java代码

```
import java.util.List;
import java.util.ArrayList;

public class Test{
	private int id = 0;

	public String name = "myname";

	public static String desc = "my desc";

	public final static String address = "beijing";

	public final List<String> myList = new ArrayList<String>();

	public int add(int a, int b){
		return a + b;
	}

	public int getId(){
		return this.id;
	}

	public void setId(int id){
		this.id = id;
	}
}

```

### class文件的内容
把如上代码编译成class文件。
```
javac Test.java
```

得到Test.class文件，用二进制打开，内容如下：
```
0000000: cafe babe 0000 0034 0030 0a00 0b00 2509
0000010: 000a 0026 0800 2709 000a 0028 0700 290a
0000020: 0005 0025 0900 0a00 2a08 002b 0900 0a00
0000030: 2c07 002d 0700 2e01 0002 6964 0100 0149
0000040: 0100 046e 616d 6501 0012 4c6a 6176 612f
0000050: 6c61 6e67 2f53 7472 696e 673b 0100 0464
0000060: 6573 6301 0007 6164 6472 6573 7301 000d
0000070: 436f 6e73 7461 6e74 5661 6c75 6508 002f
0000080: 0100 066d 794c 6973 7401 0010 4c6a 6176
0000090: 612f 7574 696c 2f4c 6973 743b 0100 0953
00000a0: 6967 6e61 7475 7265 0100 244c 6a61 7661
00000b0: 2f75 7469 6c2f 4c69 7374 3c4c 6a61 7661
00000c0: 2f6c 616e 672f 5374 7269 6e67 3b3e 3b01
00000d0: 0006 3c69 6e69 743e 0100 0328 2956 0100
00000e0: 0443 6f64 6501 000f 4c69 6e65 4e75 6d62
00000f0: 6572 5461 626c 6501 0003 6164 6401 0005
0000100: 2849 4929 4901 0005 6765 7449 6401 0003
0000110: 2829 4901 0005 7365 7449 6401 0004 2849
0000120: 2956 0100 083c 636c 696e 6974 3e01 000a
0000130: 536f 7572 6365 4669 6c65 0100 0954 6573
0000140: 742e 6a61 7661 0c00 1800 190c 000c 000d
0000150: 0100 066d 796e 616d 650c 000e 000f 0100
0000160: 136a 6176 612f 7574 696c 2f41 7272 6179
0000170: 4c69 7374 0c00 1400 1501 0007 6d79 2064
0000180: 6573 630c 0010 000f 0100 0454 6573 7401
0000190: 0010 6a61 7661 2f6c 616e 672f 4f62 6a65
00001a0: 6374 0100 0762 6569 6a69 6e67 0021 000a
00001b0: 000b 0000 0005 0002 000c 000d 0000 0001
00001c0: 000e 000f 0000 0009 0010 000f 0000 0019
00001d0: 0011 000f 0001 0012 0000 0002 0013 0011
00001e0: 0014 0015 0001 0016 0000 0002 0017 0005
00001f0: 0001 0018 0019 0001 001a 0000 003f 0003
0000200: 0001 0000 001b 2ab7 0001 2a03 b500 022a
0000210: 1203 b500 042a bb00 0559 b700 06b5 0007
0000220: b100 0000 0100 1b00 0000 1200 0400 0000
0000230: 0400 0400 0500 0900 0700 0f00 0d00 0100
0000240: 1c00 1d00 0100 1a00 0000 1c00 0200 0300
0000250: 0000 041b 1c60 ac00 0000 0100 1b00 0000
0000260: 0600 0100 0000 1000 0100 1e00 1f00 0100
0000270: 1a00 0000 1d00 0100 0100 0000 052a b400
0000280: 02ac 0000 0001 001b 0000 0006 0001 0000
0000290: 0014 0001 0020 0021 0001 001a 0000 0022
00002a0: 0002 0002 0000 0006 2a1b b500 02b1 0000
00002b0: 0001 001b 0000 000a 0002 0000 0018 0005
00002c0: 0019 0008 0022 0019 0001 001a 0000 001e
00002d0: 0001 0000 0000 0006 1208 b300 09b1 0000
00002e0: 0001 001b 0000 0006 0001 0000 0009 0001
00002f0: 0023 0000 0002 0024                    

```



### 应用javap得到的内容
javap有很多参数，我们先利用verbose命令查看。

```
%javap -v Test
```
得到内容如下：
```
Classfile /Users/chunhuizhao/javaworkspace/Test.class
  Last modified 2015-9-16; size 760 bytes
  MD5 checksum 63a76ec621716ebb1208654a38e2f63c
  Compiled from "Test.java"
public class Test
  minor version: 0
  major version: 52
  flags: ACC_PUBLIC, ACC_SUPER
Constant pool:
   #1 = Methodref          #11.#37        // java/lang/Object."<init>":()V
   #2 = Fieldref           #10.#38        // Test.id:I
   #3 = String             #39            // myname
   #4 = Fieldref           #10.#40        // Test.name:Ljava/lang/String;
   #5 = Class              #41            // java/util/ArrayList
   #6 = Methodref          #5.#37         // java/util/ArrayList."<init>":()V
   #7 = Fieldref           #10.#42        // Test.myList:Ljava/util/List;
   #8 = String             #43            // my desc
   #9 = Fieldref           #10.#44        // Test.desc:Ljava/lang/String;
  #10 = Class              #45            // Test
  #11 = Class              #46            // java/lang/Object
  #12 = Utf8               id
  #13 = Utf8               I
  #14 = Utf8               name
  #15 = Utf8               Ljava/lang/String;
  #16 = Utf8               desc
  #17 = Utf8               address
  #18 = Utf8               ConstantValue
  #19 = String             #47            // beijing
  #20 = Utf8               myList
  #21 = Utf8               Ljava/util/List;
  #22 = Utf8               Signature
  #23 = Utf8               Ljava/util/List<Ljava/lang/String;>;
  #24 = Utf8               <init>
  #25 = Utf8               ()V
  #26 = Utf8               Code
  #27 = Utf8               LineNumberTable
  #28 = Utf8               add
  #29 = Utf8               (II)I
  #30 = Utf8               getId
  #31 = Utf8               ()I
  #32 = Utf8               setId
  #33 = Utf8               (I)V
  #34 = Utf8               <clinit>
  #35 = Utf8               SourceFile
  #36 = Utf8               Test.java
  #37 = NameAndType        #24:#25        // "<init>":()V
  #38 = NameAndType        #12:#13        // id:I
  #39 = Utf8               myname
  #40 = NameAndType        #14:#15        // name:Ljava/lang/String;
  #41 = Utf8               java/util/ArrayList
  #42 = NameAndType        #20:#21        // myList:Ljava/util/List;
  #43 = Utf8               my desc
  #44 = NameAndType        #16:#15        // desc:Ljava/lang/String;
  #45 = Utf8               Test
  #46 = Utf8               java/lang/Object
  #47 = Utf8               beijing
{
  public java.lang.String name;
    descriptor: Ljava/lang/String;
    flags: ACC_PUBLIC

  public static java.lang.String desc;
    descriptor: Ljava/lang/String;
    flags: ACC_PUBLIC, ACC_STATIC

  public static final java.lang.String address;
    descriptor: Ljava/lang/String;
    flags: ACC_PUBLIC, ACC_STATIC, ACC_FINAL
    ConstantValue: String beijing

  public final java.util.List<java.lang.String> myList;
    descriptor: Ljava/util/List;
    flags: ACC_PUBLIC, ACC_FINAL
    Signature: #23                          // Ljava/util/List<Ljava/lang/String;>;

  public Test();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=3, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: aload_0
         5: iconst_0
         6: putfield      #2                  // Field id:I
         9: aload_0
        10: ldc           #3                  // String myname
        12: putfield      #4                  // Field name:Ljava/lang/String;
        15: aload_0
        16: new           #5                  // class java/util/ArrayList
        19: dup
        20: invokespecial #6                  // Method java/util/ArrayList."<init>":()V
        23: putfield      #7                  // Field myList:Ljava/util/List;
        26: return
      LineNumberTable:
        line 4: 0
        line 5: 4
        line 7: 9
        line 13: 15

  public int add(int, int);
    descriptor: (II)I
    flags: ACC_PUBLIC
    Code:
      stack=2, locals=3, args_size=3
         0: iload_1
         1: iload_2
         2: iadd
         3: ireturn
      LineNumberTable:
        line 16: 0

  public int getId();
    descriptor: ()I
    flags: ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: getfield      #2                  // Field id:I
         4: ireturn
      LineNumberTable:
        line 20: 0

  public void setId(int);
    descriptor: (I)V
    flags: ACC_PUBLIC
    Code:
      stack=2, locals=2, args_size=2
         0: aload_0
         1: iload_1
         2: putfield      #2                  // Field id:I
         5: return
      LineNumberTable:
        line 24: 0
        line 25: 5

  static {};
    descriptor: ()V
    flags: ACC_STATIC
    Code:
      stack=1, locals=0, args_size=0
         0: ldc           #8                  // String my desc
         2: putstatic     #9                  // Field desc:Ljava/lang/String;
         5: return
      LineNumberTable:
        line 9: 0
}
SourceFile: "Test.java"
```
### 分析
>可以看到，虽然我们的java代码不长，但是class文件内容和javap输出的内容比较长。现在结合上面提到的class文件内容，简单分析一下以上内容。

>1. 最上面提到的基本信息不提。
>2. 版本号是52，跟上面分析的0x34相同。
>3. ACC_PUBLIC  ACC_SUPER 表明访问权限，对应前面提到的0001和0020
>4. 常量池。前面提到过，常量池的内容是18项。

#### 魔数 magic number
头4个字节是magic number，即
```
cafe babe
```
javap输出的文件里面没有魔数。

#### 版本号 minor_version major_verion
在javap的输出和class文件里面都可以看到版本号信息。
javap都输出如下：
```
  minor version: 0
  major version: 52
```
可以看到大版本号是52，结合上面都表格，正好是jdk8

在class文件里面，版本号占4个字节
```
minor version 0000
major version 0034
0x34 = 52
```
#### 常量池 Constant pool
常量池的前2个字节是常量池的长度。
```
0030
0x30 = 48
```
前面提到过，常量池从1开始，因此常量池里面的内容应该是47项。再查看javap的输出，可以看出正好有47个常量。

因为常量池里面的内容比较多，现在只分析部分内容。


**第一个常量的内容如下：**
0030之后是0a
```
0a   //表示Methodref 后面还有4个字节
000b //表示指向第11个class_info
0025 //表示指向第37个NameAndType_info
```
再查看javap内容：
```
#1 = Methodref          #11.#37       
```
两个的分析是一样的。

**第二个常量的内容如下：**
class文件的内容
```
09  //表示Fieldref  后面还有4个字节
000a //表示指向第10个class_info
0026  //表示指向第38个NameAndType_info
```
因为常量池太长，我们不挨个翻译class文件了，查看javap得到的结果即可。

常量池常见的类型：
```
Methodref  方法的符号引用
Fieldref   字段的符号引用
String     字符串字面量
Class      类或接口的符号引用
Utf8       utf－8编码的字符串
NamdAndType 字段或方法的部分符号引用
```

#### 访问标志 access_flag
在常量池之后，有2个字节是访问标志
```
00001a0: 6374 0100 0762 6569 6a69 6e67 0021 000a
```
上面的0021表示访问标志。
```
0x0001|0x0020=0x0021
0x0001 //ACC_PUBLIC
0x0020 //ACC_SUPER
```
与javap的内容相同。

#### 类索引、父类索引与接口索引  this_class super_class interfaces
#### 字段表集合   fileds
#### 方法表集合   methods
#### 属性表集合

## 总结
>本文首先介绍了class文件的结构，之后又介绍了javap命令，最后，通过直接分析二进制文件和分析javap输出的方式，对class文件的结构进行了回顾。一方面对于我自己学习class文件结构是一个总结和提高；另外一方面也希望给不熟悉class结构的同学一些帮助。

## 参考资料
- [《Java虚拟机原理图解》 1.1、class文件基本组织结构](http://blog.csdn.net/luanlouis/article/details/39892027)
- [《Java虚拟机原理图解》 1.2.2、Class文件中的常量池详解（上）](http://blog.csdn.net/luanlouis/article/details/39960815)
- [《Java虚拟机原理图解》3、JVM运行时数据区](http://blog.csdn.net/luanlouis/article/details/40043991)
- [《Java虚拟机原理图解》 1.2、class文件中的常量池](http://blog.csdn.net/luanlouis/article/details/40148053)
- [《Java虚拟机原理图解》 1.2.3、Class文件中的常量池详解（下）](http://blog.csdn.net/luanlouis/article/details/40301985)
- [《Java虚拟机原理图解》1.3、class文件中的访问标志、类索引、父类索引、接口索引集合](http://blog.csdn.net/luanlouis/article/details/41039269)
- [《Java虚拟机原理图解》1.4 class文件中的字段表集合--field字段在class文件中是怎样组织的](http://blog.csdn.net/luanlouis/article/details/41046443)
- [《Java虚拟机原理图解》1.5、 class文件中的方法表集合--method方法在class文件中是怎样组织的](http://blog.csdn.net/luanlouis/article/details/41113695)
- [《深入理解java虚拟机》](http://book.douban.com/subject/24722612/)
- [Java字节码（.class文件）格式详解（一）](http://www.blogjava.net/DLevin/archive/2011/09/05/358033.html)
- [Java字节码（.class文件）格式详解（二）](http://www.blogjava.net/DLevin/archive/2011/09/05/358034.html)
- [Java字节码（.class文件）格式详解（三）](http://www.blogjava.net/DLevin/archive/2011/09/05/358035.html)
- [javap 学习日记~1](http://www.cnblogs.com/yanpeng/archive/2009/06/09/1943348.html)
- [Class文件内容及常量池](http://hxraid.iteye.com/blog/687660)
- [Java二进制指令代码解析](http://www.blogjava.net/DLevin/archive/2011/09/13/358497.html)
