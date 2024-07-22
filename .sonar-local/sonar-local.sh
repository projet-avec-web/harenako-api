#!/bin/bash

./gradlew clean sonar -D "sonar.projectKey=sonar-test" -D "sonar.host.url=http://localhost:9000" -D "sonar.login=sqp_e41cf13e361e647f7a2f7eeea25440cf97176fbe"