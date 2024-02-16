package com.jeroenreijn.examples.view.appendable

import reactor.core.publisher.Sinks
import java.io.BufferedWriter
import java.io.Closeable
import java.io.OutputStream
import java.io.StringWriter
import java.nio.charset.StandardCharsets


class OutputStreamSink : OutputStream(), Closeable {
    private val stringWriter = StringWriter()
    private val bufferedWriter = BufferedWriter(stringWriter, 1024)
    private val sink: Sinks.Many<String> = Sinks.many().unicast().onBackpressureBuffer()

    fun toFlux() = sink.asFlux()

    private fun tryFlush() {
        val data = stringWriter.toString()
        stringWriter.buffer.setLength(0); // Clearing the buffer
        if (data.isNotEmpty()) {
            sink.tryEmitNext(data)
        }
    }

    override fun write(b: Int) = synchronized(bufferedWriter) {
        bufferedWriter.write(b)
        tryFlush()
    }

    override fun write(b: ByteArray, off: Int, len: Int) = synchronized(bufferedWriter) {
        bufferedWriter.write(String(b, off, len, StandardCharsets.UTF_8))
        tryFlush()
    }

    override fun close() {
        super.close()
        tryFlush()
        sink.emitComplete(Sinks.EmitFailureHandler.FAIL_FAST)
    }
}