### oss-open

#### nacos配置

```yaml
server:
  port: 10300

# 数据源
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    druid:
      driver-class-name: com.mysql.cj.jdbc.Driver
      username: 
      password: 
      url: jdbc:mysql://127.0.0.1:3306/gruul_open?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true

# Logger Config
logging:
  level:
    root: info
    com.baomidou: debug
    com.medusa: debug
    
monitor:
  useRun: true  #是否使用心跳监听
  useLog: false  #是否打印日志
  businessName: ""   #基础库名(baseType=public时可以为空,不填写)
  applicationName: "oss"  #服务名称
  baseType: "public"     #基础库类型  public-支撑基础库   business-业务基础库
  serviceType: "universalService" #服务类型    universalService-通用服务  commissionService-定制服务


```

