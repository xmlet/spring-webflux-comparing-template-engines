package com.jeroenreijn.examples.handler

import com.jeroenreijn.examples.repository.PresentationRepo
import com.jeroenreijn.examples.router.resolver.HtmlFlowResolver
import com.jeroenreijn.examples.router.resolver.KotlinXResolver
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import reactor.core.publisher.Mono

@Component
class PresentationsRouterHandler(private val repo : PresentationRepo) {

    private val templateToResolver = mapOf(
        "kotlinx" to KotlinXResolver(),
        "htmlFlow" to HtmlFlowResolver()
    )

    suspend fun handleCoroutineTemplate(req : ServerRequest) : ServerResponse {
        val template = req.templateName()

        val model = repo.findAllReactive()

        val view = templateToResolver[template]?.resolveCoroutines(model)
            ?: throw IndexOutOfBoundsException("No template with name $template")

        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .bodyValueAndAwait(view)
    }

    fun handleTemplate(req : ServerRequest) : Mono<ServerResponse> {
        val template = req.templateName()

        val model = repo.findAllReactive()

        val view = templateToResolver[template]?.resolve(model)
            ?: throw IndexOutOfBoundsException("No template with name $template")

        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view, object : ParameterizedTypeReference<String>() {})
    }


    private fun ServerRequest.templateName() = this.pathVariable("template")
}
