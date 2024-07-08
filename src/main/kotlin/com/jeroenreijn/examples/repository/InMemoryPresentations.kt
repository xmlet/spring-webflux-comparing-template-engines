package com.jeroenreijn.examples.repository

import com.jeroenreijn.examples.model.Presentation
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.reactivestreams.Publisher
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit


@Repository
class InMemoryPresentations : PresentationRepo {

    companion object {
        /**
         * Interval period between items in milliseconds.
         */
        var timeout: Long = 0
    }

    private val presentationsList = mutableListOf<Presentation>()
    private val presentationsReactive = Observable
        .fromIterable(presentationsList)


    override fun findAllReactive(): Observable<Presentation> = if(timeout == 0L) {
        presentationsReactive
    } else {
        presentationsReactive
            .concatMap { Observable.just(it).delay(timeout, TimeUnit.MILLISECONDS, Schedulers.io()) }
    }

    init {
        var counter = 0L
        val preso1 = Presentation(
            ++counter,
            "Shootout! Template engines on the JVM",
            "Jeroen Reijn",
            "Are you still using JavaServer Pages as your main template language? With the popularity of template engines for other languages like Ruby and Scala and the shift in doing more MVC in the browser there are quite some new and interesting new template languages available for the JVM. During this session we will take a look at the less known, but quite interesting new template engines and see how they compare with the industries standards."
        )

        val preso2 = Presentation(
            ++counter,
            "HoneySpider Network: a Java based system to hunt down malicious websites",
            "Niels van Eijck",
            "Legitimate websites such as news sites happen to get compromised by attackers injecting malicious content. The aim of these so-called &#8220;watering hole attacks&#8221; is to infect as many visitors of a website as possible, and are sometimes even targeted at a specific group of individuals. It is increasingly important to detect these infections at an early stage.<br/><br/>HoneySpider Network to the rescue!<br/><br/>It is a Java based open source framework that automatically scans website urls, analyses the results and reports on any malware detected.<br/>Attend this talk to gain a better understanding of malware detection and client honeypots and get an overview of the HoneySpider Network&#8217;s architecture, its code and its plugins it uses. A live demo is also included!"
        )

        val preso3 = Presentation(
            ++counter,
            "Building scalable network applications with Netty",
            "Jaap ter Woerds",
            "Since the introduction of the Java NIO API&apos;s with Java 4, developers have access to modern operating system facilities to perform asynchronous IO. Using these facilities it is possible to write networking application that that serve thousands of connected clients efficiently. Unfortunately, the NIO API&apos;s are quite low level and require a fair share of boilerplate to get started.<br/><br/>In this presentation, I will introduce the Netty framework and how its architecture helps you as a developer stay focused on the interesting parts of your network application. At the end of the presentation I will give some real world examples and show how we use Netty in the architecture of our mobile messaging platform XMS."
        )

        val preso4 = Presentation(
            ++counter,
            "Opening",
            "Bert Ertman",
            "De openingssessie van de conferentie met aandacht voor de dag zelf en nieuws vanuit de NLJUG. De sessie wordt gepresenteerd door Bert Ertman."
        )

        val preso5 = Presentation(
            ++counter,
            "Keynote door ING",
            "Amir Arroni",
            "Keynote van ING, gepresenteerd door Amir Arooni en Peter Jacobs."
        )

        val preso6 = Presentation(
            ++counter,
            "Keynote door Oracle",
            "Sharat Chander",
            "Keynote van Oracle, gepresenteerd door Sharat Chander."
        )

        val preso7 = Presentation(
            ++counter,
            "Reactieve applicaties ? klaar voor te toekomst",
            "Allard Buijze",
            "De technische eisen aan webapplicaties veranderen in hoog tempo. Enkele jaren geleden nog gebruikten de grootere applicaties enkele tientallen servers en werden response tijden van een seconde en onderhoudsvensters van enkele uren nog geaccepteerd. Tegenwoordig moeten applicaties 100% beschikbaar zijn, terwijl de gebruiker in enkele milliseconden antwoord wil krijgen. Om pieken in gebruik op te kunnen vangen moeten de applicaties op duizenden processoren in een cloud omgeving kunnen draaien.<br/><br/>De tekortkomingen van de huidige standaard architectuurprincipes kunnen worden opgevangen door een zogenaamde &#8220;reactive architecture&#8221;. Reactieve applicaties bezitten een aantal eigenschappen waardoor ze beter kunnen omgaan met opschalen, bestand zijn tegen fouten en bovendien efficienter gebruik maken van beschikbare server-bronnen.<br/><br/>In deze presentatie laat Allard zien hoe deze eigenschappen gerealiseerd kunnen worden en welke reeds bekende architectuurpatronen en frameworks hieraan een bijdrage leveren."
        )

        val preso8 = Presentation(
            ++counter,
            "HTML 5 Geolocation + WebSockets + Scalable JavaEE Backend === Awesome Realtime Location Aware Applications",
            "Shekhar Gulati",
            "Location Aware apps are everywhere and we use them heavily in our day to day life. You have seen the stuff that Foursquare has done with spatial and you want some of that hotness for your app. But, where to start? In this session, we will build a location aware app using HTML 5 on the client and scalable JavaEE + MongoDB on the server side. HTML 5 GeoLocation API help us to find user current location and MongoDB offers Geospatial indexing support which provides an easy way to get started and enables a variety of location-based applications - ranging from field resource management to social check-ins. Next we will add realtime capabilities to our application using Pusher. Pusher provides scalable WebSockets as a service. The Java EE 6 backend will be built using couple of Java EE 6 technologies -- JAXRS and CDI. Finally , we will deploy our Java EE application on OpenShift -- Red Hat&apos;s public, scalable Platform as a Service."
        )

        val preso9 = Presentation(
            ++counter,
            "Retro Gaming with Lambdas",
            "Stephen Chin",
            "Lambda expressions are coming in Java 8 and dramatically change the programming model.  They allow new functional programming patterns that were not possible before, increasing the expressiveness and power of the Java language.<br/><br/>In this university session, you will learn how to take advantage of the new lambda-enabled Java 8 APIs by building out a retro video game in JavaFX.<br/><br/>Some of the Java 8 features you will learn about include enhanced collections, functional interfaces, simplified event handlers, and the new stream API.  Start using these in your application today leveraging the latest OpenJDK builds so you can prepare for the future Java 8 release."
        )

        val preso10 = Presentation(
            ++counter,
            "Data Science with R for Java Developers",
            "Sander Mak",
            "Understanding data is increasingly important to create cutting-edge applications. A whole new data science field is emerging, with the open source R language as a leading technology. This statistical programming language is specifically designed for analyzing and understanding data.<br/><br/>In this session we approach R from the perspective of Java developers. How do you get up to speed quickly, what are the pitfalls to look out for?  Also we discuss how to bridge the divide between the R language and the JVM. After this session you can use your new skills to explore an exciting world of data analytics and machine learning! "
        )

        presentationsList.add(preso1)
        presentationsList.add(preso2)
        presentationsList.add(preso3)
        presentationsList.add(preso4)
        presentationsList.add(preso5)
        presentationsList.add(preso6)
        presentationsList.add(preso7)
        presentationsList.add(preso8)
        presentationsList.add(preso9)
        presentationsList.add(preso10)
    }
}
