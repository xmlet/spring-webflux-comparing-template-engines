package com.jeroenreijn.examples.view.appendable

import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import java.io.Closeable
import java.io.Writer

class WriterSink : Writer(), Closeable {
    private val sink = Sinks.many().replay().all<String>()

    fun asFlux(): Flux<String> {
        return sink.asFlux()
    }

    override fun close() {
        sink.emitComplete(Sinks.EmitFailureHandler.FAIL_FAST)
    }

    override fun flush() {
        
    }

    override fun write(cbuf: CharArray, off: Int, len: Int) {
        sink.emitNext(String(cbuf, off, len), Sinks.EmitFailureHandler.FAIL_FAST)
    }
}