package com.jeroenreijn.examples.view.response;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ReactiveResponseWriterImpl<T> implements ReactiveResponseWriter<T>{
    
    @Override
    public <F extends Flux<T>> Mono<Void> write(ServerWebExchange exchange, F model, TemplateResolver<F> renderFunction) {
        final ServerHttpResponse response = exchange.getResponse();
        final DataBuffer dataBuffer = response.bufferFactory().allocateBuffer();
        final OutputStreamWriter writer = new OutputStreamWriter(dataBuffer.asOutputStream());
    
        return response.writeWith(Mono.create(sub -> renderFunction.resolve(sub, model, writer, dataBuffer)));
    }

    @Override
    public <F extends Flux<T>> Mono<Void> writeAsync(ServerWebExchange exchange, F model, TemplateResolverAsync<F> renderFunction) {
        final ServerHttpResponse response = exchange.getResponse();
        final DataBuffer dataBuffer = response.bufferFactory().allocateBuffer();
        final OutputStreamWriter writer = new OutputStreamWriter(dataBuffer.asOutputStream());

        return response.writeWith(createMonoWriter(model, renderFunction, dataBuffer, writer));
    }

    @NotNull
    private <F extends Flux<T>> Mono<DataBuffer> createMonoWriter(F model, TemplateResolverAsync<F> renderFunction,
                                                                  DataBuffer dataBuffer, OutputStreamWriter writer) {

        return Mono.create(sink -> renderModelAsync(model, renderFunction, writer)
                .whenComplete((html, th) -> writeToOutput(sink, writer, dataBuffer, th)));
    }

    private <F extends Flux<T>> CompletableFuture<Void> renderModelAsync(F model, TemplateResolverAsync<F> renderFunction,
                                                                         OutputStreamWriter writer) {
        return renderFunction.resolveAsync(model, writer);
    }

    private void writeToOutput(MonoSink<DataBuffer> sub, OutputStreamWriter writer, DataBuffer buffer, Throwable th) {
        try {
            tryFinaliseOutput(sub, writer, buffer, th);
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void tryFinaliseOutput(MonoSink<DataBuffer> sub, OutputStreamWriter writer, DataBuffer buffer, Throwable th) {
        if (th == null) {
            sendSignal(sub, writer, sink -> sink.success(buffer));
        } else {
            sendSignal(sub, writer, sink -> sink.error(th));
        }
    }

    private void sendSignal(MonoSink<DataBuffer> sub, OutputStreamWriter writer, Consumer<MonoSink<DataBuffer>> signal) {
        try {
            writer.flush();
            signal.accept(sub);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
