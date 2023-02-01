package com.jeroenreijn.examples.model;

import java.io.IOException;
import java.io.Writer;
import java.util.Locale;


import org.springframework.context.MessageSource;
import org.springframework.web.servlet.LocaleResolver;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template.Fragment;

public class i18nLayout implements Mustache.Lambda {
	private MessageSource messageSource;
	public i18nLayout(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public String message(String key) {
		String text = messageSource.getMessage(key, null, Locale.getDefault());

		return text;
	}

	@Override
	public void execute(Fragment frag, Writer out) throws IOException {
		String key = frag.execute();
		String text = messageSource.getMessage(key, null, Locale.getDefault());

		out.write(text);
	}
}
