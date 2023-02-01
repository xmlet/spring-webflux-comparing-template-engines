package com.jeroenreijn.examples.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(properties = "spring.main.web-application-type=reactive")
@AutoConfigureWebTestClient(timeout = "PT1M")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PresentationIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @DisplayName("Should generated html for each template")
    @ParameterizedTest
    @MethodSource("htmlTemplates")
    void test_reactive_endpoint_for_template_blocking_for_response(String template) {

        byte[] responseBody = webTestClient.get()
                .uri(URI.create("/async/"+template))
                .accept(MediaType.ALL)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .returnResult()
                .getResponseBody();

        if (responseBody == null) {
            fail("Error on generating template");
        }

        String response = new String(responseBody);

        then(response)
                .isNotNull()
                .isNotBlank();
    }

    @DisplayName("Should generated html for each template")
    @ParameterizedTest
    @MethodSource("htmlTemplates")
    void test_reactive_endpoint_for_template(String template) {

        webTestClient.get()
                .uri(URI.create("/async/"+template))
                .accept(MediaType.ALL)
                .exchange()
                .expectStatus()
                .isOk();
    }

    static Stream<Arguments> htmlTemplates() {
        return Stream.of(
                Arguments.of(Named.of("Generate html for Thymeleaf", "thymeleaf")),
                Arguments.of(Named.of("Generate html for HtmlFlow", "htmlFlow")),
                Arguments.of(Named.of("Generate html for KotlinX", "kotlinx"))
        );
    }
}
