#### logistics-open

##### nacos配置

```yaml
version: 0.2
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
    template:
      default-receive-queue: wuliu_receive
  quartz:
    properties:
      org:
        quartz:
          scheduler:
            instanceName: LogisticsScheduler
            instanceId: AUTO
          jobStore:
            class: org.quartz.impl.jdbcjobstore.JobStoreTX
            driverDelegateClass: org.quartz.impl.jdbcjobstore.StdJDBCDelegate
            tablePrefix: QRTZ_
            isClustered: true
            clusterCheckinInterval: 10000
            useProperties: false
          threadPool:
            class: org.quartz.simpl.SimpleThreadPool
            threadCount: 10
            threadPriority: 5
            threadsInheritContextClassLoaderOfInitializingThread: true

    job-store-type: jdbc

# Logger Config
logging:
  level:
    root: info
    com.baomidou: debug
    com.medusa: debug
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
# gruul:
#   tenant:
#     column: tenant_id
#     tables:
#       - t_order
#       - t_community_manage
#       - t_deliver
#       - t_deliver_order_relation
#       - t_line_manage
#       - t_line_village_relation
#       - t_point_manage
#       - t_shipping_manage
#       - t_shipping_manage_point_manage_relation
#       - QRTZ_BLOB_TRIGGERS
#       - QRTZ_CALENDARS
#       - QRTZ_CRON_TRIGGERS
#       - QRTZ_FIRED_TRIGGERS
#       - QRTZ_JOB_DETAILS
#       - QRTZ_JOB_TAG_RELATION
#       - QRTZ_LOCKS
#       - QRTZ_PAUSED_TRIGGER_GRPS
#       - QRTZ_SCHEDULER_STATE
#       - QRTZ_SIMPLE_TRIGGERS
#       - QRTZ_SIMPROP_TRIGGERS
#       - QRTZ_TRIGGERS
#       - t_logistics_address
#       - t_logistics_deliver
#       - t_logistics_include_postage
#       - t_logistics_manage
#       - t_logistics_shipping_model
#       - t_logistics_template
monitor:
  useRun: true  #是否使用心跳监听
  useLog: false  #是否打印日志
  applicationName: "快递服务"  #服务名称
  baseType: "business"     #基础库类型  public-支撑基础库   business-业务基础库
  serviceType: "universalService" #服务类型    universalService-通用服务  commissionService-定制服务

```

