package com.jeroenreijn.examples.view;

import com.jeroenreijn.examples.view.response.ReactiveResponseWriterImpl;
import org.springframework.web.reactive.result.view.View;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.reactive.result.view.ViewResolverSupport;
import reactor.core.publisher.Mono;

import java.util.Locale;

public class RockerViewResolver extends ViewResolverSupport implements ViewResolver {
	public RockerViewResolver() {
	}
	
	@Override
	public Mono<View> resolveViewName(String viewName, Locale locale) {
		if (!viewName.contains("rocker")) {
			return Mono.empty();
		}
		
		return Mono.just(new RockerView(new ReactiveResponseWriterImpl<>()));
	}
}
