# Comparing Template engines for Spring WebFlux

This is a demo project, based on former _Comparing Template engines for Spring Web MVC_,
which accompanied [Jeroen Reijn](https://github.com/jreijn) in
["Shoot-out! Template engines for the JVM"](http://www.slideshare.net/jreijn/comparing-templateenginesjvm)
presentation, which shows the differences among several Java template engines in combination with Spring MVC,
now with **Spring WebFlux**.
Most template engines were removed from former benchmark, since they do not support
PSSR (_Progressive server-side rendering)_.

Template engines used in this project are:

* [Thymeleaf](http://www.thymeleaf.org/) - v3.0.11.RELEASE
* [HtmlFlow](https://github.com/xmlet/HtmlFlow/) - v3.5
* [Groovy Templates](https://groovy.apache.org/) - v2.5.6
* [kolinx.html](https://github.com/Kotlin/kotlinx.html) - v7.1

Another key difference of this benchmark is the use of [JMH](https://github.com/openjdk/jmh)
to perform the benchmark rather than using Apache HTTP server benchmarking tool.

## Build and run

Build the project with

    mvn clean install

Run the project with

    java -jar target/template-engines.jar -i 4 -wi 4 -f 1 -r 2 -w 2 -p route=/thymeleaf,/htmlFlow,/kotlinx

See the demo URLs:

  - http://localhost:8080/thymeleaf
  - http://localhost:8080/htmlFlow
  - http://localhost:8080/groovy
  - http://localhost:8080/kotlinx

