# python抓取网页例子


最近在学习python，刚刚完成了一个网页抓取的例子，通过python抓取全世界所有的学校以及学院的数据，并存为xml文件。数据源是人人网。

因为刚学习python，写的代码还不够Pythonic。

核心代码如下：
```


#!/usr/bin/python


import urllib.request
from html.parser import HTMLParser  
import json
import time
import xml.dom.minidom
import os


class Dept():
   id = 0
   name = ''

class University(Dept):
   depts = []

class City(Dept):
   universities  = []    

class Country(Dept):
   cities = []

class MyHtmlParser(HTMLParser):
   def __init__(self):   
       HTMLParser.__init__(self)   
       self.links = []  
       self.depts = []
   def handle_starttag(self, tag, attrs):   
       if tag == 'option':
           for att in attrs:
               for a in att:
                   if a != 'value' and a != '':
                       self.depts.append(a)
def readDept(code):
   depts = []
   html = ''
   for word in urllib.request.urlopen('http://www.renren.com/GetDep.do?id=' + str(code)).readlines():
       real = word.strip().decode('gbk')
       html =  html  + real
   hp = MyHtmlParser()   
   hp.feed(html)  
   for inst in hp.depts:
       dept = Dept()
       dept.name = inst
       depts.append(dept)
   return depts

def writeXml(city):
   impl = xml.dom.minidom.getDOMImplementation()
   dom = impl.createDocument(None, 'city', None)
   root = dom.documentElement  
   filename = city.name + '.xml'
   if os.path.isfile(filename):
       os.remove(filename)
   nameE = dom.createElement('name')
   nameT = dom.createTextNode(city.name)
   idE = dom.createElement('id')
   idT = dom.createTextNode(str(city.id))
   nameE.appendChild(nameT)
   idE.appendChild(idT)
   root.appendChild(nameE)
   root.appendChild(idE)
   univs = dom.createElement('universities')
   root.appendChild(univs)
   for uni in city.universities:
#        print('write xml' + city.name + '\t' + uni.name)
       universityE = dom.createElement('university')
       univs.appendChild(universityE)
       uniE = dom.createElement('name')
       uniT = dom.createTextNode(uni.name)
       uidE = dom.createElement('id')
       uidT = dom.createTextNode(str(uni.id))
       uniE.appendChild(uniT)
       uidE.appendChild(uidT)
       universityE.appendChild(uniE)
       universityE.appendChild(uidE)
       deptsE = dom.createElement('departments')
       universityE.appendChild(deptsE)
       for dep in uni.depts:
           deptE = dom.createElement('department')
           deptsE.appendChild(deptE)
           deptNameE = dom.createElement('name')
           deptIdE = dom.createElement('id')
           deptT = dom.createTextNode(dep.name)
           deptIdT = dom.createTextNode(str(dep.id))
           deptNameE.appendChild(deptT)
           deptIdE.appendChild(deptIdT)
           deptE.appendChild(deptNameE)

   f= open(filename, 'w', encoding='utf-8')
   dom.writexml(f, addindent='  ', newl='\n',encoding='utf-8')
   print('write xml :' + city.name + '.xml')
   f.close()  


def mkdir(path):

   path=path.strip()
   path=path.rstrip("/")
   isExists=os.path.exists(path)
   if not isExists:
       os.makedirs(path)

def readData(content):
   counties = []
   jdata = json.loads(content)
   for i in range(0,100):
       try:
           country = Country()
           country.name = jdata[i]['name']
           country.id = jdata[i]['id']
           provs = jdata[i]['provs']
           for prov in provs:
               city = City()
               city.name = prov['name']
               city.id = prov['id']
               country.cities.append(city)
               city.universities = []
               for dic in prov['univs']:
                   university = University()
                   university.id = dic['id']
                   university.name = dic['name']
#                    print('get data: \t' + university.name)
                   university.depts = readDept(university.id)
                   city.universities.append(university)
                   print('city = ' + city.name + '\tuniversity = ' + university.name)
               writeXml(city)
           counties.append(country)
       except IndexError:
           break;
   return counties


print('开始时间：' + time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time())))
f = open('data','r' )
content = f.read()
f.close()
counties = readData(content)
print('结束时间：' + time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time())))



```

其中data是从如下网站拿到的
```
http://s.xnimg.cn/allunivlist.js
```
