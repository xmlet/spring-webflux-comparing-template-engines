package com.jeroenreijn.examples.router.resolver

import com.jeroenreijn.examples.model.Presentation
import com.jeroenreijn.examples.view.appendable.HtmlFlowAppendableSink
import com.jeroenreijn.examples.view.htmlFlowTemplate
import com.jeroenreijn.examples.view.htmlFlowTemplateCoroutine
import kotlinx.coroutines.future.await
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux

class HtmlFlowResolver : TemplateResolver {

    override fun resolve(presentations : Flux<Presentation>): Publisher<String> {
        return HtmlFlowAppendableSink().let { out ->
            out.asFlux().also {
                htmlFlowTemplate
                    .writeAsync(out, presentations)
                    .thenAccept {out.close()}
            }
        }
    }

    override suspend fun resolveCoroutines(presentations: List<Presentation>): String {
        val result = HtmlFlowAppendableSink().let {out ->
            out.asFlux().also {
                htmlFlowTemplateCoroutine
                    .writeAsync(out, presentations)
                    .thenAccept {out.close()}
                    .await()
            }
        }

        return result.collectList().toFuture().await().joinToString { it }
    }
}
