#### platform-open

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

xxl:
  job:
    accessToken:
    admin:
      addresses: http://127.0.0.1:9010/xxl-job-admin/
    executor:
       ip:
       appname: platefrom-job-open
       port: 12000
       logpath: /tmp/logs/gruul-platform-open/xxl-job/jobhandler
       logretentiondays: -1

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
#微信第三方平台配置
wechat:
  open:
    domain: "https://open.bgniao.cn/api/platform-open"
    componentAppId: ""
    componentSecret: ""
    componentToken: ""
    componentAesKey: ""
    redirectUrl: "${wechat.open.domain}/mini/authorization/preauthcode/notify/$UUID"
  redis:
    host: ${redis.outernet.host}
    port: ${redis.port}
    password: ${redis.password}
    database: 5
  mp:
    configs:
      #网站应用
      - appId: ""  
        secret: ""
        token:
        aesKey:
      #公众号
      - appId: ""
        secret: ""
        token: ""
        aesKey: ""
#平台服务配置
gruul:
  tenant:
    use_shop: false
    tables:
      - t_platform_module_manager
      - t_platform_account_info
      - t_platform_combo
      - t_platform_combo_service_mapping
      - t_platform_default_value
      - t_platform_service
      - t_platform_service_manager
      - t_platform_template_clan
      - t_platform_template_info
      - t_mini_audit_record
      - t_mini_auth_info
      - t_mini_auth_token
      - t_mini_change_record
      - t_mini_info
      - t_mini_switch
      - t_mini_template_code
      - t_system_conf
      - t_send_code
      - t_base_menu
      - t_mini_subscriberi_base
      - t_auth_role_info
      - t_auth_user_role
      - t_platform_code_version
      - t_platform_shop_deploy
      - t_platform_shop_info
      - t_platform_shop_template_detail
      - t_platform_shop_template_info
      - t_platform_libraries_info
      - t_platform_service_info
      - t_platform_pay_config
      - t_platform_account_balance_record
      - t_platform_account_recharge
      - t_sys_shop_invoice_order
      - t_sys_shop_invoice_rise
      - t_sys_shop_package
      - t_sys_shop_package_order
      - t_platform_shop_template_detail_minis
platform:
  admin:
    username:   
    password: 
  config:
    miniBusinessDomain: 
base:
  url: ""  #基础库心跳统一入口
monitor:
  useRun: true  #是否使用心跳监听
  useLog: false  #是否打印日志
  businessName: ""   #基础库名(baseType=public时可以为空,不填写)
  applicationName: "总台服务"  #服务名称
  baseType: "public"     #基础库类型  public-支撑基础库   business-业务基础库
  serviceType: "universalService" #服务类型    universalService-通用服务  commissionService-定制服务
 
web-log:
  ignores:
      PlatformServiceInfoController:
        - heartbeatUrl
        - baseWarehouse
      MiniInfoController:
        - wxaGetwxacode
logging:
  level:
    com.baomidou: debug
    com.medusa: debug
    com.github.binarywang: debug

```

