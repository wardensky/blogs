# MySQL乐观锁与悲观锁



## 悲观锁

悲观锁（Pessimistic Lock），顾名思义，就是很悲观，每次去拿数据的时候都认为别人会修改，所以每次在拿数据的时候都会上锁，这样别人想拿这个数据就会block直到它拿到锁。

悲观锁：假定会发生并发冲突，屏蔽一切可能违反数据完整性的操作。

Java synchronized 就属于悲观锁的一种实现，每次线程要修改数据时都先获得锁，保证同一时刻只有一个线程能操作数据，其他线程则会被block。

## 乐观锁

乐观锁（Optimistic Lock），顾名思义，就是很乐观，每次去拿数据的时候都认为别人不会修改，所以不会上锁，但是在提交更新的时候会判断一下在此期间别人有没有去更新这个数据。乐观锁适用于读多写少的应用场景，这样可以提高吞吐量。

乐观锁：假设不会发生并发冲突，只在提交操作时检查是否违反数据完整性。

乐观锁一般来说有以下2种方式：

- 使用数据版本（Version）记录机制实现，这是乐观锁最常用的一种实现方式。何谓数据版本？即为数据增加一个版本标识，一般是通过为数据库表增加一个数字类型的 “version” 字段来实现。当读取数据时，将version字段的值一同读出，数据每更新一次，对此version值加一。当我们提交更新的时候，判断数据库表对应记录的当前版本信息与第一次取出来的version值进行比对，如果数据库表当前版本号与第一次取出来的version值相等，则予以更新，否则认为是过期数据。
- 使用时间戳（timestamp）。乐观锁定的第二种实现方式和第一种差不多，同样是在需要乐观锁控制的table中增加一个字段，名称无所谓，字段类型使用时间戳（timestamp）, 和上面的version类似，也是在更新提交的时候检查当前数据库中数据的时间戳和自己更新前取到的时间戳进行对比，如果一致则OK，否则就是版本冲突。

Java JUC中的atomic包就是乐观锁的一种实现，AtomicInteger 通过CAS（Compare And Set）操作实现线程安全的自增。

## MySQL隐式和显示锁定

MySQL InnoDB采用的是两阶段锁定协议（two-phase locking protocol）。在事务执行过程中，随时都可以执行锁定，锁只有在执行 COMMIT或者ROLLBACK的时候才会释放，并且所有的锁是在同一时刻被释放。前面描述的锁定都是隐式锁定，InnoDB会根据事务隔离级别在需要的时候自动加锁。

另外，InnoDB也支持通过特定的语句进行显示锁定，这些语句不属于SQL规范：

- SELECT ... LOCK IN SHARE MODE
- SELECT ... FOR UPDATE

## 实战

接下来，我们通过一个具体案例来进行分析：考虑电商系统中的下单流程，商品的库存量是固定的，如何保证商品数量不超卖？ 其实需要保证数据一致性：某个人点击秒杀后系统中查出来的库存量和实际扣减库存时库存量的一致性就可以。

假设，MySQL数据库中商品库存表tb_product_stock 结构定义如下：
```
CREATE TABLE `tb_product_stock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `product_id` bigint(32) NOT NULL COMMENT '商品ID',
  `number` INT(8) NOT NULL DEFAULT 0 COMMENT '库存数量',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `modify_time` DATETIME NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_pid` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品库存表';
```

对应的POJO类：
```
class ProductStock {
    private Long productId; //商品id
    private Integer number; //库存量

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
```
不考虑并发的情况下，更新库存代码如下：

```
    /**
     * 更新库存(不考虑并发)
     * @param productId
     * @return
     */
    public boolean updateStockRaw(Long productId){
        ProductStock product = query("SELECT * FROM tb_product_stock WHERE product_id=#{productId}", productId);
        if (product.getNumber() > 0) {
            int updateCnt = update("UPDATE tb_product_stock SET number=number-1 WHERE product_id=#{productId}", productId);
            if(updateCnt > 0){    //更新库存成功
                return true;
            }
        }
        return false;
    }

```    
多线程并发情况下，会存在超卖的可能。

### 悲观锁

```
/**
     * 更新库存(使用悲观锁)
     * @param productId
     * @return
     */
    public boolean updateStock(Long productId){
        //先锁定商品库存记录
        ProductStock product = query("SELECT * FROM tb_product_stock WHERE product_id=#{productId} FOR UPDATE", productId);
        if (product.getNumber() > 0) {
            int updateCnt = update("UPDATE tb_product_stock SET number=number-1 WHERE product_id=#{productId}", productId);
            if(updateCnt > 0){    //更新库存成功
                return true;
            }
        }
        return false;
    }
```

### 乐观锁
```
    /**
     * 下单减库存
     * @param productId
     * @return
     */
    public boolean updateStock(Long productId){
        int updateCnt = 0;
        while (updateCnt == 0) {
            ProductStock product = query("SELECT * FROM tb_product_stock WHERE product_id=#{productId}", productId);
            if (product.getNumber() > 0) {
                updateCnt = update("UPDATE tb_product_stock SET number=number-1 WHERE product_id=#{productId} AND number=#{number}", productId, product.getNumber());
                if(updateCnt > 0){    //更新库存成功
                    return true;
                }
            } else {    //卖完啦
                return false;
            }
        }
        return false;
    }
```    

使用乐观锁更新库存的时候不加锁，当提交更新时需要判断数据是否已经被修改（AND number=#{number}），只有在 number等于上一次查询到的number时 才提交更新。

** 注意** ：UPDATE 语句的WHERE 条件字句上需要建索引


## mysql事务，select for update，及数据的一致性处理

在MySQL的InnoDB中，预设的Tansaction isolation level 为REPEATABLE READ（可重读）

在SELECT 的读取锁定主要分为两种方式：
```
　　SELECT ... LOCK IN SHARE MODE　

　　SELECT ... FOR UPDATE
```
　　这两种方式在事务(Transaction) 进行当中SELECT 到同一个数据表时，都必须等待其它事务数据被提交(Commit)后才会执行。

　　而主要的不同在于LOCK IN SHARE MODE 在有一方事务要Update 同一个表单时很容易造成死锁。

　　简单的说，如果SELECT 后面若要UPDATE 同一个表单，最好使用SELECT ... UPDATE。

　　举个例子:

　　假设商品表单products 内有一个存放商品数量的quantity ，在订单成立之前必须先确定quantity 商品数量是否足够(quantity>0) ，然后才把数量更新为1。代码如下:
```
SELECT quantity FROM products WHERE id=3;
UPDATE products SET quantity = 1 WHERE id=3;
```

为什么不安全呢?
少量的状况下或许不会有问题，但是大量的数据存取「铁定」会出问题。如果我们需要在quantity>0 的情况下才能扣库存，假设程序在第一行SELECT 读到的quantity 是2 ，看起来数字没有错，但
是当MySQL 正准备要UPDATE 的时候，可能已经有人把库存扣成0 了，但是程序却浑然不知，将错就错的UPDATE 下去了。因此必须透过的事务机制来确保读取及提交的数据都是正确的。



于是我们在MySQL 就可以这样测试，代码如下:
```
SET AUTOCOMMIT=0;
BEGIN WORK;
SELECT quantity FROM products WHERE id=3 FOR UPDATE;
```
此时products 数据中id=3 的数据被锁住(注3)，其它事务必须等待此次事务 提交后才能执行
```
SELECT * FROM products WHERE id=3 FOR UPDATE
```
如此可以确保quantity 在别的事务读到的数字是正确的。
```
UPDATE products SET quantity = '1' WHERE id=3 ; COMMIT WORK;
```
提交(Commit)写入数据库，products 解锁。

- 注1: BEGIN/COMMIT 为事务的起始及结束点，可使用二个以上的MySQL Command 视窗来交互观察锁定的状况。
- 注2: 在事务进行当中，只有SELECT ... FOR UPDATE 或LOCK IN SHARE MODE 同一笔数据时会等待其它事务结束后才执行，一般SELECT ... 则不受此影响。
- 注3: 由于InnoDB 预设为Row-level Lock，数据列的锁定可参考这篇。
- 注4: InnoDB 表单尽量不要使用LOCK TABLES 指令，若情非得已要使用，请先看官方对于InnoDB 使用LOCK TABLES 的说明，以免造成系统经常发生死锁。





### MySQL SELECT ... FOR UPDATE 的Row Lock 与Table Lock

上面介绍过SELECT ... FOR UPDATE 的用法，不过锁定(Lock)的数据是判别就得要注意一下了。由于InnoDB 预设是Row-Level Lock，所以只有「明确」的指定主键，MySQL 才会执行Row lock (只锁住被选取的数据) ，否则MySQL 将会执行Table Lock (将整个数据表单给锁住)。

举个例子:
假设有个表单products ，里面有id 跟name 二个栏位，id 是主键。
例1: (明确指定主键，并且有此数据，row lock)
```
SELECT * FROM products WHERE id='3' FOR UPDATE;
```
例2: (明确指定主键，若查无此数据，无lock)
```
SELECT * FROM products WHERE id='-1' FOR UPDATE;
```
例3: (无主键，table lock)
```
SELECT * FROM products WHERE name='Mouse' FOR UPDATE;
```

例4: (主键不明确，table lock)
```
SELECT * FROM products WHERE id<>'3' FOR UPDATE;
```
例5: (主键不明确，table lock)
```
SELECT * FROM products WHERE id LIKE '3' FOR UPDATE;
```




## 乐观锁与悲观锁的区别

乐观锁的思路一般是表中增加版本字段，更新时where语句中增加版本的判断，算是一种CAS（Compare And Swep）操作，商品库存场景中number起到了版本控制（相当于version）的作用（ AND number=#{number}）。

悲观锁之所以是悲观，在于他认为本次操作会发生并发冲突，所以一开始就对商品加上锁（SELECT ... FOR UPDATE），然后就可以安心的做判断和更新，因为这时候不会有别人更新这条商品库存。



## 参考

- [MySQL 乐观锁与悲观锁](https://www.jianshu.com/p/f5ff017db62a)
