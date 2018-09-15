# MongoDB入门（1）- MongoDB简介
@(编程)

## 什么是MongoDB
**NoSQL**

>NoSQL systems are also sometimes called "Not only SQL" to emphasize that they may support SQL-like query languages.

**MongoDB**
>MongoDB是一个介于关系数据库和非关系数据库之间的产品，是非关系数据库当中功能最丰富，最像关系数据库的。他支持的数据结构非常松散，是类似json的bjson格式，因此可以存储比较复杂的数据类型。Mongo最大的特点是他支持的查询语言非常强大，其语法有点类似于面向对象的查询语言，几乎可以实现类似关系数据库单表查询的绝大部分功能，而且还支持对数据建立索引。

官网：https://www.mongodb.com/
协议：AGPL

## MongoDB有啥好处
**网上查的**
它的特点是高性能、易部署、易使用，存储数据非常方便。主要功能特性有：
- 面向集合存储，易存储对象类型的数据。
- 模式自由。
- 支持动态查询。
- 支持完全索引，包含内部对象。
- 支持查询。
- 支持复制和故障恢复。
- 使用高效的二进制数据存储，包括大型对象（如视频等）。
- 自动处理碎片，以支持云计算层次的扩展性
- 支持RUBY，PYTHON，JAVA，C++，PHP等多种语言。
- 文件存储格式为BSON（一种JSON的扩展）
- 可通过网络访问

**对我们来说**
- 开发方便，不用ORM
- 有GridFS


##什么时候用MongoDB
- 不需要事务的时候都可以用
- 需要快速开发的时候
- 千万不要用32位的

##MongoDB存储内容
MongoDB存储的是类似json的内容，如下：
```
/* 1 */
{
    "_id" : ObjectId("5609f6e70c151c1354461d8b"),
    "name" : "che",
    "mail" : "cheyant@qq.com",
    "selfIntroduction" : "11212",
    "password" : "8520eb891c5c7a123ab99dfa4b8795b7",
    "headpicPath" : "attached\\entrepreneurHeadPortrait\\2015-11-30\\1448866716800_QQ截图20150721084547.png",
    "headpicName" : "QQ截图20150721084547.png",
    "company" : "1",
    "companyPosition" : "12",
    "idea" : "121212",
    "skill" : "56456465654",
    "focusArea" : [
        "移动互联网",
        "汽车交通",
        "旅游"
    ],
    "focusAreaStr" : "移动互联网, 汽车交通, 旅游",
    "motto" : "",
    "telephone" : "13555665566",
    "sex" : 0,
    "viewCount" : 0,
    "sort" : 0
}

/* 2 */
{
    "_id" : ObjectId("560a0b7e0c15cb52f24695b4"),
    "name" : "cheyantao",
    "mail" : "cheyanto@foxmail.com",
    "selfIntroduction" : "大的肥大的法定的",
    "password" : "7cfc8e877bafa013db4de20d94f59ad2",
    "company" : "慧萌",
    "companyPosition" : "你猜",
    "idea" : "阿萨德飞阿萨德飞啊挨打",
    "skill" : "玩,玩,玩,",
    "focusAreaStr" : "移动互联网, 互联网金融, 汽车交通, 新兴技术",
    "motto" : "",
    "telephone" : "18233138035",
    "sex" : 0,
    "audit" : 1,
    "viewCount" : 0,
    "sort" : 0
}
```
