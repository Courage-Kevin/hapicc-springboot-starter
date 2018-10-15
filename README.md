# Spring Boot Getting Started



# 目录

- [1. 构建 Spring Boot 工程](#1-构建-spring-boot-工程)

- [2. Spring Boot 接口返回 JSON](#2-spring-boot-接口返回-json)
  - [2.1 Spring MVC 的 Controller](#21-spring-mvc-的-controller)
  - [2.2 Spring Boot 的 Controller](#22-spring-boot-的-controller)
  - [2.3 在实体类中使用注解对 JSON 响应数据进行控制](#23-在实体类中使用注解对-json-响应数据进行控制)

- [3. Spring Boot 热部署](#3-spring-boot-热部署)
  - [3.1 添加依赖](#31-添加依赖)
  - [3.2 在 application.properties 配置文件中进行配置](#32-在-applicationproperties-配置文件中进行配置)

- [4. Spring Boot 资源属性配置](#4-spring-boot-资源属性配置)
  - [4.1 Spring Boot 资源文件属性配置](#41-spring-boot-资源文件属性配置)
     - [4.1.1 添加依赖](#411-添加依赖)
     - [4.1.2 在资源文件中添加属性配置项](#412-在资源文件中添加属性配置项)
     - [4.1.3 创建实体类](#413-创建实体类)
     - [4.1.4 在 Controller 中注入](#414-在-controller-中注入)
  - [4.2 Spring Boot 资源文件配置 Server 及 Tomcat](#42-spring-boot-资源文件配置-server-及-tomcat)
     - [4.2.1 Server 服务端相关配置](#421-server-服务端相关配置)
     - [4.2.2 Server Tomcat 相关常用配置](#422-server-tomcat-相关常用配置)

- [5. Spring Boot 整合模板引擎](#5-spring-boot-整合模板引擎)
  - [5.1 Spring Boot 整合 FreeMarker](#51-spring-boot-整合-freemarker)
     - [5.1.1 添加依赖](#511-添加依赖)
     - [5.1.2 静态资源配置](#512-静态资源配置)
     - [5.1.3 创建 ftl 模板文件](#513-创建-ftl-模板文件)
     - [5.1.4 在 Controller 中返回模板文件](#514-在-controller-中返回模板文件)
  - [5.2 Spring Boot 整合 Thymeleaf](#52-spring-boot-整合-thymeleaf)
     - [5.2.1 添加依赖](#521-添加依赖)
     - [5.2.2 静态资源配置](#522-静态资源配置)
     - [5.2.3 创建 html 模板文件](#523-创建-html-模板文件)
     - [5.2.4 在 Controller 中返回模板文件](#524-在-controller-中返回模板文件)

- [6. Spring Boot 配置全局的异常捕获](#6-spring-boot-配置全局的异常捕获)
  - [6.1 Web 形式](#61-web-形式)
     - [6.1.1 创建 error 页面](#611-创建-error-页面)
     - [6.1.2 创建全局异常处理类](#612-创建全局异常处理类)
  - [6.2 Ajax 形式](#62-ajax-形式)
     - [6.2.1 创建 error 页面](#621-创建-error-页面)
     - [6.2.2 创建全局异常处理类](#622-创建全局异常处理类)
  - [6.3 兼容 Web 与 Ajax 形式](#63-兼容-web-与-ajax-形式)

- [7. Spring Boot 整合 MyBatis](#7-spring-boot-整合-mybatis)
  - [7.1 通过 MyBatis Generator 自动生成 pojo 及 mapper](#71-通过-mybatis-generator-自动生成-pojo-及-mapper)
     - [7.1.1 添加依赖](#711-添加依赖)
     - [7.1.2 创建 MyMapper 接口](#712-创建-mymapper-接口)
     - [7.1.3 在 application.properties 配置文件中进行配置](#713-在-applicationproperties-配置文件中进行配置)
     - [7.1.4 创建 MySQL 示例数据表](#714-创建-mysql-示例数据表)
     - [7.1.5 创建 MyBatis Generator (MBG) 配置文件](#715-创建-mybatis-generator-mbg-配置文件)
     - [7.1.6 自动生成 pojo 及 mapper](#716-自动生成-pojo-及-mapper)
  - [7.2 基于 MyBatis Mapper 实现基本的 CRUD 功能](#72-基于-mybatis-mapper-实现基本的-crud-功能)
     - [7.2.1 设置服务器时区](#721-设置服务器时区)
     - [7.2.2 引入第三方工具包](#722-引入第三方工具包)
     - [7.2.3 扫描 MyBatis Mapper 包路径](#723-扫描-mybatis-mapper-包路径)
     - [7.2.4 实现基本的 CRUD 功能](#724-实现基本的-crud-功能)
  - [7.3 基于 MyBatis PageHelper 实现分页功能](#73-基于-mybatis-pagehelper-实现分页功能)
  - [7.4 自定义 Mapper 的实现](#74-自定义-mapper-的实现)
  - [7.5 Spring Boot 整合持久层事务](#75-spring-boot-整合持久层事务)
     - [7.5.1 MySQL 中事务的隔离级别](#751-mysql-中事务的隔离级别)
     - [7.5.2 Spring 定义的事务传播行为](#752-spring-定义的事务传播行为)
     - [7.5.3 Spring Boot 中进行事务设置](#753-spring-boot-中进行事务设置)

- [8. Spring Boot 整合 Redis](#8-spring-boot-整合-redis)
  - [8.1 添加依赖](#81-添加依赖)
  - [8.2 在 application.properties 配置文件中进行配置](#82-在-applicationproperties-配置文件中进行配置)
  - [8.3 创建 Redis Cache 配置类](#83-创建-redis-cache-配置类)
  - [8.4 创建 RedisService 服务类](#84-创建-redisservice-服务类)

- [9. Spring Boot 整合定时任务](#9-spring-boot-整合定时任务)
  - [9.1 开启定时任务](#91-开启定时任务)
  - [9.2 创建定时任务](#92-创建定时任务)

- [10. Spring Boot 整合异步任务](#10-spring-boot-整合异步任务)
  - [10.1 开启异步任务](#101-开启异步任务)
  - [10.2 创建异步任务](#102-创建异步任务)

- [11. Spring Boot 使用拦截器](#11-spring-boot-使用拦截器)
  - [11.1 创建拦截器](#111-创建拦截器)
  - [11.2 添加拦截器](#112-添加拦截器)

- [12 Spring Boot 默认日志 Logback 配置](#12-spring-boot-默认日志-logback-配置)
  - [12.1 添加依赖](#121-添加依赖)
  - [12.2 使用 Logback 打印日志的基本方式](#122-使用-logback-打印日志的基本方式)
  - [12.3 使用 Logback 打印日志的简单方式](#123-使用-logback-打印日志的简单方式)
     - [12.3.1 安装 Lombok 插件](#1231-安装-lombok-插件)
     - [12.3.2 引入 Lombok 依赖](#1232-引入-lombok-依赖)
     - [12.3.3 打印日志](#1233-打印日志)



## 1. 构建 Spring Boot 工程

[Spring Initializer](https://start.spring.io)

[Spring Boot Reference Guide](https://docs.spring.io/spring-boot/docs/current/reference/html/)

添加 Web 依赖：

```xml
<!-- Spring Boot Web -->
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

## 2. Spring Boot 接口返回 JSON

### 2.1 Spring MVC 的 Controller

```java
@Controller
@RequestMapping("/user")
public class SpringMVCUserController {

    @RequestMapping("/getUser")
    @ResponseBody
    public User getUser() {
        User user = new User();
        // Set user properties...
        return user;
    }
}
```

### 2.2 Spring Boot 的 Controller

```java
@RestController   // @RestController = @Controller + @ResponseBody
@RequestMapping("/user")
public class SpringBootUserController {

    @RequestMapping("/getUser")
    public User getUser() {
        User user = new User();
        // Set user properties...
        return user;
    }
}
```

### 2.3 在实体类中使用注解对 JSON 响应数据进行控制

1. 隐藏属性字段

类注解：`@JsonIgnoreProperties({ "propName1", "propName2", ... })`

属性注解：`@JsonIgnore`

2. 隐藏值为空或 `null` 的属性字段

类注解 & 属性注解：`@JsonInclude(Include.NON_EMPTY)`, `@JsonInclude(Include.NON_NULL)`

3. 格式化 Date 类型的属性字段

属性注解：`@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", locale = "zh", timezone = "UTC")`

## 3. Spring Boot 热部署

### 3.1 添加依赖

```xml
<!-- 热部署 -->
<!-- devtools 可以实现页面热部署（即页面修改后会立即生效，这个可以直接在 application.properties 文件中配置 spring.thymeleaf.cache=false 来实现） -->
<!-- 实现类文件热部署（类文件修改后不会立即生效），实现对属性文件的热部署 -->
<!-- 即 devtools 会监听 classpath 下的文件变动，并且会立即重启应用（发生在保存时），注意：因为其采用的虚拟机机制，该项重启是很快的 -->
<!-- (1) base classloader (Base 类加载)：加载不可变的 Class，例如第三方提供的 jar 包 -->
<!-- (2) restart classloader (Restart 类加载器)：加载正在开发的 Class -->
<!-- 为什么重启很快，因为重启的时候只是加载了正在开发的 Class，没有重新加载第三方的 jar 包 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <!-- optional=true, 依赖不会传递, 该项目依赖 devtools, 之后依赖该项目的项目如果想要使用 devtools, 需要重新引入 -->
    <optional>true</optional>
</dependency>
```

### 3.2 在 application.properties 配置文件中进行配置

```properties
###############################################################
# DevTools 热部署相关配置
###############################################################
# 打开 devtools 热部署
spring.devtools.restart.enabled=true
# 设置重启目录，该目录下的文件更新时重启
spring.devtools.restart.additional-paths=src/main/java
# 设置不需要重启的目录，该目录下的文件更新时不重启
#spring.devtools.restart.exclude=static/**, public/**, WEB-INF/**
```

## 4. Spring Boot 资源属性配置

### 4.1 Spring Boot 资源文件属性配置

#### 4.1.1 添加依赖

```xml
<!-- 资源文件属性配置 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>
```

#### 4.1.2 在资源文件中添加属性配置项

以 `resource.properties` 资源文件为例：

```properties
com.cc.opensource.name=cc
com.cc.opensource.website=github.com/vveicc
com.cc.opensource.language=java
```

#### 4.1.3 创建实体类

以 `Resource` 类为例：

```java
@Configuration
@ConfigurationProperties(prefix = "com.cc.opensource")
@PropertySource("classpath:resource.properties")
public class Resource {

    private String name;
    private String website;
    private String language;
    
    // getter and setter...
}
```

#### 4.1.4 在 Controller 中注入

```java
@Autowired
private Resource resource;
```

### 4.2 Spring Boot 资源文件配置 Server 及 Tomcat

#### 4.2.1 Server 服务端相关配置

```properties
###############################################################
# Server 服务端相关配置
###############################################################
# 端口号
#server.port=8888
# 配置 context-path，一般来说在正式发布时不进行此配置
#server.servlet.context-path=/vveicc
# 错误页，指定发生错误时跳转的 url --> BasicErrorController
#server.error.path=/error
# session 超时时间（分钟），默认为 30 分钟
#server.servlet.session.timeout=60
# 给服务绑定 IP 地址，启动服务器时，如果本机不是该 IP 地址则抛出异常启动失败
# 只有特殊情况下才进行配置，具体根据业务需求来配置
#server.address=192.168.0.100
```

#### 4.2.2 Server Tomcat 相关常用配置

```properties
###############################################################
# Server - tomcat 相关常用配置
###############################################################
# tomcat 最大线程数，默认为 200
#server.tomcat.max-threads=120
# tomcat 的 uri 编码
#server.tomcat.uri-encoding=UTF-8
# 存放 tomcat 的日志、Dump 等文件的临时文件夹，默认为系统的 tmp 文件夹
#server.tomcat.basedir=/tmp/springboot-tomcat-tmp
# 打开 tomcat 的 Access 日志，并设置日志的格式
#server.tomcat.accesslog.enabled=true
#server.tomcat.accesslog.pattern=
# accesslog 目录，默认在 basedir/logs
#server.tomcat.accesslog.directory=
# 日志文件目录
#logging.path=/tmp/springboot-tomcat-tmp
# 日志文件名称
#logging.file=myapp.log
```

## 5. Spring Boot 整合模板引擎

### 5.1 Spring Boot 整合 FreeMarker

#### 5.1.1 添加依赖

```xml
<!-- 引入 FreeMarker 模板依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-freemarker</artifactId>
</dependency>
```

#### 5.1.2 静态资源配置

```properties
###############################################################
# FreeMarker 静态资源配置
###############################################################
# 设定模板文件路径
spring.freemarker.template-loader-path=classpath:templates
# 关闭缓存，即时刷新，正式发布时需要改为 true
spring.freemarker.cache=false
spring.freemarker.charset=UTF-8
spring.freemarker.check-template-location=true
spring.freemarker.content-type=text/html; charset=utf-8
spring.freemarker.expose-request-attributes=true
spring.freemarker.expose-session-attributes=true
spring.freemarker.request-context-attribute=request
spring.freemarker.suffix=.ftl
```

#### 5.1.3 创建 ftl 模板文件

在 `src/main/resources/templates/freemarker` 目录下创建 `index.ftl` 文件：

```html
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8" />
    <title></title>
</head>
<body>
    FreeMarker 模板引擎
    <h1>Index page</h1>
    <hr/>
    <p>Name: <i>${resource.name}</i></p>
    <p>Website: <i>${resource.website}</i></p>
    <p>Language: <i>${resource.language}</i></p>
</body>
</html>
```

#### 5.1.4 在 Controller 中返回模板文件

```java
@Controller
@RequestMapping("/ftl")
public class FreeMarkerController {

    @Autowired
    private Resource resource;

    @RequestMapping("/index")
    public String index(ModelMap map) {
        map.addAttribute("resource", resource);
        return "freemarker/index";
    }
}
```

### 5.2 Spring Boot 整合 Thymeleaf

#### 5.2.1 添加依赖

```xml
<!-- 引入 Thymeleaf 模板依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

#### 5.2.2 静态资源配置

```properties
###############################################################
# Thymeleaf 静态资源配置
###############################################################
# 设定模板文件路径
spring.thymeleaf.prefix=classpath:templates/
# 关闭缓存，即时刷新，正式发布时需要改为 true
spring.thymeleaf.cache=false
spring.thymeleaf.mode=HTML
spring.thymeleaf.encoding=UTF-8
spring.thymeleaf.servlet.content-type=text/html; charset=utf-8
spring.thymeleaf.suffix=.html
```

#### 5.2.3 创建 html 模板文件

在 `src/main/resources/templates/thymeleaf` 目录下创建 `index.html` 文件：

```html
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8" />
    <title></title>
</head>
<body>
    Thymeleaf 模板引擎
    <h1>Index page</h1>
    <hr/>
    <p>Name: <i th:text="${resource.name}">this is a name.</i></p>
    <p>Website: <i th:text="${resource.website}">this is a website.</i></p>
    <p>Language: <i th:text="${resource.language}">this is a language.</i></p>
</body>
</html>
```

#### 5.2.4 在 Controller 中返回模板文件

```java
@Controller
@RequestMapping("/th")
public clacc ThymeleafController {

    @Autowired
    private Resource resource;

    @RequestMapping("/index")
    public String index(ModelMap map) {
        map.addAttribute("resource", resource);
        return "thymeleaf/index";
    }
}
```

## 6. Spring Boot 配置全局的异常捕获

### 6.1 Web 形式

#### 6.1.1 创建 error 页面

在 `src/main/resources/templates` 目录下创建 `error.html` 文件：

```html
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>￣□￣｜｜</title>
</head>
<body>
    <h1>￣□￣｜｜ 好像出错了。。。</h1>
    <hr>
    <p>Url: <i th:text="${url}"></i></p>
    <p>Errmsg: <i th:text="${exception.message}"></i></p>
</body>
</html>
```

#### 6.1.2 创建全局异常处理类

```java
@Order(100)
@ControllerAdvice
public class HapiccWebExceptionHandler {

    public static final String DEFAULT_ERROR_VIEW = "error";

    @ExceptionHandler(value = Exception.class)
    public Object errorHandler(HttpServletRequest request, HttpServletResponse reponse, Exception e) throws Exception {

        e.printStackTrace();

        ModelAndView mav = new ModelAndView();
        mav.addObject("url", request.getRequestURL());
        mav.addObject("exception", e);
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;
    }
}
```

### 6.2 Ajax 形式

#### 6.2.1 创建 error 页面

在 `src/main/resources/templates/thymeleaf` 目录下创建 `ajaxerror.html` 文件：

```html
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title></title>

    <script th:src="@{/static/js/jquery.min.js}"></script>
</head>
<body>
    <h1>￣□￣｜｜ Ajax 错误异常。。。</h1>
    <hr>

    <script th:src="@{/static/js/ajaxerror.js}"></script>
</body>
</html>
```

在 `src/main/resources/static/js` 目录下创建 `ajaxerror.js` 文件：

```javascript
$.ajax({
    url : "/err/getAjaxerror",
    type : "POST",
    async : false,
    success : function(data) {
//        debugger;
        if (data.status == 200 && data.msg == "OK") {
            alert("success");
        } else {
            alert("发生异常：" + data.msg);
        }
    },
    error : function(response, ajaxOptions, thrownError) {
        alert("Error occurred!");
    }
});
```

#### 6.2.2 创建全局异常处理类

```java
@Order(10)
//@ControllerAdivce
@RestControllerAdvice   // @RestControllerAdvice = @ControllerAdvice + @ResponseBody
public class HapiccAjaxExceptionHandler {

    @ExceptionHandler(value = Exception.class)
//  @ResponseBody
    public HapiccJSONResult errorHandler(HttpServletRequest request, Exception e) throws Exception {

        e.printStackTrace();

        return HapiccJSONResult.errorException(e.getMessage());
    }
}
```

### 6.3 兼容 Web 与 Ajax 形式

创建通用全局异常处理类：

```java
@Order(0)
@ControllerAdvice
public class HapiccExceptionHandler {

    public static final String DEFAULT_ERROR_VIEW = "error";

    @ExceptionHandler(Exception.class)
    public Object errorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {

        e.printStackTrace();

        if (isAjax(request)) {
            return HapiccJSONResult.errorException(e.getMessage());
        } else {
            ModelAndView mav = new ModelAndView();
            mav.addObject("url", request.getRequestURL());
            mav.addObject("exception", e);
            mav.setViewName(DEFAULT_ERROR_VIEW);
            return mav;
        }
    }
    
    public static boolean isAjax(HttpServletRequest request) {
        return request.getHeader("X-Requested-With") != null 
                && request.getHeader("X-Requested-With").equals("XMLHttpRequest");
    }
}
```

## 7. Spring Boot 整合 MyBatis

### 7.1 通过 MyBatis Generator 自动生成 pojo 及 mapper

#### 7.1.1 添加依赖

```xml
<!-- Spring Boot 整合 MyBatis -->
<!-- MySQL Connector & Druid -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.1.10</version>
</dependency>
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>1.1.10</version>
</dependency>
<!-- MyBatis & Mapper & PageHelper，参考：https://github.com/abel533/MyBatis-Spring-Boot -->
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>1.3.1</version>
</dependency>
<dependency>
    <groupId>tk.mybatis</groupId>
    <artifactId>mapper-spring-boot-starter</artifactId>
    <version>1.2.4</version>
</dependency>
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper-spring-boot-starter</artifactId>
    <version>1.2.3</version>
</dependency>
<!-- MyBatis Generator -->
<dependency>
    <groupId>org.mybatis.generator</groupId>
    <artifactId>mybatis-generator-core</artifactId>
    <version>1.3.7</version>
    <scope>compile</scope>
    <optional>true</optional>
</dependency>
```

#### 7.1.2 创建 MyMapper 接口

在后续配置中会使用 MyMapper 接口：

```java
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {

    // TODO
    // FIXME 特别注意，该接口不能被扫描到，否则会出错
}
```

#### 7.1.3 在 application.properties 配置文件中进行配置

DevTools 热部署相关配置中添加 MyBatis 相关配置：

```properties
# 为 MyBatis 配置，生产环境可删除
restart.include.mapper=/mapper-[\\w-\\.]+jar
restart.include.pagehelper=/pagehelper-[\\w-\\.]+jar
```

Druid 数据源相关配置：

```properties
###############################################################
# Druid 数据源相关配置
# 参考：https://github.com/alibaba/druid/tree/master/druid-spring-boot-starter
###############################################################
spring.datasource.url=jdbc:mysql://localhost:3306/hapicc?useUnicode=yes&characterEncoding=UTF-8&useLegacyDatetimeCode=false&serverTimezone=UTC&zeroDateTimeBehavior=convertToNull
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.druid.initial-size=1
spring.datasource.druid.min-idle=1
spring.datasource.druid.max-active=20
spring.datasource.druid.test-on-borrow=true
```

MyBatis，Mapper，PageHelper 相关配置：

```properties
###############################################################
# MyBatis，Mapper，PageHelper 相关配置
# 参考：https://github.com/abel533/MyBatis-Spring-Boot
###############################################################
# MyBatis
mybatis.type-aliases-package=com.hapicc.pojo
mybatis.mapper-locations=classpath:mappers/*.xml

# Mapper
# mappers 多个接口时逗号隔开
mapper.mappers=com.hapicc.utils.generator.MyMapper
mapper.not-empty=false
mapper.identity=MYSQL

# PageHelper
pagehelper.helperDialect=mysql
pagehelper.reasonable=true
pagehelper.supportMethodsArguments=true
pagehelper.params=count=countSql
```

#### 7.1.4 创建 MySQL 示例数据表

创建 `hapicc` 数据库：

```mysql
CREATE DATABASE IF NOT EXISTS hapicc DEFAULT CHARSET utf8mb4 COLLATE utf8mb4-general-ci;
```

在 `hapicc` 数据库中创建 `sys_user` 数据表：

```mysql
USE hapicc;
CREATE TABLE `sys_user` (
  `id` varchar(32) NOT NULL,
  `img` varchar(512) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `login_name` varchar(128) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `gender` tinyint(4) NOT NULL DEFAULT '0',
  `phone` varchar(255) DEFAULT NULL,
  `phone_verified` bit(1) DEFAULT b'0',
  `email` varchar(255) DEFAULT NULL,
  `email_verified` bit(1) DEFAULT b'0',
  `date_created` datetime NOT NULL,
  `last_updated` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

注：此处创建的 `sys_user` 数据表仅供后续自动生成 pojo 及 mapper 使用。

#### 7.1.5 创建 MyBatis Generator (MBG) 配置文件

在 `src/main/resources/gen` 目录下创建 `generatorConfig.xml` MBG 配置文件：

```xml
<?xml version="1.0" encoding="UTF-8"?>

<!-- 参考：http://www.mybatis.org/generator/ -->
<!-- 参考：http://mbg.cndocs.ml/ （中文文档） -->
<!-- 参考：http://www.mybatis.org/generator/configreference/xmlconfig.html -->

<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<generatorConfiguration>
    <!-- 引入配置文件 -->
    <properties resource="application.properties" />

    <context id="MysqlContext" targetRuntime="MyBatis3Simple" defaultModelType="flat">

        <property name="beginningDelimiter" value="`" />
        <property name="endingDelimiter" value="`" />

        <plugin type="tk.mybatis.mapper.generator.MapperPlugin">
            <property name="mappers" value="com.hapicc.utils.generator.MyMapper" />
        </plugin>

        <jdbcConnection
            driverClass="${spring.datasource.driver-class-name}"
            connectionURL="${spring.datasource.url}"
            userId="${spring.datasource.username}"
            password="${spring.datasource.password}">
        </jdbcConnection>

        <!-- 配置生成的 pojo 所在目录 -->
        <javaModelGenerator targetPackage="com.hapicc.pojo" targetProject="src/main/java" />

        <!-- 配置生成的 mapper 所在目录 -->
        <sqlMapGenerator targetPackage="mappers" targetProject="src/main/resources" />

        <!-- 配置生成的 mapper 对应的 java 映射所在目录 -->
        <javaClientGenerator targetPackage="com.hapicc.mappers" targetProject="src/main/java" type="XMLMAPPER" />

        <!-- 配置需要自动生成的数据库表，tableName 支持 '%'，表示全部生成 -->
        <!-- 自动生成的实体类默认使用 tableName 的驼峰形式作为类名，也可以通过 domainObjectName 指定 -->
        <!-- 关于 table 元素的详细使用请参考：http://mbg.cndocs.ml/configreference/table.html -->
        <table tableName="sys_user"></table>

    </context>
</generatorConfiguration>
```

#### 7.1.6 自动生成 pojo 及 mapper

1. 通过运行 Java 类生成

创建 `GeneratorTrigger` Java 类：

```java
public class GeneratorTrigger {

    public static void main(String[] args) {
        try {
            GeneratorTrigger trigger = new GeneratorTrigger();
            trigger.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void execute() throws Exception {

        boolean overwrite = true;
        List<String> warnings = new ArrayList<>();

        // MyBatis Generator 配置文件
        File configFile = new File("src/main/resources/gen/generatorConfig.xml");

        ConfigurationParser parser = new ConfigurationParser(warnings);
        Configuration configuration = parser.parseConfiguration(configFile);
        DefaultShellCallback shellCallback = new DefaultShellCallback(overwrite);
        MyBatisGenerator generator = new MyBatisGenerator(configuration, shellCallback, warnings);
        generator.generate(null);
    }
}
```

运行类中的 main 方法即可自动生成 pojo 及 mapper。

2. 通过运行 MVN 命令生成

在 `pom.xml` 文件中添加 MyBatis Generator 插件：

```xml
<!-- MyBatis Generator -->
<!-- 运行 `mvn mybatis-generator:generate` 命令，效果与运行 GeneratorTrigger 类相同 -->
<plugin>
    <groupId>org.mybatis.generator</groupId>
    <artifactId>mybatis-generator-maven-plugin</artifactId>
    <version>1.3.7</version>
    <configuration>
        <configurationFile>${basedir}/src/main/resources/gen/generatorConfig.xml</configurationFile>
        <overwrite>true</overwrite>
        <verbose>true</verbose>
    </configuration>
    <dependencies>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>${mysql.version}</version>
        </dependency>
        <dependency>
            <groupId>tk.mybatis</groupId>
            <artifactId>mapper</artifactId>
            <version>3.5.3</version>
        </dependency>
    </dependencies>
</plugin>
```

在工程目录下运行 `mvn mybatis-generator:generate` 命令即可自动生成 pojo 及 mapper。

### 7.2 基于 MyBatis Mapper 实现基本的 CRUD 功能

#### 7.2.1 设置服务器时区

为了统一时区，将数据库时区和服务器时区都设置为 UTC 标准时区。

在 `@SpringBootApplication` 注解的主类中设置：

```java
@PostConstruct
public void init() {
    // 设置服务器时区为 UTC 标准时区
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
}
```

#### 7.2.2 引入第三方工具包

此处仅为了演示如何引入第三方工具包，与 CRUD 功能关系不大。

为了方便 id 的生成，在 `src/main/java` 目录下引入 `org.n3r.idworker` 工具包，详见：[idworker-client](https://github.com/bingoohuang/idworker-client)。

为了演示所需，将 `org.n3r.idworker.Sid` 类中的两个类方法改为实例方法：

```java
public /*static*/ String next() {
    // ...
}

public /*static*/ String nextShort() {
    // ...
}
```

为了将 `org.n3r.idworker.Sid` 类作为组件被扫描并使用，需要给该类添加 `@Component` 注解。

程序启动时，默认只扫描 `@SpringBootApplication` 注解的主类所在包中的组件，需要给主类添加注解以扫描所有需要的包：

```java
// 扫描所有需要的包，包含一些工具包
@ComponentScan(basePackages = { "com.hapicc", "org.n3r.idworker" })
```

#### 7.2.3 扫描 MyBatis Mapper 包路径

给 `@SpringBootApplication` 注解的主类添加注解：

```java
// 扫描 MyBatis Mapper 包路径
// 注意：此处的 MapperScan 是 tk.mybatis.spring.annotation.MapperScan
@MapperScan(basePackages = { "com.hapicc.mappers" })
```

#### 7.2.4 实现基本的 CRUD 功能

请参考 `MyBatisCRUDController` 和 `UserServiceImpl` 类中的相关方法。

### 7.3 基于 MyBatis PageHelper 实现分页功能

请参考 `UserServiceImpl` 类中的 `list` 方法。

### 7.4 自定义 Mapper 的实现

创建自定义 Mapper 接口 `SysUserMapperCustom`：

```java
public interface SysUserMapperCustom extends MyMapper<SysUser> {

    List<SysUser> selectSimpleInfoById(String id);
}
```

创建自定义 Mapper 文件 `SysUserMapperCustom.xml`：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.hapicc.mapper.SysUserMapperCustom">

    <resultMap type="com.hapicc.pojo.SysUser" id="BaseResultMap">
        <id column="id" jdbcType="VARCHAR" property="id" />
        <result column="img" jdbcType="VARCHAR" property="img" />
        <result column="name" jdbcType="VARCHAR" property="name" />
        <result column="login_name" jdbcType="VARCHAR" property="loginName" />
        <result column="password" jdbcType="VARCHAR" property="password" />
        <result column="birthday" jdbcType="DATE" property="birthday" />
        <result column="gender" jdbcType="TINYINT" property="gender" />
        <result column="phone" jdbcType="VARCHAR" property="phone" />
        <result column="phone_verified" jdbcType="BIT" property="phoneVerified" />
        <result column="email" jdbcType="VARCHAR" property="email" />
        <result column="email_verified" jdbcType="BIT" property="emailVerified" />
        <result column="date_created" jdbcType="TIMESTAMP" property="dateCreated" />
        <result column="last_updated" jdbcType="TIMESTAMP" property="lastUpdated" />
    </resultMap>

    <!-- 创建 root 用户 -->
    <select id="selectSimpleInfoById"
        parameterType="java.lang.String" resultMap="BaseResultMap">
        select
            id, name, login_name, gender
        from
            sys_user
        where
            id = #{id, jdbcType=VARCHAR}
    </select>

</mapper>
```

自定义 Mapper 的使用请参考 `UserServiceImpl` 类中的 `getUserSimpleInfo` 方法。

### 7.5 Spring Boot 整合持久层事务

#### 7.5.1 MySQL 中事务的隔离级别

MySQL Transaction Isolation Level：

| 隔离级别 | 脏读(Dirty Read) | 不可重复读(NonRepeatable Read) | 幻读(Phantom Read) |
| ------ | ------ | ------ | ------ |
| READ UNCOMMITTED | 可能 | 可能 | 可能 |
| READ COMMITTED | 不可能 | 可能 | 可能 |
| REPEATABLE READ | 不可能 | 不可能 | 可能 |
| SERIALIZABLE | 不可能 | 不可能 | 不可能 |

InnoDB 默认的事务隔离级别是 REPEATABLE READ。

参考：[MySQL 四种事务隔离级的说明](http://www.cnblogs.com/zhoujinyi/p/3437475.html)

#### 7.5.2 Spring 定义的事务传播行为

事务传播行为用来描述一个由事务传播行为修饰的方法被另一个方法调用时事务的传播。

Spring Transaction Propagation：

| 事务传播行为 | 描述 |
| ------ | ------ |
| REQUIRED | 使用当前事务，如果当前没有事务，则新建事务 |
| SUPPORTS | 使用当前事务，如果当前没有事务，则以非事务方式执行 |
| MANDATORY | 使用当前事务，如果当前没有事务，则抛出异常 |
| REQUIRES_NEW | 新建事务，如果当前存在事务，则把当前事务挂起 |
| NOT_SUPPORTED | 以非事务方式执行，如果当前存在事务，则把当前事务挂起 |
| NEVER | 以非事务方式执行，如果当前存在事务，则抛出异常 |
| NESTED | 如果当前存在事务，则嵌套作为子事务执行，子事务可以单独回滚；如果当前没有事务，则新建事务 |

参考：[Spring 事务传播行为详解](https://juejin.im/entry/5a8fe57e5188255de201062b)

#### 7.5.3 Spring Boot 中进行事务设置

```java
// 事务隔离级别 isolation 默认为 REPEATABLE READ
// 事务传播行为 propagaion 默认为 REQUIRED
// 事务读写权限 readOnly 默认为 false
@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, readOnly = false)
```

## 8. Spring Boot 整合 Redis

### 8.1 添加依赖

```xml
<!-- 引入 Redis 依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
</dependency>
```

### 8.2 在 application.properties 配置文件中进行配置

```properties
###############################################################
# Redis 相关配置
###############################################################
# Redis数据库索引（默认为 0）
spring.redis.database=0
# Redis服务器地址
spring.redis.host=localhost
# Redis服务器连接端口
spring.redis.port=6379
# Redis服务器连接密码（默认为空）
spring.redis.password=
# 连接超时时间（毫秒）
spring.redis.timeout=2000
#连接池最大连接数（使用负值表示没有限制）
spring.redis.jedis.pool.max-active=8
# 连接池最大阻塞等待时间（使用负值表示没有限制）
spring.redis.jedis.pool.max-wait=-1
# 连接池中的最大空闲连接
spring.redis.jedis.pool.max-idle=8
# 连接池中的最小空闲连接
spring.redis.jedis.pool.min-idle=0
```

### 8.3 创建 Redis Cache 配置类

```java
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    @Autowired
    private RedisProperties properties;

    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMinIdle(properties.getJedis().getPool().getMinIdle());
        poolConfig.setMaxIdle(properties.getJedis().getPool().getMaxIdle());
        poolConfig.setMaxTotal(properties.getJedis().getPool().getMaxActive());
        poolConfig.setMaxWaitMillis(properties.getJedis().getPool().getMaxWait().toMillis());

        if (!StringUtils.isEmpty(properties.getPassword())) {
            return new JedisPool(poolConfig, properties.getHost(), properties.getPort(), Protocol.DEFAULT_TIMEOUT,
                    properties.getPassword());
        } else {
            return new JedisPool(poolConfig, properties.getHost(), properties.getPort());
        }
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        // 默认使用 JdkSerializationRedisSerializer
        // 此处替换为 StringRedisSerializer 和 Jackson2JsonRedisSerializer
        RedisSerializer<String> keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Object> valueSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        valueSerializer.setObjectMapper(om);

        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setHashKeySerializer(keySerializer);
        redisTemplate.setHashValueSerializer(valueSerializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
```

### 8.4 创建 RedisService 服务类

```java
@Service
public class RedisService {

    @Autowired
    private JedisPool jedisPool;

    public void withRedis(Consumer<Jedis> consumer) {
        try (Jedis jedis = jedisPool.getResource()) {
            consumer.accept(jedis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Boolean setObj(final String key, final Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean setObj(final String key, final Object value, final Long seconds) {
        try {
            redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Object getObj(final String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * Delete all keys matching the given pattern.
     * 
     * @param pattern must not be null.
     * @return The number of keys that were removed.
     */
    public Long delPattern(final String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            return redisTemplate.delete(keys);
        }
        return 0L;
    }
}
```

具体使用请参考 `TestRedisController` 类。

## 9. Spring Boot 整合定时任务

### 9.1 开启定时任务

给 `@SpringBootApplication` 注解的主类添加注解：

```java
// 开启定时任务
@EnableScheduling
```

### 9.2 创建定时任务

```java
@Component
public class TestScheduleTask {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    // 定义每隔 3 秒执行任务
//  @Scheduled(fixedRate = 3000)
    // 通过 Cron 表达式定义每隔 3 分钟执行任务
    // 在线 Cron 表达式生成：http://www.pppet.net 或 http://cron.qqe2.com
    @Scheduled(cron = "0 */3 * * * *")
    public void reportCurrentTime() {
        System.out.println("当前时间：" + sdf.format(new Date()));
    }
}
```

## 10. Spring Boot 整合异步任务

### 10.1 开启异步任务

给 `@SpringBootApplication` 注解的主类添加注解：

```java
// 开启异步任务
@EnableAsync
```

### 10.2 创建异步任务

使用 `@Component` + `@Async` 注解创建异步任务，具体演示请参考 `TestAsyncTask` 和 `TestTaskController` 类。

## 11. Spring Boot 使用拦截器

### 11.1 创建拦截器

创建实现了 `HandlerInterceptor` 接口的拦截器类：

```java
public class OneInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // do something before handle...
        if (shouldIntercept) {
            // do something...
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // TODO do something after handle but before render...
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // TODO do something after render...
    }
}
```

### 11.2 添加拦截器

创建实现了 `WebMvcConfigurer` 接口的配置类并添加拦截器：

```java
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截器按照添加顺序进行拦截
        registry.addInterceptor(new OneInterceptor()).addPathPatterns("/*/**");
        registry.addInterceptor(new TwoInterceptor()).addPathPatterns("/one/**", "/two/**");
    }
}
```

## 12 Spring Boot 默认日志 Logback 配置

### 12.1 添加依赖

构建 Spring Boot 工程默认添加的 `spring-boot-starter` 依赖中已经包含了 `spring-boot-starter-logging`，因此不用额外添加依赖。

### 12.2 使用 Logback 打印日志的基本方式

```java
public class OneInterceptor implements HandlerInterceptor {

    // 定义 Logger 对象
    private final Logger log = LoggerFactory.getLogger(OneInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 使用 Logger 对象打印日志
        log.info("被 one 拦截，放行。。。");
        return true;
    }
}
```

注：关于日志级别，请自行查阅相关资料。

### 12.3 使用 Logback 打印日志的简单方式

#### 12.3.1 安装 Lombok 插件

STS 安装 Lombok 插件：

下载 [lombok.jar](https://projectlombok.org/download)，执行 `java -jar lombok.jar` 命令进行安装。

IDEA 安装 Lombok 插件：

`Preferences...` -> `Plugins` -> `Browse repositories...`，搜索 `Lombok` 进行安装。

#### 12.3.2 引入 Lombok 依赖

```xml
<!-- 引入 Lombok 依赖 -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```

#### 12.3.3 打印日志

`@Slf4j` + `log`：

```java
@Slf4j
public class OneInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 使用 Logger 对象打印日志
        log.info("被 one 拦截，放行。。。");
        return true;
    }
}
```
