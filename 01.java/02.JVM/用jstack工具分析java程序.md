# 用jstack工具分析java程序


最近做项目时遇到了一个问题，我的多个采集线程中，有一个线程经常挂起，线程并没有死掉，但是一直采集不到数据，为了解决这个问题，用到了jstack。

首先查找到java进程的pid，ps -ef|grep java

然后输入jstack pid

核心输出为：

```
"MSG_RECEIVE_THREAD" prio=10 tid=0x00007fd95034b000 nid=0x2db5 runnable [0x00007fd9d0c26000]
   java.lang.Thread.State: RUNNABLE
    at java.net.SocketInputStream.socketRead0(Native Method)
    at java.net.SocketInputStream.read(SocketInputStream.java:152)
    at java.net.SocketInputStream.read(SocketInputStream.java:122)
    at com.ibm.mq.jmqi.remote.internal.RemoteTCPConnection.receive(RemoteTCPConnection.java:1343)
    at com.ibm.mq.jmqi.remote.internal.system.RemoteConnection.receiveTSH(RemoteConnection.java:2834)
    at com.ibm.mq.jmqi.remote.internal.system.RemoteConnection.initSess(RemoteConnection.java:1428)
    at com.ibm.mq.jmqi.remote.internal.system.RemoteConnection.connect(RemoteConnection.java:1106)
    - locked <0x00000000e7d97f18> (a com.ibm.mq.jmqi.remote.internal.system.RemoteConnection$ConnectionMutex)
    at com.ibm.mq.jmqi.remote.internal.system.RemoteConnectionPool.getConnection(RemoteConnectionPool.java:349)
    at com.ibm.mq.jmqi.remote.internal.RemoteFAP.jmqiConnect(RemoteFAP.java:1511)
    at com.ibm.mq.MQSESSION.MQCONNX_j(MQSESSION.java:915)
    at com.ibm.mq.MQManagedConnectionJ11.<init>(MQManagedConnectionJ11.java:226)
    at com.ibm.mq.MQClientManagedConnectionFactoryJ11._createManagedConnection(MQClientManagedConnectionFactoryJ11.java:505)
    at com.ibm.mq.MQClientManagedConnectionFactoryJ11.createManagedConnection(MQClientManagedConnectionFactoryJ11.java:547)
    at com.ibm.mq.StoredManagedConnection.<init>(StoredManagedConnection.java:95)
    at com.ibm.mq.MQSimpleConnectionManager.allocateConnection(MQSimpleConnectionManager.java:182)
    at com.ibm.mq.MQQueueManagerFactory.obtainBaseMQQueueManager(MQQueueManagerFactory.java:869)
    at com.ibm.mq.MQQueueManagerFactory.procure(MQQueueManagerFactory.java:761)
    at com.ibm.mq.MQQueueManagerFactory.constructQueueManager(MQQueueManagerFactory.java:712)
    at com.ibm.mq.MQQueueManagerFactory.createQueueManager(MQQueueManagerFactory.java:171)
    at com.ibm.mq.MQQueueManager.<init>(MQQueueManager.java:603)
    at com.wisdombud.unicom.monitor.listener.MessageByMQ.<init>(MessageByMQ.java:48)
    at com.wisdombud.unicom.monitor.listener.CollectMain$14.run(CollectMain.java:257)
    at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:471)
    at java.util.concurrent.FutureTask.runAndReset(FutureTask.java:304)
    at java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.access$301(ScheduledThreadPoolExecutor.java:178)
    at java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:293)
    at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145)
    at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)
    at java.lang.Thread.run(Thread.java:745)

```    
可以看到，线程是在运行的，不过挂在
```

java.net.SocketInputStream.socketRead0

```

通过这句话，可以找到问题的原因，因为IBM的某一个产品发生了问题。

如果没有jstack，就不知道应该如何处理了。

jstack的基本用法：

命令格式：

```
jstack [ option ] pid
基本参数：
-F当’jstack [-l] pid’没有相应的时候强制打印栈信息
-l长列表. 打印关于锁的附加信息,例如属于java.util.concurrent的ownable synchronizers列表.
-m打印java和native c/c++框架的所有栈信息.
-h | -help打印帮助信息
pid 需要被打印配置信息的java进程id,可以用jps查询.
```

具体用法
jstack -l 进程ID


另外，更多内容请参考：
http://www.blogjava.net/jzone/articles/303979.html
