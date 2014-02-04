gatein-oauth-2.0
================

RedHat GateIn demo portlets for OAuth 2.0 integration with Google and FaceBook

0. Pre - Milestone
 - [x] - maven 3
 - [x] - gitHub
 - [x] - JSF 2.2
 - [x] - CDI

1. Milestone #1 - Google
 - [x] - OAuth 2 Google
 - [x] - load Profile information
 - [x] - load Drive files

2. Milestone #2 - FaceBook
 - [x] - OAuth 2 FaceBook
 - [x] - events for user
 - [/] - messages for user

3. Milestone #3 - GateIn import
 - [x] - create portlets importable to RedHat GateIn portal

This is a master diploma thesis of Bc. Matej Kobza, student at Faculty of Informatics, Masaryk University, Brno, 2013

How to run this project
---------------
1. To run this project go to the gatein-oauth-2.0-core into
src/main/resources/ and follow instructions in files **google.properties** and **facebook.properties**
2. Download GateIn portal from http://www.jboss.org/gatein/download
3. Start standalone GateIn Portal
    For Linux:   JBOSS_HOME/bin/standalone.sh
    For Windows: JBOSS_HOME\bin\standalone.bat
4. Build main project gatein-oauth-2.0-master by running mvn install
5. Go to gatein-oauth-2.0-master -> gatein-oauth-2.0-web -> target and copy oauth-2-gtn-web-1.0.0-snapshot.war
6. Copy the war into JBOSS_HOME/standalone/deployments