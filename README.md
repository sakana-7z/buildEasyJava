### 说明

这是一个SpringBoot生成项目 作为学习使用

大部分参考自：https://www.bilibili.com/video/BV1EN4y1c7sL

这个仅作为个人学习使用 目前市面上有更好的工具





### 配置

#### easyJava

##### application.properties

在这个文件中配置路径

```properties
suffix.bean.param=Query

#参数模糊搜索后缀
suffix.bean.query.fuzzy=Fuzzy
#参数日期起止
suffix.bean.query.time.start=Start
suffix.bean.query.time.end=End

#mapper的后缀
suffix.mapper=Mapper

# 基础路径
path.base=E:/JAVA/workspace-EasyJava/easyJava-demo/src/main
# 包基础路径
package.base=com.example
package.po=entity.po
package.query=entity.query
package.result=entity
package.mapper=mapper
package.service=service
package.service.impl=service.impl
package.controller=controller


#作者名称
author.name=卖火柴的屑魔女
```





#### 目标项目

先创建项目

##### application.properties

```properties
#在生成的springBoot项目中进行配置
#这里写一个简单的基本配置
spring.application.name=demo

#数据库配置
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/mybatis
spring.datasource.username=root
spring.datasource.password=123456

#匹配驼峰
mybatis.configuration.map-underscore-to-camel-case=true
```



### else

功能并没有完全实现 属于半成品  代码也有很多冗余 生成的格式大概也并不规范

后续改进








