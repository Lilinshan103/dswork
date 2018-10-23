
set p=%~dp0
pushd %p%

set BAT_HOME=%p%

:JavaHome
if "%JAVA_HOME%" == "" goto noJavaHome
set PATH=%JAVA_HOME%\bin;%PATH%
set classpath=.;%JAVA_HOME%\lib
goto okJava

:noJavaHome
if "%JRE_HOME%" == "" goto noJreHome
set PATH=%JRE_HOME%\bin;%PATH%
set classpath=.;%JRE_HOME%\lib
goto okJava
:noJreHome
goto noJava


:noJava
set JRE_HOME=E:\WorkServer\Java\jre1.7.0_79
goto noJavaHome
:okJava


set libpath=.
for /R lib %%v in (*.jar) do set libpath=!libpath!;%%v
set libpath=%libpath:.;=%
