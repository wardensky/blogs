# mac多版本python安装 pymysql

系统里面安装了多个python的版本，有2.7和3.4等。默认的2.7版本，但我开发需要3.4版本的。

默认情况下，用pip安装PyMySQL
```
$sudo pip install PyMySQL
```
安装之后，在命令行里面测试
```
>>>import pymysql
```
如果没有报错，则表示安装成功。

但是，这个是在2.7版本下安装成功的。跑到eclipse里面去敲代码，还提示错误。只好到3.4版本下再下载一个。

找到3.4到安装路径，我的是
```
/Library/Frameworks/Python.framework/Versions/3.4/bin
```
这个目录下有个pip3.4，运行以下命令，安装pymysql
```
./pip3.4 install PyMySQL
```
安装成功之后，在python3.4版本下就可以应用pymysql了。
