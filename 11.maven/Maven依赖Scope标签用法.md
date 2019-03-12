# Maven依赖Scope标签用法


在一个maven项目中，如果存在编译需要而发布不需要的jar包，可以用scope标签，值设为provided。如下：
```
<dependency>
     <groupId>javax.servlet.jsp</groupId>
     <artifactId>jsp-api</artifactId>
     <version>2.1</version>
     <scope>provided</scope>
</dependency>
```
scope的其他参数如下：

## compile
默认的scope，表示 dependency 都可以在生命周期中使用。而且，这些dependencies 会传递到依赖的项目中。适用于所有阶段，会随着项目一起发布

>This is the default scope, used if none is specified. Compile dependencies are available in all classpaths of a project. Furthermore, those dependencies are propagated to dependent projects.

## provided
跟compile相似，但是表明了dependency 由JDK或者容器提供，例如Servlet AP和一些Java EE APIs。这个scope 只能作用在编译和测试时，同时没有传递性。
>This is much like compile, but indicates you expect the JDK or a container to provide the dependency at runtime. For example, when building a web application for the Java Enterprise Edition, you would set the dependency on the Servlet API and related Java EE APIs to scope provided because the web container provides those classes. This scope is only available on the compilation and test classpath, and is not transitive.

## runtime
表示dependency不作用在编译时，但会作用在运行和测试时，如JDBC驱动，适用运行和测试阶段。
>This scope indicates that the dependency is not required for compilation, but is for execution. It is in the runtime and test classpaths, but not the compile classpath.

## test
表示dependency作用在测试时，不作用在运行时。 只在测试时使用，用于编译和运行测试代码。不会随项目发布。
>This scope indicates that the dependency is not required for normal use of the application, and is only available for the test compilation and execution phases. This scope is not transitive.

## system
跟provided 相似，但是在系统中要以外部JAR包的形式提供，maven不会在repository查找它。

>This scope is similar to provided except that you have to provide the JAR which contains it explicitly. The artifact is always available and is not looked up in a repository.


## import
>This scope is only supported on a dependency of type pom in the <dependencyManagement> section. It indicates the dependency to be replaced with the effective list of dependencies in the specified POM's <dependencyManagement> section. Since they are replaced, dependencies with a scope of import do not actually participate in limiting the transitivity of a dependency.
