@echo off
if exist %1\ (
	setlocal ENABLEDELAYEDEXPANSION
	xcopy "%~dp0\common\log\slf4j-api-1.7.25.jar" "%1" /y
	xcopy "%~dp0\common\aopalliance-1.0.jar" "%1" /y
	xcopy "%~dp0\common\gson-2.8.5.jar" "%1" /y
	xcopy "%~dp0\common\jstl-1.2.jar" "%1" /y
	for /r "%~dp0/common/commons" %%v in (*.jar) do (
		xcopy "%%v" "%1" /y
	)
	for /r "%~dp0/common/dswork" %%v in (*.jar) do (
		xcopy "%%v" "%1" /y
	)
	for /r "%~dp0/common/spring" %%v in (*.jar) do (
		xcopy "%%v" "%1" /y
	)
	for /r "%~dp0/common/log/log4j2" %%v in (*.jar) do (
		xcopy "%%v" "%1" /y
	)
	xcopy "%~dp0\mybatis\mybatis-3.4.6.jar" "%1" /y
	xcopy "%~dp0\mybatis\mybatis-spring-1.3.2.jar" "%1" /y
	xcopy "%~dp0\database\mysql\mysql-connector-java-5.1.45-bin.jar" "%1" /y
	endlocal
)
pause