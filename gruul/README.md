# gruul

#### 介绍
gruul 小程序商城

#### 软件架构
1. 模块说明
	```yaml
	gruul
	├── gruul-common  --公共模块
	├── gruul-nacos  --注册配置中心[naocs端口号]
	├── gruul-gateway-open  --gateway网关[10999]
	├── gruul-ops  --运维模块
	│   ├── gruul-ops-job  --xxl-job-admin[9010]
	│   ├── gruul-ops-monitor(未上传)  --Spring Boot Admin[5001]
	├── gruul-oss-open  --OSS模块
	│   ├── gruul-oss-api  --OSS公共api
	│   ├── gruul-oss-service  --OSS业务处理[]
	goods
	goods
	goods
	├── gruul-order-open  --订单模块
	│   ├── gruul-order-api  --订单公共api
	│   ├── gruul-order-service  --订单业务处理[10300]
	|
	platform
	├── gruul-platform-open  --平台模块
	│   ├── gruul-platform-api  --平台公共api
	│   ├── gruul-platform-service  --平台业务处理[10200]
	```
2. swagger使用
	- 在主类上添加注解 @EnableGruulSwagger2
	 ```java
	     @EnableGruulSwagger2
	     @SpringCloudApplication
	     @EnableFeignClients
	     @MapperScan("com.medusa.gruul.oss.dao")
	     public class OSSApplication {
	     
	        public static void main(String[] args) {
	            SpringApplication.run(OSSApplication.class, args);
	        }
	     
	     }
	```
	- 定制化swagger配置详见com.medusa.gruul.common.swagger.config.SwaggerProperties中的属性

3. 多租户使用
	- 在表中预留租户字段（默认为tenant_id）
	- tables表示需要过滤的表，默认所有表都需要包含 tenant_id 字段
	```yaml
	# 租户表维护
	gruul:
	  tenant:
	    column: tenant_id
	    tables:
	      - t_file
	
	```



#### 安装教程

1. 下载本项目

2. 初始化数据库
	- 运行建库语句sql文件 (每个项目doc下有先对应的sql文件)
	- 运行其他的sql文件
3. 修改配置文件
	- 修改gruul-*-open\src\main\resources\bootstrap.yml 中的nacos连接配置
	- 运行nacos  默认账号密码（nacos/nacos） 
        - 加入相关配置配置文件
	- 修改application-open.yml文件中的redis配置
	- 修改oss-open.yml等文件中的数据库配置
4. 安装lombok插件
	详见 [idea安装Lombok](https://www.jianshu.com/p/37e24fe833d6)

5. 启动顺序  
	1.NacosApplication       (nacos) (该处指安装的nacos)  
	2.GatewayApplication.java  (网关)

	3.XxlJobAdminApplication(xxl-job-admin)  
	4.随意(应用)
6. 项目部署
    
        正式部署  用过jenkins进行代码构建打包
            gitlab(代码存储工具)  
            jenkins(代码集成) 
            Docker(容器管理) 
            DockerHarbor(可视化镜像仓库)
            Nexus(私服) 
            JDK
            MAVEN
        简易部署
           本地打jar包  (sms)   mvn clean install package deploy
           执行Dockerfile(sms)
           启动镜像
            docker run --name gruul-oss-open-0.1 --restart always -p 10300:10300  -v /tmp/logs/gruul:/tmp/logs/gruul -e SPRING_CLOUD_NACOS_DISCOVERY_METADATA_VERSION=0.1 -e SPRING_PROFILES_ACTIVE=open 镜像:版本号
           命令详解
             -- name 配置docker容器名称
             -- restart always 自动重启
             -p 10300:10300 保留端口及端口映射
             -e SPRING_CLOUD_NACOS_DISCOVERY_METADATA_VERSION=0.1 (指定版本)
             -e SPRING_PROFILES_ACTIVE=open (设置使用配置)
             -v /tmp/logs/gruul:/tmp/logs/gruul (挂载目录)
         
#### 开发说明

1. git commit 提交规范  
	关于git Commit message 可以阅读相关文章[Commit message 和 Change log 编写指南
](http://www.ruanyifeng.com/blog/2016/01/commit_message_change_log.html)  
	基于实际情况考虑，对上文中提到的commit log规范进行了简化,具体如下：
	```yaml
    <type>: <subject>
    <body>
	```
	1. type  
		提交 commit 的类型，包括以下几种  
	    feat: 新功能  
	    fix: 修复问题  
	    docs: 修改文档  
	    style: 修改代码格式，不影响代码逻辑  
	    refactor: 重构代码，理论上不影响现有功能  
	    perf: 提升性能  
	    test: 增加修改测试用例  
	    revert: 回退  
	 2. subject  
		 用一句话清楚的描述这次提交做了什么。书写要遵循以下四种规则：格式尽量使用谓宾，使用谓宾不通顺时，可以使用主谓，例如：  
		 谓宾：修复xxxx  
	     主谓：支持xxxx
	     
	     - 除了名称之外，描述尽可能使用中文，方便不同开发者理解
	     - 结尾不加句号
	     - 描述控制在20个汉字以内
     3. body  
        对本地提交的详细描述，不建议。我们建议多次少量提交，而不是一次巨量的提交，有助于revert和code review。
        
	辅助工具 安装插件 git-commit-template (随意)
    
2. 编码规范
	安装插件Alibaba Java Coding Guidelines plugin 提交代码前须使用插件检查代码，全部修改之后在进行提交


3.api jar包本地注入方式 
      
      修改gruul/pom.xml中的 modules 注释所有子模块进行maven clean install 
      恢复gruul-common/pom.xml中的module 注释所有子模块进行 maven clean install 
            按如下顺序进行注释 maven clean install 
            1.gruul-common-verify
            2.gruul-common-base
            3.gruul-common-gray
            4.gruul-common-auth
       恢复gruul/gateway  方法同上
       恢复业务代码api  操作 gruul/gruul-**-open/pom.xml
       注释modules中的module maven clean install 
          恢复gruul-xxx-api 针对恢复gruul-xxx-api 执行 maven clean install 
          反复操作所有业务模板针对 gruul-xxx-api进行 maven clean install 
      最终效果
      
            


      
![输入图片说明](https://images.gitee.com/uploads/images/2021/0910/145912_fd38abf1_5199717.png "屏幕截图.png")      
        

 ![输入图片说明](https://images.gitee.com/uploads/images/2021/0910/150414_965fff96_5199717.png "屏幕截图.png")


