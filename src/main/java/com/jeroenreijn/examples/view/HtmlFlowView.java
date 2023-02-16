package com.jeroenreijn.examples.view;

import com.jeroenreijn.examples.model.AsyncWrapper;
import com.jeroenreijn.examples.model.Presentation;
import com.jeroenreijn.examples.view.response.ReactiveResponseWriter;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.result.view.AbstractView;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class HtmlFlowView extends AbstractView {
	
	private final ReactiveResponseWriter<Presentation> responseWriter;
	private static final String PRESENTATIONS_KEY = "presentations";
	
	public HtmlFlowView(ReactiveResponseWriter<Presentation> responseWriter) {
		this.responseWriter = responseWriter;
	}
	
	@NotNull
	@Override
	protected Mono<Void> renderInternal(Map<String, Object> model, MediaType mediaType, @NotNull ServerWebExchange serverWebExchange) {
		
		final Flux<Presentation> presentations = ((AsyncWrapper) model.get(PRESENTATIONS_KEY)).getPresentations();
		return responseWriter.writeAsync(serverWebExchange, presentations, this::renderHtmlFlowAsync);
	}

	private CompletableFuture<Void> renderHtmlFlowAsync(Flux<Presentation> presentations, OutputStreamWriter writer) {
		return new HtmlFlowIndexView()
				.templatePresentations(writer, presentations);
	}
}
