#!/bin/bash
# build extension without SNAPSHOT suffix
mvn clean package -DskipTests -DprojectVersion=legacy -Dchangelist=
if [[ "$?" -ne 0 ]] ; then
  echo 'could not run maven package'; exit $rc
fi

# get keycloak version from pom
KEYCLOAK_VERSION=$(mvn help:evaluate -Dexpression=keycloak.version -q -DforceStdout)

# start docker
KEYCLOAK_VERSION="$KEYCLOAK_VERSION-legacy" docker-compose up --build --detach
