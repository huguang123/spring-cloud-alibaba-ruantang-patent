# 基础镜像
FROM openjdk:17-jdk-alpine

# 指定工作目录
WORKDIR /app

# 将 jar 包添加到工作目录，比如 target/yuoj-backend-user-service-0.0.1-SNAPSHOT.jar
ADD target/service-permissions-0.0.1-SNAPSHOT.jar .

# 暴露端口
EXPOSE 9600

# 启动命令
ENTRYPOINT ["java","-jar","/app/service-permissions-0.0.1-SNAPSHOT.jar","--spring.profiles.active=test"]
