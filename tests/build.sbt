name := "SSHD docker container tests"

scalaVersion := "2.11.8"

version := "1.0-SNAPSHOT"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"

libraryDependencies += "com.github.docker-java" % "docker-java" % "3.0.12"

libraryDependencies += "com.veact" %% "scala-ssh" % "0.8.0"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.7"