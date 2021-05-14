#!/bin/bash

if [ -f $TOMORR_PID_FILE ]; then
  tomorrpid=$(cat $TOMORR_PID_FILE)
  kill -15 $tomorrpid
fi