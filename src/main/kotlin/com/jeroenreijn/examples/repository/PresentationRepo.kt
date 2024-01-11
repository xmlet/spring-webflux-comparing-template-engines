package com.jeroenreijn.examples.repository

import com.jeroenreijn.examples.model.Presentation
import kotlinx.coroutines.flow.Flow
import reactor.core.publisher.Flux

sealed interface PresentationRepo {

    fun findAllReactive() : Flux<Presentation>

    fun findAllSync() : List<Presentation>

    fun findAllFlow(): Flow<Presentation>;
}
