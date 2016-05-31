#!/bin/sh

cd ../Model/
./build.sh
cd ../Servlet/

FOLDER=project4

ROOT_DIR=/var/lib/tomcat7/webapps/$FOLDER/WEB-INF
#ROOT_DIR=/home/ubuntu/Spring_2016/cs_122b/project3_14/$FOLDER/WEB-INF

LIB_FILES=$ROOT_DIR/lib/*
CURRENT_DIR=$ROOT_DIR/sources/Servlet/

CLASS_DIR=$ROOT_DIR/classes/
sudo javac -classpath $CURRENT_DIR:$LIB_FILES:$CLASS_DIR *.java -d $CLASS_DIR
sudo service tomcat7 restart
