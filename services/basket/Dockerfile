FROM maven:3-openjdk-17 AS build

WORKDIR /app

COPY ../  .

RUN mvn install -DskipTests

FROM eclipse-temurin:17-jre-alpine AS runtime

WORKDIR /app

COPY --from=build /app/services/basket/target/basket.jar basket.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "basket.jar"]