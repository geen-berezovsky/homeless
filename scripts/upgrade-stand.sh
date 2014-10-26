#!/bin/bash
if [ -f ~/full.tar.gz ] ; then
    rm -f ~/full.tar.gz
fi
if [ -f ~/db.zip ] ; then
    rm -f ~/db.zip
fi
if [ -d ~/tmp_ext ] ; then
    rm -rf ~/tmp_ext
    mkdir ~/tmp_ext
fi
if [ -d ~/demo ] ; then
    rm -rf ~/demo
    mkdir ~/demo
fi

CONNECT_STR="stinger@10.0.0.2"
echo "Getting info about latest backup for local restoring"
LAST_FULL=`ssh ${CONNECT_STR} 'ls /ServRoot/DB/Backup/full-* | sort | tail -n 1'`
echo "Latest backup is ${LAST_FULL}. Downloading it..."
scp stinger@10.0.0.2:${LAST_FULL} ~/full.tar.gz
ls -la ~/full.tar.gz
echo "Getting info about latest DB data backup for local restoring"
LAST_DB=`ssh ${CONNECT_STR} 'ls /ServRoot/DB/Backup/db-* | sort | tail -n 1'`
echo "Latest DB data backup is ${LAST_DB}. Downloading it..."
scp stinger@10.0.0.2:${LAST_DB} ~/db.zip
cp ../db/patch.sql ~/tmp_ext/
pushd ~/tmp_ext
tar -zxf ~/full.tar.gz
unzip ~/db.zip
echo "Moving to the demo stand"
mv -f Photo ~/demo/
echo "Moving contracts to the demo stand"
mv -f Contracts ~/demo/
echo "Deleting the existing database"
echo "DROP DATABASE homeless" | mysql --user=homeless --password=homeless homeless
echo "Creating the new database"
echo "CREATE DATABASE homeless CHARACTER SET utf8 COLLATE utf8_general_ci;" | mysql --user=homeless --password=homeless
echo "Loading the DB dump"
mysql --user=homeless --password=homeless homeless < homeless.sql
echo "Applying patch for the database"
mysql --user=homeless --password=homeless homeless < patch.sql
echo "Deleting all other data from temp directory"
rm -rf ~/tmp_ext/*
CUR_DATE=`date`
echo "Setting the new update timestamp ${CUR_DATE}"
echo ${CUR_DATE}>~/demo/timestamp.txt
popd
echo "Updating application at application server"
echo "Stopping tomcat"
/opt/tomcat/bin/shutdown.sh
echo "Waiting 10 seconds to stop"
sleep 10
echo "Deleting old application"
rm -rf /opt/tomcat/webapps/homeless*
echo "Deploying the new application"
mv -f ../target/homeless.war /opt/tomcat/webapps/
echo "Starting tomcat"
/opt/tomcat/bin/startup.sh
echo "Application has been updated"
