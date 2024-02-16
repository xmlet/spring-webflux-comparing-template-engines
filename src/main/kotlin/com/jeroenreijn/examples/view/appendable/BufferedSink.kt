package com.jeroenreijn.examples.view.appendable

import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import java.io.BufferedWriter
import java.io.Closeable
import java.io.StringWriter

class BufferedSink : Closeable {
    private val stringWriter = StringWriter()
    val bufferedWriter = BufferedWriter(stringWriter, 80)
    private val sink: Sinks.Many<String> = Sinks.many().unicast().onBackpressureBuffer()

    fun asFlux(): Flux<String> = sink.asFlux()

    fun tryFlush() {
        val data = stringWriter.toString()
        stringWriter.buffer.setLength(0); // Clearing the buffer
        if (data.isNotEmpty()) {
            sink.tryEmitNext(data)
        }
    }

    override fun close() {
        bufferedWriter.flush()
        tryFlush()
        sink.emitComplete(Sinks.EmitFailureHandler.FAIL_FAST)
    }

}