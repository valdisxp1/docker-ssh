name := "SSHD docker container tests"

scalaVersion := "2.11.8"

version := "1.0-SNAPSHOT"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"

libraryDependencies += "com.github.docker-java" % "docker-java" % "3.0.12"
