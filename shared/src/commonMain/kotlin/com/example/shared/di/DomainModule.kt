package com.example.shared.di

import com.example.shared.domain.usecase.GetMovieDetailUseCase
import com.example.shared.domain.usecase.GetPopularMoviesUseCase
import org.koin.dsl.module

internal val domainModule = module {
    factory { GetPopularMoviesUseCase(get()) }
    factory { GetMovieDetailUseCase(get()) }
}
