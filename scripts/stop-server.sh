#!/bin/bash

if [ -f /var/run/tomorr/tomorr.pid ]; then
  tomorrpid=$(cat /var/run/tomorr/tomorr.pid)
  kill -15 $tomorrpid
fi