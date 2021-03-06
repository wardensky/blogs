# MyBatis学习-映射文件标签

MyBatis 真正的核心在映射文件中。比直接使用 JDBC 节省95%的代码。而且将 SQL 语句独立在 Java 代码之外，可以进行更为细致的 SQL 优化。



## 映射文件的顶级元素

- select：映射查询语句
- insert：映射插入语句
- update：映射更新语句
- delete：映射删除语句
- sql：可以重用的sql代码块
- resultMap：最复杂，最有力量的元素，用来描述如何从数据库结果集中加载你的对象
- cache：配置给定命名空间的缓存
- cache-ref：从其他命名空间引用缓存配置


## select 标签的属性信息


### 源代码实例
```
<select id="selectByPrimaryKey" parameterType="java.lang.Long"
  resultMap="BaseResultMap">
  select
  <include refid="Base_Column_List" />
  from SEC_CONTROL_SUB_RULE
  where ID = #{id,jdbcType=DECIMAL}
</select>
```

```
<select id="selectByEncryptSerialNo" resultMap="BaseResultMap" parameterType="java.lang.String" >
  select
  <include refid="Base_Column_List" />
  from RES_STB
  where ENCRYPT_SERIAL_NO = #{serialNo,jdbcType=VARCHAR}
</select>
```

### 解释

#### id

- 必须配置
- id是命名空间中的唯一标识符，可被用来代表这条语句
- 一个命名空间（namespace）对应一个dao接口
- 这个id也应该对应dao里面的某个方法（sql相当于方法的实现），因此id应该与方法名一致

#### parameterType

- 可选配置，默认由mybatis自动选择处理
- 将要传入语句的参数的完全限定名或别名，如果不配置，mybatis会通过ParamterHandler根据参数类型默认选择合适的typeHandler进行处理
- parameterType 主要指定参数类型，可以是int, short, long, string等类型，也可以是复杂类型（如对象）

#### resultType

- resultType 与 resultMap 二选一配置
- 用来指定返回类型，指定的类型可以是基本类型，也可以是java容器，也可以是javabean

```
<select id="selectCountByCardSn" resultType="java.lang.Integer" parameterType="java.lang.String" >
  select
    count(ID)
  from CUS_USER
  where CARD_SERIAL_NO = #{sn,jdbcType=VARCHAR} AND DICT_USER_STATUS_TYPE != 4
</select>
```

#### resultMap

- resultType 与 resultMap 二选一配置
- 用于引用我们通过 resultMap 标签定义的映射类型，这也是mybatis组件高级复杂映射的关键

#### flushCache

- 可选配置
- 将其设置为true，任何时候语句被调用，都会导致本地缓存和二级缓存被清空，默认值：false

#### useCache

- 可选配置
- 将其设置为true，会导致本条语句的结果被二级缓存，默认值：对select元素为true

#### timeout

- 可选配置
- 这个设置是在抛出异常之前，驱动程序等待数据库返回请求结果的秒数，默认值为：unset（依赖驱动）

#### fetchSize

- 可选配置
- 这是尝试影响驱动程序每次批量返回的结果行数和这个设置值相等。默认值为：unset（依赖驱动）

#### statementType

- 可选配置
- STATEMENT, PREPARED或CALLABLE的一种，这会让MyBatis使用选择Statement,
- PrearedStatement或CallableStatement，默认值：PREPARED

#### resultSetType

- 可选配置
- FORWARD_ONLY，SCROLL_SENSITIVE 或 SCROLL_INSENSITIVE 中的一个，默认值为：unset（依赖驱动）


## resultMap 标签的属性信息

### 源代码实例

```
<resultMap id="BaseResultMap" type="com.wisdombud.dth.boss.res.pojo.ResStbPojo" >
  <id column="ID" property="id" jdbcType="DECIMAL" />
  <result column="VENDOR_ID" property="vendorId" jdbcType="DECIMAL" />
  <result column="VENDOR_NAME" property="vendorName" jdbcType="VARCHAR" />
  <result column="MODEL_ID" property="modelId" jdbcType="DECIMAL" />
  <result column="MODEL_NAME" property="modelName" jdbcType="VARCHAR" />
  <result column="DICT_STB_TYPE_VALUE" property="dictStbTypeValue" jdbcType="DECIMAL" />
  <result column="DICT_STB_TYPE_NAME" property="dictStbTypeName" jdbcType="VARCHAR" />
  <result column="PRE_STBSTA_TYPE_VALUE" property="preStbstaTypeValue" jdbcType="DECIMAL" />
  <result column="PRE_STBSTA_TYPE_NAME" property="preStbstaTypeName" jdbcType="VARCHAR" />
  <result column="DICT_STBSTA_TYPE_VALUE" property="dictStbstaTypeValue" jdbcType="DECIMAL" />
  <result column="DICT_STBSTA_TYPE_NAME" property="dictStbstaTypeName" jdbcType="VARCHAR" />
  <result column="ORG_ID" property="orgId" jdbcType="DECIMAL" />
  <result column="SERIAL_NO" property="serialNo" jdbcType="VARCHAR" />
  <result column="ENCRYPT_SERIAL_NO" property="encryptSerialNo" jdbcType="VARCHAR" />
  <result column="IMEI_NO" property="imeiNo" jdbcType="VARCHAR" />
  <result column="USE_USER_ID" property="useUserId" jdbcType="DECIMAL" />
  <result column="USE_USER_LOCATION_CODE" property="useUserLocationCode" jdbcType="VARCHAR" />
  <result column="USE_CARD_SERIAL_NO" property="useCardSerialNo" jdbcType="VARCHAR" />
  <result column="OPER_LOGIN_NO" property="operLoginNo" jdbcType="VARCHAR" />
  <result column="OPER_FUNC_CODE" property="operFuncCode" jdbcType="VARCHAR" />
  <result column="OPER_SERIAL_NUM" property="operSerialNum" jdbcType="DECIMAL" />
  <result column="CREATE_LOGIN_NO" property="createLoginNo" jdbcType="VARCHAR" />
  <result column="CREATE_TIME" property="createTime" jdbcType="TIMESTAMP" />
  <result column="CREATE_SERIAL_NUM" property="createSerialNum" jdbcType="DECIMAL" />
  <result column="LAST_UPDATE_TIME" property="lastUpdateTime" jdbcType="TIMESTAMP" />
  <result column="LAST_LOGIN_NO" property="lastLoginNo" jdbcType="VARCHAR" />
  <result column="LAST_SERIAL_NUM" property="lastSerialNum" jdbcType="DECIMAL" />
  <result column="REMARK" property="remark" jdbcType="VARCHAR" />
  <result column="RE1" property="re1" jdbcType="VARCHAR" />
  <result column="RE2" property="re2" jdbcType="VARCHAR" />
  <result column="RE3" property="re3" jdbcType="VARCHAR" />
  <result column="RE4" property="re4" jdbcType="VARCHAR" />
</resultMap>

```

```
<resultMap id="BaseResultMap"
  type="com.wisdombud.dth.boss.control.pojo.SecControlSubRulePojo">
  <id column="ID" jdbcType="DECIMAL" property="id" />
  <result column="RULE_ID" jdbcType="DECIMAL" property="ruleId" />
  <result column="ACTION_CODE" jdbcType="DECIMAL"
    property="actionCode" />
  <result column="IS_VALID" jdbcType="DECIMAL" property="isValid" />
  <result column="IS_WHITE" jdbcType="DECIMAL" property="isWhite" />
  <result column="FACTOR_CODE" jdbcType="DECIMAL"
    property="factorCode" />
  <result column="OPERATOR_CODE" jdbcType="DECIMAL"
    property="operatorCode" />
  <result column="QUANTITY_VALUE" jdbcType="DECIMAL"
    property="quantityValue" />
  <result column="DAYS" jdbcType="DECIMAL" property="days" />
</resultMap>
```

```
<resultMap
  type="com.wisdombud.dth.boss.control.pojo.SecControlSubRulePojo"
  id="SecControlRuleManyMap" extends="BaseResultMap">
  <collection property="manyList"
    ofType="com.wisdombud.dth.boss.control.pojo.SecControlSubRuleManyPojo">
    <result column="ID" property="id" />
    <result column="SUB_RULE_ID" property="subRuleId" />
    <result column="VALUE_ID" property="valueId" />
    <result column="VALUE_STRING" property="valueString" />
    <result column="EXPRESSION" property="expression" />
  </collection>
</resultMap>
```

### 解释


#### id

id 必须唯一， 用于标示这个resultMap的唯一性，在使用resultMap的时候，就是通过id引用


#### type

type 对应的返回类型，可以是javabean, 也可以是其它

#### extends

extends 继承其他resultMap标签

#### id

唯一性，注意啦，这个id用于标示这个javabean对象的唯一性， 不一定会是数据库的主键（不要把它理解为数据库对应表的主键）

#### property

属性对应javabean的属性名

#### column

- 对应数据库表的列名
- （这样，当javabean的属性与数据库对应表的列名不一致的时候，就能通过指定这个保持正常映射了）

#### result

result 与id相比，对应普通属性

#### constructor

constructor 对应javabean中的构造方法

- idArg 对应构造方法中的id参数
- arg 对应构造方法中的普通参数

#### collection

为关联关系，是实现一对多的关键
1. property 为javabean中容器对应字段名
2. ofType 指定集合中元素的对象类型
3. select 使用另一个查询封装的结果
4. column 为数据库中的列名，与select配合使用

当使用select属性时，无需下面的配置

```
<id property="" column=""/>
<result property="" column=""/>
```

#### association

为关联关系，是实现一对一的关键
1. property 为javabean中容器对应字段名
2. javaType 指定关联的类型，当使用select属性时，无需指定关联的类型
3. select 使用另一个select查询封装的结果
4. column 为数据库中的列名，与select配合使用

使用select属性时，无需下面的配置
```
<id property="" column=""/>
<result property="" column=""/>
```

## insert 标签的属性信息

### 源代码实例

```
<insert id="insert"
  parameterType="com.wisdombud.dth.boss.control.pojo.SecControlSubRulePojo">
  insert into SEC_CONTROL_SUB_RULE (ID, RULE_ID, ACTION_CODE,
  IS_VALID,
  IS_WHITE, FACTOR_CODE,
  OPERATOR_CODE, QUANTITY_VALUE,
  DAYS)
  values
  (#{id,jdbcType=DECIMAL},
  #{ruleId,jdbcType=DECIMAL},
  #{actionCode,jdbcType=DECIMAL},
  #{isValid,jdbcType=DECIMAL},
  #{isWhite,jdbcType=DECIMAL},
  #{factorCode,jdbcType=DECIMAL},
  #{operatorCode,jdbcType=DECIMAL},
  #{quantityValue,jdbcType=DECIMAL},
  #{days,jdbcType=DECIMAL})
</insert>
```


```
<insert id="insertSelective"
  parameterType="com.wisdombud.dth.boss.control.pojo.SecControlSubRulePojo">
  insert into SEC_CONTROL_SUB_RULE
  <trim prefix="(" suffix=")" suffixOverrides=",">
    <if test="id != null">
      ID,
    </if>
    <if test="ruleId != null">
      RULE_ID,
    </if>
    <if test="actionCode != null">
      ACTION_CODE,
    </if>
    <if test="isValid != null">
      IS_VALID,
    </if>
    <if test="isWhite != null">
      IS_WHITE,
    </if>
    <if test="factorCode != null">
      FACTOR_CODE,
    </if>
    <if test="operatorCode != null">
      OPERATOR_CODE,
    </if>
    <if test="quantityValue != null">
      QUANTITY_VALUE,
    </if>
    <if test="days != null">
      DAYS,
    </if>
  </trim>
  <trim prefix="values (" suffix=")" suffixOverrides=",">
    <if test="id != null">
      #{id,jdbcType=DECIMAL},
    </if>
    <if test="ruleId != null">
      #{ruleId,jdbcType=DECIMAL},
    </if>
    <if test="actionCode != null">
      #{actionCode,jdbcType=DECIMAL},
    </if>
    <if test="isValid != null">
      #{isValid,jdbcType=DECIMAL},
    </if>
    <if test="isWhite != null">
      #{isWhite,jdbcType=DECIMAL},
    </if>
    <if test="factorCode != null">
      #{factorCode,jdbcType=DECIMAL},
    </if>
    <if test="operatorCode != null">
      #{operatorCode,jdbcType=DECIMAL},
    </if>
    <if test="quantityValue != null">
      #{quantityValue,jdbcType=DECIMAL},
    </if>
    <if test="days != null">
      #{days,jdbcType=TIMESTAMP},
    </if>
  </trim>
</insert>
```
### 解释

```
<insert
　　<!--
　　　　同 select 标签
　　 -->
　　id="insertProject"

　　<!--
　　　　同 select 标签
　　 -->
　　paramterType="projectInfo"
　　
　　<!--
　　　　1. useGeneratedKeys（可选配置，与 keyProperty 相配合）
　　　　设置为true，并将 keyProperty 属性设为数据库主键对应的实体对象的属性名称
　　 -->
　　useGeneratedKeys="true"

　　<!--
　　　　2. keyProperty（可选配置，与 useGeneratedKeys 相配合）
　　　　用于获取数据库自动生成的主键
　　 -->
　　keyProperty="projectId"
>
```

## update

### 源代码实例

```
<update id="updateByRuleId">
  update SEC_CONTROL_SUB_RULE
  <set>
    IS_VALID = #{isValid,jdbcType=DECIMAL},
  </set>
  where RULE_ID = #{ruleId,jdbcType=DECIMAL}
</update>
```

```
<update id="updateByPrimaryKeySelective"
  parameterType="com.wisdombud.dth.boss.control.pojo.SecControlSubRulePojo">
  update SEC_CONTROL_SUB_RULE
  <set>
    <if test="ruleId != null">
      RULE_ID = #{ruleId,jdbcType=DECIMAL},
    </if>
    <if test="actionCode != null">
      ACTION_CODE = #{actionCode,jdbcType=DECIMAL},
    </if>
    <if test="isValid != null">
      IS_VALID = #{isValid,jdbcType=DECIMAL},
    </if>
    <if test="isWhite != null">
      IS_WHITE = #{isWhite,jdbcType=DECIMAL},
    </if>
    <if test="factorCode != null">
      FACTOR_CODE = #{factorCode,jdbcType=DECIMAL},
    </if>
    <if test="operatorCode != null">
      OPERATOR_CODE = #{operatorCode,jdbcType=DECIMAL},
    </if>
    <if test="quantityValue != null">
      QUANTITY_VALUE = #{quantityValue,jdbcType=DECIMAL},
    </if>
    <if test="days != null">
      DAYS =
      #{days,jdbcType=TIMESTAMP},
    </if>
  </set>
  where ID = #{id,jdbcType=DECIMAL}
</update>
```

### 解释

## delete

### 源代码实例

```
<delete id="deleteByRuleId">
  DELETE FROM SEC_CONTROL_SUB_RULE
  WHERE RULE_ID =
  #{ruleId,jdbcType=DECIMAL}
</delete>
```

### 解释

## 重用 sql 标签

### 源代码实例
```
<sql id="Base_Column_List">
  ID, RULE_ID, ACTION_CODE, IS_VALID, IS_WHITE, FACTOR_CODE,
  OPERATOR_CODE,
  QUANTITY_VALUE,
  DAYS
</sql>
```
### 解释
```
<sql id="userColumns">id,username,password</sql>
这个 SQL 片段可以被包含在其他语句中，eg：


<select id="selectProjectList" paramertType="int" resultType="hashmap">
　　SELECT
    　　<include refid="userColumns"/>
　　FROM
    　　t_project_002_project_info
</select>


```

## 完全限定名使用别名替代

在 mybatis 配置文件中，使用如下配置
```
<typeAliases>
    <typeAlias type="com.enh.bean.ProjectInfo" alias="projectInfo"/>
</typeAliases>
```
那么在 Mapper 中，可以直接使用 projectInfo，即表示 com.enh.bean.ProjectInfo



## 命名空间

每个sql映射文件的要元素中，都需要指定一个名称空间，用以确保每个映射语句的id属性不会重复。如

```
<mapper namespace="com.enh.mapper.PersonMapper">
```

在Java代码中引用某个 sql 映射时，使用的亦是含有名称空间的全路径。如
```
session.update("com.enh.mapper.PersonMapper.udpateUser", user);  

```

## 参考

- [MyBatis学习-映射文件标签篇(select、resultMap)](https://www.cnblogs.com/libra0920/p/6208587.html)
