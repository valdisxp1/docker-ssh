FROM java:openjdk-8-jre-alpine
MAINTAINER Valdis Adamsons <valdisxp1@gmail.com>

RUN apk add --no-cache wget openssl bash
RUN wget -qO - https://github.com/sbt/sbt/releases/download/v0.13.15/sbt-0.13.15.tgz | tar xzv -C /
RUN ["ln", "-s", "/sbt/bin/sbt", "/usr/bin/sbt"]
RUN ["sbt","sbtVersion"]

RUN apk add --no-cache openssh

cmd ["sbt"]
