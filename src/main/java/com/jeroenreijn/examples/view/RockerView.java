package com.jeroenreijn.examples.view;

import com.fizzed.rocker.Rocker;
import com.jeroenreijn.examples.model.AsyncWrapper;
import com.jeroenreijn.examples.model.Presentation;
import com.jeroenreijn.examples.view.response.ReactiveResponseWriter;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.result.view.AbstractView;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public class RockerView extends AbstractView {
	
	private static final String PRESENTATIONS_KEY = "presentations";
	private final ReactiveResponseWriter<Presentation> responseWriter;
	
	public RockerView(ReactiveResponseWriter<Presentation> responseWriter) {
		this.responseWriter = responseWriter;
	}
	
	@Override
	protected Mono<Void> renderInternal(Map<String, Object> model, MediaType mediaType, ServerWebExchange serverWebExchange) {
		final Flux<Presentation> presentations = ((AsyncWrapper) model.get(PRESENTATIONS_KEY)).getPresentations();
		
		
		return responseWriter.write(serverWebExchange, presentations, (res, __, a, b) -> Rocker.template("index.rocker.html")
				// rocket does not support Flux type
				
				//rocket does not work because we cannot block on a reactor thread
				.bind(PRESENTATIONS_KEY, presentations.collectList().block())
				.bind("i18n", model.get("i18n"))
				.render()
				.toString());
	}
}
