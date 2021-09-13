FROM anapsix/alpine-java:8_server-jre_unlimited

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

ADD gruul-payment-open-service/target/*.jar gruul-payment.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom" ,"-jar" , "-Xms256m","-Xmx256m" ,"/gruul-payment.jar"]
# 小群增加无用注册 测试自动构建功能