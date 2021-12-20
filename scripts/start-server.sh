#!/bin/bash

aws s3 cp /etc/tomorr/application.yml $TOMORR_S3_EXTERNAL_PROPS
nohup /usr/java/jdk-17.0.1/bin/java -jar /usr/tmp/tomorr-*.jar --spring.config.additional-location=optional:/etc/tomorr/application.yml > /var/log/tomorr/tomorr.log 2>&1 &
echo "Start command executed"