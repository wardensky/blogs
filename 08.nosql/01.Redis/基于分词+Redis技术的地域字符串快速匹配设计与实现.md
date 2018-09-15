#基于分词+Redis技术的地域字符串快速匹配设计与实现



## 背景
现在有这样一个需求，客户输入一个地域字符串，返回真正的地址。比如输入“陕西硖石交口村”，返回“陕西宝鸡市金台区硖石镇交口村”。因为输入是最终用户输入，输入的内容很不规范，要准确快速的查找到数据，需要一定的设计。

## 问题

主要存在以下几个问题。
1. 数据如何存储的问题。现有的数据是正规的省、市、县、乡、村五级形式存储，全国的所有村都包含，大概几十万。因为要考虑查询的效率，所以存储的形式很重要。
2. 如何准确快递的查询，用sql的like是肯定不行的，因为用户的输入不能控制，而且like的效率也不高。

## 解决方案

考虑到不能用like，准备用分词。既然不用like，那么也没必要用mysql数据库。Redis数据库有着比较高的速度，准备把所有的省市县乡村连起来，存储为key，分词之后，用redis的```keys *```	快速查询。然后再在查询之后的结果里面处理。

### 存储为redis

存储为redis比较简单，

### 分词

分词采用了IKAnalyzer

### 具体实现

### 遇到问题
在全国大部分地区，上面的方法都是有效的。可是在测试的过程中，遇到新疆和内蒙等地区，这个方法就不灵了。原因就是因为分词不准确了，基本上会分成单个字。
为了解决这个问题，又加上了下面这个算法。

## 总结

采用这个方法，速度比较快，查询均在1秒中之内完成。
## 代码

### 分词的代码
```
	private static List<String> fenci(String text) {
		List<String> ret = Lists.newArrayList();
		Analyzer anal = new IKAnalyzer(true);
		StringReader reader = new StringReader(text);
		TokenStream ts = anal.tokenStream("", reader);
		CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
		try {
			while (ts.incrementToken()) {
				ret.add(term.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		reader.close();
		return ret;
	}
```
输入```陕西硖石交口村```,输出
```
陕西
硖
石
交口
村
```
