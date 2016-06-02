#!/bin/sh
sudo javac -cp "lib/*:src/*" src/*.java -d classes/
cd classes
java SaxParser

