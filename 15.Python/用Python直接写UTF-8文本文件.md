# 用Python直接写UTF-8文本文件

当我们这样建立文件时
```
f = file('x1.txt', 'w')
f.write(u'中文')
f.close()
```
直接结果应该是类似
```
f.write(u'中文')
UnicodeEncodeError: 'ascii' codec can't encode characters in position 0-16: ordinal not in range(128)
```
要直接写 utf-8 文件怎么办呢?
```
import codecs
f = codecs.open("pru_uni.txt", "w", "utf-8")
txt = unicode("campeón\n", "utf-8")
f.write(txt)
f.write(u'中文\n')
f.close()
```
