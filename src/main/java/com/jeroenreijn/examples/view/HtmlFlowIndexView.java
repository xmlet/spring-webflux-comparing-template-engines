package com.jeroenreijn.examples.view;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.CompletableFuture;

import htmlflow.HtmlFlow;
import htmlflow.HtmlPage;
import htmlflow.HtmlViewAsync;
import org.xmlet.htmlapifaster.Body;
import org.xmlet.htmlapifaster.Div;
import org.xmlet.htmlapifaster.EnumMediaType;
import org.xmlet.htmlapifaster.EnumRelType;

import com.jeroenreijn.examples.model.Presentation;

import org.xmlet.htmlapifaster.Html;
import reactor.core.publisher.Flux;

public class HtmlFlowIndexView {

	public CompletableFuture<String> templatePresentations(OutputStreamWriter writer, Flux<Presentation> presentations) {
		return HtmlFlow
				.viewAsync(template -> renderTemplate(template, writer))
				.threadSafe()
				.renderAsync(presentations);
	}

	private void renderTemplate(HtmlPage view, OutputStreamWriter writer) {
		view
				.html()
				.head()
				.meta().attrCharset("UTF-8").__()
				.meta().attrName("viewport").attrContent("width=device-width, initial-scale=1.0").__()
				.meta()
				.addAttr("http-equiv", "X-UA-Compatible")
				.attrContent("IE=Edge")
				.__() // meta
				.title().text("JFall 2013 Presentations - HtmlFlow").__()
				.link()
				.attrRel(EnumRelType.STYLESHEET)
				.attrHref("/webjars/bootstrap/4.3.1/css/bootstrap.min.css")
				.attrMedia(EnumMediaType.SCREEN)
				.__() // link
				.__() // head
				.body()
				.div().attrClass("container")
				.div().attrClass("pb-2 mt-4 mb-3 border-bottom")
				.h1().text("JFall 2013 Presentations - HtmlFlow").__()
				.__() // div
				.<Flux<Presentation>>await((div,model,onCompletion) -> model
						.doOnNext(presentation -> template(div, presentation))
						.doOnNext(pres -> {
							try {
								writer.append(pres.toString());
							} catch (IOException e) {
								throw new RuntimeException(e);
							}
						})
						.doOnComplete(() -> {
							onCompletion.finish();
							try {
								writer.flush();
							} catch (IOException e) {
								throw new RuntimeException(e);
							}
						})
						.subscribe() // foreach
				)
				.__() // container
				.script().attrSrc("/webjars/jquery/3.1.1/jquery.min.js").__()
				.script().attrSrc("/webjars/bootstrap/4.3.1/js/bootstrap.min.js").__()
				.__() // body
				.__(); // html
	}


	private static void template(Div<Body<Html<HtmlPage>>> div, Presentation presentation) {
		div
				.div().attrClass("card mb-3 shadow-sm rounded")
				.div().attrClass("card-header")
				.h5()
				.of(h5 -> h5
						.attrClass("card-title")
						.text(presentation.getTitle() + " - " + presentation.getSpeakerName())
				)
				.__() // h5
				.__() // div
				.div()
				.of(d -> d
						.attrClass("card-body")
						.text(presentation.getSummary())
				)
				.__() // div
				.__(); // div
	}
}
