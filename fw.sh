#!/bin/sh

FOLDER_NAME=fabflix
GIT_LOCATION=/home/ubuntu/Spring_2016/cs_122b/
WAR_NAME=project5_14

sudo cp -r ../$FOLDER_NAME $GIT_LOCATION/$WAR_NAME
sudo jar -cvf $GIT_LOCATION/$WAR_NAME.war *
