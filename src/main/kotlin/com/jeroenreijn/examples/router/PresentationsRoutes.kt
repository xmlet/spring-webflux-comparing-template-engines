package com.jeroenreijn.examples.router

import com.jeroenreijn.examples.repository.PresentationRepo
import com.jeroenreijn.examples.view.appendable.AppendableSink
import com.jeroenreijn.examples.view.htmlFlowTemplate
import com.jeroenreijn.examples.view.kotlinXReactive
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.context.annotation.Bean
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable
import reactor.core.publisher.Mono

@Component
class PresentationsRoutes(private val repo : PresentationRepo) {

    @Bean
    fun presentationsCoRouter() = coRouter {
        "/router".nest {
            GET("/thymeleaf/coroutine") { handleTemplateThymeleaf(it).awaitSingle() }
            GET("/htmlFlow/coroutine") { handleTemplateHtmlFlow().awaitSingle() }
            GET("/kotlinx/coroutine") { handleTemplateKotlinX().awaitSingle() }
        }
    }

    @Bean
    fun presentationsRouter(): RouterFunction<ServerResponse> = RouterFunctions
        .route()
        .path("/router") { builder ->
            builder
                .GET("/thymeleaf", this::handleTemplateThymeleaf)
                .GET("/htmlFlow") { this.handleTemplateHtmlFlow() }
                .GET("/kotlinx") { this.handleTemplateKotlinX() }
        }
        .build()


    private fun handleTemplateThymeleaf(req: ServerRequest): Mono<ServerResponse> {
        val model = mapOf<String, Any>(
            "reactivedata" to ReactiveDataDriverContextVariable(repo.findAllReactive(), 1)
        )
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .render("index-thymeleaf", model);
    }


    private fun handleTemplateHtmlFlow() : Mono<ServerResponse> {
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
