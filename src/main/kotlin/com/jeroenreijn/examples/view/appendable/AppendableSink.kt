package com.jeroenreijn.examples.view.appendable

import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks


interface AppendableSink : Appendable, AutoCloseable {
    fun asFlux(): Flux<String>
}

fun appendableSink(block: AppendableSink.() -> Unit) : AppendableSink {
    val sink = AppendableSinkImpl()
    sink.block()
    return sink
}


suspend fun appendableSinkSuspendable(block: suspend AppendableSink.() -> Unit) : AppendableSink {
    val sink = AppendableSinkImpl()
    sink.block()
    return sink
}

private class AppendableSinkImpl : AppendableSink {
    val sink = Sinks.many().replay().all<String>()
    override fun close() {
        sink.emitComplete(Sinks.EmitFailureHandler.FAIL_FAST)
    }

    override fun asFlux(): Flux<String> {
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