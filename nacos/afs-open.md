###  gruul-afs-open

```
售后服务模块
```

#### nacos配置

```yaml
version: 0.2
# 数据源
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    druid:
      driver-class-name: com.mysql.cj.jdbc.Driver
      username: 
      password: 
      url: jdbc:mysql://127.0.0.1:3306/gruul_open?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true
  rabbitmq:
    host: 127.0.0.1
    port: 5672
    username: 
    password: 
    publisher-confirms: true
    publisher-returns: true
    listener:
      simple:
        acknowledge-mode: manual
gruul:
  tenant:
    use-shop: true

# Logger Config
logging:
  level:
    root: info
    com.baomidou: debug
    com.medusa: debug
monitor:
  useRun: false  #是否使用心跳监听
  useLog: false  #是否打印日志
  applicationName: "售后服务"  #服务名称
  baseType: "business"     #基础库类型  public-支撑基础库   business-业务基础库
  serviceType: "universalService" #服务类型    universalService-通用服务  commissionService-定制服务

```

