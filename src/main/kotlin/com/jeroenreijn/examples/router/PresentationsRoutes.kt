package com.jeroenreijn.examples.router

import com.jeroenreijn.examples.model.Presentation
import com.jeroenreijn.examples.repository.PresentationRepo
import com.jeroenreijn.examples.view.*
import com.jeroenreijn.examples.view.appendable.AppendableSink
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.BackpressureStrategy.DROP
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import org.springframework.context.annotation.Bean
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class PresentationsRoutes(repo : PresentationRepo) {

    /*
     * We are using next one for synchronous blocking render.
     */
    private val scope = CoroutineScope(Dispatchers.Default)
    /*
     * It executes the initial continuation of a coroutine in the current
     * call-frame and lets the coroutine resume in whatever thread.
     * Better performance but not suitable for blocking IO, only for asynchronous usage.
     */
    private val unconf = CoroutineScope(Dispatchers.Unconfined)
    private val presentationsFlux = repo.findAllReactive()
    private val presentationsFlow = repo.findAllReactive().toFlowable(DROP).asFlow()

    @Bean
    fun presentationsRouter() = router {
        GET("/thymeleaf") { handleTemplateThymeleaf() }
        GET("/htmlFlow") { handleTemplateHtmlFlowFromFlux() }
        GET("/htmlFlow/suspending") { handleTemplateHtmlFlowSuspending() }
        GET("/kotlinx") { handleTemplateKotlinX() }
        GET("/thymeleaf/sync") { handleTemplateThymeleafSync() }
        GET("/htmlFlow/sync") { handleTemplateHtmlFlowSync() }
        GET("/kotlinx/sync") { handleTemplateKotlinXSync() }
    }


    private fun handleTemplateThymeleafSync(): Mono<ServerResponse> {
        val model = mapOf<String, Any>(
            "reactivedata" to presentationsFlux.blockingIterable()
        )
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .render("index-thymeleaf", model);
    }

    private fun handleTemplateThymeleaf(): Mono<ServerResponse> {
        val model = mapOf<String, Any>(
            "reactivedata" to ReactiveDataDriverContextVariable(presentationsFlux, 1)
        )
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .render("index-thymeleaf", model);
    }


    private fun handleTemplateHtmlFlowSync() : Mono<ServerResponse> {
        val view = AppendableSink()
        /*
         * We need another co-routine to render concurrently and ensure
         * progressive server-side rendering (PSSR)
         */
        scope.launch { view.start {
            htmlFlowTemplateSync
                .setOut(this)
                .write(presentationsFlux)
            this.close()
        }}
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }


    private fun handleTemplateHtmlFlowFromFlux() : Mono<ServerResponse> {
        val view = AppendableSink().start {
            htmlFlowTemplate
                .writeAsync(this, presentationsFlux)
                .thenAccept {this.close()}
        }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateHtmlFlowSuspending() : Mono<ServerResponse> {
        val view = AppendableSink()
        /*
         * We need another co-routine to render concurrently and ensure
         * progressive server-side rendering (PSSR)
         * Here we are using Unconfined running in same therad and avoiding context switching.
         * That's ok since we are NOT blocking on htmlFlowTemplateSuspending.
         */
        unconf.launch { view.startSuspend {
            htmlFlowTemplateSuspending
                .write(this, presentationsFlow)
            this.close()
        }}
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }


    private fun handleTemplateKotlinXSync() : Mono<ServerResponse> {
        val view = AppendableSink()
        /*
         * We need another co-routine to render concurrently and ensure
         * progressive server-side rendering (PSSR)
         */
        scope.launch { view.startSuspend {
            kotlinXSync(this, presentationsFlux)
            this.close()
        }}
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateKotlinX() : Mono<ServerResponse> {
        val view = AppendableSink().start {
            kotlinXReactive(this, presentationsFlux)
        }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }
}
