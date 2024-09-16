FROM maven:3-openjdk-17 AS build

WORKDIR /app

COPY ../  .

# Install the local package
WORKDIR /app/event-bus-rabbitmq
RUN mvn install

WORKDIR /app/services/catalog
# download dependencies without running the full build
RUN mvn dependency:go-offline

RUN mvn clean package -DskipTests

FROM openjdk:17-alpine AS runtime

WORKDIR /app

COPY --from=build /app/services/catalog/target/catalog.jar catalog.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "catalog.jar"]