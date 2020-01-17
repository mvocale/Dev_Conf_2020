# Dev_Conf_2020_Demo
Demo for DevConf 2020 related to the future of Java SE, JakartaEE and Microprofile in cloud environment.
It contains two projects:
- A simple project, weather-app-eap, that is based on JAX-RS, JPA and Microprofile Health specification. The code is taken from the application weather-app [GitHub Pages](https://github.com/tqvarnst/weather-app) used in Katacoda JEE Openshift learning (https://www.katacoda.com/openshift/courses/middleware/middleware-javaee8) and modified to run on top of JBoss EAP 7.2.4 and Openshift 4.2
- A simple project, weather-app-quarkus, that is based on JAX-RS, JPA and Microprofile Health specification developed using Quarkus framework (https://quarkus.io/). The code is taken from the application weather-app GitHub Pages used in Katacoda JEE Openshift learning (https://www.katacoda.com/openshift/courses/middleware/middleware-javaee8) and modified to use Quarkus and Openshift 4.2. The code was
integrate with Microprofile health, opentracing and metrics specifications.

## Install on Openshift
To install the entire demo project on Openshift, remote cluster or local Red Hat Code Ready Container environment, you can launch the deploy-openshift script:

```sh
$ ./deploy-openshift.sh
```

Remember to login to Openshift environment before launch the script.