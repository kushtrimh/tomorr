#!/bin/bash

sleep 10
health_check_res=$(curl http://localhost:8098/actuator/health)
if [[ $health_check_res =~ ^.*"UP".*$ ]]; then
  exit 0
else
  exit 1
fi