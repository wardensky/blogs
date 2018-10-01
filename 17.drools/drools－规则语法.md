# drools－规则语法

## 文章结构
1. 基础api
2. FACT对象
3. 规则
4. 函数

## 1. 基础api

在 Drools 当中，规则的编译与运行要通过Drools 提供的各种API 来实现，这些API 总体来讲可以分为三类：规则编译、规则收集和规则的执行。完成这些工作的API 主要有KnowledgeBuilder、KnowledgeBase、StatefulKnowledgeSession、StatelessKnowledgeSession、、等，它们起到了对规则文件进行收集、编译、查错、插入fact、设置global、执行规则或规则流等作用

### 1.1 KnowledgeBuilder

KnowledgeBuilder 在业务代码当中收集已经编写好的规则， 然后对这些规则文件进行编译， 最终产生一批编译好的规则包（KnowledgePackage）给其它的应用程序使用。KnowledgeBuilder 在编译规则的时候可以通过其提供的hasErrors()方法得到编译规则过程中发现规则是否有错误，如果有的话通过其提供的getErrors()方法将错误打印出来，以帮助我们找到规则当中的错误信息。



### 1.2 KnowledgeBase

KnowledgeBase 是 Drools 提供的用来收集应用当中知识（knowledge）定义的知识库对象，在一个KnowledgeBase 当中可以包含普通的规则（rule）、规则流(rule flow)、函数定义(function)、用户自定义对象（type model）等。KnowledgeBase 本身不包含任何业务数据对象（fact 对象，后面有相应章节着重介绍fact 对象），业务对象都是插入到由KnowledgeBase产生的两种类型的session 对象当中（StatefulKnowledgeSession 和StatelessKnowledgeSession，后面会有对应的章节对这两种类型的对象进行介绍），通过session 对象可以触发规则执行或开始一个规则流执行。



### 1.3. StatefulKnowledgeSessions

StatefulKnowledgeSession 对象是一种最常用的与规则引擎进行交互的方式，它可以与规则引擎建立一个持续的交互通道，在推理计算的过程当中可能会多次触发同一数据集。在用户的代码当中，最后使用完StatefulKnowledgeSession 对象之后，一定要调用其dispose()方法以释放相关内存资源。

### 1.4. StateLessKnowledgeSession

StatelessKnowledgeSession 的作用与StatefulKnowledgeSession 相仿，它们都是用来接收业务数据、执行规则的。事实上，StatelessKnowledgeSession 对StatefulKnowledgeSession 做了包装，使得在使用StatelessKnowledgeSession 对象时不需要再调用dispose()方法释放内存资源了。

在drools 6.x以后这些API 都整合到kie API 中了

## 2. FACT对象

Fact 是指在Drools 规则应用当中，将一个普通的JavaBean 插入到规则的WorkingMemory当中后的对象。规则可以对Fact 对象进行任意的读写操作，当一个JavaBean 插入到WorkingMemory 当中变成Fact 之后，Fact 对象不是对原来的JavaBean 对象进行Clon，而是原来JavaBean 对象的引用。

## 3. 规则



### 3.1规则文件

在 Drools 当中，一个标准的规则文件就是一个以“.drl”结尾的文本文件，由于它是一个标准的文本文件，所以可以通过一些记事本工具对其进行打开、查看和编辑。

规则是放在规则文件当中的，一个规则文件可以存放多个规则，除此之外，在规则文件当中还可以存放用户自定义的函数、数据对象及自定义查询等相关在规则当中可能会用到的一些对象。常用的有：package package-name、imports、globals、functions、queries、rules

对于一个规则文件而言，首先声明package 是必须的，除package 之外，其它对象在规则文件中的顺序是任意的，也就是说在规则文件当中必须要有一个package 声明，同时package 声明必须要放在规则文件的第一行。在Drools 的规则文件当中package 对于规则文件中规则的管理只限于逻辑上的管理，，而不管其在物理上的位置如何，这点是规则与Java 文件的package 的区别。对于同一package 下的用户自定义函数、自定义的查询等，不管这些函数与查询是否在同一个规则文件里面，在规则里面是可以直接使用的，这点和Java 的同一package 里的Java类调用是一样的。



### 3.2 规则语言
```
rule "name"
attributes
    when
        LHS
    Then
        RHS
end
```
一个规则通常包括三个部分：属性部分（attribute）、条件部分（LHS）和结果部分（RHS）。对于一个完整的规则来说，这三个部分都是可选的，也就是说如下 所示的规则是合法的：
```
rule "name"
when
then
end
```


### 3.3 条件部分

条件部分又被称之为Left Hand Side，简称为LHS，下文当中，如果没有特别指出，那么所说的LHS 均指规则的条件部分，在一个规则当中when 与then 中间的部分就是LHS 部分。在LHS 当中，可以包含0~n 个条件，如果LHS 部分没空的话，那么引擎会自动添加一个eval(true)的条件，由于该条件总是返回true，所以LHS 为空的规则总是返回true。LHS 部分是由一个或多个条件组成，条件又称之为pattern（匹配模式），多个pattern之间用可以使用and 或or 来进行连接，同时还可以使用小括号来确定pattern 的优先级。
一个pattern 的语法如下：

[绑定变量名：]Object([field 约束])1

对于一个pattern 来说“绑定变量名”是可选的，如果在当前规则的LHS 部分的其它的pattern 要用到这个对象，那么可以通过为该对象设定一个绑定变量名来实现对其引用，对于绑定变量的命名，通常的作法是为其添加一个“$”符号作为前缀，这样可以很好的与Fact的属性区别开来；绑定变量不仅可以用在对象上，也可以用在对象的属性上面，命名方法与对象的命名方法相同；“field 约束”是指当前对象里相关字段的条件限制，示例如下：
```
rule "rule1"
when
$customer:Customer(age>20,gender==’male’)
Order(customer==$customer,price>1000)
then
<action>…
End
```
此段规则的含义为：的规则就包含两个pattern，第一个pattern 有三个约束，分别是：对象类型必须是Cutomer；同时Cutomer 的age 要大于20 且gender 要是male；第二个pattern 也有三个约束，分别是：对象类型必须是Order，同时Order 对应的Cutomer 必须是前面的那个Customer 且当前这个Order 的price 要大于1000。在这两个pattern 没有符号连接，在Drools当中在pattern 中没有连接符号，那么就用and 来作为默认连接，所以在该规则的LHS 部分中两个pattern 只有都满足了才会返回true。默认情况下，每行可以用“;”来作为结束符（和Java 的结束一样），当然行尾也可以不加“;”结尾。

####  3.3.1 约束连接  

对于对象内部的多个约束的连接，可以采用“&&”（and）、“||”(or)和“,”(and)来实现，“&&”（and）、“||”(or)和“,”这三个连接符号如果没有用小括号来显示的定义优先级的话，那么它们的执行顺序是：“&&”（and）、“||”(or)和“,” “&&”优先级最高，表面上看“,”与“&&”具有相同的含义，但是有一点需要注意，“，”与“&&”和“||”不能混合使用，也就是说在有“&&”或“||”出现的LHS 当中，是不可以有“，”连接符出
现的，反之亦然。

####  3.3.2 比较操作符

在当中共提供了十二种类型的比较操作符，分别是：>、>=、<、<=、= =、!=、contains、not contains、memberof、not memberof、matches、not matches；在这十二种类型的比较操作符当中，前六个是比较常见也是用的比较多的比较操作符，着重对后六种类型的比较操作符进行介绍。



##### 3.3.2.1 contains

比较操作符contains 是用来检查一个Fact 对象的某个字段（该字段要是一个Collection或是一个Array 类型的对象）是否包含一个指定的对象。


```
when
$order:Order();
$customer:Customer(age >20, orders contains $order);
then
System.out.println($customer.getName());
End
```
contains 只能用于对象的某个Collection/Array 类型的字段与另外一个值进行比较，作为比较的值可以是一个静态的值，也可以是一个变量(绑定变量或者是一个global 对象)。



##### 3.3.2.2 not contains

not contains 作用与contains 作用相反，not contains 是用来判断一个Fact 对象的某个字段（Collection/Array 类型）是不是不包含一个指定的对象，和contains 比较符相同，它也只能用在对象的field 当中。



##### 3.3.2.3 memberOf

memberOf 是用来判断某个Fact 对象的某个字段是否在一个集合（Collection/Array）当中，用法与contains 有些类似，但也有不同，memberOf 的语法如下：Object(fieldName memberOf value[Collection/Array])可以看到memberOf 中集合类型的数据是作为被比较项的，集合类型的数据对象位于memberOf 操作符后面，同时在用memberOf 比较操作符时被比较项一定要是一个变量(绑定变量或者是一个global 对象)，而不能是一个静态值。



##### 3.3.2.4 not memberOf

该操作符与memberOf 作用洽洽相反，是用来判断Fact 对象当中某个字段值是不是中某个集合（Collection/Array）当中，同时被比较的集合对象只能是一个变量（绑定变量或global对象）。



##### 3.3.2.5 matches

matches 是用来对某个Fact 的字段与标准的Java 正则表达式进行相似匹配，被比较的字符串可以是一个标准的Java 正则表达式，但有一点需要注意，那就是正则表达式字符串当中不用考虑“\”的转义问题
```
when
$customer:Customer(name matches "李.*");
then
System.out.println($customer.getName());
end
```
该规则是用来查找所有Customer 对象的name 属性是不是以“李”字开头，如果满足这一条件那么就将该Customer 对象的name 属性打印出来。



##### 3.3.2.6 not matches

与matches 作用相反，是用来将某个Fact 的字段与一个Java 标准正则表达式进行匹配，看是不是能与正则表达式匹配。not matches 使用语法如下：
```
Object(fieldname not matches “正则表达式”)1
```


### 3.4 结果部分

结果部分又被称之为Right Hand Side，简称为RHS，在一个规则当中then 后面部分就是RHS，只有在LHS 的所有条件都满足时RHS 部分才会执行。

RHS 部分是规则真正要做事情的部分，可以将因条件满足而要触发的动作写在该部分当中，在RHS 当中可以使用LHS 部分当中定义的绑定变量名、设置的全局变量、或者是直接编写Java 代码（对于要用到的Java 类，需要在规则文件当中用import 将类导入后方能使用，这点和Java 文件的编写规则相同）。

在规则当中LHS 就是用来放置条件的，所以在RHS 当中虽然可以直接编写Java 代码，但不建议在代码当中有条件判断，如果需要条件判断，那么请重新考虑将其放在LHS 当中，否则就违背了使用规则的初衷。

在 Drools 当中，在RHS 里面，提供了一些对当前Working Memory 实现快速操作的宏函数或宏对象，比如insert/insertLogical、update 和retract 就可以实现对当前Working Memory中的Fact 对象进行新增、删除或者是修改.



#### 3.4.1 insert


```
insert(new Object());1
```

一旦调用insert 宏函数，那么Drools 会重新与所有的规则再重新匹配一次，对于没有设置no-loop 属性为true 的规则，如果条件满足，不管其之前是否执行过都会再执行一次，这个特性不仅存在于insert 宏函数上，后面介绍的update、retract 宏函数同样具有该特性，所以在某些情况下因考虑不周调用insert、update 或retract容易发生死循环，这点大家需要注意.


```
when
eval(true);
then
Customer cus=new Customer();
cus.setName("张三");
insert(cus);
end
````
#### 3.4.2 update

update 函数意义与其名称一样，用来实现对当前Working Memory 当中的Fact 进行更新，update 宏函数的作用与StatefulSession 对象的update 方法的作用基本相同，都是用来告诉当前的Working Memory 该Fact 对象已经发生了变化。


```
when
$customer:Customer(name=="张三",age<10);
then
$customer.setAge($customer.getAge()+1);
update($customer);
System.out.println("----------"+$customer.getName());
End
```



#### 3.4.3 retract

retract 用来将Working Memory 当中某个Fact 对象从Working Memory 当中删除，下面就通过一个例子来说明retract 宏函数的用法。
```
retract($customer);

```

#### 3.4.4 modify

modify 是一个表达式块，它可以快速实现对Fact 对象多个属性进行修改，修改完成后会自动更新到当前的Working Memory 当中。它的基本语法格式如下：


```
modify(fact-expression){
<修改Fact 属性的表达式>[,<修改Fact 属性的表达式>*]
}
```

示例


```
when
$customer:Customer(name=="张三",age==20);
then
System.out.println("modify before customer
id:"+$customer.getId()+";age:"+$customer.getAge());
modify($customer){
setId("super man"),
setAge(30)
}
End
```
这里有一点需要注意，那就是和insert、update、retract 对Working Memory 的操作一样，一旦使用了modify 块对某个Fact 的属性进行了修改，那么会导致引擎重新检查所有规则是否匹配条件，而不管其之前是否执行过。



### 3.5 属性部分

规则属性是用来控制规则执行的重要工具，在规则的属性共有13 个，它们分别是：activation-group、agenda-group、
auto-focus、date-effective、date-expires、dialect、duration、enabled、lock-on-active、no-loop、ruleflow-group、salience、when，这些属性分别适用于不同的场景，下面我们就来分别介绍这些属性的含义及用法。

#### 3.5.1 salience

用来设置规则执行的优先级，salience 属性的值是一个数字，数字越大执行优先级越高，同时它的值可以是一个负数。默认情况下，规则的ssalience 默认值为0，所以如果我们不手动设置规则的salience 属性，那么它的执行顺序是随机（但是一般都是按照加载顺序。）
```
rule "rule1"
salience 1
when
eval(true)
then
System.out.println("rule1");
End
```


#### 3.5.2 no-loop

no-loop 属性的作用是用来控制已经执行过的规则在条件再次满足时是否再次执行。默认情况下规则的no-loop属性的值为false，如果no-loop 属性值为true，那么就表示该规则只会被引擎检查一次，

no-loop 是 对于自己规则的操作引起重新匹配，只执行一次。由于其他规则的操作引起的重新匹配会执行一次。所以如果有两个
规则都是一直满足条件，且then 中有update 操作（如下例子）。那么将会进入死循环，此时应该使用 lock-on-active 属性保证只匹配一次。

```

rule "rule1"
salience 1
no-loop true
when
$customer:Customer(name=="张三")
then
update($customer);
System.out.println("customer name:"+$customer.getName());
End

```

#### 3.5.3 date-effective

控制规则只有在到达后才会触发。只有当系统时间>=date-effective 设置的时间值时，规则才会触发执行，否则执行将不执行。在没有设置该属性的情况下，规则随时可以触发，没有这种限制。

date-effective 可接受的日期格式为“dd-MMM-yyyy”，例如2009 年9 月25 日
如果您的操作系统为中文的，那么应该写成“25-Sep-2009”；如果是英文操作系统“25-九月-2009”


```
rule "rule1"
date-effective " 25-九月-2009"
when
eval(true);
then
System.out.println("rule1 is execution!");
End

```

#### 3.5.4 date-expires

该属性的作用与date-effective 属性恰恰相反， date-expires 的作用是用来设置规则的有效期。如果date-expires 的值大于系统时间，那么规则就执行，否则就不执行。

具体用法与date-effective 属性相同。



#### 3.5.5 enabled

enabled 属性比较简单，它是用来定义一个规则是否可用的。该属性的值是一个布尔值，默认该属性的值为true，表示规则是可用的，如果手工为一个规则添加一个enabled 属性，并且设置其enabled 属性值为false，那么引擎就不会执行该规则。



#### 3.5.6 dialect

该属性用来定义规则当中要使用的语言类型，目前Drools 版本当中支持两种类型的语言：mvel 和java，默认情况下，如果没有手工设置规则的dialect，那么使用的java 语言。



#### 3.5.7 duration

如果设置了该属性，那么规则将在该属性指定的值之后在另外一个线程里触发。该属性对应的值为一个长整型，单位是毫秒.


```
rule "rule1"
duration 3000
when
eval(true)
then
System.out.println("rule thread
id:"+Thread.currentThread().getId());
end
```
表示该规则将在3000 毫秒之后在另外一个线程里触发。



#### 3.5.8 lock-on-active

确认规则只执行一次。 将lock-on-action 属性的值设置为true，可能避免因某些Fact 对象被修改而使已经执行过的规则再次被激活执行。lock-on-active 是no-loop 的增强版属性。lock-on-active 属性默认值为false。



#### 3.5.9 activation-group

该属性的作用是将若干个规则划分成一个组，用一个字符串来给这个组命名，这样在执行的时候，具有相同activation-group 属性的规则中只要有一个会被执行，其它的规则都将不再执行。

在一组具有相同activation-group 属性的规则当中，只有一个规则会被执行，其它规则都将不会被执行。当然对于具有相同activation-group 属性的规则当中究竟哪一个会先执行，则可以用类似salience 之类属性来实现。

```

rule "rule1"
activation-group "test"
when
eval(true)
then
System.out.println("rule1 execute");
end

rule "rule 2"
activation-group "test"
when
eval(true)
then
System.out.println("rule2 execute");
End
```
rule1 和rule2 这两个规则因为具体相同名称的activation-group 属性，所以它们只有一个会被执行。



#### 3.5.10 agenda-group

Agenda Group 是用来在Agenda 的基础之上，对现在的规则进行再次分组，具体的分组方法可以采用为规则添加agenda-group 属性来实现。
agenda-group 属性的值也是一个字符串，通过这个字符串，可以将规则分为若干个Agenda Group，默认情况下，引擎在调用这些设置了agenda-group 属性的规则的时候需要显示的指定某个Agenda Group 得到Focus（焦点），这样位于该Agenda Group 当中的规则才会触发执行，否则将不执行。


```
rule "rule1"
agenda-group "001"
when
eval(true)
then
System.out.println("rule1 execute");
end

rule "rule 2"
agenda-group "002"
when
eval(true)
then
System.out.println("rule2 execute");
End
```
java 代码


```
//getSession 获取KieSession 的方法自己写的。
 KieSession ks = getSession();
 //设置agenda-group 的auto-focus 使其执行           ks.getAgenda().getAgendaGroup("group1").setFocus();

```



#### 3.5.11 auto-focus

前面我们也提到auto-focus 属性，它的作用是用来在已设置了agenda-group 的规则上设置该规则是否可以自动独取Focus，如果该属性设置为true，那么在引擎执行时，就不需要显示的为某个Agenda Group 设置Focus，否则需要。

对于规则的执行的控制，还可以使用Agenda Filter 来实现。在Drools 当中，提供了一个名为org.drools.runtime.rule.AgendaFilter 的Agenda Filter 接口，用户可以实现该接口，通过规则当中的某些属性来控制规则要不要执行。org.drools.runtime.rule.AgendaFilter 接口只有一个方法需要实现，方法体如下：
```
public boolean accept(Activation activation);
```
在该方法当中提供了一个Activation 参数，通过该参数我们可以得到当前正在执行的规则对象或其它一些属性，该方法要返回一个布尔值，该布尔值就决定了要不要执行当前这个规则，返回true 就执行规则，否则就不执行。

在引擎执行规则的时候，我们希望使用规则名来对要执行的规则做一个过滤，此时就可以通过AgendaFilter 来实现，示例代码既为我们实现的一个AgendaFilter 类源码。


```
import org.drools.runtime.rule.Activation;
import org.drools.runtime.rule.AgendaFilter;
public class TestAgendaFilter implements AgendaFilter {
private String startName;
public TestAgendaFilter(String startName){
this.startName=startName;
}
public boolean accept(Activation activation) {
String ruleName=activation.getRule().getName();
if(ruleName.startsWith(this.startName)){
return true;
}else{
return false;
}
}
}

```

从实现类中可以看到，我们采用的过滤方法是规则名的前缀，通过Activation 得到当前的Rule 对象，然后得到当前规则的name，再用这个name 与给定的name 前缀进行比较，如果相同就返回true，否则就返回false。

java 中调用

```

            TestAgendaFilter filter= new TestAgendaFilter("activa");
           int count = ks.fireAllRules(filter);12

```

#### 3.5.12 ruleflow-group

在使用规则流的时候要用到ruleflow-group 属性，该属性的值为一个字符串，作用是用来将规则划分为一个个的组，然后在规则流当中通过使用ruleflow-group 属性的值，从而使用对应的规则。

## 4. 函数

函数是定义在规则文件当中一代码块，作用是将在规则文件当中若干个规则都会用到的业务操作封装起来，实现业务代码的复用，减少规则编写的工作量。函数的编写位置可以是规则文件当中package 声明后的任何地方，Drools 当中函数声明。


```
function void/Object functionName(Type arg...) {
/*函数体的业务代码*/
}
```

Drools 当中的函数以function 标记开头，如果函数体没有返回值，那么function 后面就是void，如果有返回值这里的void 要换成对应的返回值对象，接下来就是函数的名称函数名称的定义可以参考Java 类当中方法的命名原则，对于一个函数可以有若干个输入参数，所以函数名后面的括号当中可以定义若干个输入参数。

Drools 为我们提供了一个特殊的import 语句：import function，通过该import语句，可以实现将一个Java 类中静态方法引入到一个规则文件当中，使得该文件当中的规则可以像使用普通的Drools 函数一样来使用Java 类中某个静态方法。


```
import function test.RuleTools.printInfo;1
```

通过使用import function 关键字，将test.RuleTools 类中静态方法printInfo 引入到当前规则文件中。

调用方式


```
RuleTools.printInfo（...）
```
## 参考

- [drools －规则语法](https://blog.csdn.net/u012373815/article/details/53872025)
