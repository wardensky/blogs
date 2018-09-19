# Mybatis动态sql

## 简介
Mybatis动态sql可以让我们在Xml映射文件内，以标签的形式编写动态sql，完成逻辑判断和动态拼接sql的功能，Mybatis提供了9种动态sql标签
```
trim
where
set
foreach
if
choose
when
otherwise
bind。
```

其执行原理为，使用OGNL从sql参数对象中计算表达式的值，根据表达式的值动态拼接sql，以此来完成动态sql的功能。


## 自己项目里面的例子

```
<select id="search" resultMap="SearchRetVoMap" parameterType="com.wisdombud.dth.boss.dto.cust.SearchParamVo" >
 select
c.ID CUST_ID, u.ID USER_ID, u.NAME USER_NAME, u.DICT_USER_STATUS_TYPE DICT_USER_STATUS_TYPE, u.DICT_USER_STATUS_NAME DICT_USER_STATUS_NAME,
u.CARD_SERIAL_NO CARD_SERIAL_NO, u.STB_ENCRYPT_SERIAL_NO STB_ENCRYPT_SERIAL_NO,
u.STB_MODEL_ID STB_MODEL_ID, u.STB_MODEL_NAME STB_MODEL_NAME,
u.LOCATION_ID LOCATION_ID, u.LOCATION_CODE LOCATION_CODE, u.LOCATION_FULL_NAME LOCATION_FULL_NAME, u.ADDRESS_DETAIL ADDRESS_DETAIL,
c.MOBILE MOBILE, u.LOCATION_FULL_NAME || u.ADDRESS_DETAIL FULL_NAME
from CUS_CUST c, CUS_USER u
where c.ID = u.CUST_ID
<choose>
      <when test="ids != null and ids.size > 0">
      and u.USER_ID in
  <foreach collection="ids" item="id" index="index"
     open="(" close=")" separator=",">
           #{id}
   </foreach>
     </when>
          <otherwise>
                 and u.USER_ID = #{session.id,jdbcType=DECIMAL}
           </otherwise>
 </choose>

<choose>
 <when test="village != null and village.length > 0">
         and u.LOCATION_CODE = #{village,jdbcType=VARCHAR}
     </when>
     <when test="township != null and township.length > 0">
         and u.LOCATION_CODE like CONCAT(#{township,jdbcType=VARCHAR},'%')
     </when>
     <otherwise>
         and u.LOCATION_CODE like CONCAT(#{county,jdbcType=VARCHAR},'%')
     </otherwise>
 </choose>				
<if test="name != null and name.length > 0"> and u.NAME like CONCAT(#{name,jdbcType=VARCHAR},'%') </if>
 <if test="userStatusType != null"> and u.DICT_USER_STATUS_TYPE = #{userStatusType,jdbcType=DECIMAL} </if>
 <if test="stbEncryptSerialNo != null and stbEncryptSerialNo.length > 0"> and u.STB_ENCRYPT_SERIAL_NO = #{stbEncryptSerialNo,jdbcType=VARCHAR} </if>
<if test="sortField != null and sortField.length > 0 and  sortOrder != null and sortOrder.length > 0"> order by u.${sortField} ${sortOrder} </if>
</select>
```


## MyBatis动态SQL

MyBatis 的强大特性之一便是它的动态 SQL，即拼接SQL字符串。如果你有使用 JDBC 或其他类似框架的经验，你就能体会到根据不同条件拼接 SQL 语句有多么痛苦。拼接的时候要确保不能忘了必要的空格，还要注意省掉列名列表最后的逗号。利用动态 SQL 这一特性可以彻底摆脱这种痛苦。

通常使用动态 SQL 不可能是独立的一部分,MyBatis 当然使用一种强大的动态 SQL 语言来改进这种情形,这种语言可以被用在任意的 SQL 映射语句中。

动态 SQL 元素和使用 JSTL 或其他类似基于 XML 的文本处理器相似。在 MyBatis 之前的版本中,有很多的元素需要来了解。MyBatis 3 大大提升了它们,现在用不到原先一半的元素就可以了。MyBatis 采用功能强大的基于 OGNL 的表达式来消除其他元素。



## 动态SQL标签
```
if，choose (when, otherwise)，trim (where, set)，foreach
```

### if标签

```
<select id="queryByIdAndTitle"
     resultType="Blog">
  SELECT * FROM BLOG
  WHERE 1=1
  <if test="id!= null and title!=null">
    AND id=#{id} and title=#{title}
  </if>
</select>

```

注：if标签一般用于非空验证，如上例，若id为空，if标签里的代码，将不会执行，反之，则会执行。

### choose(when,otherwise)


```
<select id="queryBy"
     resultType="Blog">
  SELECT * FROM BLOG WHERE 1=1
  <choose>
    <when test="title != null">
      AND title like #{title}
    </when>
    <otherwise>
      AND id= 1
    </otherwise>
  </choose>
</select>

```


注：choose(when,otherwise)标签相当于switch(case,default) ，如上例，若title 为空，when标签里的代码，将不会执行，默认执行otherwise标签里面的代码。



### trim(where,set)标签

#### where标签
```
<select id="queryBy" resultType="com.scme.pojo.User" parameterType="com.scme.pojo.User">
  select * from user
  <where>
    <if test="username!=null and password!=null">
      and username=#{username} and password=#{password}
    </if>
  </where>
</select>
```


注：假设上例传入的username，password不为空，代码就可以运行成功！但朋友们可能有疑问了，实际上执行的sql语句是什么呢？其实，sql为：select * from user

where username=? and password=?  朋友们是否发现，where标签代替了sql中where关键字，但if中的and不见了。其实where标签可以自动去除是“AND”或“OR”开头的sql中的“AND”或“OR”关键字。



如果 where 元素没有按正常套路出牌，我们还是可以通过自定义 trim 元素来定制sql,实现where标签的效果。代码如下：

```
<select id="queryBy" resultType="com.scme.pojo.User" parameterType="com.scme.pojo.User">
  select * from user
  <trim prefix="WHERE" prefixOverrides="AND |OR ">
    <if test="username!=null and password!=null">
      and username=#{username} and password=#{password}
    </if>
  </trim>

<!-- 效果同上
  <where>
    <if test="username!=null and password!=null">
      and username=#{username} and password=#{password}
    </if>
  </where> -->
</select>
```

#### set标签

```
<update id="updateUser" parameterType="com.scme.pojo.User">
                 update user
                 <set>
                     <if test="username!=null">
                             username=#{username}
                     </if>
                 </set>
                 <where>
                     <if test="id!=null">
                             id=#{id}
                     </if>
                 </where>
</update>
```


注：set标签功能和where标签差不多，set标签代替了sql中set关键字,set标签可以自动去除sql中的多余的“，”



同理，trim标签也可以实现set标签的功能

```
<update id="updateUser" parameterType="com.scme.pojo.User">
         update user
 　　　　　　　　<trim prefix="set" prefixOverrides=",">
　　　　　　　　　　　　　　<if test="username!=null"> username=#{username} </if>
　　　　　　　　</trim>
　　
 　　　　　　　　<where>
　　　　
　　　　　　　　　　　　<if test="id!=null"> id=#{id} </if>
　　　　　　　　　　
　　　　　　　　</where>

 </update>
```


### foreach标签

foreach标签实现批量删除



```
<delete id="batchDelete" parameterType="java.lang.String">
  delete from user
  where id in
  <foreach item="id" index="index" collection="list"
      open="(" separator="," close=")">
        #{id}
  </foreach>
</delete >
```


注：foreach标签可迭代任何对象（如列表、集合等）和任何的字典或者数组对象传递给foreach作为集合参数，当使用可迭代对象或者数组时，index是当前迭代的次数，item的值是本次迭代获取的元素。当使用字典（或者Map.Entry对象的集合）时，index是键，item是值。collection标签可以填（'list','array','map'）。

foreach元素的属性主要有 item，index，collection，open，separator，close。

- item表示集合中每一个元素进行迭代时的别名；
- index指 定一个名字，用于表示在迭代过程中，每次迭代到的位置；
- open表示该语句以什么开始，
- separator表示在每次进行迭代之间以什么符号作为分隔符；
- close表示以什么结束。



## bind

bind 元素可以从 OGNL 表达式中创建一个变量并将其绑定到上下文。比如：

```
<select id="selectBlogsLike" resultType="Blog">
  <bind name="pattern" value="'%' + _parameter.getTitle() + '%'" />
  SELECT * FROM BLOG
  WHERE title LIKE #{pattern}
</select>
```

## Multi-db vendor support

一个配置了```_databaseId```变量的 databaseIdProvider 对于动态代码来说是可用的，这样就可以根据不同的数据库厂商构建特定的语句。比如下面的例子：

```
<insert id="insert">
  <selectKey keyProperty="id" resultType="int" order="BEFORE">
    <if test="_databaseId == 'oracle'">
      select seq_users.nextval from dual
    </if>
    <if test="_databaseId == 'db2'">
      select nextval for seq_users from sysibm.sysdummy1"
    </if>
  </selectKey>
  insert into users values (#{id}, #{name})
</insert>
```

动态 SQL 中可插拔的脚本语言

MyBatis 从 3.2 开始支持可插拔的脚本语言，因此你可以在插入一种语言的驱动（language driver）之后来写基于这种语言的动态 SQL 查询。

可以通过实现下面接口的方式来插入一种语言：

```
public interface LanguageDriver {
  ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql);
  SqlSource createSqlSource(Configuration configuration, XNode script, Class<?> parameterType);
  SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType);
}
```



一旦有了自定义的语言驱动，你就可以在 mybatis-config.xml 文件中将它设置为默认语言：
```
<typeAliases>
  <typeAlias type="org.sample.MyLanguageDriver" alias="myLanguage"/>
</typeAliases>
<settings>
  <setting name="defaultScriptingLanguage" value="myLanguage"/>
</settings>
```


除了设置默认语言，你也可以针对特殊的语句指定特定语言，这可以通过如下的 lang 属性来完成：
```
<select id="selectBlog" lang="myLanguage">
  SELECT * FROM BLOG
</select>

```
或者在你正在使用的映射中加上注解 @Lang 来完成：
```
public interface Mapper {
  @Lang(MyLanguageDriver.class)
  @Select("SELECT * FROM BLOG")
  List<Blog> selectBlog();
```

注意 可以将 Apache Velocity 作为动态语言来使用，更多细节请参考 MyBatis-Velocity 项目。


## 参考

- [MyBatis动态SQL————MyBatis动态SQL标签的用法](https://www.cnblogs.com/zjl6/p/6965361.html)
