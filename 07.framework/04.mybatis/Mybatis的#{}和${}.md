# Mybatis的#{}和${}


${}是Properties文件中的变量占位符，它可以用于标签属性值和sql内部，属于静态文本替换，比如${driver}会被静态替换为com.mysql.jdbc.Driver。

\#{}是sql的参数占位符，Mybatis会将sql中的#{}替换为?号，在sql执行前会使用PreparedStatement的参数设置方法，按序给sql的?号占位符设置参数值，比如ps.setInt(0, parameterValue)，#{item.name}的取值方式为使用反射从参数对象中获取item对象的name属性值，相当于param.getItem().getName()。



## 关于 “#{}”

在传统的 JDBC 的编程中，占位符用 “?” 来表示，然后再加载 SQL 之前按照 “?” 的位置设置参数。而 “#{}” 在 MyBatis 中也代表一种占位符，该符号接受输入参数，在大括号中编写参数名称来接受对应参数。当 “#{}” 接受简单类型时可以用 value 或者其他任意名称来获取。

**示例代码**

代码1
```
<select id="selectByPrimaryKey" resultMap="BaseResultMap" parameterType="java.lang.Long" >
  select
  <include refid="Base_Column_List" />
  from CUS_USER
  where ID = #{id,jdbcType=DECIMAL}
</select>
```

代码2
```
<select id="selectByRuleId" parameterType="java.lang.Long"
  resultMap="SecControlRuleManyMap">
  SELECT subRule.*,
  subRuleMany.*
  FROM
  SEC_CONTROL_SUB_RULE
  subRule,
  SEC_CONTROL_SUB_RULE_MANY subRuleMany
  WHERE subRule.RULE_ID
  =#{ruleId,jdbcType=DECIMAL} AND subRule.ID =
  subRuleMany.SUB_RULE_ID
</select>
```

## 关于 “${}”

在 SQL 配置中，有时候需要拼接 SQL 语句（例如模糊查询时），用 “#{}” 是无法达到目的的。在 MyBatis 中，“${}” 代表一个 “拼接符号” ，可以在原有 SQL 语句上拼接新的符合 SQL 语法的语句。使用 “${}” 拼接符号拼接 SQL ，会引起 SQL 注入，所以一般不建议使用 “${}”。

**示例代码**

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
