### sms-open

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
    port: 5673
    username: 
    password: 
    
rabbitmq:
  host: 127.0.0.1
  username: 
  password: 
  port: 5673

# 是否启动LCN负载均衡策略(优化选项，开启与否，功能不受影响)
tx-lcn:
  logger:
    enabled: true
    driver-class-name: ${spring.datasource.druid.driver-class-name}
    jdbc-url: ${spring.datasource.druid.url}
    username: ${spring.datasource.druid.username}
    password: ${spring.datasource.druid.password}
  ribbon:
    loadbalancer:
      dtx:
        enabled: true
# tx-manager 的配置地址，可以指定TM集群中的任何一个或多个地址
# tx-manager 下集群策略，每个TC都会从始至终<断线重连>与TM集群保持集群大小个连接。
# TM方，每有TM进入集群，会找到所有TC并通知其与新TM建立连接。
# TC方，启动时按配置与集群建立连接，成功后，会再与集群协商，查询集群大小并保持与所有TM的连接
  client:
    manager-address: 

# Logger Config
logging:
  level:
    root: info
    com.baomidou: debug
    com.medusa: debug

xxl:
  job:
    accessToken:
    admin:
      addresses: http://127.0.0.1:9010/xxl-job-admin/
    executor:
       ip:
       appname: sms-job-open
       port: 12000
       logpath: /tmp/logs/gruul-sms-open/xxl-job/jobhandler
       logretentiondays: -1
monitor:
  useRun: true  #是否使用心跳监听
  useLog: true  #是否打印日志
  businessName: ""   #基础库名(baseType=public时可以为空,不填写)
  applicationName: "短信服务"  #服务名称
  baseType: "public"     #基础库类型  public-支撑基础库   business-业务基础库
  serviceType: "universalService" #服务类型    universalService-通用服务  commissionService-定制服务


```

