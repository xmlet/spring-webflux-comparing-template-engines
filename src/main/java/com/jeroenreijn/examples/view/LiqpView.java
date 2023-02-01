package com.jeroenreijn.examples.view;

import com.jeroenreijn.examples.model.AsyncWrapper;
import com.jeroenreijn.examples.model.Presentation;
import com.jeroenreijn.examples.view.response.ReactiveResponseWriter;
import liqp.Template;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;
import org.springframework.web.reactive.result.view.AbstractView;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class LiqpView extends AbstractView {
	private static final Logger LOGGER = LoggerFactory.getLogger(LiqpView.class);
	
	private final ReactiveResponseWriter<Presentation> responseWriter;
	
	public LiqpView(ReactiveResponseWriter<Presentation> responseWriter) {
		this.responseWriter = responseWriter;
	}
	
	protected String renderModel(Map<String, Object> model, ServerWebExchange serverWebExchange) throws RuntimeException {
		String templateUrl = "classpath:templates/liqp/index-liqp.liqp";
		try {
			File templateFile = ResourceUtils.getFile(templateUrl);
			if (templateFile.exists()) {
				// Liqp serializes the entire "model" to JSON Object and then to Map. This fails for custom and Spring classes
				model.remove("springMacroRequestContext");
				model.remove("org.springframework.validation.BindingResult.i18n");
				model.remove("org.springframework.validation.BindingResult.presentations");
				model.remove("org.springframework.validation.BindingResult.reactivedata");
				model.remove("i18n");
				model.remove("reactivedata");
				
				// Just in case, we need it as in all other view resolvers
				model.put("contextPath", serverWebExchange.getRequest().getPath().value());
				
				return Template.parse(templateFile).render(model);
			} else {
				LOGGER.error("Template not found: {}", templateUrl);
				return "";
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@NotNull
	@Override
	protected Mono<Void> renderInternal(Map<String, Object> model, MediaType mediaType, @NotNull ServerWebExchange serverWebExchange) {
		
		final Flux<Presentation> presentations = ((AsyncWrapper) model.get("presentations")).getPresentations();
		return responseWriter.write(serverWebExchange, presentations, (res, __, a, b) -> this.renderModel(model, serverWebExchange));
	}
	
	private String getUrl(ServerWebExchange webExchange) {
		return webExchange.getRequest().getPath().value();
	}
}
