package com.jeroenreijn.examples.view.response;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.OutputStreamWriter;

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
}
