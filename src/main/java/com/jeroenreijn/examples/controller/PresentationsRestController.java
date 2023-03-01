package com.jeroenreijn.examples.controller;

import com.jeroenreijn.examples.model.Presentation;
import com.jeroenreijn.examples.services.PresentationsService;
import com.jeroenreijn.examples.view.HtmlFlowIndexView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/rest")
public class PresentationsRestController {

    private final PresentationsService service;

    public PresentationsRestController(PresentationsService service) {
        this.service = service;
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/htmlFlow")
    public String htmlFlowTemplate() throws ExecutionException, InterruptedException {
        Flux<Presentation> presentationFlux = this.service.findAllReactive();

        StringBuilder template = new StringBuilder();

        CompletableFuture<Void> future = new HtmlFlowIndexView()
                .templatePresentationsFromAppendable(template, presentationFlux);

        future.get();

        return template.toString();
    }
}
