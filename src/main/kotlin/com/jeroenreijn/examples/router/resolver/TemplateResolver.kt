package com.jeroenreijn.examples.router.resolver

import com.jeroenreijn.examples.model.Presentation
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux

interface TemplateResolver {

    fun resolve(presentations : Flux<Presentation>) : Publisher<String>
}
