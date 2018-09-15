# Redis 中文存储乱码问题



在Redis 中存储中文，读取会出现乱码（其实不是乱码，只是不是我们存的中文显示）

```
Redis> set test "我们"  
OK  
Redis> get test  
"\xe6\x88\x91\xe4\xbb\xac"
```

如何在get时取到它的中文呢？只需要在Redis-cli 后面加上 –raw

R```
Redis-cli --raw
Redis> get test  
"我们"  
```
