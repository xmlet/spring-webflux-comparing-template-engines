package com.jeroenreijn.examples;

import com.jeroenreijn.examples.repository.InMemoryPresentations;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import static java.lang.System.out;

/**
 * Set the timeout interleaving datasource items in Millis, e.g. 1 Milli:
 *   mvn spring-boot:run -Dspring-boot.run.jvmArguments="-DbenchTimeout=10"
 */
@SpringBootApplication
public class Launch {
	public static void main(String[] args) {
		final var strTimeout = System.getProperty("benchTimeout");
		final int timeout = strTimeout != null ? Integer.parseInt(strTimeout) : 300;
		InMemoryPresentations.Companion.setTimeout(timeout);
		SpringApplication.run(Launch.class, args);
	}
}
