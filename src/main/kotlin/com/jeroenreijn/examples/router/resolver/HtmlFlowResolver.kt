package com.jeroenreijn.examples.router.resolver

import com.jeroenreijn.examples.model.Presentation
import com.jeroenreijn.examples.view.appendable.AppendableSink
import com.jeroenreijn.examples.view.htmlFlowTemplate
import reactor.core.publisher.Flux

class HtmlFlowResolver : TemplateResolver {

    override fun resolve(presentations : Flux<Presentation>) = AppendableSink {
        htmlFlowTemplate
            .writeAsync(this, presentations)
            .thenAccept {this.close()}
        }
        .asFlux()
}
