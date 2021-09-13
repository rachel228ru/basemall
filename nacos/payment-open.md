#### payment-open

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
  
#过滤表
gruul:
  tenant:
    use_shop: false
    tables:
      - t_payment
      - t_payment_wechat
      - t_payment_record

pay:
  notify: "https://域名/api/payment-open/notify"
  workerId: 1
  datacenterId: 1
  ipsWorkerId: 2
  ipsDatacenterId: 2
  ipsVersion: 2.0.0
  ipsURL: https://api.ips.com.cn
  ipsTradePlatformPaySubUrl: /trade/platform/pay
  ipsNotify: https://域名/api/payment-open/ips_notify
  wxRefundUrl: https://域名/api/payment-open/wx_refund_notify

xxl:
  job:
    accessToken:
    admin:
      addresses: http://127.0.0.1:9010/xxl-job-admin/
    executor:
       ip:
       appname: payment-job-open
       port: 12000
       logpath: /tmp/logs/gruul-payment/xxl-job/jobhandler
       logretentiondays: -1
monitor:
  useRun: true  #是否使用心跳监听
  useLog: true  #是否打印日志
  businessName: ""   #基础库名(baseType=public时可以为空,不填写)
  applicationName: "支付服务"  #服务名称
  baseType: "public"     #基础库类型  public-支撑基础库   business-业务基础库
  serviceType: "universalService" #服务类型    universalService-通用服务  commissionService-定制服务


```

