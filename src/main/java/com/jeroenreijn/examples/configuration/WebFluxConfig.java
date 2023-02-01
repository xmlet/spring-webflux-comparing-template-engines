package com.jeroenreijn.examples.configuration;

import com.jeroenreijn.examples.model.Presentation;
import com.jeroenreijn.examples.repository.InMemoryPresentationsRepository;
import com.jeroenreijn.examples.repository.PresentationsRepository;
import com.jeroenreijn.examples.view.HtmlFlowViewResolver;
import com.jeroenreijn.examples.view.KotlinxHtmlViewResolver;
import com.jeroenreijn.examples.view.LiqpViewResolver;
import com.jeroenreijn.examples.view.RockerViewResolver;
import com.jeroenreijn.examples.view.TrimouViewResolver;
import com.jeroenreijn.examples.view.response.ReactiveResponseWriter;
import com.jeroenreijn.examples.view.response.ReactiveResponseWriterImpl;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.ViewResolverRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.thymeleaf.spring5.view.reactive.ThymeleafReactiveViewResolver;

@Configuration
@EnableWebFlux
public class WebFluxConfig implements ApplicationContextAware, WebFluxConfigurer {
	private ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.viewResolver(this.rockerViewResolver());
		registry.viewResolver(this.htmlFlowViewResolver());
		registry.viewResolver(this.trimouViewResolver());
		registry.viewResolver(this.liqpViewResolver());
		registry.viewResolver(this.kotlinxHtmlViewResolver());
		registry.viewResolver(applicationContext.getBean(ThymeleafReactiveViewResolver.class));
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/robots.txt").addResourceLocations("/robots.txt");
		registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/");
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

	@Bean
	public ReactiveResponseWriter<Presentation> reactiveResponseWriter() {
		return new ReactiveResponseWriterImpl<>();
	}
	
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:/messages");
		messageSource.setDefaultEncoding("UTF-8");

		return messageSource;
	}

	@Bean
	public PresentationsRepository presentationsRepository() {
		return new InMemoryPresentationsRepository();
	}

	@Bean
	public ViewResolver htmlFlowViewResolver() {
		return new HtmlFlowViewResolver();
	}

	@Bean
	public TrimouViewResolver trimouViewResolver() {
		MessageSource messageSource = applicationContext.getBean(MessageSource.class);
		return new TrimouViewResolver(messageSource);
	}

	@Bean
	public ViewResolver rockerViewResolver() {
		return new RockerViewResolver();
	}

	@Bean
	public LiqpViewResolver liqpViewResolver() {
		return new LiqpViewResolver(applicationContext.getBean(MessageSource.class));
	}

	@Bean
	public ViewResolver kotlinxHtmlViewResolver() {
		return new KotlinxHtmlViewResolver();
	}


	@Controller
	static class FaviconController {
		@RequestMapping("favicon.ico")
		String favicon() {
			return "forward:/resources/images/favicon.ico";
		}
	}
}
