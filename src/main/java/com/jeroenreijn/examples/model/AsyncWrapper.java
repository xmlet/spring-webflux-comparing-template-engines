package com.jeroenreijn.examples.model;

import reactor.core.publisher.Flux;

/**
 * This is needed because Spring when passes the Flux to the View resolver blocks the Flux and transforms into a list.
 * Which does not allow for the reactive behaviour to be tested. With this wrapper Spring keeps the Flux's inside the wrapper.
 * <p/>
 * This can be observed by using thymeleaf of Spring WebFlux, we need to pass the Flux into a IReactiveDataDriverContextVariable,
 * to make sure the Flux's are not consumed.
 */

public class AsyncWrapper {
    private final Flux<Presentation> presentations;
    
    public AsyncWrapper(Flux<Presentation> presentations) {
        this.presentations = presentations;
    }
    
    public Flux<Presentation> getPresentations() {
        return presentations;
    }
}
