#!/bin/bash
echo "Updating application at application server"
echo "Stopping tomcat"
/opt/tomcat-reporting/bin/shutdown.sh
echo "Waiting 10 seconds to stop"
sleep 10
echo "Deleting old application"
rm -rf /opt/tomcat-reporting/webapps/homeless*
echo "Deploying the new application"
mv -f ../target/homeless-report-engine.war /opt/tomcat-reporting/webapps/
echo "Starting tomcat"
/opt/tomcat-reporting/bin/startup.sh
echo "Application has been updated"
