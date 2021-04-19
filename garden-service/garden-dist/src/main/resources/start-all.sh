#!/bin/sh
# author: Mr.Tang
# date:   2021年 02月 05日 星期五 11:23:31 CST

BASE_DIR=$(cd `dirname $0`/;pwd)
cd ${BASE_DIR}

sh ${BASE_DIR}/cloud-auth-*/bin/startup.sh
echo ${BASE_DIR}/cloud-auth-*/*.log
sleep 5

sh ${BASE_DIR}/cloud-sys-*/bin/startup.sh
echo ${BASE_DIR}/cloud-sys-*/*.log
sleep 5

sh ${BASE_DIR}/cloud-zuul-*/bin/startup.sh
echo ${BASE_DIR}/cloud-zuul-*/*.log

jps
echo "started all services!"
