FROM maven:3-openjdk-17 AS build

WORKDIR /app

COPY ../  .

RUN mvn install -DskipTests

FROM eclipse-temurin:17-jre-alpine AS runtime

WORKDIR /app

COPY --from=build /app/services/payment/target/payment.jar payment.jar

ENTRYPOINT ["java", "-jar", "payment.jar"]