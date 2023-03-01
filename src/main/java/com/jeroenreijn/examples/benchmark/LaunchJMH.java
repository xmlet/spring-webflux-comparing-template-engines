package com.jeroenreijn.examples.benchmark;

import com.jeroenreijn.examples.Launch;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.net.URI;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MINUTES)
@Fork(value = 1)
@State(Scope.Thread)
public class LaunchJMH {
    
    static ConfigurableApplicationContext context;
    static WebTestClient webTestClient;
    
    @Setup(Level.Trial)
    public synchronized void startupSpring() {
        try {
            if (context == null) {
                context = SpringApplication.run(Launch.class);
                ApplicationContext webApplicationContext = context;
                webTestClient = WebTestClient.bindToApplicationContext(webApplicationContext)
                        .configureClient()
                        .responseTimeout(Duration.ofMinutes(1))
                        .build();
            }
        } catch (Exception e) {
            //Force JMH crash
            throw new RuntimeException(e);
        }
    }
    
    @TearDown(Level.Trial)
    public synchronized void shutdownSpring() {
        try {
            if (context != null) {
                SpringApplication.exit(context);
                context = null;
            }
        } catch (Exception e) {
            //Force JMH crash
            throw new RuntimeException(e);
        }
    }
    
    @Benchmark
    public String benchmarkThymeleaf() {
        return new String(webTestClient.get()
                .uri(URI.create("/async/thymeleaf"))
                .accept(MediaType.ALL)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody().returnResult().getResponseBody());
    }
    
    @Benchmark
    public String benchmarkKotlinx() {
        return new String(webTestClient.get()
                .uri(URI.create("/async/kotlinx"))
                .accept(MediaType.ALL)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody().returnResult().getResponseBody());
    }
    
    @Benchmark
    public String benchmarkHtmlFlow() {
        return new String(webTestClient.get()
                .uri(URI.create("/async/htmlFlow"))
                .accept(MediaType.ALL)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody().returnResult().getResponseBody());
    }
}
