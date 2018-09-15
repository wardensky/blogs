#git学习笔记7-branch


 
## 前言
本文是git学习笔记的第7篇文章，本文主要讲git里面的branch（分支），重点回答以下问题。

1. 分支是什么？
2. 如何打分支？
3. 怎么看已有的分支
4. 如何删除分支？
4. 怎么切换到某一个分支上？
5. 怎么合并分支？
4. 如何把分支推送到远程服务器？
4. 如何根据某一个分支下载版本？

参考
[Git 分支 - 何谓分支](https://git-scm.com/book/zh/v1/Git-分支-何谓分支)
[创建与合并分支](https://www.liaoxuefeng.com/wiki/0013739516305929606dd18361248578c67b8067c8c017b000/001375840038939c291467cc7c747b1810aab2fb8863508000)
## 分支是什么？
在svn或者cvs里面，分支更多的是把所有的内容都复制一份，不管是在服务器上还是本地。然而，在git里面不是这样处理的，他只是一个指针的变化。这样处理，会让git比svn创建分支快很多，也是为什么git里面鼓励多创建分支。
 下面几幅图，形象的说明了git里面的分支。

![Alt text](./18333fig0304-tn.png)
<span style="text-align:center;">图1</span>
上图中，创建了一个testing的分支（先不要管创建的命令是什么）。

![Alt text](./18333fig0305-tn.png)
图2
上图中，可以看出来HEAD是指向master的.

![Alt text](./18333fig0306-tn.png)
图3
上图中，已经把HEAD指向了分支testing
![Alt text](./18333fig0307-tn.png)
图4
上图中，分支testing继续向前演进，HEAD继续指向testing，master则停在原地不动。

![Alt text](./18333fig0308-tn.png)
图5
上图中，切换回master分支。

![Alt text](./18333fig0309-tn.png)
图6
上图中，master分支继续向前演进。只需要合并就可以了。

![Alt text](./0.png)
图7
上图中，把分支合并到了master里面。

图采用了不同博客里面的，所以风格不同，但就是这个意思。

看这些图把分支搞明白了，打分支就是一些命令而已了。

## 如何打分支？
```
git branch b1
```


## 怎么看已有的分支
```
git branch
```
信息如下：
```
➜  hhtd_root git:(master) git branch
  b
  b1
 * master
```
星号的意思是HEAD在master上面

```
>➜  hhtd_root git:(b) git branch
 * b
  master
```
这个在branch b上面
## 如何删除分支？
```
git branch -d b1
```
## 怎么切换到某一个分支上
```
git checkout b1
```

## 怎么合并分支？
```
git merge b1
```
如果在master上面，执行上面的命令，意思是把b1合并到master上面。


## 如何查看远端服务器上所有的分支
```
git remote show origin
```
我的输出如下：
```
* remote origin
  Fetch URL: ssh://git@tunnel.wisdomhr.cn:16524/home/git/hhtd.git
  Push  URL: ssh://git@tunnel.wisdomhr.cn:16524/home/git/hhtd.git
  HEAD branch: master
  Remote branches:
    b       tracked
    master  tracked
    pay_dev tracked
  Local branch configured for 'git pull':
    master merges with remote master
  Local refs configured for 'git push':
    b      pushes to b      (up to date)
    master pushes to master (up to date)
```

## 如何根据某一个分支下载版本？
```
git checkout -b pay_dev origin/pay_dev
```
从远端服务器上下拉pay_dev版本到本地pay_dev版本，并把本地分支切换到pay_dev。
我的输出如下：
```
Branch pay_dev set up to track remote branch pay_dev from origin.
Switched to a new branch 'pay_dev'
```

## 如何把分支推送到远程服务器？


```
git push origin  test
```

## 如何删除远程分支
```
git push origin :spider_dev
```
注意，origin后面有一个空格
