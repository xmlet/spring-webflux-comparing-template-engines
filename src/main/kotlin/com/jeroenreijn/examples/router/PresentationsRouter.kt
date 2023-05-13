package com.jeroenreijn.examples.router

import com.jeroenreijn.examples.handler.PresentationsRouterHandler
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.coRouter

@Component
class PresentationsRouter(private val presentationsHandler: PresentationsRouterHandler) {

    @Bean
    fun presentationsCoRouter() = coRouter {
        "/router".nest {
            GET("/{template}/coroutine", presentationsHandler::handleCoroutineTemplate)
        }
    }

    @Bean
    fun presentationsRouter(): RouterFunction<ServerResponse> = RouterFunctions
        .route()
        .path("/router") { builder ->
            builder
                .GET("/{template}", presentationsHandler::handleTemplate)
        }
        .build()
}
