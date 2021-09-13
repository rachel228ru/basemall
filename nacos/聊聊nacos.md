**Nacos**

nacos 安装

     解压 tar.gz
       移动到bin目录下  cd /nacos/bin 
       单机版启动 ：sh startup.sh -m standalone    
       访问 ：ip:8848/nacos   
              user :nacos
              pwd:nacos
       如需使用数据库进行配置请执行初始化sql及更改配置
       /nacos/conf/nacos-mysql.sql
       /nacos/conf/application.properties.example
      注：nacos密码有进行加密 要自己生成一个
          org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
          String pwd = new BCryptPasswordEncoder().encode("123456");
  
 nacos 配置详解
    
    application-open.yml为默认配置当 其他服务yml没有当前配置下会使用application-open.yml中配置
    如上线使用 请注释 application-open.yml内的swagger配置
    
    logging  为日志打印。可根据业务进行日志等级调整。
    
    server:
      port: xxxx (端口配置) 如项目本地 及nacos中都配置该端口则默认使用nacos中的端口配置
    monitor:
      monitor为基础库监控 二开可考虑使用
    wechat:
      open: 该处为微信第三方平台(开放平台)的相关配置
      domain:域名 默认格式 (域名/api/platform-open)
       componentAppId:  第三方平台应用 APPID
       componentSecret: 第三方平台应用 APPSECRET
       componentToken:  第三方平台应用 Token
       componentAesKey: 第三方平台应用 AesKey
       redirectUrl: "${wechat.open.domain}/mini/authorization/preauthcode/notify/$UUID" 
    gruul:
      tenant:
        use_shop: false  不使用shopId 
        tables:   细分到表 
    platform:  管理台配置
      admin:
        username:   管理台默认账号(二开可考虑使用)
        password:   管理台默认密码(二开可考虑使用)
    xxl:
      job:
        accessToken: xxl定时任务配置
    
    pay:
      notify:  回调域名
      ips*  :  第三方支付相关配置(二开可参考)
  
  
    如遇问题可加入qq群,会有专人解答
    
    
    //