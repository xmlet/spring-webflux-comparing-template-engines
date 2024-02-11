package com.jeroenreijn.examples.view.appendable

import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import java.io.Closeable

class AppendableSink : Appendable, Closeable {
    val sink: Sinks.Many<String> = Sinks.many().unicast().onBackpressureBuffer()

    inline fun start(block: AppendableSink.() -> Unit) : AppendableSink {
        block()
        return this
    }

    suspend inline fun startSuspend(block: suspend AppendableSink.() -> Unit) : AppendableSink {
        block()
        return this
    }

    override fun close() {
        sink.emitComplete(Sinks.EmitFailureHandler.FAIL_FAST)
    }

    fun asFlux(): Flux<String> {
        return sink.asFlux()
    }
    override fun append(csq: CharSequence): Appendable {
        sink.tryEmitNext(csq.toString())
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
}