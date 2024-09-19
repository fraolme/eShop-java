FROM maven:3-openjdk-17 AS build

WORKDIR /app

COPY ../  .

RUN mvn install -DskipTests

FROM eclipse-temurin:17-jre-alpine AS runtime

WORKDIR /app

COPY --from=build /app/services/ordering/target/ordering.jar ordering.jar

COPY --from=build /app/services/ordering/setup_hosts.sh /app/setup_hosts.sh

RUN chmod +x /app/setup_hosts.sh

EXPOSE 8083

ENTRYPOINT ["/app/setup_hosts.sh"]

CMD ["java", "-jar", "ordering.jar"]