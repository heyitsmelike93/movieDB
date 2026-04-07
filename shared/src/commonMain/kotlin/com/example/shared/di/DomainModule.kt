package com.example.shared.di

import com.example.shared.domain.usecase.GetMovieDetailUseCase
import com.example.shared.domain.usecase.GetPopularMoviesUseCase
import com.example.shared.domain.usecase.ObserveFavoriteMovieIdsUseCase
import com.example.shared.domain.usecase.ObserveMovieFavoriteStatusUseCase
import com.example.shared.domain.usecase.ToggleFavoriteMovieUseCase
import org.koin.dsl.module

internal val domainModule = module {
    single { GetPopularMoviesUseCase(get()) }
    single { GetMovieDetailUseCase(get()) }
    single { ObserveFavoriteMovieIdsUseCase(get()) }
    single { ObserveMovieFavoriteStatusUseCase(get()) }
    single { ToggleFavoriteMovieUseCase(get()) }
}
