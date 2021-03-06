#!/bin/bash

if [ "$1" == "" ] ; then
    echo "ERROR: REVISIONS ARE NOT SPECIFIED"
    echo "Usage: ./deploy-demo.sh homeless__REVISION homeless-report-engine__REVISION"
    exit 1
fi
REV_H=$1
REV_HRE=$2

black='\E[30;40m'
red='\E[31;40m'
green='\E[32;40m'
yellow='\E[33;40m'
blue='\E[34;40m'
magenta='\E[35;40m'
cyan='\E[36;40m'
white='\E[37;40m'

cecho () {
    local default_msg="[EMPTY MESSAGE]"
    message=${1:-$default_msg}
    color=${2:-$black}

    echo -e "$color"
    echo "$message"
    tput sgr0
    return
}

check_res() {
    if [ $1 -ne 0 ] ; then
	cecho "An error has been happened while performing the last operation" $red
	exit 1
    fi
    cecho "done..." $cyan
    return 0
}

print_file() {
    while IFS='' read -r line || [[ -n "$line" ]]; do
	cecho "$line" $yellow
    done < "$1"
}

BASE=/opt/homeless
ROOT=${BASE}/storage
TEMP=${ROOT}/temp
TIMESTAMP=${BASE}/timestamp.txt
TEMPLATES=${BASE}/templates
BACKUPS=${ROOT}/BACKUPS
CONTRACTS=${ROOT}/contracts
IMAGES=${ROOT}/images
PROFILES=${ROOT}/profiles

SOURCES_ROOT=/opt/homeless/sources
SOURCES_HOMELESS=${SOURCES_ROOT}/homeless
SOURCES_HOMELESS_REPORT_ENGINE=${SOURCES_ROOT}/homeless-report-engine

TOMCAT_HOME="${BASE}/tools/tomcat"

PASS=`cat /opt/homeless/tools/tomcat/conf/server.xml | grep "password=" | sed -e "s/^.*password=\"//" | sed -e "s/\"//" | tr -d '\n' | tr -d '\r'`

LAST_INV=`ssh tomcat@10.2.0.9 "ls /opt/homeless/storage/BACKUPS/REGULAR/inv-*.zip | sort | tail -n 1"`
LAST_DB=`ssh tomcat@10.2.0.9 "ls /opt/homeless/storage/BACKUPS/REGULAR/db-*.zip | sort | tail -n 1"`

pushd ${TEMP} > /dev/null 2>&1
rm -rf *
scp tomcat@10.2.0.9:${LAST_INV} inv.zip
check_res $?
scp tomcat@10.2.0.9:${LAST_DB} db.zip
check_res $?
unzip db.zip > /dev/null
unzip inv.zip > /dev/null
rm -rf ${CONTRACTS}/* && mv -f ./opt/homeless/storage/contracts/* -f ${CONTRACTS}
rm -rf ${IMAGES}/* && mv -f ./opt/homeless/storage/images/* -f ${IMAGES}
rm -rf ${PROFILES}/* && mv -f ./opt/homeless/storage/profiles/* -f ${PROFILES}
# don`t replace reports templates from backup!
#rm -rf ${TEMPLATES}/* && mv -f ./opt/homeless/storage/templates/* -f ${TEMPLATES}

cecho "Deleting the existing database homeless_demo" $green
echo "DROP DATABASE homeless_demo" | mysql --user=homeless_demo --password=${PASS} homeless_demo
check_res $?
cecho "Creating the new database" $green
echo "CREATE DATABASE homeless_demo CHARACTER SET utf8 COLLATE utf8_general_ci;" | mysql --user=homeless_demo --password=${PASS}
check_res $?
cecho "Loading the DB dump from last backup" $green
mysql --user=homeless_demo --password=${PASS} homeless_demo < ${TEMP}/homeless.sql
check_res $?

popd > /dev/null 2>&1

THIS_IP=`/sbin/ifconfig eth0 | awk '/inet addr/{print substr($2,6)}'`

# 1. Updating sources
cecho "Updating sources for homeless project..." $green

pushd ${SOURCES_HOMELESS} > /dev/null 2>&1
    cecho "Updating directory `pwd`" $green
    hg pull 2>&1
    check_res $?
    hg up -r ${REV_H} -C 2>&1
    check_res $?
popd > /dev/null 2>&1

pushd ${SOURCES_HOMELESS_REPORT_ENGINE} > /dev/null 2>&1
    cecho "Updating directory `pwd`" $green
    hg pull 2>&1
    check_res $?
    hg up -r ${REV_HRE} -C 2>&1
    check_res $?
popd > /dev/null 2>&1

# 2. Replacing configuration according current layout

pushd ${SOURCES_HOMELESS} > /dev/null 2>&1
    SPRING_CONFIG=./src/main/webapp/WEB-INF/applicationContext.xml
    cecho "Replacing Spring configuration `readlink -f ${SPRING_CONFIG}`" $green
    sed -i "s/DevelopmentDB/ProductionDB/" ${SPRING_CONFIG}
    check_res $?
popd > /dev/null 2>&1

pushd ${SOURCES_HOMELESS_REPORT_ENGINE} > /dev/null 2>&1
    SPRING_CONFIG=./src/main/webapp/WEB-INF/spring-config.xml
    cecho "Replacing Spring configuration `readlink -f ${SPRING_CONFIG}`" $green
    sed -i "s/DevelopmentDB/ProductionDB/" ${SPRING_CONFIG}
    check_res $?
popd > /dev/null 2>&1

# 3. Building applications (WAR files)


pushd ${SOURCES_HOMELESS} > /dev/null 2>&1
    mvn clean package
    check_res $?
    
    #For avoiding changes by mistake
    mv -f target/homeless.war target/demo-homeless.war
popd > /dev/null 2>&1

pushd ${SOURCES_HOMELESS_REPORT_ENGINE} > /dev/null 2>&1
    mvn clean package
    check_res $?
popd > /dev/null 2>&1

# 3.5 Replace report and document templates from app source
pushd ${SOURCES_HOMELESS_REPORT_ENGINE} > /dev/null 2>&1
    cecho "Replacing templates in ${TEMPLATES}" $green
    rm -rf ${TEMPLATES}/* && cp -f ./templates/* ${TEMPLATES}
    check_res $?
popd > /dev/null 2>&1

# 4. Applying db patch to the prod instance if necessary
#
#    NOTE: CURRENTLY IT IS THE MANUAL PROCEDURE BECAUSE OF SECURITY REASONS
#
# ***************************************************

# 5. Stopping running application, backing up logs, cleaning environment and run everything again

pushd ${TOMCAT_HOME}/bin > /dev/null 2>&1
    cecho "Stopping Tomcat if it is running" $green
    ./shutdown.sh
    if [ $? -ne 0 ] ; then
	ps fax | grep "${TOMCAT_HOME}" | awk '{print $1}' | xargs kill -9 > /dev/null 2>&1
    fi
popd > /dev/null 2>&1

cecho "Waiting 10 sec for finishing the Tomcat process..." $magenta
sleep 10

pushd ${TOMCAT_HOME}/webapps > /dev/null 2>&1
    cecho "Deleting old applications" $green
    rm -rf demo-homeless homeless-report-engine
    check_res $?
    rm -f demo-homeless.war homeless-report-engine.war
    check_res $?
    cecho "Deploying new applications" $green
    cp -f ${SOURCES_HOMELESS}/target/demo-homeless.war .
    check_res $?
    cp -f ${SOURCES_HOMELESS_REPORT_ENGINE}/target/homeless-report-engine.war .
    check_res $?
popd > /dev/null 2>&1

pushd ${TOMCAT_HOME}/logs > /dev/null 2>&1

    cecho "Placing timestamp before starting homeless application" $magenta
    D=`date +"%H:%M:%S %d.%m.%Y"`
    echo "!!!DEMO!!! ${D} (${REV_H}, ${REV_HRE}) !!!DEMO!!!">${TIMESTAMP}
    check_res $?

    cecho "Starting Tomcat applications..." $green

    # Running Tomcat from logs directory for relocating audit log in to the proper place - TOMCAT/logs directory
    ${TOMCAT_HOME}/bin/startup.sh
    check_res $?

    cecho "Waiting 3 sec for starting the Tomcat process..." $magenta
    sleep 3

    # PID for init scripts
    ps fax | grep /opt/homeless/tools/tomcat | grep -v "\_ grep" | awk '{print $1}' > ${TOMCAT_HOME}/temp/tomcat.pid
popd > /dev/null 2>&1

cecho "DEPLOYMENT SUCCESSFUL" $magenta

exit
