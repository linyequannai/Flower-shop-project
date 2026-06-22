@echo off
cd /d "%~dp0"
set JAVA_HOME=C:\Program Files\Java\jdk-21.0.11
mvn clean spring-boot:run
