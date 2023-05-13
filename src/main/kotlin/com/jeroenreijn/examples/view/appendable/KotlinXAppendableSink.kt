package com.jeroenreijn.examples.view.appendable

import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import java.io.Closeable

class KotlinXAppendableSink : Appendable, Closeable {
    private val sink = Sinks.many().replay().all<String>()

    override fun append(csq: CharSequence): java.lang.Appendable {
        sink.emitNext(csq.toString(), Sinks.EmitFailureHandler.FAIL_FAST)
        return this
    }

    override fun append(csq: CharSequence, start: Int, end: Int): java.lang.Appendable {
        append(csq.toString())
        return this
    }

    override fun append(c: Char): java.lang.Appendable {
        append(c.toString())
        return this
    }
    override fun close() {
        sink.emitComplete(Sinks.EmitFailureHandler.FAIL_FAST)
    }

    fun asFlux(): Flux<String> {
        return sink.asFlux()
    }
}
