package com.jeroenreijn.examples.router.resolver

import com.jeroenreijn.examples.model.Presentation
import com.jeroenreijn.examples.view.appendable.AppendableSink
import com.jeroenreijn.examples.view.kotlinXReactive
import reactor.core.publisher.Flux

class KotlinXResolver : TemplateResolver {

    override fun resolve(presentations: Flux<Presentation>) = AppendableSink {
        kotlinXReactive(this, presentations) }
        .asFlux()
}
