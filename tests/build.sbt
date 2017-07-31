name := "SSHD docker container tests"

scalaVersion := "2.12.3"

version := "1.0-SNAPSHOT"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"

libraryDependencies += "com.dimafeng" %% "testcontainers-scala" % "0.7.0" % "test"
