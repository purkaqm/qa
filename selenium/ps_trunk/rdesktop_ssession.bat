@ECHO OFF
REM
REM This script kills session for specified user, then create new and wait while it is active
REM

SET RDESKTOP=rdesktop.bat
SET ARGS=%*
SET SCREEN_RESOLUTION=1440x2000
SET THIS_HOST=""
SET USER=NO-USER
SET DOMAIN=NO-DOMAIN

REM determine user from input
FOR %%i IN (%*) DO (IF /I "%%i"=="LOCALHOST" (SET THIS_HOST=%%i))
IF '%1'=='-u' (SET USER=%2%)
IF '%2'=='-u' (SET USER=%3%)
IF '%3'=='-u' (SET USER=%4%)
IF '%4'=='-u' (SET USER=%5%)
IF '%5'=='-u' (SET USER=%6%)
IF '%6'=='-u' (SET USER=%7%)
IF '%7'=='-u' (SET USER=%8%)
IF '%8'=='-u' (SET USER=%9%)
IF '%9'=='-u' (SET USER=%10%)
IF '%10'=='-u' (SET USER=%11%)
IF '%11'=='-u' (SET USER=%12%)
IF '%12'=='-u' (SET USER=%13%)

IF '%USER%'=='NO-USER' GOTO USAGE
CALL SET ARGS=%%ARGS:-u %USER%=%%

REM determine domain from input
IF '%1'=='-d' (SET DOMAIN=%2%)
IF '%2'=='-d' (SET DOMAIN=%3%)
IF '%3'=='-d' (SET DOMAIN=%4%)
IF '%4'=='-d' (SET DOMAIN=%5%)
IF '%5'=='-d' (SET DOMAIN=%6%)
IF '%6'=='-d' (SET DOMAIN=%7%)
IF '%7'=='-d' (SET DOMAIN=%8%)
IF '%8'=='-d' (SET DOMAIN=%9%)
IF '%9'=='-d' (SET DOMAIN=%10%)
IF '%10'=='-d' (SET DOMAIN=%11%)
IF '%11'=='-d' (SET DOMAIN=%12%)
IF '%12'=='-d' (SET DOMAIN=%13%)
CALL SET ARGS=%%ARGS:-d %DOMAIN%=%%

REM select right user if input like '... -u "pstest,pstest,pstest2,pstest,pstest,pstest2,pstest" ...'
FOR /F "Skip=1" %%i IN ('WMIC Path Win32_LocalTime Get DayOfWeek') DO (
    SET DAY=%%i
    GOTO BREAK_WMIC
)
:BREAK_WMIC
IF "%DAY%"=="0" (SET DAY=7)

ECHO.This Day is [%DAY%]
FOR /F "Tokens=%DAY% Delims=," %%k IN (%USER%) DO SET USER=%%k
ECHO.This User is [%USER%]
ECHO.Command line is [%ARGS%]

IF /I "%THIS_HOST%"=="LOCALHOST" (
    FOR /F "Skip=1 Tokens=2,3" %%A IN ('QWINSTA %USER%') DO (
        ECHO.kill [%USER%] session [%%A][%%B] on [%THIS_HOST%]
        RWINSTA %%A
        RWINSTA %%B
    )
    FOR /F %%i IN ('hostname') DO (
       IF '%DOMAIN%'=='NO-DOMAIN' (SET DOMAIN=%%i)
       GOTO BREAK_HOST
    )
)
:BREAK_HOST

ECHO.All active sessions:
QWINSTA

ECHO.Command is : "%RDESKTOP% -l INFO -g %SCREEN_RESOLUTION% %ARGS% -u %USER% -d %DOMAIN%"
CMD /C CD /D %~dp0 & %RDESKTOP% -l INFO -g %SCREEN_RESOLUTION% %ARGS% -u %USER% -d %DOMAIN%

ECHO.Rdp finish with exit code [%ERRORLEVEL%]

IF /I "%THIS_HOST%"=="LOCALHOST" GOTO WAIT_UNTIL
GOTO EXIT

:WAIT_UNTIL
ECHO.Wait while user [%USER%] is active on [%THIS_HOST%]
:LAB
CMD /C QWINSTA %USER% | FINDSTR /I %USER% 1>NUL
IF %ERRORLEVEL%==0 GOTO LAB
ECHO.No active session now
GOTO EXIT

:USAGE
ECHO.Usage: %~n0 -u USERNAME -p PASSWORD -d DOMAIN -g WxH -s CMD HOST
ECHO.Where:
ECHO.      HOST                                         remote host or ip-address
ECHO.   -d DOMAIN                                       logon domain
ECHO.   -g WxH                                          desktop geometry
ECHO.   -p PASSWORD                                     password
ECHO.   -s CMD                                          shell command
ECHO.   -u USERNAME                                     user name
:EXIT


