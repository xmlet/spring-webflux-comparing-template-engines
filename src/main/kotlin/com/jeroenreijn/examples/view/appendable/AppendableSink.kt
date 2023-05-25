package com.jeroenreijn.examples.view.appendable

import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.lang.StringBuilder
import java.lang.System.lineSeparator


class AppendableSink(block: AppendableSink.() -> Unit) : Appendable, AutoCloseable, Publisher<String> {
    private var finished: Boolean = false
    private val buffer = StringBuilder()
    private val lines = mutableListOf<String>()
    private lateinit var subscriber: Subscriber<in String>

    init {
        this.block()
    }

    private fun checkAndFlushBuffer() {
        val ls = buffer.split(lineSeparator()) // Clear the buffer
        buffer.clear().also { it.append(ls.last()) }  // The buffer remains with last line
        lines.addAll(ls.dropLast(1))                  // Add all first lines to lines List
    }

    override fun append(csq: CharSequence): java.lang.Appendable {
        buffer.append(csq)
        /*
         * Short path when there is no newline.
         */
        if(!csq.contains(lineSeparator())) return this
        /*
         * Otherwise, split buffer and append lines to lines List.
         */
        checkAndFlushBuffer()
        return this
    }

    override fun append(csq: CharSequence, start: Int, end: Int) = append(csq.subSequence(start, end))

    override fun append(c: Char) = append(c.toString())

    override fun close() {
        finished = true
    }

    override fun subscribe(sub: Subscriber<in String>) {
        subscriber = sub
        sub.onSubscribe(object : Subscription {
            override fun request(n: Long) {
                for(i in 1..n) {
                    if(lines.isEmpty()) {
                        if(finished) {
                            if(buffer.isNotEmpty()) subscriber.onNext(buffer.toString())
                            subscriber.onComplete()
                        }
                        break
                    }
                    subscriber.onNext(lines[0] + lineSeparator())
                    lines.removeAt(0)
                }
            }

            override fun cancel() {
                sub.onComplete()
            }
        })
    }

}
