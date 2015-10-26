@echo on
REM
echo This script will get the latest Homeless project version and install into temporary environment only for testing needs
echo First, set up the external files. Tomcat should be added and configured manually according instruction. MySQL service should be already installed, configured and started.
echo Also pandoc.exe latest version should be installed and available in the PATH
echo This script will update MySQL database, resources and the code only.

set FULL_DUMP=
set MYSQL_BIN="C:\Program Files\MySQL\MySQL Server 5.6\bin"
set MYSQL_ROOT_USER=root
set MYSQL_ROOT_PASSWORD=*****

REM
REM
REM Homeless dir is C:\homeless-project\testdir
set HOMELESS_HOME=C:\homeless-project\testdir
REM
REM Tomcat dir is %HOMELESS_HOME%\tomcat
set TOMCAT_HOME=%HOMELESS_HOME%\TOMCAT
REM
REM HG_HOME for Homeless project is %HOMELESS_HOME%\homeless
set HG_HOMELESS_HOME=%HOMELESS_HOME%\homeless
REM
REM HG_HOME for Homeless Report Engine project is %HOMELESS_HOME%\homeless-report-engine
set HG_HOMELESS_REPORT_ENGINE_HOME=%HOMELESS_HOME%\homeless-report-engine

pushd %TOMCAT_HOME%\bin
	call shutdown.bat > NUL 2>&1
	echo Waiting 10 seconds to stop...
	ping 127.0.0.1 -n 10 > NUL
popd

del /S /Q %TOMCAT_HOME%\webapps\homeless.war > NUL 2&>1
del /S /Q %TOMCAT_HOME%\webapps\homeless-report-engine.war > NUL 2&>1
del /S /Q %TOMCAT_HOME%\webapps\homeless > NUL 2&>1
del /S /Q %TOMCAT_HOME%\webapps\homeless-report-engine > NUL 2&>1

rd /S /Q %TOMCAT_HOME%\webapps\homeless > NUL 2&>1
rd /S /Q %TOMCAT_HOME%\webapps\homeless-report-engine > NUL 2&>1

REM mkdir %HOMELESS_HOME% %HG_HOMELESS_HOME% %HG_HOMELESS_REPORT_ENGINE_HOME%

pushd %HG_HOMELESS_HOME%
	call hg pull
	call hg up -C
	pushd lib
		call mvn install:install-file -DgroupId=ru.homeless.org.primefaces -DartifactId=primefaces -Dversion=4.0.6-UR2 -Dfile=primefaces-4.0.6-UR2.jar -Dpackaging=jar
		call mvn install:install-file -DgroupId=ru.homeless.org.primefaces -DartifactId=primefaces -Dversion=4.0.6-UR2 -Dfile=primefaces-4.0.6-UR2-sources.jar -Dpackaging=jar -Dclassifier=sources
	popd
	call mvn clean package
popd

pushd %MYSQL_BIN%
	call mysql.exe --user=%MYSQL_ROOT_USER% --password=%MYSQL_ROOT_PASSWORD% < %HG_HOMELESS_HOME%\db\init-test.sql
	call mysql.exe --user=homeless_test --password=homeless_test homeless_test < %HG_HOMELESS_HOME%\db\homeless.base.sql
	call mysql.exe --user=homeless_test --password=homeless_test homeless_test < %HG_HOMELESS_HOME%\db\patch.sql
	call mysql.exe --user=homeless_test --password=homeless_test homeless_test < %HG_HOMELESS_HOME%\db\postinstall.sql
popd

pushd %HG_HOMELESS_REPORT_ENGINE_HOME%
	call hg pull
	call hg up -C
	call mvn clean package
popd
