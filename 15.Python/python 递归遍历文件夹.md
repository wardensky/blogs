# python 递归遍历文件夹


```
#!/usr/bin/python

import os.path


def readXmls(folder):
    #三个参数：分别返回1.父目录 2.所有文件夹名字（不含路径） 3.所有文件名字
    for parent,dirnames,filenames in os.walk(folder):    
        for dirname in  dirnames:                     
            print("parent is:" + parent)
            print("dirname is" + dirname)            
            #readXmls(os.path.join(parent, dirname))这句不能有！！！
        for filename in filenames:                      
            print("parent is:" + parent)
            print("filename is:" + filename)
            print("the full name of the file is:" + os.path.join(parent,filename)) #输出文件路径信息


if __name__ == '__main__':
    readXmls('xmlfiles')



```

－－－－－－备注－－－－－
os.walk方法本来就是递归，调试了很久也没搞明白……
