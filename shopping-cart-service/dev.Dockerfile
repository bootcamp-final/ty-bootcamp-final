FROM adoptopenjdk/openjdk11:alpine-jre

COPY ./target/*.jar /app.jar

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "/app.jar"]
