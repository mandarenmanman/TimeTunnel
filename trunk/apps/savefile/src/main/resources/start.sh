#!/bin/bash


base_dir=`dirname $0`
PID_FILE=${base_dir}/.savefile.pid;

LIB_PATH=${base_dir}/../lib
CONF_PATH=${base_dir}/../conf


CLASSPATH=$LIB_PATH/*:$CONF_PATH

script="java -DROUTER=localhost:9999 -DRPCTIMEOUT=30000 -classpath $CLASSPATH  com.taobao.timetunnel.savefile.app.SaveFileApp"
echo $script > /tmp/timetunnel.savefile.start
nohup $script >> /tmp/timetunnel.savefile.start
pid=$!
echo ${pid}>${PID_FILE}
