#!/bin/sh

FOLDER=project4
WAR_NAME=fabflix_webapp
GIT_LOCATION=/home/ubuntu/Spring_2016/cs_122b/project4_14/

sudo cp -r ../$FOLDER $GIT_LOCATION/$WAR_NAME
sudo jar -cvf $GIT_LOCATION/$WAR_NAME.war *
