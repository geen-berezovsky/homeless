#!/bin/bash

# Preparing new database which is the full copy of production

BACKUP_DIR=/opt/backup/homeless-production
DEMO_INVENTORY=/opt/HOMELESS-DEMO/Demo-Inventory/
DEMO_APP=/opt/HOMELESS-DEMO
INVENTORY_PREFIX=/opt/HOMELESS-PRODUCTION/Inventory
_TMP=/tmp/_demo
rm -rf ${_TMP} && mkdir -p ${_TMP}

echo "Deleting the existing database homeless_demo"
echo "DROP DATABASE homeless_demo" | mysql --user=homeless_demo --password=homeless_demo homeless_demo
echo "Creating the new database"
echo "CREATE DATABASE homeless_demo CHARACTER SET utf8 COLLATE utf8_general_ci;" | mysql --user=homeless_demo --password=homeless_demo
echo "Loading the DB dump from last backup"
LAST_DB_BACKUP=`ls ${BACKUP_DIR}/db-* | sort | tail -n 1`
echo "Last backup is ${BACKUP_DIR}/${LAST_DB_BACKUP}. Installing it to the DEMO instance"
unzip -d ${_TMP} ${BACKUP_DIR}/${LAST_DB_BACKUP}
mysql --user=homeless_demo --password=homeless_demo homeless_demo < ${_TMP}/homeless.sql
rm -f ${_TMP}/homeless.sql
echo "Applying new patch for the database"
mysql --user=homeless_demo --password=homeless_demo homeless_demo < ../db/patch.sql

# Preparing inventory
echo "Preparing inventory"
LAST_INVENTORY_BACKUP=`ls ${BACKUP_DIR}/inv-* | sort | tail -n 1`
pushd ${DEMO_INVENTORY}
    unzip ${BACKUP_DIR}/${LAST_INVENTORY_BACKUP}
popd

mv -f ${DEMO_INVENTORY}/${INVENTORY_PREFIX}/Photo ${DEMO_INVENTORY}
mv -f ${DEMO_INVENTORY}/${INVENTORY_PREFIX}/Contracts ${DEMO_INVENTORY}
mv -f ${DEMO_INVENTORY}/${INVENTORY_PREFIX}/Profiles ${DEMO_INVENTORY}

# Using latest templates from homeless-report-engine project!
#mv -f ${DEMO_INVENTORY}/${INVENTORY_PREFIX}/Templates ${DEMO_INVENTORY}
rm -rf ${DEMO_INVENTORY}/opt

echo "Starting TOMCAT"
${DEMO_APP}/tomcat/bin/startup.sh
echo "Application has been updated"
