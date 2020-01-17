# Weather app on Quarkus
This is a simple project based on JAX-RS, JPA and Microprofile Health specification. 

The code is taken from the application weather-app [GitHub Pages](https://github.com/tqvarnst/weather-app) used in Katacoda JEE Openshift learning (https://www.katacoda.com/openshift/courses/middleware/middleware-javaee8) and modified to run on top of Quarkus and Openshift 4.2.

## Install on Openshift
Here you can find the instruction to run the application as atomic project using Red Hat Code Ready Container developer environment

1. Start the Red Hat Code Ready Container container

```sh
$ crc start
```

2. Login to Openshift 4 environment as developer

```sh
$ oc login -u developer -p developer https://api.crc.testing:6443
```

3. Create a new project 

```sh
$ oc new-project weather-app-eap --display-name="Weather App Quarkus"
```

4. Create the Postgresql environment

```sh
$ oc import-image rhscl/postgresql-10-rhel7 --from=registry.access.redhat.com/rhscl/postgresql-10-rhel7 --confirm \
$ oc new-app -e POSTGRESQL_USER=mauro -ePOSTGRESQL_PASSWORD=secret -ePOSTGRESQL_DATABASE=weather postgresql-10-rhel7 --name=weather-postgresql
$ oc new-app -e POSTGRESQL_USER=mauro -ePOSTGRESQL_PASSWORD=secret -ePOSTGRESQL_DATABASE=weather postgresql-10-rhel7 --name=weather-postgresql
```

5. Create the build related to the weather app that will be deployed as fat jar with Quarkus

```sh
$ oc new-build --binary=true --name=weather-app-quarkus -l app=weather-app-quarkus
```

6. Patch the build config in order to set the Docker strategy

```sh
$ oc patch bc/weather-app-quarkus -p "{\"spec\":{\"strategy\":{\"dockerStrategy\":{\"dockerfilePath\":\"src/main/docker/Dockerfile.jvm\"}}}}"
```
 
7. Run the Maven build

```sh
$ mvn package -Dquarkus.profile=dev-postgresql-db
```

8. Start the build of the application on Openshift

```sh
$ oc start-build weather-app-quarkus --from-dir=. --follow
```

9. Create the weather application for Quarkus and configure it

```sh
$ oc new-app --image-stream=weather-app-quarkus:latest \
   -e quarkus.datasource.url='jdbc:postgresql://weather-postgresql:5432/weather' \
   -e quarkus.jaeger.service-name=weatherservice \
   -e quarkus.jaeger.sampler-type=const \
   -e quarkus.jaeger.sampler-param=1 \
   -e quarkus.jaeger.endpoint=http://jaeger-collector:14268/api/traces
```

10. Expose the route in order to make the application available outside of Openshift

```sh
$ oc expose service weather-app-quarkus
```

### Test the application
You can test the application using the route http://weather-app-quarkus-dev-conf-2020-demo.apps-crc.testing. You will be able to connect to the weather application and check the weather in the selected cities.

You can also update the expected weather connecting to postgresql and change the value of the weather

```sh
$ oc rsh dc/weather-postgresql \

$ psql -U $POSTGRESQL_USER $POSTGRESQL_DATABASE -c "update city set weathertype='rainy-5' where id='nyc'";
```
Now you can check again the weather for New York city and verify that the expected weather is rainy.

You can also test the liveness of the application, as described into the Microprofile health specifications, using the URL http://weather-app-quarkus-dev-conf-2020-demo.apps-crc.testing/health/live. You will see the message that is the outcome of the database check validation.

You can also get the metrics of the application, as described into the Microprofile Metrics specifications, using the command:
```sh
$ curl -H"Accept: application/json" http://weather-app-quarkus-dev-conf-2020-demo.apps-crc.testing/metrics/application
```

## Test outside Openshift
If you want to run the application without Openshift you can lauch the following command:

```sh
$ mvn package quarkus:dev -Dquarkus.profile=dev-memory-db
```

In this way you can test the application connecting to http://localhost:8080. The application will use an h2 in memory database.