package com.jeroenreijn.examples.repository;

import java.util.List;
import java.util.Optional;

import com.jeroenreijn.examples.model.Presentation;
import reactor.core.publisher.Flux;

public interface PresentationsRepository {
	List<Presentation> findAll();
	
	Flux<Presentation> findAllReactive();

	Optional<Presentation> findById(Long id);
}
