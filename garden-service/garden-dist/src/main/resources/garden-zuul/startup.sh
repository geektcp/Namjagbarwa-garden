#!/bin/sh
## author: Mr.Tang
## date  : 2021年 03月 23日 星期二 10:58:07 CST

INSTALL_DIR=$(cd `dirname $0`/..;pwd)
cd ${INSTALL_DIR}
nohup java ${JDK_OPT} -jar ${INSTALL_DIR}/lib/cloud-zuul-*.jar 2>&1 >../zuul.log  &
