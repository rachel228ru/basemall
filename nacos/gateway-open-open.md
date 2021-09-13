#### gateway-open

##### nacos配置

```yaml
server:
  port: 10999


spring:
  cloud:
    gateway:
      discovery:
        locator: 
          lowerCaseServiceId: true
          enabled: true

ribbon:
  rule:
    gray-enabled: true

```

