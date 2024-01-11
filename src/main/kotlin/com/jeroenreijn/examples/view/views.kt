package com.jeroenreijn.examples.view

import com.jeroenreijn.examples.model.Presentation
import com.jeroenreijn.examples.view.appendable.AppendableSink
import htmlflow.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.future.await
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import org.xmlet.htmlapifaster.*
import reactor.core.publisher.Flux
import java.util.concurrent.CompletableFuture


val htmlFlowTemplate: HtmlViewAsync<Flux<Presentation>> = HtmlFlow.viewAsync { view ->
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
                .doOnNext {
                    presentationFragment(
                        div,
                        it
                    )
                }
                .doOnComplete { onCompletion.finish() }
                .subscribe()
        } // foreach
        .`__`() // container
        .script().attrSrc("/webjars/jquery/3.1.1/jquery.min.js").`__`()
        .script().attrSrc("/webjars/bootstrap/4.3.1/js/bootstrap.min.js").`__`()
        .`__`() // body
        .`__`() // html
}

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
                presentationFragment(
                    div,
                    it
                )
            }
        } // foreach
        .`__`() // container
        .script().attrSrc("/webjars/jquery/3.1.1/jquery.min.js").`__`()
        .script().attrSrc("/webjars/bootstrap/4.3.1/js/bootstrap.min.js").`__`()
        .`__`() // body
        .`__`() // html
}

val htmlFlowTemplateSync: HtmlView<List<Presentation>> = HtmlFlow.view<List<Presentation>> { view -> view
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
    .dynamic<List<Presentation>> { div, model ->
        model
            .forEach {
                presentationFragment(
                    div,
                    it
                )
            }
    } // foreach
    .`__`() // container
    .script().attrSrc("/webjars/jquery/3.1.1/jquery.min.js").`__`()
    .script().attrSrc("/webjars/bootstrap/4.3.1/js/bootstrap.min.js").`__`()
    .`__`() // body
    .`__`() // html
}.threadSafe()


private fun presentationFragment(div: Div<Body<Html<HtmlPage>>>, presentation: Presentation) {
    div
        .div().attrClass("card mb-3 shadow-sm rounded")
        .div().attrClass("card-header")
        .h5()
        .of {
            it
                .attrClass("card-title")
                .text(presentation.title + " - " + presentation.speakerName)
        }
        .`__`() // h5
        .`__`() // div
        .div()
        .of {
            it
                .attrClass("card-body")
                .text(presentation.summary)

        }
        .`__`() // div
        .`__`() // div
}

fun kotlinXReactive(sink : AppendableSink, presentations : Flux<Presentation>) {
    sink
        .appendHTML()
        .html {
            head {
                meta { charset = "utf-8" }
                meta { name = "viewport"; content = "width=device-width, initial-scale=1.0" }
                meta { httpEquiv = MetaHttpEquiv.contentLanguage; content = "IE=Edge" }
                title { text("JFall 2013 Presentations - htmlApi") }
                link { rel = LinkRel.stylesheet; href = "/webjars/bootstrap/4.3.1/css/bootstrap.min.css"; media = LinkMedia.screen }
            }
            body {
                div {
                    classes = setOf("container")
                    div {
                        classes = setOf("pb-2 mt-4 mb-3 border-bottom")
                        h1 { text("JFall 2013 Presentations - kotlinx.html") }
                    }
                    presentations
                        .doOnNext {
                            div {
                                classes = setOf("card mb-3 shadow-sm rounded")
                                div {
                                    classes = setOf("card-header")
                                    h3 {
                                        classes = setOf("card-title")
                                        text(it.title + " - " + it.speakerName)
                                    }
                                }
                                div {
                                    classes = setOf("card-body")
                                    unsafe { raw(it.summary) }
                                }
                            }
                        }
                        .doOnComplete {
                            sink.close()
                        }
                        .subscribe()
                }

                script { src = "/webjars/jquery/3.1.1/jquery.min.js" }
                script { src = "/webjars/bootstrap/4.3.1/js/bootstrap.min.js" }
            }
        }
}

fun kotlinXSync(sink : Appendable, presentations : List<Presentation>) {
    sink
        .appendHTML()
        .html {
            head {
                meta { charset = "utf-8" }
                meta { name = "viewport"; content = "width=device-width, initial-scale=1.0" }
                meta { httpEquiv = MetaHttpEquiv.contentLanguage; content = "IE=Edge" }
                title { text("JFall 2013 Presentations - htmlApi") }
                link { rel = LinkRel.stylesheet; href = "/webjars/bootstrap/4.3.1/css/bootstrap.min.css"; media = LinkMedia.screen }
            }
            body {
                div {
                    classes = setOf("container")
                    div {
                        classes = setOf("pb-2 mt-4 mb-3 border-bottom")
                        h1 { text("JFall 2013 Presentations - kotlinx.html") }
                    }
                    presentations
                        .forEach {
                            div {
                                classes = setOf("card mb-3 shadow-sm rounded")
                                div {
                                    classes = setOf("card-header")
                                    h3 {
                                        classes = setOf("card-title")
                                        text(it.title + " - " + it.speakerName)
                                    }
                                }
                                div {
                                    classes = setOf("card-body")
                                    unsafe { raw(it.summary) }
                                }
                            }
                        }
                }

                script { src = "/webjars/jquery/3.1.1/jquery.min.js" }
                script { src = "/webjars/bootstrap/4.3.1/js/bootstrap.min.js" }
            }
        }
}