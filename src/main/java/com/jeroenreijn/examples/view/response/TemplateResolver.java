package com.jeroenreijn.examples.view.response;

import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.MonoSink;

import java.io.OutputStreamWriter;

@FunctionalInterface
public interface TemplateResolver<T> {
    
    String resolve(MonoSink<DataBuffer> subscriber, T model, OutputStreamWriter writer, DataBuffer buffer);
}
