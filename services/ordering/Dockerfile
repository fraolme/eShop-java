FROM maven:3-openjdk-17 AS build

WORKDIR /app

COPY ../  .

RUN mvn install -DskipTests

FROM eclipse-temurin:17-jre-alpine AS runtime

WORKDIR /app

COPY --from=build /app/services/ordering/target/ordering.jar ordering.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "ordering.jar"]