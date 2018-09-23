# 对pymysql的简单封装

```
#coding=utf-8
#!/usr/bin/python

import pymysql


class MYSQL:
    """
    对pymysql的简单封装
    """
    def __init__(self,host,user,pwd,db):
        self.host = host
        self.user = user
        self.pwd = pwd
        self.db = db

    def __GetConnect(self):
        """
        得到连接信息
        返回: conn.cursor()
        """
        if not self.db:
            raise(NameError,"没有设置数据库信息")
        self.conn = pymysql.connect(host=self.host,user=self.user,password=self.pwd,database=self.db,charset="utf8")
        cur = self.conn.cursor()
        if not cur:
            raise(NameError,"连接数据库失败")
        else:
            return cur

    def ExecQuery(self,sql):
        """
        执行查询语句
        返回的是一个包含tuple的list，list的元素是记录行，tuple的元素是每行记录的字段

        调用示例：
                ms = MYSQL(host="localhost",user="sa",pwd="123456",db="PythonWeiboStatistics")
                resList = ms.ExecQuery("SELECT id,NickName FROM WeiBoUser")
                for (id,NickName) in resList:
                    print str(id),NickName
        """
        cur = self.__GetConnect()
        cur.execute(sql)
        resList = cur.fetchall()

        #查询完毕后必须关闭连接
        self.conn.close()
        return resList

    def ExecNonQuery(self,sql):
        """
        执行非查询语句

        调用示例：
            cur = self.__GetConnect()
            cur.execute(sql)
            self.conn.commit()
            self.conn.close()
        """
        cur = self.__GetConnect()
        cur.execute(sql)
        self.conn.commit()
        self.conn.close()

def main():

    mysql = MYSQL(host="192.168.163.36",user="wisdomhr",pwd="wisdomhr",db="WISDOMHR")
    resList = mysql.ExecQuery("SELECT CITY FROM RES_SCHOOL")
    for inst in resList:
        print(inst)
if __name__ == '__main__':
    main()
```

用法如下:

```
#!/usr/bin/python

#version 3.4

import wispymysql

mysql = wispymysql.MYSQL(host="192.168.163.36",user="wisdomhr",pwd="wisdomhr",db="WISDOMHR")
selectsql = "SELECT ID, CITY FROM RES_SCHOOL WHERE CITY LIKE '%\r\n%'"
result = mysql.ExecQuery(selectsql)

for (dbid, city) in result:
    rightcity = city.replace('\r\n','')
    updatesql= "UPDATE RES_SCHOOL SET CITY = '" + rightcity + "' WHERE ID = " + str(dbid)
    print(updatesql)
    mysql.ExecNonQuery(updatesql)


```
