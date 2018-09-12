# jinfo用法


jinfo（Configuration Info for Java）的作用是实时地查看和调整虚拟机各项参数。使用jps命令的-v参数可以查看虚拟机启动时显式指定的参数列表，但如果想知道未被显式指定的参数的系统默认值，除了去找资料外，就只能使用jinfo的-flag选项进行查询了。


>jinfo prints Java configuration information for a given Java process or core file  or  a  remote  debug  server.   Configuration information includes Java System properties and Java virtual machine command line flags.

jinfo的语法

```
jinfo [ option ] pid
jinfo [ option ] executable core
jinfo [ option ] [ server-id@ ] remote-hostname-or-IP
```

参数列表如下：

```
<no option>   prints both command line flags as well as System properties name, value pairs

-flags         prints command line flags as name, value pairs

-sysprops      prints JavaSystem properties as name, value pairs

-h             prints a help message

-help          prints a help message
```

如果不输入任何参数，在我的电脑上得到的是这样的结果。

```
Java System Properties:

java.runtime.name = Java(TM) SE Runtime Environment
java.vm.version = 24.65-b04
sun.boot.library.path = /Library/Java/JavaVirtualMachines/jdk1.7.0_67.jdk/Contents/Home/jre/lib
gopherProxySet = false
java.vendor.url = http://java.oracle.com/
java.vm.vendor = Oracle Corporation
path.separator = :
file.encoding.pkg = sun.io
java.vm.name = Java HotSpot(TM) 64-Bit Server VM
sun.os.patch.level = unknown
sun.java.launcher = SUN_STANDARD
user.country = CN
user.dir = /Users/chunhuizhao/Documents/workspace/test/com.chzhao.test
java.vm.specification.name = Java Virtual Machine Specification
java.runtime.version = 1.7.0_67-b01
java.awt.graphicsenv = sun.awt.CGraphicsEnvironment
os.arch = x86_64
java.endorsed.dirs = /Library/Java/JavaVirtualMachines/jdk1.7.0_67.jdk/Contents/Home/jre/lib/endorsed
line.separator =

java.io.tmpdir = /var/folders/7r/9vgl0jjs7r3c4rrb2lczq5qw0000gp/T/
java.vm.specification.vendor = Oracle Corporation
os.name = Mac OS X
sun.jnu.encoding = UTF-8
java.library.path = /Users/chunhuizhao/Library/Java/Extensions:/Library/Java/Extensions:/Network/Library/Java/Extensions:/System/Library/Java/Extensions:/usr/lib/java:.
java.specification.name = Java Platform API Specification
java.class.version = 51.0
sun.management.compiler = HotSpot 64-Bit Tiered Compilers
os.version = 10.10.2
http.nonProxyHosts = local„ÄÅ169.254/16|*.local„ÄÅ169.254/16
user.home = /Users/chunhuizhao
user.timezone =
java.awt.printerjob = sun.lwawt.macosx.CPrinterJob
file.encoding = UTF-8
java.specification.version = 1.7
user.name = chunhuizhao
java.class.path = /Users/chunhuizhao/Documents/workspace/test/com.chzhao.test/bin
java.vm.specification.version = 1.7
sun.arch.data.model = 64
sun.java.command = com.chzhao.jvm.test.Main
java.home = /Library/Java/JavaVirtualMachines/jdk1.7.0_67.jdk/Contents/Home/jre
user.language = zh
java.specification.vendor = Oracle Corporation
awt.toolkit = sun.lwawt.macosx.LWCToolkit
java.vm.info = mixed mode
java.version = 1.7.0_67
java.ext.dirs = /Users/chunhuizhao/Library/Java/Extensions:/Library/Java/JavaVirtualMachines/jdk1.7.0_67.jdk/Contents/Home/jre/lib/ext:/Library/Java/Extensions:/Network/Library/Java/Extensions:/System/Library/Java/Extensions:/usr/lib/java
sun.boot.class.path = /Library/Java/JavaVirtualMachines/jdk1.7.0_67.jdk/Contents/Home/jre/lib/resources.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_67.jdk/Contents/Home/jre/lib/rt.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_67.jdk/Contents/Home/jre/lib/sunrsasign.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_67.jdk/Contents/Home/jre/lib/jsse.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_67.jdk/Contents/Home/jre/lib/jce.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_67.jdk/Contents/Home/jre/lib/charsets.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_67.jdk/Contents/Home/jre/lib/jfr.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_67.jdk/Contents/Home/jre/classes
java.vendor = Oracle Corporation
file.separator = /
java.vendor.url.bug = http://bugreport.sun.com/bugreport/
sun.io.unicode.encoding = UnicodeBig
sun.cpu.endian = little
socksNonProxyHosts = local„ÄÅ169.254/16|*.local„ÄÅ169.254/16
ftp.nonProxyHosts = local„ÄÅ169.254/16|*.local„ÄÅ169.254/16
sun.cpu.isalist =

VM Flags:

-ea -Dfile.encoding=UTF-8


```
