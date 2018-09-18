#Struts2入门（2）-常用struts2标签


## if、elseif和else


```
<%@ page contentType="text/html; charset=UTF-8"  isELIgnored="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <title>Condition Flow</title>
    </head>
    <body>
        <h3>Condition Flow</h3>
        <s:set name="name" value="'max'"/>
        <s:if test="#name == 'max'">
            Max's file here
        </s:if>
        <s:elseif test="#name == 'scott'">
            Scott's file here
        </s:elseif>
        <s:else>
            Other's file here
        </s:else>
    </body>
</html>

```

## i18n
## iterator

```
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
    List list = new ArrayList();
    list.add("Max");
    list.add("Scott");
    list.add("Jeffry");
    list.add("Joe");
    list.add("Kelvin");
    request.setAttribute("names", list);
%>

<html>
    <head>
        <title>Iterator</title>
    </head>
    <body>
        <h3>Names:
        </h3>
        <ol>
            <s:iterator value="#request.names" status="stuts">
                <s:if test="#stuts.odd == true">
                    <li>White
                        <s:property/></li>
                </s:if>
                <s:else>
                    <li style="background-color:gray"><s:property/></li>
                </s:else>
            </s:iterator>
        </ol>

    </body>
</html>

```
##include

##param

##set

##text


##url

##property


##参考
[Tag Reference](https://struts.apache.org/docs/tag-reference.html)
[常用的Struts 2.0的标志（Tag）介绍](常用的Struts 2.0的标志（Tag）介绍)
