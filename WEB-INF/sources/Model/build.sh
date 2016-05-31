#!/bin/sh

FOLDER=project4

ROOT_DIR=/var/lib/tomcat7/webapps/$FOLDER/WEB-INF # for AWS instance
#ROOT_DIR=/home/ubuntu/Spring_2016/cs_122b/project3_14/$FOLDER/WEB-INF # for testing

CURRENT_DIR=$ROOT_DIR/sources/Model/
LIB_DIR=$ROOT_DIR/lib/*

CLASS_DIR=$ROOT_DIR/classes/

sudo javac -classpath $CURRENT_DIR:$LIB_DIR *.java -d $CLASS_DIR
