package com.jeroenreijn.examples.router

import com.jeroenreijn.examples.repository.PresentationRepo
import com.jeroenreijn.examples.view.appendable.AppendableSink
import com.jeroenreijn.examples.view.htmlFlowTemplate
import com.jeroenreijn.examples.view.htmlFlowTemplateSync
import com.jeroenreijn.examples.view.kotlinXReactive
import com.jeroenreijn.examples.view.kotlinXSync
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.context.annotation.Bean
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.coRouter
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable
import reactor.core.publisher.Mono

@Component
class PresentationsRoutes(private val repo : PresentationRepo) {

    @Bean
    fun presentationsCoRouter() = coRouter {
        "/router".nest {
            GET("/thymeleaf/coroutine") { handleTemplateThymeleaf().awaitSingle() }
            GET("/htmlFlow/coroutine") { handleTemplateHtmlFlow().awaitSingle() }
            GET("/kotlinx/coroutine") { handleTemplateKotlinX().awaitSingle() }
        }
    }

    @Bean
    fun presentationsRouter(): RouterFunction<ServerResponse> = RouterFunctions
        .route()
        .path("/router") { builder ->
            builder
                .GET("/thymeleaf/sync") { this.handleTemplateThymeleafSync() }
                .GET("/htmlFlow/sync") { this.handleTemplateHtmlFlowSync() }
                .GET("/kotlinx/sync") { this.handleTemplateKotlinXSync() }
                .GET("/thymeleaf") { this.handleTemplateThymeleaf() }
                .GET("/htmlFlow") { this.handleTemplateHtmlFlow() }
                .GET("/kotlinx") { this.handleTemplateKotlinX() }
        }
        .build()

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
        return repo
            .findAllReactive()
            .collectList()
            .flatMap {
                val html = htmlFlowTemplateSync.render(it)
                ServerResponse
                    .ok()
                    .contentType(MediaType.TEXT_HTML)
                    .bodyValue(html)
            }
    }


    private fun handleTemplateHtmlFlow() : Mono<ServerResponse> {
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
            .asFlux()

        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view, object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateKotlinXSync() : Mono<ServerResponse> {
        return repo
            .findAllReactive()
            .collectList()
            .flatMap { lst ->
                val html = StringBuilder()
                    .also { strBuilder -> kotlinXSync(strBuilder, lst) }
                    .let { it.toString() }
                ServerResponse
                    .ok()
                    .contentType(MediaType.TEXT_HTML)
                    .bodyValue(html)
            }
    }

    private fun handleTemplateKotlinX() : Mono<ServerResponse> {
        val view = AppendableSink {
                kotlinXReactive(this, repo.findAllReactive())
            }
            .asFlux()
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view, object : ParameterizedTypeReference<String>() {})
    }
}
