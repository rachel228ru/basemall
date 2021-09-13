FROM anapsix/alpine-java:8_server-jre_unlimited

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

ADD gruul-platform-open-service/target/*.jar gruul-platform.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom" ,"-jar" , "-Xms512m","-Xmx512m" ,"/gruul-platform.jar"]
