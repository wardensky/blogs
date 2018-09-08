# jstat用法


jstat（JVM Statistics Monitoring Tool）是用于监视虚拟机各种运行状态信息的命令行工具。它可以显示本地或者远程虚拟机进程中的类装载、内存、垃圾收集、JIT编译等运行数据，在没有GUI图形界面，只提供了纯文本控制台环境的服务器上，它将是运行期定位虚拟机性能问题的首选工具。
jstat命令格式为：

```
jstat [ option vmid [interval[s|ms] [count]] ]
```

对于命令格式中的VMID与LVMID需要特别说明一下：如果是本地虚拟机进程，VMID与LVMID是一致的，如果是远程虚拟机进程，那VMID的格式应当是：

```
[protocol:][//]lvmid[@hostname[:port]/servername]
```

参数interval和count代表查询间隔和次数，如果省略这两个参数，说明只查询一次。假设需要每250毫秒查询一次进程2764垃圾收集状况，一共查询20次，那命令应当是：

```
jstat -gc 2764 250 20
```

选项option代表着用户希望查询的虚拟机信息，主要分为3类：类装载、垃圾收集、运行期编译状况，其参数如下：

Option|Displays...
:---------------|:---------------
class            | Statistics on the behavior of the class loader       
compiler | Statistics on the behavior of the  HotSpot  Just-In-Time compiler                                        
gc | Statistics  on the behavior of the garbage collected heap    
gccapacity | Statistics of the capacities of the generations  and their corresponding spaces.     
gccause| Summary  of  garbage  collection statistics (same as -gcutil), with the cause of the last and current (if applicable) garbage collection events.   
gcnew | Statistics of the behavior of the new generation.    
gcnewcapacity | Statistics  of  the sizes of the new generations and its corresponding spaces.  
gcold |Statistics of the behavior of the old and  permanent generations.  
gcoldcapacity|  Statistics of the sizes of the old generation.
gcpermcapacity| Statistics of the sizes of the permanent generation.
gcutil | Summary of garbage collection statistics.
printcompilation | Summary of garbage collection statistics.          

jstat执行样例：

```
IvydeMacBook-Air% jstat -gc 536
 S0C    S1C    S0U    S1U      EC       EU        OC         OU       PC     PU    YGC     YGCT    FGC    FGCT     GCT   
2560.0 2560.0  0.0    0.0   16896.0   5070.7   43520.0      0.0     21504.0 2901.1      0    0.000   0      0.000    0.000
```

显示|说明
:---------------|:---------------
S0C|Current survivor space 0 capacity(KB).
S1C|Current survivor space 1 capacity(KB).
S0U|Survivor space 0 utilization(KB).
S1U|Survivor space 1 utilization(KB).
EC|Current eden space capacity(KB).
EU|Eden space utilization(KB).
OC|Current oldpace capacity(KB).
OU|Old space utilization(KB).
PC|Current permanet space capacity(KB).
PU|Permanent space utilization(KB).
YGC|Number of young generation GC Events.
YGCT|Young generation garbage collection time.
FGC|Number of full GC events
FGCT|Full garbage collection time.
GCT|Total garbage collection time.
