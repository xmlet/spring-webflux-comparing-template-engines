package com.jeroenreijn.examples.view.response;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class ReactiveResponseWriterImpl<T> implements ReactiveResponseWriter<T>{
    
    @Override
    public <F extends Flux<T>> Mono<Void> write(ServerWebExchange exchange, F model, TemplateResolver<F> renderFunction) {
        final ServerHttpResponse response = exchange.getResponse();
        final DataBuffer dataBuffer = response.bufferFactory().allocateBuffer();
        final OutputStreamWriter writer = new OutputStreamWriter(dataBuffer.asOutputStream());
    
        return response.writeWith(Mono.create(sub -> {
            try {
                final String html = renderFunction.resolve(sub, model, writer, dataBuffer);
                writer.append(html);
                writer.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    @Override
    public <F extends Flux<T>> Mono<Void> writeAsync(ServerWebExchange exchange, F model, TemplateResolverAsync<F> renderFunction) {
        final ServerHttpResponse response = exchange.getResponse();
        final DataBuffer dataBuffer = response.bufferFactory().allocateBuffer();
        final OutputStreamWriter writer = new OutputStreamWriter(dataBuffer.asOutputStream());

        //TODO review this
        return response.writeWith(Mono.create(sub -> renderFunction.resolveAsync(sub, model, writer, dataBuffer)
                    .flatMap(html -> tryAppendToWriter(writer, html))
                    .doOnTerminate(() -> tryFlushWriter(writer))));
    }

    private void tryFlushWriter(OutputStreamWriter writer) {
        try {
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private Mono<Writer> tryAppendToWriter(OutputStreamWriter writer, String html) {
        try {
            return Mono.just(writer.append(html));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
