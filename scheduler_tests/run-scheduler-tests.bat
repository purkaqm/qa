@echo off

setlocal

REM **** precondition *****************
REM
REM The test requires that the user preferences of the user defined bellow
REM are set to the GMT time zone. Othwerise the test will fail.
REM Also, the holidays around chrismas need to be configured as in the 
REM example config file uploaded in this directory
REM 

rem **** Make modifications here ******

set JAVA_HOME=C:\jdk1.3.0_02
set JIKES_HOME=C:\Program Files\jikes-1.15\bin
set PRODUCT_HOME=C:\CVSRoot\main-latest\product

set USER_NAME=admin
set PASSWORD=admin
set context_name=test

rem **** Make modifications here ******

set JRELIB=%JAVA_HOME%\jre\lib\rt.jar;%JAVA_HOME%\jre\lib\i18n.jar

set WSLIB=%PRODUCT_HOME%\lib\jar\activation.jar;%PRODUCT_HOME%\lib\jar\concurrent.jar;%PRODUCT_HOME%\lib\jar\jaxp.jar;%PRODUCT_HOME%\lib\jar\jcert.jar;%PRODUCT_HOME%\lib\jar\jdbc2_0-stdext.jar;%PRODUCT_HOME%\lib\jar\jndi.jar;%PRODUCT_HOME%\lib\jar\jnet.jar;%PRODUCT_HOME%\lib\jar\jsse.jar;%PRODUCT_HOME%\lib\jar\jta-spec1_0_1.jar;%PRODUCT_HOME%\lib\jar\mail.jar;%PRODUCT_HOME%\lib\jar\servlet.jar;%PRODUCT_HOME%\lib\jar\Sprinta2000.jar;%PRODUCT_HOME%\lib\jar\xalan.jar;%PRODUCT_HOME%\lib\jar\xerces.jar;%PRODUCT_HOME%\lib\jar\junit.jar;%PRODUCT_HOME%\lib\jar\powersteering.jar

echo Performing tests javac compiling...
md classes
dir /b /s ..\unit\com\cinteractive\directedgraph\tests\*.java > sources
dir /b /s ..\unit\com\cinteractive\ps3\scheduler\tests\*.java >> sources
%JAVA_HOME%\bin\javac -nowarn -classpath classes;%WSLIB%;junit.jar -d classes @sources 2> javac.log

echo Performing tests...
%JAVA_HOME%\bin\java -cp classes;%WSLIB% -Dcom.cinteractive.ps3.scheduler.tests.context.name=%context_name% -Dcom.cinteractive.ps3.uri.client.root=\ -Dcom.cinteractive.ps3.scheduler.tests.user.password=%PASSWORD% -Dcom.cinteractive.ps3.scheduler.tests.generictest=%PRODUCT_HOME%\test\scheduler_tests\generic_test.xml -Dcom.cinteractive.ps3.home=%PRODUCT_HOME%\web -Djavax.xml.parsers.DocumentBuilderFactory=org.apache.xerces.jaxp.DocumentBuilderFactoryImpl -Dcom.cinteractive.ps3.scheduler.tests.user.name=%USER_NAME% -Dcom.cinteractive.ps3.scheduler.tests.path=%PRODUCT_HOME%\test\scheduler_tests junit.textui.TestRunner com.cinteractive.ps3.scheduler.tests.AllTests 2> err.log > out.log

echo Deleting temporary files...
rem rd /s /q classes
rem del sources

echo Done.
echo See *.log files

endlocal