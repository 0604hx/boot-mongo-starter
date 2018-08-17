# 通用项目脚手架
> 基于`spring boot`的项目脚手架，since `2017年8月22日`

## 分支描述

`mysql`分支集成了对 MySQL 的操作（同时也适用于其他 SQL 数据库）。

默认的主键类型为 `Long`

**注意**

1. 此分支下的代码使用纯`Java`进行编写

### RememberME功能

默认情况下`RememberMe`功能为关闭，详见：`com/zeus/web/security/AuthConfig.java`

一旦此功能开启，将使用数据库保存 Token，此时需要先建立一个数据表`persistent_logins`，建表语句在
`org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl` 中可以找到：

```sql
create table persistent_logins (username varchar(64) not null, series varchar(64) primary key,token varchar(64) not null, last_used timestamp not null)
```

## 如何使用

按照`commons-example`中的例子就行展开即可。

> 如何运行 `commons-example`

1. 创建`test`数据库，如果需要使用 RememberMe 功能，需要创建表 `persistent_logins`
2. 运行 `com.zeus.example.ExampleApplication`
3. 用浏览器打开 `http://localhost:8080/login` 进行登录

## 分支列表

分支 | 技术栈 | 备注 
---------|----------|---------
master| mongodb、security|
mysql|MySQL、security|同时适用于其他`SQL`数据库
mysql-jwt|MySQL、security、JWT|同时适用于其他`SQL`数据库


## 附录

### 参考资料

[使用 Spring Data JPA 简化 JPA 开发](https://www.ibm.com/developerworks/cn/opensource/os-cn-spring-jpa/)

[SpringSecurity remember-me 功能](http://www.cnblogs.com/1xin1yi/p/7389160.html)

### LOGS

**2017年8月22日**

1. 创建分支