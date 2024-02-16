package com.jeroenreijn.examples.view.appendable

import reactor.core.publisher.Flux
import java.io.Closeable

class AppendableSink : Appendable, Closeable {
    private val sink = BufferedSink()

    fun asFlux(): Flux<String> {
        return sink.asFlux()
    }

    override fun append(csq: CharSequence): Appendable {
        sink.bufferedWriter.append(csq)
        sink.tryFlush()
        return this
    }

    override fun append(csq: CharSequence, start: Int, end: Int): Appendable {
        sink.bufferedWriter.append(csq, start, end)
        sink.tryFlush()
        return this
    }

    override fun append(c: Char): Appendable {
        append(c.toString())
        return this
    }

    override fun close() {
        sink.close()
    }
}