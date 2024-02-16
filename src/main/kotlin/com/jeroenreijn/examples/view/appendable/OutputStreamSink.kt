package com.jeroenreijn.examples.view.appendable

import reactor.core.publisher.Flux
import java.io.Closeable
import java.io.OutputStream
import java.nio.charset.StandardCharsets


class OutputStreamSink : OutputStream(), Closeable {
    private val sink = BufferedSink()

    fun asFlux(): Flux<String> = sink.asFlux()

    override fun write(b: Int) {
        sink.bufferedWriter.write(b)
        sink.tryFlush()
    }

    override fun write(b: ByteArray, off: Int, len: Int) {
        sink.bufferedWriter.write(String(b, off, len, StandardCharsets.UTF_8))
        sink.tryFlush()
    }

    override fun close() {
        super.close()
        sink.close()
    }
}