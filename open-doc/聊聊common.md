gruul-common
    
    common是基于子服务抽出来的一个公共模块


1. 模块说明
    
   ```yaml
	
   common
	├── gruul-common  --公共模块
	│   ├── gruul-common-auth   -- Jwt认证授权
	│   ├── gruul-common-base   -- 基础模块
	|   ├── gruul-common-gray   -- 灰度路由
	|   ├── gruul-common-verify -- 验证证书模块
   
 2.gruul-common-base 重点介绍
    
   ```yaml
	
   gruul-common-base
	├──  com.medusa.gruul.common.core
	│   ├── EscapeLogin.java            -- 免登入注解,可在没有token情况调用接口 使用 @EscapeLogin
	│   ├── RestExceptionHandler.java   -- 全局异常捕捉    未捕捉异常 (400,"服务器内部错误")  
    │   ├──                                     自定义异常捕捉   自定义异常(自定义code,"自定义msg")
    │   ├──                                     throw new ServiceException(900100,"商品信息不存在");   
	|   ├── ServiceException.java       -- 自定义异常 
	|   ├── JavaTimeModule.java         -- 时间默认序列化
	|   ├── EnableMonitorHeartbeat.java -- 心跳监听注解 (开源版未使用，区分基础库实现多版本并行)
	|   ├── JavaTimeModule.java         -- 时间默认序列化
	|   ├── QueryParam.java             -- 查询参数 
    |   ├──                                    size == null || size <= 0   size = CommonConstants.DEFAULT_PAGE_SIZE(10); 
	|   ├── SmsSendConfig.java          -- 短信配置 
	|   ├── AreaUtil.java               -- 省市区选择器工具类  (可在resource/file下找到对应省市区json)
	|   ├── CurUserUtil.java            -- 请求用户信息工具类 
	|   ├── IPUtils.java                -- IP地址 (打印日志使用) 
	|   ├── IResultCode.java            -- 状态码 
	|   ├── JwtUtils.java               -- jwt工具类
	|   ├── Result.java                 -- 响应结果集 
	|   ├── ResultList.java             -- 响应结果集(不需要分页的obj 物流) 
	|   ├── ResultPageList.java         -- 响应结果集(需要分页的obj 物流)  
	|   ├── SystemCode.java             -- 系统内置code

   gruul-common-base
	├──  com.medusa.gruul.common.*
	|   ├── EscapeShop.java             -- 免商铺ID注入注解 (不注入shopId)
	|   ├── EscapeTenant.java           -- 免商铺ID注入注解 (不注入TenantId)
	|   ├── xxxxxxAspect.java           -- 切点默认值
	|   ├── BaseEntity.java             -- 基础Entity 如还未能深刻理解流程 新建表时请加上以下字段 is_deleted update_time create_time tenant_id
	|   ├── BaseNoTenantEntity.java     -- 基础NoTenantEntity 不包含tenant_id
	|   ├── IMetaObjectHandler.java     -- 填充器(mybatisplus) 
                                              insertFill 插入 创建时间(当前时间) 修改时间(当前时间) 删除状态(未删除)
                                              updateFill 修改 修改时间(当前时间)
	|   ├── MybatisPlusConfig.java      -- MybatisPlus配置(sql执行处理。默认加上ShopId(店铺Id) TenantId(租户Id)) 
	|   ├── CurUserContextHolder.java   -- 用户请求线程 获取当前租户Id
	|   ├── GruulShopHandler.java       -- 租户维护处理器(ShopId) 
	|   ├── GruulTenantHandler.java     -- 租户维护处理器(TenantId) 
	|   ├── ShopContextHolder.java      -- 商铺工具类(ShopId) 
	|   ├── TenantContextHolder.java    -- 租户工具类(TenantId) 
	|   ├── TenantContextHolderFilter.java    -- header过滤(TenantId)
	|   ├── CurMiniUserInfoDto.java     -- 小程序token解析内容Dto
	|   ├── CurPcUserInfoDto.java       -- Pc  token解析内容Dto
	|   ├── CurShopInfoDto.java         -- 店铺 token解析内容Dto
	|   ├── CurUserDto.java             -- 标注作废(开源版依旧使用) token解析信息(通用) 
	|   ├── HeartBeatDto.java           -- 基础库(开源版未存在实现) 
	|   ├── SendCodeVerifyDto.java      -- 短信校验 
	|   ├── WebLogAspect.java           -- 日志信息(请求打印信息) 
	|   ├── SysLogUtils.java            -- 系统日志工具类 
    
    |   ├── redis/*.java                -- RedisVisitorBaseFacade
    |   ├── swagger/*.java              -- swagger配置

 
 如存疑问不解可加qq群探讨沟通

  


    