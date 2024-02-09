package com.jeroenreijn.examples.router

import com.jeroenreijn.examples.model.Presentation
import com.jeroenreijn.examples.repository.PresentationRepo
import com.jeroenreijn.examples.view.*
import com.jeroenreijn.examples.view.appendable.appendableSink
import com.jeroenreijn.examples.view.appendable.appendableSinkSuspendable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.withContext
import org.springframework.context.annotation.Bean
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.coRouter
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class PresentationsRoutes(repo : PresentationRepo) {

    private val scope = CoroutineScope(Dispatchers.Default)
    private val presentationsFlux: Flux<Presentation> = repo.findAllReactive()
    private val presentationsFlow: Flow<Presentation> = repo.findAllReactive().asFlow()

    @Bean
    fun presentationsCoRouter() = coRouter {
        "/router".nest {
            GET("/thymeleaf") { handleTemplateThymeleaf().awaitSingle() }
            GET("/htmlFlow") { handleTemplateHtmlFlowFromFlux().awaitSingle() }
            GET("/htmlFlow/suspending") { handleTemplateHtmlFlowSuspending().awaitSingle() }
            GET("/kotlinx") { handleTemplateKotlinX().awaitSingle() }
            /*
             * For the next routes ee must switch context because
             * we are using blocking IO in this KotlinXSync template.
             * Otherwise, we will get an exception:
             *   IllegalStateException: block()/blockFirst()/blockLast() are blocking, which is not supported in thread parallel-...
             */
            GET("/thymeleaf/sync") { handleTemplateThymeleafSync().awaitSingle() }
            GET("/htmlFlow/sync") {
                    handleTemplateHtmlFlowSync().awaitSingle()
            }
            GET("/kotlinx/sync") {
                    handleTemplateKotlinXSync().awaitSingle()
            }
        }
    }

    private fun handleTemplateThymeleafSync(): Mono<ServerResponse> {
        val model = mapOf<String, Any>(
            "reactivedata" to presentationsFlux
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
        val view = appendableSinkSuspendable {
            htmlFlowTemplateSync
                .setOut(this)
                .write(presentationsFlux)
            this.close()
        }
        scope.launch { view.start() }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }


    private fun handleTemplateHtmlFlowFromFlux() : Mono<ServerResponse> {
        val view = appendableSink {
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
        val view = appendableSinkSuspendable {
            htmlFlowTemplateSuspending
                .write(this, presentationsFlow)
            this.close()
        }
        scope.launch { view.start() }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }


    private fun handleTemplateKotlinXSync() : Mono<ServerResponse> {
        val view = appendableSinkSuspendable {
            kotlinXSync(this, presentationsFlux)
            this.close()
        }
        scope.launch { view.start() }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateKotlinX() : Mono<ServerResponse> {
        val view = appendableSink {
                kotlinXReactive(this, presentationsFlux)
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }
}
