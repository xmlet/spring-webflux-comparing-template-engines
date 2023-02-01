package com.jeroenreijn.examples.view.response;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveResponseWriter<T> {
    
    <F extends Flux<T>> Mono<Void> write(ServerWebExchange exchange, F model, TemplateResolver<F> renderFunction);
}
