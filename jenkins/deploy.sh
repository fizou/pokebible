#!/usr/bin/env bash

echo 'The following script deploy the java application to the Tomcat'
set -x
mvn jar:jar install:install help:evaluate -Dexpression=project.name
set +x

echo 'The following complex command extracts the value of the <name/> element'
echo 'within <project/> of your Java/Maven project''s "pom.xml" file.'
set -x
NAME=`mvn help:evaluate -Dexpression=project.name | grep "^[^\[]"`
set +x

echo 'The following complex command behaves similarly to the previous one but'
echo 'extracts the value of the <version/> element within <project/> instead.'
set -x
VERSION=`mvn help:evaluate -Dexpression=project.version | grep "^[^\[]"`
set +x

echo 'Final Step...'
set -x
curl -u admin:admin --upload-file target/${NAME}-${VERSION}.war "http://tomcat:8080/manager/text/deploy?path=/pokebible&update=true"
