#### application-open

##### nacos配置

```yaml
spring:
  redis:
    password: 
    host: 127.0.0.1
    port: 6378
  servlet:
    multipart:
      max-file-size: 500MB
      max-request-size: 100MB


management:
  endpoints:
    web:
      exposure:
        include: '*'
  endpoint:
    health:
      show-details: ALWAYS

feign:
  hystrix:
    enabled: true
  okhttp:
    enabled: true
  httpclient:
    enabled: false
  client:
    config:
      default:
        connectTimeout: 200000
        readTimeout: 200000
  compression:
    request:
      enabled: true
    response:
      enabled: true
# hystrix If you need to use ThreadLocal bound variables in your RequestInterceptor`s
# you will need to either set the thread isolation strategy for Hystrix to `SEMAPHORE or disable Hystrix in Feign.
hystrix:
  command:
    default:
      execution:
        isolation:
          strategy: SEMAPHORE
          thread:
            timeoutInMilliseconds: 60000
  shareSecurityContext: true

gray:
  rule:
    enabled: true
    
ribbon:
  ReadTimeout: 10000
  ConnectTimeout: 10000

mybatis-plus:
  mapper-locations: classpath*:/mapper/**/*.xml
  #实体扫描，多个package用逗号或者分号分隔
  typeAliasesPackage:  com.medusa.gruul.*.api.entity
  typeEnumsPackage: com.medusa.gruul.*.api.enums
  global-config:
    banner: false
    db-config:
      id-type: auto


swagger:
  enable: true
  host: 
  #host: dev.superprism.cn/api
  title:  Swagger API
  version: v2.0
  license: Powered By Medusa

# Logger Config
logging:
  file: /application.log
  pattern:
    file: '%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(%5p) %clr(${PID}){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n%wEx'
  level:
    root: error
    com.alibaba.nacos: error
    com.medusa: debug
    com.baomidou: debug
    com.medusa.gruul.common.data.tenant.TenantContextHolderFilter: error
#默认配置的redis
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

```

