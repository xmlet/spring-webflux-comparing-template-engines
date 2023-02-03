package com.jeroenreijn.examples.view.response;

import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

import java.io.OutputStreamWriter;

@FunctionalInterface
public interface TemplateResolverAsync<T> {

    Mono<String> resolveAsync(MonoSink<DataBuffer> subscriber, T model, OutputStreamWriter writer, DataBuffer buffer);
}
