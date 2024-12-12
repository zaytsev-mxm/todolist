#!/bin/bash

./gradlew -t installDist &

sleep 10

./gradlew -t run