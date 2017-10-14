@REM ECHO OFF

SET PROPER_RDP=lib/properJavaRDP.jar
cmd /c cd /d %~dp0 & java -jar %PROPER_RDP% -l WARN %*
