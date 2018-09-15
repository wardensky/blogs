#mysql的substring函数
 

类似java里面的substring函数，mysql里面用的是```LEFT```和```RIGHT```。
感觉这两个函数更友好啊。


##LEFT（）
```
LEFT（ string, length）
```
>该 函数 返回 字符串 的 前 几个 字符。 如果 你想 提取 字符串 的 末尾 部分 而 不是 开头 部分， 请使用 RIGHT（） 函数。 它们 是 多 字节 安全（ multibyte- safe） 的 函数。

如下 面的 示例 所示：
```
mysql> select left(code,4) from city limit 0, 1;
+--------------+
| left(code,4) |
+--------------+
| 1100         |
+--------------+
1 row in set (0.00 sec)
```

RIGHT同理。

##SUBSTRING
```
substring（str, pos）
substring（str, pos, length）
```
例子

```
select substring（content,5） as abstract from my_content_t
select substring（content,5,200） as abstract from my_content_t
```
