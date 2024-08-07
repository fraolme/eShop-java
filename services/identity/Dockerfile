FROM quay.io/keycloak/keycloak:25.0.2

# Set environment variables for Keycloak
ENV KEYCLOAK_ADMIN=admin
ENV KEYCLOAK_ADMIN_PASSWORD=admin

COPY eshop-realm.json /opt/keycloak/data/import/eshop-realm.json

EXPOSE 8080

ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev", "--import-realm"]