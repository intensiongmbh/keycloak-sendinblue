#!/bin/bash
# build jar without SNAPSHOT suffix
mvn clean package -DskipTests -Dchangelist=
if [[ "$?" -ne 0 ]] ; then
  echo 'could not run maven package'; exit $rc
fi
# get keycloak version from pom
KEYCLOAK_VERSION=$(mvn help:evaluate -Dexpression=keycloak.version -q -DforceStdout)
# save file content
dockerComposeFile=$(<docker-compose.yml)
# replace keycloak version for docker
sed -i "s/{{ KEYCLOAK_VERSION }}/$KEYCLOAK_VERSION/g" docker-compose.yml
# start docker
docker-compose up --build --detach
# revert file contents to original state
echo "$dockerComposeFile" > docker-compose.yml