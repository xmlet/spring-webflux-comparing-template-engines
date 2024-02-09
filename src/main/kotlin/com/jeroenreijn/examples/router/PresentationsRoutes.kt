package com.jeroenreijn.examples.router

import com.jeroenreijn.examples.repository.PresentationRepo
import com.jeroenreijn.examples.view.*
import com.jeroenreijn.examples.view.appendable.AppendableSink
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.withContext
import org.springframework.context.annotation.Bean
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.coRouter
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable
import reactor.core.publisher.Mono

@Component
class PresentationsRoutes(private val repo : PresentationRepo) {

    @Bean
    fun presentationsCoRouter() = coRouter {
        "/router".nest {
            GET("/thymeleaf") { handleTemplateThymeleaf().awaitSingle() }
            GET("/htmlFlow") { handleTemplateHtmlFlowFromFlux().awaitSingle() }
            GET("/kotlinx") { handleTemplateKotlinX().awaitSingle() }
            /*
             * For the next routes ee must switch context because
             * we are using blocking IO in this KotlinXSync template.
             * Otherwise, we will get an exception:
             *   IllegalStateException: block()/blockFirst()/blockLast() are blocking, which is not supported in thread parallel-...
             */
            GET("/thymeleaf/sync") { handleTemplateThymeleafSync().awaitSingle() }
            GET("/htmlFlow/sync") {
                withContext(Dispatchers.IO) {
                    handleTemplateHtmlFlowSync().awaitSingle()
                }
            }
            GET("/kotlinx/sync") {
                withContext(Dispatchers.IO) {
                    handleTemplateKotlinXSync().awaitSingle()
                }
            }
        }
    }

    private fun handleTemplateThymeleafSync(): Mono<ServerResponse> {
        val model = mapOf<String, Any>(
            "reactivedata" to repo.findAllReactive().collectList()
        )
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .render("index-thymeleaf", model);
    }

    private fun handleTemplateThymeleaf(): Mono<ServerResponse> {
        val model = mapOf<String, Any>(
            "reactivedata" to ReactiveDataDriverContextVariable(repo.findAllReactive(), 1)
        )
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .render("index-thymeleaf", model);
    }


    private fun handleTemplateHtmlFlowSync() : Mono<ServerResponse> {
        val view = AppendableSink {
            htmlFlowTemplateSync
                .setOut(this)
                .write(repo.findAllReactive())
            this.close()
        }

        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }


    private fun handleTemplateHtmlFlowFromFlux() : Mono<ServerResponse> {
        /* SOLVE performance bottle neck
        return Mono
            .fromCompletionStage( htmlFlowTemplate
                .renderAsync(repo.findAllReactive()))
            .flatMap {
                ServerResponse
                    .ok()
                    .contentType(MediaType.TEXT_HTML)
                    .bodyValue(it)
            }
        */

        val view = AppendableSink {
                htmlFlowTemplate
                    .writeAsync(this, repo.findAllReactive())
                    .thenAccept {this.close()}
            }

        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateHtmlFlowFromFlow() : Mono<ServerResponse> {
        val view = AppendableSink {
            htmlFlowTemplateSuspending
                .writeAsync(this, repo.findAllFlow())
                .thenAccept {this.close()}
        }

        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }


    private fun handleTemplateKotlinXSync() : Mono<ServerResponse> {
        val view = AppendableSink {
            kotlinXSync(this, repo.findAllReactive())
            this.close()
        }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateKotlinX() : Mono<ServerResponse> {
        val view = AppendableSink {
                kotlinXReactive(this, repo.findAllReactive())
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }
}
