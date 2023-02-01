package com.jeroenreijn.examples.view;

import com.jeroenreijn.examples.view.response.ReactiveResponseWriterImpl;
import org.springframework.web.reactive.result.view.View;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.reactive.result.view.ViewResolverSupport;
import reactor.core.publisher.Mono;

import java.util.Locale;

public class HtmlFlowViewResolver extends ViewResolverSupport implements ViewResolver {
	public HtmlFlowViewResolver() {
	}
	
	@Override
	public Mono<View> resolveViewName(String viewName, Locale locale) {
		
		if (!viewName.contains("htmlFlow")) {
			return Mono.empty();
		}
		
		return Mono.just(new HtmlFlowView(new ReactiveResponseWriterImpl<>()));
	}
}
