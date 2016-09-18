#!/bin/bash

if [ "$1" == "" ] ; then
    echo "ERROR: REVISIONS ARE NOT SPECIFIED"
    echo "Usage: ./deploy-prod.sh homeless__REVISION homeless-report-engine__REVISION"
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

COMMAND_PULL_SOURCES="hg pull"

COMMAND_UPDATE_SOURCES_HOMELESS="hg up -C -r ${REV_H}"
COMMAND_UPDATE_SOURCES_HRE="hg up -C -r ${REV_HRE}"

THIS_IP=`/sbin/ifconfig eth0 | awk '/inet addr/{print substr($2,6)}'`

# 1. Updating sources
cecho "Updating sources for homeless project..." $green

pushd ${SOURCES_HOMELESS} > /dev/null 2>&1
    cecho "Updating directory `pwd` with revision ${REV_H}" $green
    (${COMMAND_PULL_SOURCES}) 2>&1
    check_res $?
    (${COMMAND_UPDATE_SOURCES_HOMELESS}) 2>&1
    check_res $?
popd > /dev/null 2>&1

pushd ${SOURCES_HOMELESS_REPORT_ENGINE} > /dev/null 2>&1
    cecho "Updating directory `pwd` with revision ${REV_HRE}" $green
    (${COMMAND_PULL_SOURCES}) 2>&1
    check_res $?
    (${COMMAND_UPDATE_SOURCES_HRE}) 2>&1
    check_res $?
popd > /dev/null 2>&1

# 2. Replacing configuration according current layout

pushd ${SOURCES_HOMELESS} > /dev/null 2>&1
    APP_CONFIG=./src/main/resources/application.properties
    cecho "Replacing properties in the file `readlink -f ${APP_CONFIG}`" $green
    sed -i "s|photos.*=.*|photos = ${IMAGES}|" ${APP_CONFIG}
    check_res $?
    sed -i "s|timestampFile.*=.*|timestampFile = ${BASE}\/timestamp.txt|" ${APP_CONFIG}    
    check_res $?    
    sed -i "s|reportEngineUrl.*=.*|reportEngineUrl = http:\/\/${THIS_IP}/homeless-report-engine\/|" ${APP_CONFIG}        
    check_res $?
    sed -i "s|profilesDir.*=.*|profilesDir = ${PROFILES}|" ${APP_CONFIG}
    check_res $?
    print_file ${APP_CONFIG}
    SPRING_CONFIG=./src/main/webapp/WEB-INF/applicationContext.xml
    cecho "Replacing Spring configuration `readlink -f ${SPRING_CONFIG}`" $green
    sed -i "s/DevelopmentDB/ProductionDB/" ${SPRING_CONFIG}
    check_res $?
popd > /dev/null 2>&1

pushd ${SOURCES_HOMELESS_REPORT_ENGINE} > /dev/null 2>&1
    APP_CONFIG=./src/main/resources/application.properties
    cecho "Replacing properties in the file `readlink -f ${APP_CONFIG}`" $green
    sed -i "s|templatesDir.*=.*|templatesDir = ${TEMPLATES}|" ${APP_CONFIG}
    check_res $?
    sed -i "s|contractsDir.*=.*|contractsDir = ${CONTRACTS}|" ${APP_CONFIG}
    check_res $?    
    sed -i "s|profilesDir.*=.*|profilesDir = ${PROFILES}|" ${APP_CONFIG}
    check_res $?
    print_file ${APP_CONFIG}
    SPRING_CONFIG=./src/main/webapp/WEB-INF/spring-config.xml
    cecho "Replacing Spring configuration `readlink -f ${SPRING_CONFIG}`" $green
    sed -i "s/DevelopmentDB/ProductionDB/" ${SPRING_CONFIG}
    check_res $?
popd > /dev/null 2>&1

# 3. Building applications (WAR files)


pushd ${SOURCES_HOMELESS} > /dev/null 2>&1
    mvn clean package
    check_res $?
popd > /dev/null 2>&1

pushd ${SOURCES_HOMELESS_REPORT_ENGINE} > /dev/null 2>&1
    mvn clean package
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
    rm -rf homeless homeless-report-engine
    check_res $?
    rm -f homeless.war homeless-report-engine.war
    check_res $?
    cecho "Deploying new applications" $green
    cp -f ${SOURCES_HOMELESS}/target/homeless.war .
    check_res $?
    cp -f ${SOURCES_HOMELESS_REPORT_ENGINE}/target/homeless-report-engine.war .
    check_res $?
popd > /dev/null 2>&1

pushd ${TOMCAT_HOME}/logs > /dev/null 2>&1
    logs_file_stamp=`date +"%d_%m_%Y__%H_%M_%S"`
    logs_file_name=${BACKUPS}/logs_backup_${logs_file_stamp}.zip
    cecho "Backing up logs to ${logs_file_name}" $green
    zip -9 -r ${logs_file_name} *
    rm -f *.out *.txt * log
    check_res $?

    cecho "Placing timestamp before starting homeless application" $magenta
    D=`date +"%H:%M:%S %d.%m.%Y"`
    echo "${D} (${REV_H}, ${REV_HRE})">${TIMESTAMP}
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
