#!/bin/bash
# get keycloak version from running container
keycloakImage=$(docker inspect keycloak-sendinblue -f '{{.Config.Image}}')
keycloakVersion="${keycloakImage#*:}"
# save file content
dockerComposeFile=$(<docker-compose.yml)
# replace keycloak version for docker
sed -i "s/{{ KEYCLOAK_VERSION }}/$keycloakVersion/g" docker-compose.yml
# stop docker
docker-compose down -v
# revert file contents to original state
echo "$dockerComposeFile" > docker-compose.yml