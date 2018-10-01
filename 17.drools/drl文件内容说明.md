# drl文件内容说明

```
package rules
import com.neo.drools.entity.Order
rule "zero"
    no-loop true
    lock-on-active true
    salience 1
    when
        $s : Order(amout <= 100)
    then
        $s.setScore(0);
        update($s);
end
rule "add100"
    no-looptrue
    lock-on-active true
    salience 1
    when
        $s : Order(amout > 100 && amout <= 500)
    then
        $s.setScore(100);
        update($s);
end
rule "add500"
    no-looptrue
    lock-on-active true
    salience 1
    when
        $s : Order(amout > 500 && amout <= 1000)
    then
        $s.setScore(500);
        update($s);
end
rule "add1000"
    no-looptrue
    lock-on-active true
    salience 1
    when
        $s : Order(amout > 1000)
    then
        $s.setScore(1000);
        update($s);
end
```

* package 与Java语言类似，drl的头部需要有package和import的声明,package不必和物理路径一致。
* import 导出java Bean的完整路径，也可以将Java静态方法导入调用。
* rule 规则名称，需要保持唯一性，可以无限次执行。
* no-loop 定义当前的规则是否不允许多次循环执行，默认是 false，也就是当前的规则只要满足条件,可以无限次执行。
* lock-on-active 将lock-on-active属性的值设置为true，可避免因某些Fact对象被修改而使已经执行过的规则再次被激活执行。
* salience 用来设置规则执行的优先级，salience 属性的值是一个数字,数字越大执行优先级越高, 同时它的值可以是一个负数。默认情况下，规则的 salience 默认值为 0。如果不设置规则的 salience 属性,那么执行顺序是随机的。
* when 条件语句，就是当到达什么条件的时候
* then 根据条件的结果，来执行什么动作
* end 规则结束
