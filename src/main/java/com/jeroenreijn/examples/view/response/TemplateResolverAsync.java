package com.jeroenreijn.examples.view.response;

import java.io.OutputStreamWriter;
import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface TemplateResolverAsync<T> {

    CompletableFuture<Void> resolveAsync(T model, OutputStreamWriter writer);
}
