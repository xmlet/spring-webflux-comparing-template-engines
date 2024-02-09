package com.jeroenreijn.examples.view

import com.jeroenreijn.examples.model.Presentation
import htmlflow.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import org.xmlet.htmlapifaster.*
import reactor.core.publisher.Flux

val htmlFlowTemplate: HtmlViewAsync<Flux<Presentation>> = HtmlFlow.viewAsync<Flux<Presentation>?> { view ->
    view
        .html()
        .head()
        .meta().attrCharset("UTF-8").`__`()
        .meta().attrName("viewport").attrContent("width=device-width, initial-scale=1.0").`__`()
        .meta()
        .addAttr("http-equiv", "X-UA-Compatible")
        .attrContent("IE=Edge")
        .`__`() // meta
        .title().text("JFall 2013 Presentations - HtmlFlow").`__`()
        .link()
        .attrRel(EnumRelType.STYLESHEET)
        .attrHref("/webjars/bootstrap/4.3.1/css/bootstrap.min.css")
        .attrMedia(EnumMediaType.SCREEN)
        .`__`() // link
        .`__`() // head
        .body()
        .div().attrClass("container")
        .div().attrClass("pb-2 mt-4 mb-3 border-bottom")
        .h1().text("JFall 2013 Presentations - HtmlFlow").`__`()
        .`__`() // div
        .await<Flux<Presentation>>
        { div, model, onCompletion ->
            model
                .doOnNext { div.text(presentationFragment.render(it)) }
                .doOnComplete { onCompletion.finish() }
                .subscribe()
        } // foreach
        .`__`() // container
        .script().attrSrc("/webjars/jquery/3.1.1/jquery.min.js").`__`()
        .script().attrSrc("/webjars/bootstrap/4.3.1/js/bootstrap.min.js").`__`()
        .`__`() // body
        .`__`() // html
}.threadSafe()

val htmlFlowTemplateSuspending: HtmlViewAsync<Flow<Presentation>> = HtmlFlow.viewAsync { view ->
    view
        .html()
        .head()
        .meta().attrCharset("UTF-8").`__`()
        .meta().attrName("viewport").attrContent("width=device-width, initial-scale=1.0").`__`()
        .meta()
        .addAttr("http-equiv", "X-UA-Compatible")
        .attrContent("IE=Edge")
        .`__`() // meta
        .title().text("JFall 2013 Presentations - HtmlFlow").`__`()
        .link()
        .attrRel(EnumRelType.STYLESHEET)
        .attrHref("/webjars/bootstrap/4.3.1/css/bootstrap.min.css")
        .attrMedia(EnumMediaType.SCREEN)
        .`__`() // link
        .`__`() // head
        .body()
        .div().attrClass("container")
            .div().attrClass("pb-2 mt-4 mb-3 border-bottom")
                .h1().text("JFall 2013 Presentations - HtmlFlow").`__`()
            .`__`() // div
        .suspending { div, model: Flow<Presentation> -> model
            .collect {
                div.text(presentationFragment.render(it))
            }
        } // foreach
        .`__`() // container
        .script().attrSrc("/webjars/jquery/3.1.1/jquery.min.js").`__`()
        .script().attrSrc("/webjars/bootstrap/4.3.1/js/bootstrap.min.js").`__`()
        .`__`() // body
        .`__`() // html
}

val htmlFlowTemplateSync: HtmlView<Flux<Presentation>> = HtmlFlow.view<Flux<Presentation>> { view -> view
    .html()
    .head()
    .meta().attrCharset("UTF-8").`__`()
    .meta().attrName("viewport").attrContent("width=device-width, initial-scale=1.0").`__`()
    .meta()
    .addAttr("http-equiv", "X-UA-Compatible")
    .attrContent("IE=Edge")
    .`__`() // meta
    .title().text("JFall 2013 Presentations - HtmlFlow").`__`()
    .link()
    .attrRel(EnumRelType.STYLESHEET)
    .attrHref("/webjars/bootstrap/4.3.1/css/bootstrap.min.css")
    .attrMedia(EnumMediaType.SCREEN)
    .`__`() // link
    .`__`() // head
    .body()
    .div().attrClass("container")
    .div().attrClass("pb-2 mt-4 mb-3 border-bottom")
    .h1().text("JFall 2013 Presentations - HtmlFlow").`__`()
    .`__`() // div
    .dynamic<Flux<Presentation>> { div, model ->
        model
            .doOnNext {
                div.text(presentationFragment.render(it))
            }
            .blockLast()
    } // foreach
    .`__`() // container
    .script().attrSrc("/webjars/jquery/3.1.1/jquery.min.js").`__`()
    .script().attrSrc("/webjars/bootstrap/4.3.1/js/bootstrap.min.js").`__`()
    .`__`() // body
    .`__`() // html
}.threadSafe()


val presentationFragment = HtmlFlow.view<Presentation> { page ->
    page
        .div().attrClass("card mb-3 shadow-sm rounded")
        .div().attrClass("card-header")
        .h5()
            .attrClass("card-title")
            .dynamic<Presentation>{ h5, presentation -> h5.text(presentation.title + " - " + presentation.speakerName)}
        .`__`() // h5
        .`__`() // div
        .div()
            .attrClass("card-body")
            .dynamic<Presentation>{div, presentation -> div.text(presentation.summary)}
        .`__`() // div
        .`__`() // div
}.setIndented(false)
