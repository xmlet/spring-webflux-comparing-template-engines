package com.jeroenreijn.examples.view;

import com.jeroenreijn.examples.view.response.ReactiveResponseWriterImpl;
import liqp.filters.Filter;
import org.springframework.context.MessageSource;
import org.springframework.web.reactive.result.view.View;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.reactive.result.view.ViewResolverSupport;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import reactor.core.publisher.Mono;

import java.util.Locale;

public class LiqpViewResolver extends ViewResolverSupport implements ViewResolver {
	public LiqpViewResolver(MessageSource messageSource) {
		Filter.registerFilter(new Filter("i18n") {
			@Override
			public Object apply(Object value, Object... params) {
				return messageSource.getMessage(value.toString(), null, Locale.ENGLISH);
			}
		});
	}
	
	@Override
	public Mono<View> resolveViewName(String viewName, Locale locale) {
		if (!viewName.contains("liqp")) {
			return Mono.empty();
		}
		
		return Mono.just(new LiqpView(new ReactiveResponseWriterImpl<>()));
	}
}
