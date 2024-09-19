FROM maven:3-openjdk-17 AS build

WORKDIR /app

COPY ../  .

RUN mvn install -DskipTests

FROM eclipse-temurin:17-jre-alpine AS runtime

WORKDIR /app

COPY --from=build /app/services/basket/target/basket.jar basket.jar

COPY --from=build /app/services/basket/setup_hosts.sh /app/setup_hosts.sh

RUN chmod +x /app/setup_hosts.sh

EXPOSE 8082

# this script will remove localhost to 127.0.0.1 mapping from /etc/hosts and replaces it with the ip of the host all to access keycloak server
# the issue is on the host we need to run keycloak on localhost so it can be accessed from browser while the client spring boot
# needs to access keycloak to validate JWT tokens and both urls need to be the same otherwise you will get iss invalid cliam exception
# from spring security but localhost inside the containers points to the container itself not the host machine.
# i didn't want to add a hostname on the hostmachine and map it to docker network ip so the best solution i got was
# removing the 127.0.0.1 mapping and add a localhost mapping to the host machine ip.
ENTRYPOINT ["/app/setup_hosts.sh"]

CMD ["java", "-jar", "basket.jar"]