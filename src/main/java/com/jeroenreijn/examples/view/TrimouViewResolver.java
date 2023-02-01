package com.jeroenreijn.examples.view;

import com.jeroenreijn.examples.model.SpringMessageSourceHelper;
import com.jeroenreijn.examples.view.response.ReactiveResponseWriterImpl;
import org.springframework.context.MessageSource;
import org.springframework.web.reactive.result.view.View;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.reactive.result.view.ViewResolverSupport;
import org.trimou.Mustache;
import org.trimou.engine.MustacheEngine;
import org.trimou.engine.MustacheEngineBuilder;
import reactor.core.publisher.Mono;

import java.util.Locale;

public class TrimouViewResolver extends ViewResolverSupport implements ViewResolver {
	private final MustacheEngine engine;

	public TrimouViewResolver(MessageSource messageSource) {
		TrimouSpringResourceTemplateLocator loader = new TrimouSpringResourceTemplateLocator();
		this.engine = MustacheEngineBuilder.newBuilder()
				.addTemplateLocator(loader)
				.registerHelper("springMsg", new SpringMessageSourceHelper(messageSource))
				.build();
	}
	
	@Override
	public Mono<View> resolveViewName(String viewName, Locale locale) {
		
		if (!viewName.contains("trimou")) {
			return Mono.empty();
		}
		
		
		Mustache mustache = engine.getMustache("trimou/"+viewName);
		if (mustache != null) {
			TrimouView trimouView = new TrimouView(mustache, new ReactiveResponseWriterImpl<>());
			trimouView.setMustache(mustache);
			
			return Mono.just(trimouView);
		}
		return Mono.empty();
	}
}
