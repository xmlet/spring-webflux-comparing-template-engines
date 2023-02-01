package com.jeroenreijn.examples.controller;


import com.jeroenreijn.examples.model.AsyncWrapper;
import com.jeroenreijn.examples.model.Presentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.LocaleResolver;

import com.jeroenreijn.examples.model.i18nLayout;
import com.jeroenreijn.examples.services.PresentationsService;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Controller
@RequestMapping("/")
public class PresentationsController {

	@Autowired
	PresentationsService presentationsService;

	@Autowired
	MessageSource messageSource;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String home(final ModelMap modelMap) {
		return showList("jsp", modelMap);
	}

	@RequestMapping(value = "/{template}", method = RequestMethod.GET)
	public String showList(@PathVariable(value = "template") final String template,
			final ModelMap model) {
		model.addAttribute("presentations", presentationsService.findAll());
		model.addAttribute("i18n", new i18nLayout(messageSource));

		return "index-" + template;
	}
	
	@GetMapping("/async/{template}")
	public String showListWebflux(@PathVariable(value = "template") final String template,
						   final Model model) {
		final Flux<Presentation> presentations = presentationsService.findAllReactive().delayElements(Duration.ofSeconds(1L));
		IReactiveDataDriverContextVariable rx = new ReactiveDataDriverContextVariable(presentations);
		model.addAttribute("reactivedata", rx);
		model.addAttribute("presentations", new AsyncWrapper(presentations));
		model.addAttribute("i18n", new i18nLayout(messageSource));
		
		return "index-" + template;
	}
}
