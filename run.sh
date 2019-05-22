#!/bin/bash

find  bin/. -name  '*.class' -type  f -exec  rm -rf  {} \;

javac -d bin -classpath src src/fripbird.java

java -classpath bin fripbird

