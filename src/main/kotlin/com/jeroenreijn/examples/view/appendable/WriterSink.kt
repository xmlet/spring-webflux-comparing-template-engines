package com.jeroenreijn.examples.view.appendable

import reactor.core.publisher.Flux
import java.io.Closeable
import java.io.Writer

class WriterSink : Writer(), Closeable {
    private val sink = BufferedSink()

    fun asFlux(): Flux<String> {
        return sink.asFlux()
    }

    override fun close() = sink.close()

    override fun flush() = sink.tryFlush()

    override fun write(cbuf: CharArray, off: Int, len: Int) {
        sink.bufferedWriter.write(cbuf, off, len)
        sink.tryFlush()
    }
}