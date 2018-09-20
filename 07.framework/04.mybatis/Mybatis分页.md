# Mybatis分页


Mybatis使用RowBounds对象进行分页，它是针对ResultSet结果集执行的内存分页，而非物理分页，可以在sql内直接书写带有物理分页的参数来完成物理分页功能，也可以使用分页插件来完成物理分页。

分页插件的基本原理是使用Mybatis提供的插件接口，实现自定义插件，在插件的拦截方法内拦截待执行的sql，然后重写sql，根据dialect方言，添加对应的物理分页语句和物理分页参数。

举例：select * from student，拦截sql后重写为：select t.* from （select * from student）t limit 0，10


一定要搞明白物理分页和逻辑分页的区别。


```

	@Override
	public Ret<PageEntity<LmaVo>> page(LmaSearchVo vo) {
        PageHelper.startPage(vo.getCurrentPage(), vo.getPageSize());
        if (StringUtils.isNotEmpty(vo.getSortOrder()) && StringUtils.isNotEmpty(vo.getSortColumn())) {
			PageHelper.orderBy(vo.getSortColumn() + " " + vo.getSortOrder());
        }
        final PageInfo<LmaVo> info = new PageInfo<>(llmiMapper.findByPageAndCondition(vo));  
        PageEntity<LmaVo> page = new PageEntity<>(vo);
        page.setResults(info.getList());
        page.setTotalResults(info.getTotal());
        page.setTotalPages();
        return Ret.success(page);
	}
```


```
Config PageHelper

1. Using in mybatis-config.xml

<!--
    In the configuration file,
    plugins location must meet the requirements as the following order:
    properties?, settings?,
    typeAliases?, typeHandlers?,
    objectFactory?,objectWrapperFactory?,
    plugins?,
    environments?, databaseIdProvider?, mappers?
-->
<plugins>
    <plugin interceptor="com.github.pagehelper.PageInterceptor">
        <!-- config params as the following -->
        <property name="param1" value="value1"/>
    </plugin>
</plugins>
2. Using in Spring application.xml

config org.mybatis.spring.SqlSessionFactoryBean as following:

<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
  <!-- other configuration -->
  <property name="plugins">
    <array>
      <bean class="com.github.pagehelper.PageInterceptor">
        <property name="properties">
          <!-- config params as the following -->
          <value>
            param1=value1
          </value>
        </property>
      </bean>
    </array>
  </property>
</bean>
```



```
<!-- 配置分页插件 -->
    <plugins>
        <plugin interceptor="com.github.pagehelper.PageInterceptor">
            <!-- 设置数据库类型 Oracle,Mysql,MariaDB,SQLite,Hsqldb,PostgreSQL六种数据库-->
            <property name="helperDialect" value="mysql"/>
        </plugin>
    </plugins>
```



```
<select id="selectByPageAndSelections" resultMap="BaseResultMap">
        SELECT *
        FROM doc
        ORDER BY doc_abstract
    </select>
```


```
@Service
public class DocServiceImpl implements IDocService {
    @Autowired
    private DocMapper docMapper;

    @Override
    public PageInfo<Doc> selectDocByPage1(int currentPage, int pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        List<Doc> docs = docMapper.selectByPageAndSelections();
        PageInfo<Doc> pageInfo = new PageInfo<>(docs);
        return pageInfo;
    }
}
```
##  什么时候会导致不安全的分页？

PageHelper 方法使用了静态的 ThreadLocal 参数，分页参数和线程是绑定的。

只要你可以保证在 PageHelper 方法调用后紧跟 MyBatis 查询方法，这就是安全的。因为 PageHelper 在 finally 代码段中自动清除了 ThreadLocal 存储的对象。

如果代码在进入 Executor 前发生异常，就会导致线程不可用，这属于人为的 Bug（例如接口方法和 XML 中的不匹配，导致找不到 MappedStatement 时）， 这种情况由于线程不可用，也不会导致 ThreadLocal 参数被错误的使用。

但是如果你写出下面这样的代码，就是不安全的用法：
```
PageHelper.startPage(1, 10);
List<Country> list;
if(param1 != null){
    list = countryMapper.selectIf(param1);
} else {
    list = new ArrayList<Country>();
}
```
这种情况下由于 param1 存在 null 的情况，就会导致 PageHelper 生产了一个分页参数，但是没有被消费，这个参数就会一直保留在这个线程上。当这个线程再次被使用时，就可能导致不该分页的方法去消费这个分页参数，这就产生了莫名其妙的分页。

上面这个代码，应该写成下面这个样子：
```
List<Country> list;
if(param1 != null){
    PageHelper.startPage(1, 10);
    list = countryMapper.selectIf(param1);
} else {
    list = new ArrayList<Country>();
}
```
这种写法就能保证安全。

如果你对此不放心，你可以手动清理 ThreadLocal 存储的分页参数，可以像下面这样使用：
```
List<Country> list;
if(param1 != null){
    PageHelper.startPage(1, 10);
    try{
        list = countryMapper.selectAll();
    } finally {
        PageHelper.clearPage();
    }
} else {
    list = new ArrayList<Country>();
}
```
这么写很不好看，而且没有必要。

## 参考

- [Mybatis分页插件PageHelper简单使用](https://www.cnblogs.com/ljdblog/p/6725094.html)
