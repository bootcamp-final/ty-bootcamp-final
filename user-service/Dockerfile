FROM adoptopenjdk/openjdk11:alpine-slim as builder

WORKDIR /src/app

COPY . .

RUN ./mvnw clean install

FROM adoptopenjdk/openjdk11:alpine-jre as runner

COPY --from=builder /src/app/target/*.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
