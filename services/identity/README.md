# eShop Identity Service

we use keycloak as identity server, uses eshop-realm.json to
configure a keycloak realm with clients and users 

two clients are configured with eshop-realm.json
- basketswaggerui
- orderingswaggerui

to run the Keycloak IAM server
```bash
docker run -d --name eshop-identity -p 8084:8080 eshop-identity
```

to restart the docker container
```bash
docker restart eshop-identity
```