FROM gradle:8.5-jdk21 AS build
COPY  . /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle assemble
FROM eclipse-temurin:21-jre

RUN mkdir -p /usr/local/newrelic
ADD ./newrelic/newrelic.jar /usr/local/newrelic/newrelic.jar
ADD ./newrelic/newrelic.yml /usr/local/newrelic/newrelic.yml

EXPOSE 8083

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/snippet-permission.jar

ENTRYPOINT ["java","-javaagent:/usr/local/newrelic/newrelic.jar", "-jar", "-Dspring.profiles.active=production","/app/snippet-permission.jar"]
