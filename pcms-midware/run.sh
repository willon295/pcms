#!/bin/bash 

jar_pro=`jps | grep jar |  grep -v grep |  awk '{print $1}'`
if [ -z "$jar_pro" ]; then
    echo "no"
else
    kill -9 $jar_pro
fi
rm -rf  /opt/app/pcms*
mv  *.gz  /opt/app/
cd  /opt/app/
tar  -zxf  *.gz
cd  /opt/app/pcms-midware
nohup java -server -Xms256m -Xmx256m -XX:PermSize=64m -XX:MaxPermSize=128m -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005 -jar  *.jar  >> `date +%Y%m%d`.log &

