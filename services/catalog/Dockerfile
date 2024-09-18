FROM maven:3-openjdk-17 AS build

WORKDIR /app

COPY ../  .

RUN mvn install -DskipTests

FROM eclipse-temurin:17-jre-alpine AS runtime

WORKDIR /app

COPY --from=build /app/services/catalog/target/catalog.jar catalog.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "catalog.jar"]