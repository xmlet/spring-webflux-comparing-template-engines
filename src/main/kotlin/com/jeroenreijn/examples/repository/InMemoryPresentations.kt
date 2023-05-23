package com.jeroenreijn.examples.repository

import com.jeroenreijn.examples.model.Presentation
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
class InMemoryPresentations(private val javaRepo : PresentationsRepository) : PresentationRepo {

    override fun findAllReactive(): Flux<Presentation> = javaRepo.findAllReactive()

}
