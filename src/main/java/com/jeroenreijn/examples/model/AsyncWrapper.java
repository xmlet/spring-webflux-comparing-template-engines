package com.jeroenreijn.examples.model;

import reactor.core.publisher.Flux;

public class AsyncWrapper {
    private final Flux<Presentation> presentations;
    
    public AsyncWrapper(Flux<Presentation> presentations) {
        this.presentations = presentations;
    }
    
    public Flux<Presentation> getPresentations() {
        return presentations;
    }
}
