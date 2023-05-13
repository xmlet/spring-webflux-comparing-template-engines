package com.jeroenreijn.examples.repository

import com.jeroenreijn.examples.model.Presentation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
class InMemoryPresentations(private val javaRepo : PresentationsRepository) : PresentationRepo {

    override fun findAllReactive(): Flux<Presentation> = javaRepo.findAllReactive()

    override suspend fun findAllCoroutines(): List<Presentation> {
        return withContext(Dispatchers.Default) {
            val result = mutableListOf<Presentation>()
            for (presentation in javaRepo.findAll()) {
                delay(2000L)
                result.add(presentation)
            }

            result
        }
    }
}
