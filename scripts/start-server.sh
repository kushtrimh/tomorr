#!/bin/bash

nohup /usr/java/jdk-16.0.1/bin/java -jar /usr/tmp/tomorr.jar --spring.profiles.active=$TOMORR_RUN_PROFILE > $TOMORR_LOG_DIR/tomorr.log 2>&1 &
echo "Start command executed"