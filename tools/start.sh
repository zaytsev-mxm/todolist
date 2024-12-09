#!/bin/bash

set -o allexport; source ./.env; set +o allexport;

./gradlew -t installDist &

sleep 10

./gradlew -t run