# git学习笔记5-撤销操作


git撤销有四种情况，一是没有commit，想恢复所有文件；二是没有commit，想恢复全部文件；三是已经commit了，想恢复某个版本；四是本地已经提交了，想从远程库恢复。


## 1. 没有commit，在工作区，恢复所有文件

如果有多个文件被修改了，还没有commit，想恢复。在svn里面用```revert```就好了。
在git下面，可以用
```
git reset --hard HEAD
```
所有文件都恢复到原来的状态了。

## 2. 没有commit，在工作区，恢复一个文件
可以如果只是想恢复某一个文件怎么办呢？可以用下面这个命令：
```
git checkout HEAD <file>
```

## 3. 已经提交到Stage，想恢复某一个版本
```
git revert <commit>
```
那怎么看某一个commit呢？
```
git log
```

## 4. 本地已经commit了，想从远程库上恢复
这种情况完全类似svn的```revert```	命令，用以下命令即可:
```
git reset --hard origin/master
```
上面的master也可以改为branchxxx
