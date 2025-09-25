## 多阶段构建：先用 Maven 构建，再用精简 JDK8 运行

# 1) Build 阶段
FROM maven:3.8.8-eclipse-temurin-8 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
# 复制 settings-docker.xml 到容器内
COPY settings-docker.xml /root/.m2/settings.xml
RUN mvn -s /root/.m2/settings.xml -B -DskipTests clean package

# 2) Runtime 阶段
FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar
EXPOSE 8088
ENTRYPOINT ["java", "-jar", "app.jar"]
