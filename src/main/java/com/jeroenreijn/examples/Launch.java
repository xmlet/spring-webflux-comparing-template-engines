package com.jeroenreijn.examples;

import com.jeroenreijn.examples.repository.InMemoryPresentations;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class Launch {
	public static void main(String[] args) {
		InMemoryPresentations.Companion.setTimeout(300);
		SpringApplication.run(Launch.class, args);
	}
}
