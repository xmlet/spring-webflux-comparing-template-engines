package com.jeroenreijn.examples.router.resolver

import com.jeroenreijn.examples.model.Presentation
import com.jeroenreijn.examples.view.appendable.KotlinXAppendableSink
import com.jeroenreijn.examples.view.kotlinXCoroutine
import com.jeroenreijn.examples.view.kotlinXReactive
import kotlinx.coroutines.future.await
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux

class KotlinXResolver : TemplateResolver {

    override fun resolve(presentations: Flux<Presentation>): Publisher<String> {
        return KotlinXAppendableSink()
            .let { out ->
                out.asFlux().also {
                    kotlinXReactive(out, presentations)
                }
            }
    }

    override suspend fun resolveCoroutines(presentations: List<Presentation>): String {
        val result = KotlinXAppendableSink()
            .let { out ->
                out.asFlux().also {
                    kotlinXCoroutine(out, presentations)
                }
            }

        return result.collectList().toFuture().await().joinToString { it }
    }
}
