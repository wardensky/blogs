# python 应用xml.dom.minidom读xml

##xml文件
```
<?xml version="1.0" encoding="utf-8"?>
 <city>
   <name>上海</name>
   <id>2</id>
   <universities>
     <university>
       <name>复旦大学</name>
       <id>2001</id>
       <departments>
         <department>
           <name>上海医学院</name>
         </department>
         <department>
           <name>世界经济系</name>
         </department>
         <department>
           <name>中国语言文学系</name>
         </department>
      </departments>
    </university>
  </universities>
</city>

```
##代码
读xml代码如下：
```
def readXml(file):
    unis = []
    dom = xml.dom.minidom.parse(file)
    root = dom.documentElement
    for child in root.childNodes:
        if child.nodeName == 'universities':
            for uni in child.childNodes:
                if uni.nodeName == 'university':
                    university = University()
                    unis.append(university)
                    university.depts = []
                    for depts in uni.childNodes:
                        if depts.nodeName == 'name':
                            for name in depts.childNodes:
                                university.name = name.nodeValue
                                print(name.nodeValue)
                        if depts.nodeName == 'departments':
                            for dept in depts.childNodes:
                                for deptname in dept.childNodes:
                                    for inst in deptname.childNodes:
                                        university.depts.append(inst.nodeValue)
    return unis
```

##说明

- 使用parse()或者createDocument()返回的为DOM对象
- 使用DOM的documentElement属性可以锋利RootElement;
- DOM为树形结构，包含许多的nodes，其中element是node的一种，可以包含了node; textNode也是一种node,但它只能作为树叶
- 每个node都有nodeName, nodeValue，nodeType属性，nodeValue是结点的值，只对textNode有效。对于textNode想等到他的文本内容可以使用: .data属性
- 要获得一个textnode都值，需要再遍历一遍其childnodes，否则得到的是None
- nodeType是结点的类型，现在有以下几种：
```
ATTRIBUTE_NODE = 2
CDATA_SECTION_NODE = 4
COMMENT_NODE = 8
DOCUMENT_FRAGMENT_NODE = 11
DOCUMENT_NODE = 9
DOCUMENT_TYPE_NODE = 10
ELEMENT_NODE = 1
ENTITY_NODE = 6
ENTITY_REFERENCE_NODE = 5
NOTATION_NODE = 12
PROCESSING_INSTRUCTION_NODE = 7
TEXT_NODE = 3
```
判断node的类型代码如下：
```
node.nodeType == node.TEXT_NODE
```
