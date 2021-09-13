gruul-platform
    
    platform作为平台模板。主要功能有管理台,代理,商家入驻,微信第三方平台相关等功能
    这里进行了删减如想了解功能可联系wx客服获取账号查看相关功能
    
1. 功能说明
    

   ```yaml
	
   platform-service
	├── com.medusa.gruul.platform.conf  --相关配置
	│   ├── AliyunStorageConfig.java    -- 阿里云存储配置
	│   ├── MeConstant.java             -- 平台内部使用得魔法值常量
	|   ├── PaltformProperties.java     -- 管理台账号密码(二开可以考虑使用)
	|   ├── PlatformRedis.java          -- 平台redis
	|   ├── QiniouStorageConfig.java    -- 七牛云存储配置
	|   ├── RedisProperties.java        -- 平台注入相关redis
	|   ├── SystemConfig.java           -- 系统配置
	|   ├── SystemConstant.java         -- 系统配置key值
	|   ├── TencentStorageConfig.java   -- 腾讯云存储配置
	|   ├── WechatOpenProperties.java   -- 第三方平台(微信开放平台)
	|   ├── WxConfiguration.java        -- 属性注入并初始化
	|   ├── WxMpProperties.java         -- 微信公众号属性
	|   ├── XxlJobConfig.java           -- xxl-job-admin 默认配置

