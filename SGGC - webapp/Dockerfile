FROM maven:3.5.2-jdk-8-alpine AS MAVEN_BUILD
MAINTAINER Toby Peel
COPY pom.xml /build/
COPY src /build/src/
WORKDIR /build/
RUN mvn clean package -Dmaven.test.skip=true

FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY --from=MAVEN_BUILD /build/target/sggcws-1.0-SNAPSHOT.jar /app/
ENTRYPOINT ["java","-jar","sggcws-1.0-SNAPSHOT.jar"]
