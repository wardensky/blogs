# jps用法


jps(Java Virtual Machine Process Status Tool)是JDK 1.5提供的一个显示当前所有java进程pid的命令，简单实用，非常适合在linux/unix平台上简单察看当前java进程的一些简单情况。其用法类似ps命令。
其命令格式为：

```
jps [option] [hostid]
```
常见用法：

```
jps
```
在我的电脑上，输出为：

```
IvydeMacBook-Air% jps
6979 Main
7077 Jps
6973
```
其常见参数包括：

```
-m 输出传递给main 方法的参数，在嵌入式jvm上可能是null

$> jps -m
28715 Jps -m
23789 BossMain
23651 Resin -socketwait 32768 -stdout /data/aoxj/resin/log/stdout.log -stderr /data/aoxj/resin/log/stderr.log

-l 输出应用程序main class的完整package名 或者 应用程序的jar文件完整路径名

$> jps -l
28729 sun.tools.jps.Jps
23789 com.asiainfo.aimc.bossbi.BossMain
23651 com.caucho.server.resin.Resin

-v 输出传递给JVM的参数

$> jps -v
23789 BossMain
28802 Jps -Denv.class.path=/data/aoxj/bossbi/twsecurity/java/trustwork140.jar:/data/aoxj/bossbi/twsecurity/java/:/data/aoxj/bossbi/twsecurity/java/twcmcc.jar:/data/aoxj/jdk15/lib/rt.jar:/data/aoxj/jd

k15/lib/tools.jar -Dapplication.home=/data/aoxj/jdk15 -Xms8m
23651 Resin -Xss1m -Dresin.home=/data/aoxj/resin -Dserver.root=/data/aoxj/resin -Djava.util.logging.manager=com.caucho.log.LogManagerImpl -

Djavax.management.builder.initial=com.caucho.jmx.MBeanServerBuilderImpl
```
