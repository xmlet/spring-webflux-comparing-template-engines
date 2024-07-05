package com.jeroenreijn.examples.view.appendable

import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import java.io.Closeable
import java.io.OutputStream
import java.nio.charset.StandardCharsets


class OutputStreamSink : OutputStream(), Closeable {
    private val sink = Sinks.many().replay().all<String>()

    fun asFlux(): Flux<String> = sink.asFlux()

    override fun write(b: Int) {
        throw UnsupportedOperationException()
    }

    override fun write(b: ByteArray, off: Int, len: Int) {
        sink.emitNext(String(b, off, len, StandardCharsets.UTF_8), Sinks.EmitFailureHandler.FAIL_FAST)
    }

    override fun close() {
        super.close()
        sink.emitComplete(Sinks.EmitFailureHandler.FAIL_FAST)
    }
}