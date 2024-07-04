package com.jeroenreijn.examples.repository

import com.jeroenreijn.examples.model.Presentation
import io.reactivex.rxjava3.core.Observable

sealed interface PresentationRepo {

    fun findAllReactive() : Observable<Presentation>

}
