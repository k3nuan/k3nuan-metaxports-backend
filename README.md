# backend

This document describe the technical project of the backend [API](API.md).

## I. Codebase

Available in [GitHub](https://github.com).

## II. Dependencies

Managed with Maven in pom.xml file. In order to reduce the need of having
a Maven environment configured in build machines, we are adding the maven wrapper
which will manage and configure maven for us on any environment that will require it.

## IV. Build, release, run

**Local environment**

* Run service

	`spring-boot:run -Dspring-boot.run.fork=false`

* Build the jar file executing 

  `./mvnw clean package -DskipTests`

  It will create the main jar file in directory `target`. This is the artifact we will use to create the docker image.
