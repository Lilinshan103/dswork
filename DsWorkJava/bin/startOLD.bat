@echo off
setlocal

title "我的应用程序"

call sconfig.bat

set classpath=%classpath%;%BAT_HOME%\lib\aopalliance-1.0.jar
set classpath=%classpath%;%BAT_HOME%\lib\commons-collections-3.2.2.jar
set classpath=%classpath%;%BAT_HOME%\lib\commons-dbcp-1.4.jar
set classpath=%classpath%;%BAT_HOME%\lib\commons-io-2.5.jar
set classpath=%classpath%;%BAT_HOME%\lib\commons-lang-2.6.jar
set classpath=%classpath%;%BAT_HOME%\lib\commons-logging-1.2.jar
set classpath=%classpath%;%BAT_HOME%\lib\commons-pool-1.6.jar
set classpath=%classpath%;%BAT_HOME%\lib\dswork.core.jar
set classpath=%classpath%;%BAT_HOME%\lib\dswork.jdbc.jar
set classpath=%classpath%;%BAT_HOME%\lib\dswork.ognl.jar
set classpath=%classpath%;%BAT_HOME%\lib\dswork.spring.jar
set classpath=%classpath%;%BAT_HOME%\lib\log4j-api-2.3.jar
set classpath=%classpath%;%BAT_HOME%\lib\log4j-core-2.3.jar
set classpath=%classpath%;%BAT_HOME%\lib\log4j-slf4j-impl-2.3.jar
set classpath=%classpath%;%BAT_HOME%\lib\slf4j-api-1.7.22.jar
set classpath=%classpath%;%BAT_HOME%\lib\spring-aop.jar
set classpath=%classpath%;%BAT_HOME%\lib\spring-beans.jar
set classpath=%classpath%;%BAT_HOME%\lib\spring-context.jar
set classpath=%classpath%;%BAT_HOME%\lib\spring-core.jar
set classpath=%classpath%;%BAT_HOME%\lib\spring-expression.jar
set classpath=%classpath%;%BAT_HOME%\lib\spring-jdbc.jar
set classpath=%classpath%;%BAT_HOME%\lib\spring-tx.jar
set classpath=%classpath%;%BAT_HOME%\lib\mybatis-3.2.8.jar
set classpath=%classpath%;%BAT_HOME%\lib\mybatis-spring-1.2.5.jar

set classpath=%classpath%;%BAT_HOME%\lib\axis.jar
set classpath=%classpath%;%BAT_HOME%\lib\commons-discovery-0.2.jar
set classpath=%classpath%;%BAT_HOME%\lib\jaxrpc.jar
set classpath=%classpath%;%BAT_HOME%\lib\saaj.jar
set classpath=%classpath%;%BAT_HOME%\lib\wsdl4j-1.6.2.jar

set classpath=%classpath%;%BAT_HOME%\lib\mysql-connector-java-5.1.31-bin.jar
set classpath=%classpath%;%BAT_HOME%\lib\sqlite-jdbc-3.7.2.jar
set classpath=%classpath%;%BAT_HOME%\lib\mail.jar
set classpath=%classpath%;%BAT_HOME%\lib\gson-2.3.1.jar

SET classpath=%classpath%;%BAT_HOME%\classes;

java -classpath %classpath% dswork.cs.Start
