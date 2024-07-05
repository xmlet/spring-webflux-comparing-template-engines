package com.jeroenreijn.examples.view.appendable

import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import java.io.Closeable

class AppendableSink : Appendable, Closeable {
    private val sink = Sinks.many().replay().all<String>()

    fun asFlux(): Flux<String> {
        return sink.asFlux()
    }

    override fun append(csq: CharSequence): Appendable {
        sink.emitNext(csq.toString(), Sinks.EmitFailureHandler.FAIL_FAST)
        return this
    }

    override fun append(csq: CharSequence, start: Int, end: Int): Appendable {
        append(csq.subSequence(start, end))
        return this
    }

    override fun append(c: Char): Appendable {
        append(c.toString())
        return this
    }

    override fun close() {
        sink.emitComplete(Sinks.EmitFailureHandler.FAIL_FAST)
    }
}