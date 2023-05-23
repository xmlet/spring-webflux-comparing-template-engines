package com.jeroenreijn.examples.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.net.URI;
import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(properties = "spring.main.web-application-type=reactive")
@AutoConfigureWebTestClient(timeout = "PT1M")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PresentationIntegrationTest {

    private static final String THYMELEAF_FORMED_HTML_ASSERTION() { return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8"/>
                <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
                <meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
                <title>JFall 2013 Presentations - Thymeleaf</title>
                <link rel="stylesheet" href="/webjars/bootstrap/4.3.1/css/bootstrap.min.css"/>
            </head>
            <body>
            	<div class="container">
                    <div class="pb-2 mt-4 mb-3 border-bottom">
                <h1>JFall 2013 Presentations - Thymeleaf</h1>
            </div>
                    <div class="card mb-3 shadow-sm rounded">
                        <div class="card-header">
                            <h5 class="card-title">Shootout! Template engines on the JVM - Jeroen Reijn</h5>
                        </div>
                        <div class="card-body">Are you still using JavaServer Pages as your main template language? With the popularity of template engines for other languages like Ruby and Scala and the shift in doing more MVC in the browser there are quite some new and interesting new template languages available for the JVM. During this session we will take a look at the less known, but quite interesting new template engines and see how they compare with the industries standards.</div>
                    </div><div class="card mb-3 shadow-sm rounded">
                        <div class="card-header">
                            <h5 class="card-title">HoneySpider Network: a Java based system to hunt down malicious websites - Niels van Eijck</h5>
                        </div>
                        <div class="card-body">Legitimate websites such as news sites happen to get compromised by attackers injecting malicious content. The aim of these so-called &#8220;watering hole attacks&#8221; is to infect as many visitors of a website as possible, and are sometimes even targeted at a specific group of individuals. It is increasingly important to detect these infections at an early stage.<br/><br/>HoneySpider Network to the rescue!<br/><br/>It is a Java based open source framework that automatically scans website urls, analyses the results and reports on any malware detected.<br/>Attend this talk to gain a better understanding of malware detection and client honeypots and get an overview of the HoneySpider Network&#8217;s architecture, its code and its plugins it uses. A live demo is also included!</div>
                    </div><div class="card mb-3 shadow-sm rounded">
                        <div class="card-header">
                            <h5 class="card-title">Building scalable network applications with Netty - Jaap ter Woerds</h5>
                        </div>
                        <div class="card-body">Since the introduction of the Java NIO API&apos;s with Java 4, developers have access to modern operating system facilities to perform asynchronous IO. Using these facilities it is possible to write networking application that that serve thousands of connected clients efficiently. Unfortunately, the NIO API&apos;s are quite low level and require a fair share of boilerplate to get started.<br/><br/>In this presentation, I will introduce the Netty framework and how its architecture helps you as a developer stay focused on the interesting parts of your network application. At the end of the presentation I will give some real world examples and show how we use Netty in the architecture of our mobile messaging platform XMS.</div>
                    </div><div class="card mb-3 shadow-sm rounded">
                        <div class="card-header">
                            <h5 class="card-title">Opening - Bert Ertman</h5>
                        </div>
                        <div class="card-body">De openingssessie van de conferentie met aandacht voor de dag zelf en nieuws vanuit de NLJUG. De sessie wordt gepresenteerd door Bert Ertman.</div>
                    </div><div class="card mb-3 shadow-sm rounded">
                        <div class="card-header">
                            <h5 class="card-title">Keynote door ING - Amir Arroni</h5>
                        </div>
                        <div class="card-body">Keynote van ING, gepresenteerd door Amir Arooni en Peter Jacobs.</div>
                    </div><div class="card mb-3 shadow-sm rounded">
                        <div class="card-header">
                            <h5 class="card-title">Keynote door Oracle - Sharat Chander</h5>
                        </div>
                        <div class="card-body">Keynote van Oracle, gepresenteerd door Sharat Chander.</div>
                    </div><div class="card mb-3 shadow-sm rounded">
                        <div class="card-header">
                            <h5 class="card-title">Reactieve applicaties ? klaar voor te toekomst - Allard Buijze</h5>
                        </div>
                        <div class="card-body">De technische eisen aan webapplicaties veranderen in hoog tempo. Enkele jaren geleden nog gebruikten de grootere applicaties enkele tientallen servers en werden response tijden van een seconde en onderhoudsvensters van enkele uren nog geaccepteerd. Tegenwoordig moeten applicaties 100% beschikbaar zijn, terwijl de gebruiker in enkele milliseconden antwoord wil krijgen. Om pieken in gebruik op te kunnen vangen moeten de applicaties op duizenden processoren in een cloud omgeving kunnen draaien.<br/><br/>De tekortkomingen van de huidige standaard architectuurprincipes kunnen worden opgevangen door een zogenaamde &#8220;reactive architecture&#8221;. Reactieve applicaties bezitten een aantal eigenschappen waardoor ze beter kunnen omgaan met opschalen, bestand zijn tegen fouten en bovendien efficienter gebruik maken van beschikbare server-bronnen.<br/><br/>In deze presentatie laat Allard zien hoe deze eigenschappen gerealiseerd kunnen worden en welke reeds bekende architectuurpatronen en frameworks hieraan een bijdrage leveren.</div>
                    </div><div class="card mb-3 shadow-sm rounded">
                        <div class="card-header">
                            <h5 class="card-title">HTML 5 Geolocation + WebSockets + Scalable JavaEE Backend === Awesome Realtime Location Aware Applications - Shekhar Gulati</h5>
                        </div>
                        <div class="card-body">Location Aware apps are everywhere and we use them heavily in our day to day life. You have seen the stuff that Foursquare has done with spatial and you want some of that hotness for your app. But, where to start? In this session, we will build a location aware app using HTML 5 on the client and scalable JavaEE + MongoDB on the server side. HTML 5 GeoLocation API help us to find user current location and MongoDB offers Geospatial indexing support which provides an easy way to get started and enables a variety of location-based applications - ranging from field resource management to social check-ins. Next we will add realtime capabilities to our application using Pusher. Pusher provides scalable WebSockets as a service. The Java EE 6 backend will be built using couple of Java EE 6 technologies -- JAXRS and CDI. Finally , we will deploy our Java EE application on OpenShift -- Red Hat&apos;s public, scalable Platform as a Service.</div>
                    </div><div class="card mb-3 shadow-sm rounded">
                        <div class="card-header">
                            <h5 class="card-title">Retro Gaming with Lambdas - Stephen Chin</h5>
                        </div>
                        <div class="card-body">Lambda expressions are coming in Java 8 and dramatically change the programming model.  They allow new functional programming patterns that were not possible before, increasing the expressiveness and power of the Java language.<br/><br/>In this university session, you will learn how to take advantage of the new lambda-enabled Java 8 APIs by building out a retro video game in JavaFX.<br/><br/>Some of the Java 8 features you will learn about include enhanced collections, functional interfaces, simplified event handlers, and the new stream API.  Start using these in your application today leveraging the latest OpenJDK builds so you can prepare for the future Java 8 release.</div>
                    </div><div class="card mb-3 shadow-sm rounded">
                        <div class="card-header">
                            <h5 class="card-title">Data Science with R for Java Developers - Sander Mak</h5>
                        </div>
                        <div class="card-body">Understanding data is increasingly important to create cutting-edge applications. A whole new data science field is emerging, with the open source R language as a leading technology. This statistical programming language is specifically designed for analyzing and understanding data.<br/><br/>In this session we approach R from the perspective of Java developers. How do you get up to speed quickly, what are the pitfalls to look out for?  Also we discuss how to bridge the divide between the R language and the JVM. After this session you can use your new skills to explore an exciting world of data analytics and machine learning! </div>
                    </div>
                </div>
               \s
                <script src="/webjars/jquery/3.1.1/jquery.min.js"></script>
                <script src="/webjars/bootstrap/4.3.1/js/bootstrap.min.js"></script>
                        
               \s
            </body>
            </html>
            """;
    }
    private static final String HTML_FLOW_HTML_ASSERTION() { return """
            <!DOCTYPE html>
            <html>
            	<head>
            		<meta charset="UTF-8">
            		<meta name="viewport" content="width=device-width, initial-scale=1.0">
            		<meta http-equiv="X-UA-Compatible" content="IE=Edge">
            		<title>
            			JFall 2013 Presentations - HtmlFlow
            		</title>
            		<link rel="stylesheet" href="/webjars/bootstrap/4.3.1/css/bootstrap.min.css" media="screen">
            	</head>
            	<body>
            		<div class="container">
            			<div class="pb-2 mt-4 mb-3 border-bottom">
            				<h1>
            					JFall 2013 Presentations - HtmlFlow
            				</h1>
            			</div>
            			<div class="card mb-3 shadow-sm rounded">
            				<div class="card-header">
            					<h5 class="card-title">
            						Shootout! Template engines on the JVM - Jeroen Reijn
            					</h5>
            				</div>
            				<div class="card-body">
            					Are you still using JavaServer Pages as your main template language? With the popularity of template engines for other languages like Ruby and Scala and the shift in doing more MVC in the browser there are quite some new and interesting new template languages available for the JVM. During this session we will take a look at the less known, but quite interesting new template engines and see how they compare with the industries standards.
            				</div>
            			</div>
            			<div class="card mb-3 shadow-sm rounded">
            				<div class="card-header">
            					<h5 class="card-title">
            						HoneySpider Network: a Java based system to hunt down malicious websites - Niels van Eijck
            					</h5>
            				</div>
            				<div class="card-body">
            					Legitimate websites such as news sites happen to get compromised by attackers injecting malicious content. The aim of these so-called &#8220;watering hole attacks&#8221; is to infect as many visitors of a website as possible, and are sometimes even targeted at a specific group of individuals. It is increasingly important to detect these infections at an early stage.<br/><br/>HoneySpider Network to the rescue!<br/><br/>It is a Java based open source framework that automatically scans website urls, analyses the results and reports on any malware detected.<br/>Attend this talk to gain a better understanding of malware detection and client honeypots and get an overview of the HoneySpider Network&#8217;s architecture, its code and its plugins it uses. A live demo is also included!
            				</div>
            			</div>
            			<div class="card mb-3 shadow-sm rounded">
            				<div class="card-header">
            					<h5 class="card-title">
            						Building scalable network applications with Netty - Jaap ter Woerds
            					</h5>
            				</div>
            				<div class="card-body">
            					Since the introduction of the Java NIO API&apos;s with Java 4, developers have access to modern operating system facilities to perform asynchronous IO. Using these facilities it is possible to write networking application that that serve thousands of connected clients efficiently. Unfortunately, the NIO API&apos;s are quite low level and require a fair share of boilerplate to get started.<br/><br/>In this presentation, I will introduce the Netty framework and how its architecture helps you as a developer stay focused on the interesting parts of your network application. At the end of the presentation I will give some real world examples and show how we use Netty in the architecture of our mobile messaging platform XMS.
            				</div>
            			</div>
            			<div class="card mb-3 shadow-sm rounded">
            				<div class="card-header">
            					<h5 class="card-title">
            						Opening - Bert Ertman
            					</h5>
            				</div>
            				<div class="card-body">
            					De openingssessie van de conferentie met aandacht voor de dag zelf en nieuws vanuit de NLJUG. De sessie wordt gepresenteerd door Bert Ertman.
            				</div>
            			</div>
            			<div class="card mb-3 shadow-sm rounded">
            				<div class="card-header">
            					<h5 class="card-title">
            						Keynote door ING - Amir Arroni
            					</h5>
            				</div>
            				<div class="card-body">
            					Keynote van ING, gepresenteerd door Amir Arooni en Peter Jacobs.
            				</div>
            			</div>
            			<div class="card mb-3 shadow-sm rounded">
            				<div class="card-header">
            					<h5 class="card-title">
            						Keynote door Oracle - Sharat Chander
            					</h5>
            				</div>
            				<div class="card-body">
            					Keynote van Oracle, gepresenteerd door Sharat Chander.
            				</div>
            			</div>
            			<div class="card mb-3 shadow-sm rounded">
            				<div class="card-header">
            					<h5 class="card-title">
            						Reactieve applicaties ? klaar voor te toekomst - Allard Buijze
            					</h5>
            				</div>
            				<div class="card-body">
            					De technische eisen aan webapplicaties veranderen in hoog tempo. Enkele jaren geleden nog gebruikten de grootere applicaties enkele tientallen servers en werden response tijden van een seconde en onderhoudsvensters van enkele uren nog geaccepteerd. Tegenwoordig moeten applicaties 100% beschikbaar zijn, terwijl de gebruiker in enkele milliseconden antwoord wil krijgen. Om pieken in gebruik op te kunnen vangen moeten de applicaties op duizenden processoren in een cloud omgeving kunnen draaien.<br/><br/>De tekortkomingen van de huidige standaard architectuurprincipes kunnen worden opgevangen door een zogenaamde &#8220;reactive architecture&#8221;. Reactieve applicaties bezitten een aantal eigenschappen waardoor ze beter kunnen omgaan met opschalen, bestand zijn tegen fouten en bovendien efficienter gebruik maken van beschikbare server-bronnen.<br/><br/>In deze presentatie laat Allard zien hoe deze eigenschappen gerealiseerd kunnen worden en welke reeds bekende architectuurpatronen en frameworks hieraan een bijdrage leveren.
            				</div>
            			</div>
            			<div class="card mb-3 shadow-sm rounded">
            				<div class="card-header">
            					<h5 class="card-title">
            						HTML 5 Geolocation + WebSockets + Scalable JavaEE Backend === Awesome Realtime Location Aware Applications - Shekhar Gulati
            					</h5>
            				</div>
            				<div class="card-body">
            					Location Aware apps are everywhere and we use them heavily in our day to day life. You have seen the stuff that Foursquare has done with spatial and you want some of that hotness for your app. But, where to start? In this session, we will build a location aware app using HTML 5 on the client and scalable JavaEE + MongoDB on the server side. HTML 5 GeoLocation API help us to find user current location and MongoDB offers Geospatial indexing support which provides an easy way to get started and enables a variety of location-based applications - ranging from field resource management to social check-ins. Next we will add realtime capabilities to our application using Pusher. Pusher provides scalable WebSockets as a service. The Java EE 6 backend will be built using couple of Java EE 6 technologies -- JAXRS and CDI. Finally , we will deploy our Java EE application on OpenShift -- Red Hat&apos;s public, scalable Platform as a Service.
            				</div>
            			</div>
            			<div class="card mb-3 shadow-sm rounded">
            				<div class="card-header">
            					<h5 class="card-title">
            						Retro Gaming with Lambdas - Stephen Chin
            					</h5>
            				</div>
            				<div class="card-body">
            					Lambda expressions are coming in Java 8 and dramatically change the programming model.  They allow new functional programming patterns that were not possible before, increasing the expressiveness and power of the Java language.<br/><br/>In this university session, you will learn how to take advantage of the new lambda-enabled Java 8 APIs by building out a retro video game in JavaFX.<br/><br/>Some of the Java 8 features you will learn about include enhanced collections, functional interfaces, simplified event handlers, and the new stream API.  Start using these in your application today leveraging the latest OpenJDK builds so you can prepare for the future Java 8 release.
            				</div>
            			</div>
            			<div class="card mb-3 shadow-sm rounded">
            				<div class="card-header">
            					<h5 class="card-title">
            						Data Science with R for Java Developers - Sander Mak
            					</h5>
            				</div>
            				<div class="card-body">
            					Understanding data is increasingly important to create cutting-edge applications. A whole new data science field is emerging, with the open source R language as a leading technology. This statistical programming language is specifically designed for analyzing and understanding data.<br/><br/>In this session we approach R from the perspective of Java developers. How do you get up to speed quickly, what are the pitfalls to look out for?  Also we discuss how to bridge the divide between the R language and the JVM. After this session you can use your new skills to explore an exciting world of data analytics and machine learning!\s
            				</div>
            			</div>
            		</div>
            		<script src="/webjars/jquery/3.1.1/jquery.min.js">
            		</script>
            		<script src="/webjars/bootstrap/4.3.1/js/bootstrap.min.js">
            		</script>
            	</body>
            </html>""";
    }
    private static final String KOTLINX_HTML_ASSERTION(){ return """
            <html>
              <head>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <meta http-equiv="content-language" content="IE=Edge">
                <title>JFall 2013 Presentations - htmlApi</title>
                <link rel="Stylesheet" href="/webjars/bootstrap/4.3.1/css/bootstrap.min.css" media="screen">
              </head>
              <body>
                <div class="container">
                  <div class="pb-2 mt-4 mb-3 border-bottom">
                    <h1>JFall 2013 Presentations - kotlinx.html</h1>
                  </div>
                </div>
                <script src="/webjars/jquery/3.1.1/jquery.min.js"></script>
                <script src="/webjars/bootstrap/4.3.1/js/bootstrap.min.js"></script>
              </body>
            </html>
            <div class="card mb-3 shadow-sm rounded">
              <div class="card-header">
                <h3 class="card-title">Shootout! Template engines on the JVM - Jeroen Reijn</h3>
              </div>
              <div class="card-body">Are you still using JavaServer Pages as your main template language? With the popularity of template engines for other languages like Ruby and Scala and the shift in doing more MVC in the browser there are quite some new and interesting new template languages available for the JVM. During this session we will take a look at the less known, but quite interesting new template engines and see how they compare with the industries standards.</div>
            </div>
            <div class="card mb-3 shadow-sm rounded">
              <div class="card-header">
                <h3 class="card-title">HoneySpider Network: a Java based system to hunt down malicious websites - Niels van Eijck</h3>
              </div>
              <div class="card-body">Legitimate websites such as news sites happen to get compromised by attackers injecting malicious content. The aim of these so-called &#8220;watering hole attacks&#8221; is to infect as many visitors of a website as possible, and are sometimes even targeted at a specific group of individuals. It is increasingly important to detect these infections at an early stage.<br/><br/>HoneySpider Network to the rescue!<br/><br/>It is a Java based open source framework that automatically scans website urls, analyses the results and reports on any malware detected.<br/>Attend this talk to gain a better understanding of malware detection and client honeypots and get an overview of the HoneySpider Network&#8217;s architecture, its code and its plugins it uses. A live demo is also included!</div>
            </div>
            <div class="card mb-3 shadow-sm rounded">
              <div class="card-header">
                <h3 class="card-title">Building scalable network applications with Netty - Jaap ter Woerds</h3>
              </div>
              <div class="card-body">Since the introduction of the Java NIO API&apos;s with Java 4, developers have access to modern operating system facilities to perform asynchronous IO. Using these facilities it is possible to write networking application that that serve thousands of connected clients efficiently. Unfortunately, the NIO API&apos;s are quite low level and require a fair share of boilerplate to get started.<br/><br/>In this presentation, I will introduce the Netty framework and how its architecture helps you as a developer stay focused on the interesting parts of your network application. At the end of the presentation I will give some real world examples and show how we use Netty in the architecture of our mobile messaging platform XMS.</div>
            </div>
            <div class="card mb-3 shadow-sm rounded">
              <div class="card-header">
                <h3 class="card-title">Opening - Bert Ertman</h3>
              </div>
              <div class="card-body">De openingssessie van de conferentie met aandacht voor de dag zelf en nieuws vanuit de NLJUG. De sessie wordt gepresenteerd door Bert Ertman.</div>
            </div>
            <div class="card mb-3 shadow-sm rounded">
              <div class="card-header">
                <h3 class="card-title">Keynote door ING - Amir Arroni</h3>
              </div>
              <div class="card-body">Keynote van ING, gepresenteerd door Amir Arooni en Peter Jacobs.</div>
            </div>
            <div class="card mb-3 shadow-sm rounded">
              <div class="card-header">
                <h3 class="card-title">Keynote door Oracle - Sharat Chander</h3>
              </div>
              <div class="card-body">Keynote van Oracle, gepresenteerd door Sharat Chander.</div>
            </div>
            <div class="card mb-3 shadow-sm rounded">
              <div class="card-header">
                <h3 class="card-title">Reactieve applicaties ? klaar voor te toekomst - Allard Buijze</h3>
              </div>
              <div class="card-body">De technische eisen aan webapplicaties veranderen in hoog tempo. Enkele jaren geleden nog gebruikten de grootere applicaties enkele tientallen servers en werden response tijden van een seconde en onderhoudsvensters van enkele uren nog geaccepteerd. Tegenwoordig moeten applicaties 100% beschikbaar zijn, terwijl de gebruiker in enkele milliseconden antwoord wil krijgen. Om pieken in gebruik op te kunnen vangen moeten de applicaties op duizenden processoren in een cloud omgeving kunnen draaien.<br/><br/>De tekortkomingen van de huidige standaard architectuurprincipes kunnen worden opgevangen door een zogenaamde &#8220;reactive architecture&#8221;. Reactieve applicaties bezitten een aantal eigenschappen waardoor ze beter kunnen omgaan met opschalen, bestand zijn tegen fouten en bovendien efficienter gebruik maken van beschikbare server-bronnen.<br/><br/>In deze presentatie laat Allard zien hoe deze eigenschappen gerealiseerd kunnen worden en welke reeds bekende architectuurpatronen en frameworks hieraan een bijdrage leveren.</div>
            </div>
            <div class="card mb-3 shadow-sm rounded">
              <div class="card-header">
                <h3 class="card-title">HTML 5 Geolocation + WebSockets + Scalable JavaEE Backend === Awesome Realtime Location Aware Applications - Shekhar Gulati</h3>
              </div>
              <div class="card-body">Location Aware apps are everywhere and we use them heavily in our day to day life. You have seen the stuff that Foursquare has done with spatial and you want some of that hotness for your app. But, where to start? In this session, we will build a location aware app using HTML 5 on the client and scalable JavaEE + MongoDB on the server side. HTML 5 GeoLocation API help us to find user current location and MongoDB offers Geospatial indexing support which provides an easy way to get started and enables a variety of location-based applications - ranging from field resource management to social check-ins. Next we will add realtime capabilities to our application using Pusher. Pusher provides scalable WebSockets as a service. The Java EE 6 backend will be built using couple of Java EE 6 technologies -- JAXRS and CDI. Finally , we will deploy our Java EE application on OpenShift -- Red Hat&apos;s public, scalable Platform as a Service.</div>
            </div>
            <div class="card mb-3 shadow-sm rounded">
              <div class="card-header">
                <h3 class="card-title">Retro Gaming with Lambdas - Stephen Chin</h3>
              </div>
              <div class="card-body">Lambda expressions are coming in Java 8 and dramatically change the programming model.  They allow new functional programming patterns that were not possible before, increasing the expressiveness and power of the Java language.<br/><br/>In this university session, you will learn how to take advantage of the new lambda-enabled Java 8 APIs by building out a retro video game in JavaFX.<br/><br/>Some of the Java 8 features you will learn about include enhanced collections, functional interfaces, simplified event handlers, and the new stream API.  Start using these in your application today leveraging the latest OpenJDK builds so you can prepare for the future Java 8 release.</div>
            </div>
            <div class="card mb-3 shadow-sm rounded">
              <div class="card-header">
                <h3 class="card-title">Data Science with R for Java Developers - Sander Mak</h3>
              </div>
              <div class="card-body">Understanding data is increasingly important to create cutting-edge applications. A whole new data science field is emerging, with the open source R language as a leading technology. This statistical programming language is specifically designed for analyzing and understanding data.<br/><br/>In this session we approach R from the perspective of Java developers. How do you get up to speed quickly, what are the pitfalls to look out for?  Also we discuss how to bridge the divide between the R language and the JVM. After this session you can use your new skills to explore an exciting world of data analytics and machine learning! </div>
            </div>
            """;
    }
    @Autowired
    private WebTestClient webTestClient;

    @DisplayName("Should generated html for each template on Controllers")
    @ParameterizedTest
    @MethodSource("htmlTemplates")
    void test_reactive_endpoint_for_template_for_response(String[] templateAndAssertion) {

        byte[] responseBody = webTestClient.get()
                .uri(URI.create("/async/"+templateAndAssertion[0]))
                .accept(MediaType.ALL)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .returnResult()
                .getResponseBody();

        if (responseBody == null) {
            fail("Error on generating template");
        }

        String response = new String(responseBody);

        then(response)
                .isNotNull()
                .isNotBlank()
                .isEqualTo(templateAndAssertion[1]);
    }

    @DisplayName("Should generated html for each template on Functional Routes")
    @ParameterizedTest
    @MethodSource("htmlTemplates")
    void test_route_endpoint_for_template_for_response(String[] templateAndAssertion) {

        byte[] responseBody = webTestClient.get()
                .uri(URI.create("/router/"+templateAndAssertion[0]))
                .accept(MediaType.ALL)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .returnResult()
                .getResponseBody();

        if (responseBody == null) {
            fail("Error on generating template");
        }

        String response = new String(responseBody);

        then(response)
                .isNotNull()
                .isNotBlank()
                .isEqualTo(templateAndAssertion[1]);
    }

    @DisplayName("Should return 200 ok status code for all requests on Controllers")
    @ParameterizedTest
    @MethodSource("htmlTemplates")
    void test_reactive_endpoint_for_template_controllers(String[] templateAndAssertion) {

        webTestClient.get()
                .uri(URI.create("/async/"+templateAndAssertion[0]))
                .accept(MediaType.ALL)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @DisplayName("Should return 200 ok status code for all requests on Functional Routes")
    @ParameterizedTest
    @MethodSource("htmlTemplates")
    void test_route_endpoint_for_template(String[] templateAndAssertion) {

        webTestClient.get()
                .uri(URI.create("/router/"+templateAndAssertion[0]))
                .accept(MediaType.ALL)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @DisplayName("Should return 200 ok status code for all requests on Coroutine Routes")
    @ParameterizedTest
    @MethodSource("htmlTemplates")
    /**
     * Failing for kotlinX with:
     *
     * java.lang.IllegalStateException: You can't change tag attribute because it was already passed to the downstream
     */
    void test_coroutine_endpoint_for_template(String[] templateAndAssertion) {

        webTestClient.get()
                .uri(URI.create("/router/"+templateAndAssertion[0]+"/coroutine"))
                .accept(MediaType.ALL)
                .exchange()
                .expectStatus()
                .isOk();
    }


    static Stream<Arguments> htmlTemplates() {
        return Stream.of(
                Arguments.of(Named.of("Generate html for Thymeleaf",
                        new String[]{"thymeleaf", THYMELEAF_FORMED_HTML_ASSERTION()})),
                Arguments.of(Named.of("Generate html for HtmlFlow",
                        new String[]{"htmlFlow", HTML_FLOW_HTML_ASSERTION()})),
                Arguments.of(Named.of("Generate html for KotlinX",
                        new String[]{"kotlinx", KOTLINX_HTML_ASSERTION()}))
        );
    }
}
