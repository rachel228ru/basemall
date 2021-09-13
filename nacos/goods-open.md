#### goods-open

##### nacos配置

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
    # publisher-confirms: true
    # publisher-returns: true
    # listener:
    #   simple:
    #     acknowledge-mode: manual

# 是否启动LCN负载均衡策略(优化选项，开启与否，功能不受影响)
tx-lcn:
  logger:
    enabled: true
    driver-class-name: ${spring.datasource.druid.driver-class-name}
    jdbc-url: ${spring.datasource.druid.url}
    username: ${spring.datasource.druid.username}
    password: ${spring.datasource.druid.password}
  ribbon:
    ReadTimeout: 30000
    ConnectTimeout: 30000
    loadbalancer:
      dtx:
        enabled: true
# tx-manager 的配置地址，可以指定TM集群中的任何一个或多个地址
# tx-manager 下集群策略，每个TC都会从始至终<断线重连>与TM集群保持集群大小个连接。
# TM方，每有TM进入集群，会找到所有TC并通知其与新TM建立连接。
# TC方，启动时按配置与集群建立连接，成功后，会再与集群协商，查询集群大小并保持与所有TM的连接
  client:
    manager-address: 
redis:
  network:
    isintra: false
  intranet:
    host: 127.0.0.1
  outernet:
    host: 127.0.0.1
  port: 6378
  timeout: 3000
  password: 
  database: 0
  listFlush: true   #list记录根据添加修改删除自动清除
  expireTime: 900     #缓存有效时间15分钟
  expireTimeShiro: 1800     #缓存有效时间30分钟 
monitor:
  useRun: true  #是否使用心跳监听
  useLog: false  #是否打印日志
  applicationName: "商品服务"  #服务名称
  baseType: "business"     #基础库类型  public-支撑基础库   business-业务基础库
  serviceType: "universalService" #服务类型    universalService-通用服务  commissionService-定制服务

