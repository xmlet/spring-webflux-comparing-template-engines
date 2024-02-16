package com.jeroenreijn.examples.router

import com.fizzed.rocker.runtime.OutputStreamOutput
import com.jeroenreijn.examples.repository.PresentationRepo
import com.jeroenreijn.examples.view.*
import com.jeroenreijn.examples.view.JStachioView.PresentationsModel
import com.jeroenreijn.examples.view.appendable.AppendableSink
import com.jeroenreijn.examples.view.appendable.OutputStreamSink
import com.jeroenreijn.examples.view.appendable.WriterSink
import io.pebbletemplates.pebble.PebbleEngine
import io.pebbletemplates.pebble.template.PebbleTemplate
import io.reactivex.rxjava3.core.BackpressureStrategy.DROP
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import org.springframework.context.annotation.Bean
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable
import reactor.core.publisher.Mono

@Component
class PresentationsRoutes(repo : PresentationRepo) {
    /**
     * We are using next one for synchronous blocking render.
     */
    private val scope = CoroutineScope(Dispatchers.Default)
    /**
     * It executes the initial continuation of a coroutine in the current
     * call-frame and lets the coroutine resume in whatever thread.
     * Better performance but not suitable for blocking IO, only for asynchronous usage.
     */
    private val unconf = CoroutineScope(Dispatchers.Unconfined)
    /**
     * Views
     */
    private val enginePebble: PebbleEngine = PebbleEngine.Builder().autoEscaping(false).build()
    private val viewPresentationsPebble: PebbleTemplate = enginePebble.getTemplate("templates/pebble/presentations.pebble.html")
    /**
     * Data models
     */
    private val presentationsFlux = repo.findAllReactive()
    private val presentationsIter = presentationsFlux.blockingIterable()
    private val presentationsModelJStachio: PresentationsModel = PresentationsModel(presentationsIter)
    private val presentationsModelPebble = mapOf("presentations" to presentationsIter)
    private val presentationsFlow = repo.findAllReactive().toFlowable(DROP).asFlow()

    @Bean
    fun presentationsRouter() = router {
        GET("/thymeleaf") { handleTemplateThymeleaf() }
        GET("/htmlFlow") { handleTemplateHtmlFlowFromFlux() }
        GET("/htmlFlow/suspending") { handleTemplateHtmlFlowSuspending() }
        GET("/kotlinx") { handleTemplateKotlinX() }
        GET("/rocker/sync") { handleTemplateRockerSync() }
        GET("/jstachio/sync") { handleTemplateJStachioSync() }
        GET("/pebble/sync") { handleTemplatePebbleSync() }
        GET("/thymeleaf/sync") { handleTemplateThymeleafSync() }
        GET("/htmlFlow/sync") { handleTemplateHtmlFlowSync() }
        GET("/kotlinx/sync") { handleTemplateKotlinXSync() }
    }

    private fun handleTemplateRockerSync(): Mono<ServerResponse> {
        val out = OutputStreamSink().also { scope.launch {
            templates
                .rocker
                .presentations
                .template(presentationsIter)
                .render { contentType, charset -> OutputStreamOutput(contentType, it, charset) }
            it.close()
        }}
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(out.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateJStachioSync(): Mono<ServerResponse> {
        val out = OutputStreamSink().also { scope.launch {
            JStachioView.presentationsWrite(presentationsModelJStachio, it)
            it.close()
        }}
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(out.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplatePebbleSync(): Mono<ServerResponse> {
        val out = WriterSink().also { scope.launch {
            viewPresentationsPebble.evaluate(it, presentationsModelPebble)
            it.close()
        }}
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(out.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateThymeleafSync(): Mono<ServerResponse> {
        val model = mapOf<String, Any>(
            "presentations" to presentationsIter
        )
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .render("index-thymeleaf", model);
    }

    private fun handleTemplateThymeleaf(): Mono<ServerResponse> {
        val model = mapOf<String, Any>(
            "presentations" to ReactiveDataDriverContextVariable(presentationsFlux, 1)
        )
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .render("index-thymeleaf", model);
    }

    private fun handleTemplateHtmlFlowSync() : Mono<ServerResponse> {
        /*
         * We need another co-routine to render concurrently and ensure
         * progressive server-side rendering (PSSR)
         */
        val view = AppendableSink().also { scope.launch {
            htmlFlowTemplateSync
                .setOut(it)
                .write(presentationsFlux)
            it.close()
        }}
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }


    private fun handleTemplateHtmlFlowFromFlux() : Mono<ServerResponse> {
        val view = AppendableSink().also { sink ->
            htmlFlowTemplate
                .writeAsync(sink, presentationsFlux)
                .thenAccept {sink.close()}
        }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateHtmlFlowSuspending() : Mono<ServerResponse> {
        /*
         * We need another co-routine to render concurrently and ensure
         * progressive server-side rendering (PSSR)
         * Here we are using Unconfined running in same therad and avoiding context switching.
         * That's ok since we are NOT blocking on htmlFlowTemplateSuspending.
         */
        val view = AppendableSink().also { unconf.launch {
            htmlFlowTemplateSuspending
                .write(it, presentationsFlow)
            it.close()
        }}
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }


    private fun handleTemplateKotlinXSync() : Mono<ServerResponse> {
        /*
         * We need another co-routine to render concurrently and ensure
         * progressive server-side rendering (PSSR)
         */
        val view = AppendableSink().also { scope.launch {
            kotlinXSync(it, presentationsFlux)
            it.close()
        }}
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateKotlinX() : Mono<ServerResponse> {
        val view = AppendableSink().also {
            kotlinXReactive(it, presentationsFlux)
        }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }
}
