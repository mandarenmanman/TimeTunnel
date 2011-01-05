#!/bin/bash

export PYTHONPATH=/home/admin/TimeTunnel/pclient/src/:$PYTHONPATH

base_dir=`dirname $0`
PID_FILE=${base_dir}/.ptailfile.pid;

script=${base_dir}/../src/tailfile.py

startup_log=/tmp/ptailfile.start

echo ${script} > ${startup_log}

python ${script} 1>/dev/null 2>>${startup_log} &

pid=$!

echo ${pid}>${PID_FILE}

