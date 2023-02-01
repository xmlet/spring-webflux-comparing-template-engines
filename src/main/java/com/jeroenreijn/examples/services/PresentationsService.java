package com.jeroenreijn.examples.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jeroenreijn.examples.model.Presentation;
import com.jeroenreijn.examples.repository.PresentationsRepository;
import reactor.core.publisher.Flux;

/**
 * Simple service for fetching presentations.
 *
 * @author Jeroen Reijn
 */
@Service
public class PresentationsService {

	@Autowired
	PresentationsRepository presentationsRepository;

	public Iterable<Presentation> findAll() {
		return this.presentationsRepository.findAll();
	}
	
	public Flux<Presentation> findAllReactive() {
		return this.presentationsRepository.findAllReactive();
	}
}
